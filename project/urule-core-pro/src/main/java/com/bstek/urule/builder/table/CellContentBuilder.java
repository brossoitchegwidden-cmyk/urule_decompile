package com.bstek.urule.builder.table;

import com.bstek.urule.model.rule.lhs.And;
import com.bstek.urule.model.rule.lhs.Criteria;
import com.bstek.urule.model.rule.lhs.Criterion;
import com.bstek.urule.model.rule.lhs.Junction;
import com.bstek.urule.model.rule.lhs.Left;
import com.bstek.urule.model.rule.lhs.LeftType;
import com.bstek.urule.model.rule.lhs.Or;
import com.bstek.urule.model.rule.lhs.PredefineLeftPart;
import com.bstek.urule.model.rule.lhs.VariableLeftPart;
import com.bstek.urule.model.scorecard.ComplexColumn;
import com.bstek.urule.model.table.Cell;
import com.bstek.urule.model.table.Column;
import com.bstek.urule.model.table.Condition;
import com.bstek.urule.model.table.Joint;
import com.bstek.urule.model.table.JointType;
import java.util.List;

/** Converts decision-table cell content into condition criteria trees. */
public class CellContentBuilder {
   public Criterion buildCriterion(Cell cell, Column col) {
      Joint joint = cell.getJoint();
      if (joint == null) {
         return null;
      }

      List conditions = joint.getConditions();
      List joints = joint.getJoints();
      if (conditions != null && conditions.size() != 0 || joints != null && joints.size() != 0) {
         Junction junction = null;
         if (conditions.size() == 1) {
            return this.newCriteria(col, (Condition)conditions.get(0));
         }

         if (joint.getType().equals(JointType.and)) {
            junction = new And();
         } else {
            junction = new Or();
         }

         this.buildConditionsCriterion(conditions, junction, col);
         this.buildJointsCriterion(joints, col, junction);
         return junction;
      } else {
         return null;
      }
   }

   public Criterion buildCriterion(Cell cell, ComplexColumn col) {
      Joint joint = cell.getJoint();
      if (joint == null) {
         return null;
      }

      List conditions = joint.getConditions();
      List joints = joint.getJoints();
      if (conditions != null && conditions.size() != 0 || joints != null && joints.size() != 0) {
         Junction junction = null;
         if (conditions.size() == 1) {
            return this.newCriteria(cell, col, (Condition)conditions.get(0));
         }

         if (joint.getType().equals(JointType.and)) {
            junction = new And();
         } else {
            junction = new Or();
         }

         this.buildConditionsCriterion(cell, conditions, junction, col);
         this.buildJointsCriterion(cell, joints, col, junction);
         return junction;
      } else {
         return null;
      }
   }

   private void buildJointsCriterion(List<Joint> joints, Column column, Junction junction) {
      if (joints != null && joints.size() != 0) {
         for (Joint joint : joints) {
            Junction junction2 = joint.getJunction();
            List conditions = joint.getConditions();
            this.buildConditionsCriterion(conditions, junction2, column);
            List joints2 = joint.getJoints();
            this.buildJointsCriterion(joints2, column, junction2);
            junction.addCriterion(junction2);
         }
      }
   }

   private void buildJointsCriterion(Cell cell, List<Joint> joints, ComplexColumn complexColumn, Junction junction) {
      if (joints != null && joints.size() != 0) {
         for (Joint joint : joints) {
            Junction junction2 = joint.getJunction();
            List conditions = joint.getConditions();
            this.buildConditionsCriterion(cell, conditions, junction2, complexColumn);
            List joints2 = joint.getJoints();
            this.buildJointsCriterion(cell, joints2, complexColumn, junction2);
            junction.addCriterion(junction2);
         }
      }
   }

   private void buildConditionsCriterion(List<Condition> conditions, Junction junction, Column column) {
      if (conditions != null && conditions.size() != 0) {
         for (Condition condition : conditions) {
            Criteria criteria = this.newCriteria(column, condition);
            junction.addCriterion(criteria);
         }
      }
   }

   private void buildConditionsCriterion(Cell cell, List<Condition> conditions, Junction junction, ComplexColumn complexColumn) {
      if (conditions != null && conditions.size() != 0) {
         for (Condition condition : conditions) {
            Criteria criteria = this.newCriteria(cell, complexColumn, condition);
            junction.addCriterion(criteria);
         }
      }
   }

   private Criteria newCriteria(Column column, Condition condition) {
      Criteria criteria = new Criteria();
      Left left = new Left();
      if (column.isPredefine()) {
         PredefineLeftPart predefineLeftPart = new PredefineLeftPart();
         predefineLeftPart.setDatatype(column.getPredefineDatatype());
         predefineLeftPart.setUuid(column.getUuid());
         predefineLeftPart.setName(column.getPredefineName());
         predefineLeftPart.setVariableCategory(column.getPredefineVariableCategory());
         predefineLeftPart.setVariableCategoryUuid(column.getPredefineVariableCategoryUuid());
         predefineLeftPart.setPropertyUuid(column.getPredefinePropertyUuid());
         predefineLeftPart.setPropertyName(column.getPredefinePropertyName());
         predefineLeftPart.setPropertyLabel(column.getPredefinePropertyLabel());
         left.setLeftPart(predefineLeftPart);
         left.setType(LeftType.predefine);
      } else {
         VariableLeftPart variableLeftPart = new VariableLeftPart();
         variableLeftPart.setVariableCategory(column.getVariableCategory());
         variableLeftPart.setVariableName(column.getVariableName());
         variableLeftPart.setCategoryUuid(column.getCategoryUuid());
         variableLeftPart.setUuid(column.getUuid());
         variableLeftPart.setVariableLabel(column.getVariableLabel());
         variableLeftPart.setKeyLabel(column.getKeyLabel());
         variableLeftPart.setKeyName(column.getKeyName());
         variableLeftPart.setKeyUuid(column.getKeyUuid());
         variableLeftPart.setKeyCategoryUuid(column.getKeyCategoryUuid());
         variableLeftPart.setDatatype(column.getDatatype());
         left.setLeftPart(variableLeftPart);
         left.setType(LeftType.variable);
      }

      criteria.setLeft(left);
      criteria.setOp(condition.getOp());
      criteria.setValue(condition.getValue());
      return criteria;
   }

   private Criteria newCriteria(Cell cell, ComplexColumn complexColumn, Condition condition) {
      Criteria criteria = new Criteria();
      Left left = new Left();
      VariableLeftPart variableLeftPart = new VariableLeftPart();
      variableLeftPart.setVariableCategory(complexColumn.getVariableCategory());
      variableLeftPart.setVariableLabel(cell.getVariableLabel());
      variableLeftPart.setVariableName(cell.getVariableName());
      variableLeftPart.setDatatype(cell.getDatatype());
      variableLeftPart.setKeyLabel(cell.getKeyLabel());
      variableLeftPart.setKeyUuid(cell.getKeyUuid());
      variableLeftPart.setKeyCategoryUuid(cell.getKeyCategoryUuid());
      variableLeftPart.setKeyName(cell.getKeyName());
      variableLeftPart.setCategoryUuid(complexColumn.getUuid());
      variableLeftPart.setUuid(cell.getUuid());
      left.setLeftPart(variableLeftPart);
      left.setType(LeftType.variable);
      criteria.setLeft(left);
      criteria.setOp(condition.getOp());
      criteria.setValue(condition.getValue());
      return criteria;
   }
}
