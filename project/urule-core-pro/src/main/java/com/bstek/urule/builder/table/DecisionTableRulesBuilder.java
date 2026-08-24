package com.bstek.urule.builder.table;

import com.bstek.urule.action.AbstractAction;
import com.bstek.urule.action.Action;
import com.bstek.urule.action.ConsolePrintAction;
import com.bstek.urule.action.PredefineAssignAction;
import com.bstek.urule.action.VariableAssignAction;
import com.bstek.urule.exception.RuleException;
import com.bstek.urule.model.rule.Rhs;
import com.bstek.urule.model.rule.Rule;
import com.bstek.urule.model.rule.Value;
import com.bstek.urule.model.rule.lhs.And;
import com.bstek.urule.model.rule.lhs.Criterion;
import com.bstek.urule.model.rule.lhs.Lhs;
import com.bstek.urule.model.table.Cell;
import com.bstek.urule.model.table.Column;
import com.bstek.urule.model.table.ColumnType;
import com.bstek.urule.model.table.DecisionTable;
import com.bstek.urule.model.table.Row;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DecisionTableRulesBuilder {
   private CellContentBuilder cellContentBuilder;

   public List<Rule> buildRules(DecisionTable table, String path) {
      ArrayList rules = new ArrayList();
      List rows = table.getRows();
      Integer salience = table.getSalience();
      int number = rows.size();
      List columns = table.getColumns();

      for (int index = 0; index < rows.size(); index++) {
         Row row = (Row)rows.get(index);
         Rule rule = new Rule();
         rule.setFile(path);
         if (salience != null) {
            int number2 = salience + (number - index);
            rule.setSalience(number2);
         }

         rule.setMutexGroup(table.getMutexGroup());
         rule.setDebug(table.getDebug());
         rule.setSalience(table.getSalience());
         rule.setExpiresDate(table.getExpiresDate());
         rule.setEffectiveDate(table.getEffectiveDate());
         rule.setEnabled(table.getEnabled());
         rule.setName("r" + row.getNum());
         Lhs lhs = new Lhs();
         And and = new And();
         rule.setLhs(lhs);
         Rhs rhs = new Rhs();
         rule.setRhs(rhs);
         rules.add(rule);
         Value localValue = null;

         for (Column column : (Iterable<Column>)(Iterable<?>)(columns)) {
            Cell cell = this.resolveCell(table, row.getNum(), column.getNum());
            ColumnType type = column.getType();
            switch (type) {
               case Criteria:
                  Criterion criterion = this.cellContentBuilder.buildCriterion(cell, column);
                  if (criterion != null) {
                     and.addCriterion(criterion);
                  }
                  break;
               case ConsolePrint:
                  localValue = cell.getValue();
                  if (localValue != null) {
                     ConsolePrintAction consolePrintAction = new ConsolePrintAction();
                     consolePrintAction.setPriority(1000 - column.getNum());
                     consolePrintAction.setValue(localValue);
                     rhs.addAction(consolePrintAction);
                  }
                  break;
               case Assignment:
                  localValue = cell.getValue();
                  if (localValue != null) {
                     if (column.isPredefine()) {
                        PredefineAssignAction predefineAssignAction = new PredefineAssignAction();
                        predefineAssignAction.setPriority(1000 - column.getNum());
                        predefineAssignAction.setValue(localValue);
                        predefineAssignAction.setDatatype(column.getPredefineDatatype());
                        predefineAssignAction.setUuid(column.getUuid());
                        predefineAssignAction.setName(column.getPredefineName());
                        predefineAssignAction.setVariableCategoryUuid(column.getPredefineVariableCategoryUuid());
                        predefineAssignAction.setVariableCategory(column.getPredefineVariableCategory());
                        predefineAssignAction.setPropertyUuid(column.getPredefinePropertyUuid());
                        predefineAssignAction.setPropertyName(column.getPredefinePropertyName());
                        predefineAssignAction.setPropertyDatatype(column.getPredefinePropertyDatatype());
                        predefineAssignAction.setPropertyLabel(column.getPredefinePropertyLabel());
                        rhs.addAction(predefineAssignAction);
                     } else {
                        VariableAssignAction variableAssignAction = new VariableAssignAction();
                        variableAssignAction.setPriority(1000 - column.getNum());
                        variableAssignAction.setValue(localValue);
                        variableAssignAction.setDatatype(column.getDatatype());
                        variableAssignAction.setVariableName(column.getVariableName());
                        variableAssignAction.setCategoryUuid(column.getCategoryUuid());
                        variableAssignAction.setUuid(column.getUuid());
                        variableAssignAction.setVariableLabel(column.getVariableLabel());
                        variableAssignAction.setVariableCategory(column.getVariableCategory());
                        variableAssignAction.setKeyLabel(column.getKeyLabel());
                        variableAssignAction.setKeyName(column.getKeyName());
                        variableAssignAction.setKeyCategoryUuid(column.getKeyCategoryUuid());
                        variableAssignAction.setKeyUuid(column.getKeyUuid());
                        rhs.addAction(variableAssignAction);
                     }
                  }
                  break;
               case ExecuteMethod:
                  Action action = cell.getAction();
                  if (action != null) {
                     AbstractAction abstractAction = (AbstractAction)action;
                     abstractAction.setPriority(1000 - column.getNum());
                     rhs.addAction(abstractAction);
                  }
            }
         }

         if (and.getCriterions() != null) {
            lhs.setCriterion(and);
         }
      }

      return rules;
   }

   private Cell resolveCell(DecisionTable decisionTable, int number, int number2) {
      Map cellMap = decisionTable.getCellMap();
      Cell cell = null;

      for (int index = number; index > -1; index--) {
         String cellKey = decisionTable.buildCellKey(index, number2);
         if (cellMap.containsKey(cellKey)) {
            cell = (Cell)cellMap.get(cellKey);
            break;
         }
      }

      if (cell == null) {
         throw new RuleException("Decision table cell[" + number + "," + number2 + "] not exist.");
      } else {
         return cell;
      }
   }

   public void setCellContentBuilder(CellContentBuilder cellContentBuilder) {
      this.cellContentBuilder = cellContentBuilder;
   }
}
