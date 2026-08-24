package com.bstek.urule.builder.table;

import com.bstek.urule.action.VariableAssignAction;
import com.bstek.urule.model.crosstab.ConditionCrossCell;
import com.bstek.urule.model.crosstab.CrossCell;
import com.bstek.urule.model.crosstab.CrossColumn;
import com.bstek.urule.model.crosstab.CrossRow;
import com.bstek.urule.model.crosstab.CrosstabDefinition;
import com.bstek.urule.model.crosstab.LeftColumn;
import com.bstek.urule.model.crosstab.TopRow;
import com.bstek.urule.model.crosstab.ValueCrossCell;
import com.bstek.urule.model.rule.Rhs;
import com.bstek.urule.model.rule.Rule;
import com.bstek.urule.model.rule.Value;
import com.bstek.urule.model.rule.lhs.And;
import com.bstek.urule.model.rule.lhs.Criteria;
import com.bstek.urule.model.rule.lhs.Criterion;
import com.bstek.urule.model.rule.lhs.Junction;
import com.bstek.urule.model.rule.lhs.Left;
import com.bstek.urule.model.rule.lhs.LeftType;
import com.bstek.urule.model.rule.lhs.Lhs;
import com.bstek.urule.model.rule.lhs.Or;
import com.bstek.urule.model.rule.lhs.PredefineLeftPart;
import com.bstek.urule.model.rule.lhs.VariableLeftPart;
import com.bstek.urule.model.table.Condition;
import com.bstek.urule.model.table.Joint;
import com.bstek.urule.model.table.JointType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;

/**
 * Converts a cross-decision table into executable rules and preserves the
 * parent/child relationship of merged header cells.
 */
public class CrosstabRulesBuilder {
   public List<Rule> buildRules(CrosstabDefinition crosstab, String path) {
      List<Rule> rules = new ArrayList<>();
      List<CellRange> topRanges = new ArrayList<>();
      List<CellRange> leftRanges = new ArrayList<>();
      Map<String, CrossCell> cellsByCoordinate = this.indexCells(crosstab.getCells());
      List<CrossRow> rows = crosstab.getRows();
      int rowCount = rows.size();
      List<CrossColumn> columns = crosstab.getColumns();
      List<CellRange> valueRanges = new ArrayList<>();
      int topRowCount = 0;

      for (int rowIndex = 0; rowIndex < rowCount; rowIndex++) {
         CrossRow crossRow = rows.get(rowIndex);
         if (crossRow instanceof TopRow) {
            topRowCount++;
         }

         for (int columnIndex = 0; columnIndex < columns.size(); columnIndex++) {
            CellRange range = null;
            CrossCell crossCell = cellsByCoordinate.get(rowIndex + 1 + "," + (columnIndex + 1));
            if (crossCell != null) {
               if (crossRow instanceof TopRow) {
                  TopRow topRow = (TopRow)crossRow;
                  range = this.buildTopRange(crossCell, topRanges, topRow);
               } else {
                  CrossColumn crossColumn = columns.get(columnIndex);
                  range = this.buildLeftRange(crossCell, leftRanges, crossColumn);
                  if (crossCell instanceof ValueCrossCell) {
                     valueRanges.add(range);
                  }
               }
            }
         }
      }

      for (CellRange cellRange : valueRanges) {
         CrossCell cell = cellRange.getCell();
         Rule rule = new Rule();
         rule.setFile(path);
         rule.setDebug(crosstab.getDebug());
         rule.setSalience(crosstab.getSalience());
         rule.setExpiresDate(crosstab.getExpiresDate());
         rule.setEffectiveDate(crosstab.getEffectiveDate());
         rule.setEnabled(crosstab.getEnabled());
         rule.setName("rule(" + cell.getRow() + "," + cell.getCol() + ")");
         Lhs lhs = new Lhs();
         And and = new And();
         rule.setLhs(lhs);
         Rhs rhs = new Rhs();
         rule.setRhs(rhs);
         rules.add(rule);
         ValueCrossCell valueCrossCell = (ValueCrossCell)cell;
         Value value = valueCrossCell.getValue();
         VariableAssignAction variableAssignAction = new VariableAssignAction();
         variableAssignAction.setValue(value);
         variableAssignAction.setDatatype(crosstab.getAssignDatatype());
         variableAssignAction.setVariableName(crosstab.getAssignVariable());
         variableAssignAction.setVariableLabel(crosstab.getAssignVariableLabel());
         variableAssignAction.setVariableCategory(crosstab.getAssignVariableCategory());
         variableAssignAction.setKeyLabel(crosstab.getKeyLabel());
         variableAssignAction.setKeyName(crosstab.getKeyName());
         variableAssignAction.setCategoryUuid(crosstab.getCategoryUuid());
         variableAssignAction.setUuid(crosstab.getUuid());
         rhs.addAction(variableAssignAction);
         this.appendParentCriteria(and, cellRange);
         if (and.getCriterions() != null) {
            lhs.setCriterion(and);
         }

         CellRange topRange = this.findExactRange(topRanges, cellRange.getCell().getCol(), topRowCount);
         cellRange.setParentRange(topRange);
         this.appendParentCriteria(and, cellRange);
      }

      return rules;
   }

