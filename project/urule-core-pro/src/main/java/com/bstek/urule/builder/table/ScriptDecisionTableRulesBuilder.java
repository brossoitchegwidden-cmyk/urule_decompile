package com.bstek.urule.builder.table;

import com.bstek.urule.dsl.DSLRuleSetBuilder;
import com.bstek.urule.exception.RuleException;
import com.bstek.urule.model.rule.Library;
import com.bstek.urule.model.rule.LibraryType;
import com.bstek.urule.model.rule.RuleSet;
import com.bstek.urule.model.table.Column;
import com.bstek.urule.model.table.ColumnType;
import com.bstek.urule.model.table.Row;
import com.bstek.urule.model.table.ScriptCell;
import com.bstek.urule.model.table.ScriptDecisionTable;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;

public class ScriptDecisionTableRulesBuilder {
   private CellScriptDSLBuilder cellScriptDSLBuilder;
   private DSLRuleSetBuilder dslRuleSetBuilder;

   public RuleSet buildRules(ScriptDecisionTable table, String path) throws IOException {
      List rows = table.getRows();
      List columns = table.getColumns();
      List libraries = table.getLibraries();
      StringBuffer stringBuffer = this.buildLibraryImports(libraries);

      for (Row row : (Iterable<Row>)(Iterable<?>)(rows)) {
         stringBuffer.append("rule \"r" + row.getNum() + "\"");
         stringBuffer.append("\r\n");
         stringBuffer.append("if");
         stringBuffer.append("\r\n");
         StringBuffer stringBuffer2 = new StringBuffer();
         StringBuffer stringBuffer3 = new StringBuffer();

         for (Column column : (Iterable<Column>)(Iterable<?>)(columns)) {
            ScriptCell scriptCell = this.resolveScriptCell(table, row.getNum(), column.getNum());
            String script = scriptCell.getScript();
            if (!StringUtils.isBlank(script)) {
               ColumnType type = column.getType();
               switch (type) {
                  case Criteria:
                     String text = column.getVariableCategory() + "." + column.getVariableLabel();
                     String criteriaScript = this.cellScriptDSLBuilder.buildCriteriaScript(script, text);
                     if (!StringUtils.isBlank(criteriaScript)) {
                        criteriaScript = criteriaScript.trim();
                        if (stringBuffer2.length() > 1) {
                           stringBuffer2.append(" and ");
                        }

                        if (!criteriaScript.startsWith("(")) {
                           criteriaScript = "(" + criteriaScript + ")";
                        }

                        stringBuffer2.append(criteriaScript);
                     }
                     break;
                  case ConsolePrint:
                     stringBuffer3.append("out(" + script + ");\r\n");
                     break;
                  case Assignment:
                     String text2 = column.getVariableCategory() + "." + column.getVariableLabel();
                     stringBuffer3.append(text2 + " = " + script + ";\r\n");
                     break;
                  case ExecuteMethod:
                     stringBuffer3.append(script + ";\r\n");
               }
            }
         }

         stringBuffer.append(stringBuffer2);
         stringBuffer.append("\r\n");
         stringBuffer.append("then");
         stringBuffer.append("\r\n");
         stringBuffer.append(stringBuffer3);
         stringBuffer.append("\r\n");
         stringBuffer.append("end;");
         stringBuffer.append("\r\n");
      }

      return this.dslRuleSetBuilder.build(stringBuffer.toString(), path);
   }

   private StringBuffer buildLibraryImports(List<Library> libraries) {
      StringBuffer stringBuffer = new StringBuffer();

      for (Library library : libraries) {
         LibraryType type = library.getType();
         switch (type) {
            case Action:
               stringBuffer.append("importActionLibrary \"" + library.getPath() + "\";\r\n");
               break;
            case Constant:
               stringBuffer.append("importConstantLibrary \"" + library.getPath() + "\";\r\n");
               break;
            case Parameter:
               stringBuffer.append("importParameterLibrary \"" + library.getPath() + "\";\r\n");
               break;
            case Variable:
               stringBuffer.append("importVariableLibrary \"" + library.getPath() + "\";\r\n");
         }
      }

      return stringBuffer;
   }

   private ScriptCell resolveScriptCell(ScriptDecisionTable scriptDecisionTable, int number, int number2) {
      Map cellMap = scriptDecisionTable.getCellMap();
      ScriptCell scriptCell = null;

      for (int index = number; index > -1; index--) {
         String cellKey = scriptDecisionTable.buildCellKey(index, number2);
         if (cellMap.containsKey(cellKey)) {
            scriptCell = (ScriptCell)cellMap.get(cellKey);
            break;
         }
      }

      if (scriptCell == null) {
         throw new RuleException("Decision table cell[" + number + "," + number2 + "] not exist.");
      } else {
         return scriptCell;
      }
   }

   public void setCellScriptDSLBuilder(CellScriptDSLBuilder cellScriptDSLBuilder) {
      this.cellScriptDSLBuilder = cellScriptDSLBuilder;
   }

   public void setDslRuleSetBuilder(DSLRuleSetBuilder dslRuleSetBuilder) {
      this.dslRuleSetBuilder = dslRuleSetBuilder;
   }
}