   private CellRange findExactRange(List<CellRange> cellRanges, int position, int row) {
      CellRange cellRange = null;

      for (CellRange candidate : cellRanges) {
         if (candidate.getStart() == position && candidate.getEnd() == position && candidate.getCell().getRow() == row) {
            cellRange = candidate;
            break;
         }

         cellRange = this.findExactRange(candidate.getChildren(), position, row);
         if (cellRange != null) {
            break;
         }
      }

      return cellRange;
   }

   private void appendParentCriteria(And and, CellRange cellRange) {
      CellRange parentRange = cellRange.getParentRange();
      if (parentRange != null) {
         ConditionCrossCell cell = (ConditionCrossCell)parentRange.getCell();
         Criterion criterion = this.buildCriterion(cell, parentRange);
         if (criterion != null) {
            and.addCriterion(criterion);
         }

         this.appendParentCriteria(and, parentRange);
      }
   }

   public Criterion buildCriterion(ConditionCrossCell cell, CellRange range) {
      Joint joint = cell.getJoint();
      if (joint == null) {
         return null;
      }

      List conditions = joint.getConditions();
      List joints = joint.getJoints();
      if (conditions != null && conditions.size() != 0 || joints != null && joints.size() != 0) {
         Junction junction = null;
         if (conditions.size() == 1) {
            return this.buildCriteria((Condition)conditions.get(0), range);
         }

         if (joint.getType().equals(JointType.and)) {
            junction = new And();
         } else {
            junction = new Or();
         }

         this.appendConditions(conditions, junction, range);
         this.appendNestedJoints(joints, junction, range);
         return junction;
      } else {
         return null;
      }
   }

   private void appendNestedJoints(List<Joint> joints, Junction junction, CellRange cellRange) {
      if (joints != null && joints.size() != 0) {
         for (Joint joint : joints) {
            Junction childJunction = joint.getJunction();
            List conditions = joint.getConditions();
            this.appendConditions(conditions, childJunction, cellRange);
            List<Joint> childJoints = joint.getJoints();
            this.appendNestedJoints(childJoints, childJunction, cellRange);
            junction.addCriterion(childJunction);
         }
      }
   }

   private void appendConditions(List<Condition> conditions, Junction junction, CellRange cellRange) {
      if (conditions != null && conditions.size() != 0) {
         for (Condition condition : conditions) {
            Criteria criteria = this.buildCriteria(condition, cellRange);
            junction.addCriterion(criteria);
         }
      }
   }

   private Criteria buildCriteria(Condition condition, CellRange cellRange) {
      Criteria criteria = new Criteria();
      Left left = new Left();
      if (StringUtils.isNotBlank(cellRange.getPredefineUuid())) {
         PredefineLeftPart predefineLeftPart = new PredefineLeftPart();
         predefineLeftPart.setUuid(cellRange.getPredefineUuid());
         predefineLeftPart.setName(cellRange.getPredefineName());
         predefineLeftPart.setVariableCategory(cellRange.getPredefineVariableCategory());
         predefineLeftPart.setVariableCategoryUuid(cellRange.getPredefineVariableCategoryUuid());
         predefineLeftPart.setPropertyUuid(cellRange.getPredefinePropertyUuid());
         predefineLeftPart.setPropertyName(cellRange.getPredefinePropertyName());
         predefineLeftPart.setPropertyLabel(cellRange.getPredefinePropertyLabel());
         left.setLeftPart(predefineLeftPart);
         left.setType(LeftType.predefine);
      } else {
         VariableLeftPart variableLeftPart = new VariableLeftPart();
         variableLeftPart.setVariableCategory(cellRange.getVariableCategory());
         variableLeftPart.setVariableName(cellRange.getVariableName());
         variableLeftPart.setVariableLabel(cellRange.getVariableLabel());
         variableLeftPart.setDatatype(cellRange.getDatatype());
         variableLeftPart.setKeyLabel(cellRange.getKeyLabel());
         variableLeftPart.setKeyName(cellRange.getKeyName());
         variableLeftPart.setCategoryUuid(cellRange.getCategoryUuid());
         variableLeftPart.setUuid(cellRange.getUuid());
         left.setLeftPart(variableLeftPart);
         left.setType(LeftType.variable);
      }

      criteria.setLeft(left);
      criteria.setOp(condition.getOp());
      criteria.setValue(condition.getValue());
      return criteria;
   }

   private CellRange buildTopRange(CrossCell crossCell, List<CellRange> cellRanges, TopRow topRow) {
      int col = crossCell.getCol();
      int colspan = crossCell.getColspan();
      if (colspan > 0) {
         colspan--;
      }

      int number = col + colspan;
      CellRange cellRange = new CellRange();
      cellRange.setStart(col);
      cellRange.setEnd(number);
      cellRange.setCell(crossCell);
      cellRange.setPredefineUuid(topRow.getPredefineUuid());
      cellRange.setPredefineName(topRow.getPredefineName());
      cellRange.setPredefineDatatype(topRow.getPredefineDatatype());
      cellRange.setPredefineVariableCategory(topRow.getPredefineVariableCategory());
      cellRange.setPredefineVariableCategoryUuid(topRow.getPredefineVariableCategoryUuid());
      cellRange.setPredefinePropertyUuid(topRow.getPredefinePropertyUuid());
      cellRange.setPredefinePropertyName(topRow.getPredefinePropertyName());
      cellRange.setPredefinePropertyLabel(topRow.getPredefinePropertyLabel());
      cellRange.setVariableCategory(topRow.getVariableCategory());
      cellRange.setVariableName(topRow.getVariableName());
      cellRange.setVariableLabel(topRow.getVariableLabel());
      cellRange.setDatatype(topRow.getDatatype());
      cellRange.setKeyLabel(topRow.getKeyLabel());
      cellRange.setKeyName(topRow.getKeyName());
      cellRange.setCategoryUuid(topRow.getCategoryUuid());
      cellRange.setUuid(topRow.getUuid());
      if (col == 1) {
         cellRanges.add(cellRange);
      } else {
         CellRange parentRange = this.findContainingRange(col, number, cellRanges);
         if (parentRange != null) {
            parentRange.addChildRange(cellRange);
         } else {
            cellRanges.add(cellRange);
         }
      }

      return cellRange;
   }

   private CellRange buildLeftRange(CrossCell crossCell, List<CellRange> cellRanges, CrossColumn crossColumn) {
      int row = crossCell.getRow();
      int rowspan = crossCell.getRowspan();
      if (rowspan > 0) {
         rowspan--;
      }

      int number = row + rowspan;
      CellRange cellRange = new CellRange();
      cellRange.setStart(row);
      cellRange.setEnd(number);
      cellRange.setCell(crossCell);
      if (crossColumn instanceof LeftColumn) {
         LeftColumn leftColumn = (LeftColumn)crossColumn;
         cellRange.setPredefineUuid(leftColumn.getPredefineUuid());
         cellRange.setPredefineName(leftColumn.getPredefineName());
         cellRange.setPredefineDatatype(leftColumn.getPredefineDatatype());
         cellRange.setPredefineVariableCategory(leftColumn.getPredefineVariableCategory());
         cellRange.setPredefineVariableCategoryUuid(leftColumn.getPredefineVariableCategoryUuid());
         cellRange.setPredefinePropertyUuid(leftColumn.getPredefinePropertyUuid());
         cellRange.setPredefinePropertyName(leftColumn.getPredefinePropertyName());
         cellRange.setPredefinePropertyLabel(leftColumn.getPredefinePropertyLabel());
         cellRange.setVariableCategory(leftColumn.getVariableCategory());
         cellRange.setVariableName(leftColumn.getVariableName());
         cellRange.setVariableLabel(leftColumn.getVariableLabel());
         cellRange.setDatatype(leftColumn.getDatatype());
         cellRange.setKeyLabel(leftColumn.getKeyLabel());
         cellRange.setKeyName(leftColumn.getKeyName());
         cellRange.setCategoryUuid(leftColumn.getCategoryUuid());
         cellRange.setUuid(leftColumn.getUuid());
      }

      if (row == 1) {
         cellRanges.add(cellRange);
      } else {
         CellRange parentRange = this.findContainingRange(row, number, cellRanges);
         if (parentRange != null) {
            parentRange.addChildRange(cellRange);
         } else {
            cellRanges.add(cellRange);
         }
      }

      return cellRange;
   }

   private CellRange findContainingRange(int start, int end, List<CellRange> cellRanges) {
      CellRange cellRange = null;

      for (CellRange candidate : cellRanges) {
         boolean containsRange = false;
         if (!candidate.isValueCell() && candidate.getStart() <= start && candidate.getEnd() >= end) {
            containsRange = true;
         }

         if (containsRange) {
            List<CellRange> children = candidate.getChildren();
            if (children.size() > 0) {
               cellRange = this.findContainingRange(start, end, children);
            }

            if (cellRange == null) {
               cellRange = candidate;
            }
            break;
         }
      }

      return cellRange;
   }

   private Map<String, CrossCell> indexCells(List<CrossCell> crossCells) {
      Map<String, CrossCell> cellsByCoordinate = new HashMap<>();

      for (CrossCell crossCell : crossCells) {
         String coordinate = crossCell.getRow() + "," + crossCell.getCol();
         cellsByCoordinate.put(coordinate, crossCell);
      }

      return cellsByCoordinate;
   }
}
