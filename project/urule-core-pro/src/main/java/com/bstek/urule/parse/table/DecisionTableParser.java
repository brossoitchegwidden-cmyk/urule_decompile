package com.bstek.urule.parse.table;

import com.bstek.urule.Configure;
import com.bstek.urule.builder.RulesRebuilder;
import com.bstek.urule.exception.RuleException;
import com.bstek.urule.model.library.Datatype;
import com.bstek.urule.model.library.ResourceLibrary;
import com.bstek.urule.model.library.variable.VariableCategory;
import com.bstek.urule.model.library.variable.VariableData;
import com.bstek.urule.model.rule.Library;
import com.bstek.urule.model.rule.Predefine;
import com.bstek.urule.model.rule.PredefineGroupDefinition;
import com.bstek.urule.model.rule.Value;
import com.bstek.urule.model.rule.lhs.Junction;
import com.bstek.urule.model.table.Cell;
import com.bstek.urule.model.table.Column;
import com.bstek.urule.model.table.Condition;
import com.bstek.urule.model.table.DecisionTable;
import com.bstek.urule.parse.LibrariesParser;
import com.bstek.urule.parse.PredefinesParser;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

public class DecisionTableParser extends LibrariesParser<DecisionTable> {
   private RowParser rowParser;
   private ColumnParser columnParser;
   private CellParser cellParser;
   private RulesRebuilder rulesRebuilder;
   private PredefinesParser predefinesParser;

   public DecisionTable parse(Element element) {
      DecisionTable decisionTable = new DecisionTable();
      String text = element.attributeValue("salience");
      if (StringUtils.isNotEmpty(text)) {
         decisionTable.setSalience(Integer.valueOf(text));
      }

      decisionTable.setMutexGroup(element.attributeValue("mutex-group"));
      String text2 = element.attributeValue("effective-date");
      SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Configure.getDateFormat());
      if (StringUtils.isNotEmpty(text2)) {
         try {
            decisionTable.setEffectiveDate(simpleDateFormat.parse(text2));
         } catch (ParseException parseException) {
            throw new RuleException(parseException);
         }
      }

      String text3 = element.attributeValue("expires-date");
      if (StringUtils.isNotEmpty(text3)) {
         try {
            decisionTable.setExpiresDate(simpleDateFormat.parse(text3));
         } catch (ParseException parseException2) {
            throw new RuleException(parseException2);
         }
      }

      String text4 = element.attributeValue("enabled");
      if (StringUtils.isNotEmpty(text4)) {
         decisionTable.setEnabled(Boolean.valueOf(text4));
      }

      String text5 = element.attributeValue("debug");
      if (StringUtils.isNotEmpty(text5)) {
         decisionTable.setDebug(Boolean.valueOf(text5));
      }

      for (Object objectValue : element.elements()) {
         if (objectValue != null && objectValue instanceof Element) {
            Element element2 = (Element)objectValue;
            String name = element2.getName();
            if (this.rowParser.support(name)) {
               decisionTable.addRow(this.rowParser.parse(element2));
            } else if (name.equals("quick-test-data")) {
               String textTrim = element2.getTextTrim();
               decisionTable.setQuickTestData(textTrim);
            } else if (this.columnParser.support(name)) {
               decisionTable.addColumn(this.columnParser.parse(element2));
            } else if (this.cellParser.support(name)) {
               decisionTable.addCell(this.cellParser.parse(element2));
            }

            Library library = this.parseLibrary(element2);
            if (library != null) {
               decisionTable.addLibrary(library);
            } else if (this.predefinesParser.support(name)) {
               PredefineGroupDefinition predefineGroupDefinition = this.predefinesParser.parse(element2);
               decisionTable.setPredefineGroup(predefineGroupDefinition);
            } else if (name.equals("remark")) {
               decisionTable.setRemark(element2.getText());
            }
         }
      }

      this.processDecisionTable(decisionTable);
      return decisionTable;
   }

   private void processDecisionTable(DecisionTable decisionTable) {
      List libraries = decisionTable.getLibraries();
      List predefines = null;
      PredefineGroupDefinition predefineGroup = decisionTable.getPredefineGroup();
      if (predefineGroup != null) {
         predefines = predefineGroup.getPredefines();
      }

      ResourceLibrary resourceLibrary = this.rulesRebuilder.getResourceLibraryBuilder().buildResourceLibrary(libraries, predefines);
      if (predefineGroup != null && predefineGroup.getPredefines() != null) {
         for (Predefine predefine : predefineGroup.getPredefines()) {
            Junction junction = predefine.getJunction();
            if (junction != null) {
               this.rulesRebuilder.rebuildCriterion(junction, resourceLibrary, false);
            }

            Value localValue = predefine.getValue();
            if (localValue != null) {
               this.rulesRebuilder.rebuildValue(localValue, resourceLibrary, false);
            }
         }
      }

      for (Cell cell : decisionTable.getCellMap().values()) {
         if (cell.getAction() != null) {
            this.rulesRebuilder.rebuildAction(cell.getAction(), resourceLibrary, false);
         } else if (cell.getValue() != null) {
            this.rulesRebuilder.rebuildValue(cell.getValue(), resourceLibrary, false);
         } else if (cell.getJoint() != null && cell.getJoint() != null && cell.getJoint().getJunction() != null) {
            List conditions = cell.getJoint().getConditions();
            if (conditions != null) {
               for (Condition condition : (Iterable<Condition>)(Iterable<?>)(conditions)) {
                  Value localValue2 = condition.getValue();
                  if (localValue2 != null) {
                     this.rulesRebuilder.rebuildValue(localValue2, resourceLibrary, false);
                  }
               }
            }
         }
      }

      for (Column column : decisionTable.getColumns()) {
         if (column.isPredefine()) {
            Predefine predefine2 = resourceLibrary.getPredefine(column.getUuid());
            if (predefine2 != null) {
               column.setPredefineName(predefine2.getName());
               String type = predefine2.getType();
               if (Datatype.isType(type)) {
                  column.setPredefineDatatype(Datatype.valueOf(type));
               } else {
                  String predefinePropertyUuid = column.getPredefinePropertyUuid();
                  if (predefinePropertyUuid != null) {
                     VariableData variableByUuid = resourceLibrary.getVariableByUuid(type, predefinePropertyUuid);
                     column.setPredefineVariableCategory(variableByUuid.getCategory().getName());
                     column.setPredefinePropertyName(variableByUuid.getVariable().getName());
                     column.setPredefinePropertyDatatype(variableByUuid.getVariable().getType());
                     column.setPredefinePropertyLabel(variableByUuid.getVariable().getLabel());
                     column.setPredefineVariableCategoryUuid(variableByUuid.getCategory().getUuid());
                  } else {
                     VariableCategory variableCategoryByUuid = resourceLibrary.getVariableCategoryByUuid(type);
                     column.setPredefineVariableCategory(variableCategoryByUuid.getName());
                     column.setPredefineVariableCategoryUuid(variableCategoryByUuid.getUuid());
                  }
               }
            }
         }

         String variableCategory = column.getVariableCategory();
         String variableName = column.getVariableName();
         if (!StringUtils.isBlank(variableCategory) && !StringUtils.isBlank(variableName)) {
            VariableData variableByUuid2 = resourceLibrary.getVariableByUuid(column.getCategoryUuid(), column.getUuid());
            if (variableByUuid2 == null) {
               variableByUuid2 = resourceLibrary.getVariableByName(column.getVariableCategory(), column.getVariableName());
            }

            if (variableByUuid2 == null) {
               throw new RuleException("决策表条件列头变量【" + column.getVariableCategory() + "." + column.getVariableLabel() + "】未在库文件中定义.");
            }

            if (column.getKeyUuid() == null) {
               column.setDatatype(variableByUuid2.getVariable().getType());
               column.setVariableName(variableByUuid2.getVariable().getName());
               column.setVariableLabel(variableByUuid2.getVariable().getLabel());
               column.setVariableCategory(variableByUuid2.getCategory().getName());
               if (column.getCategoryUuid() == null) {
                  column.setCategoryUuid(variableByUuid2.getCategory().getUuid());
                  column.setUuid(variableByUuid2.getVariable().getUuid());
               }
            } else {
               column.setKeyLabel(variableByUuid2.getVariable().getLabel());
               column.setKeyName(variableByUuid2.getVariable().getName());
               variableByUuid2 = resourceLibrary.getVariableByUuid(column.getKeyCategoryUuid(), column.getKeyUuid());
               if (variableByUuid2 != null) {
                  column.setVariableName(variableByUuid2.getVariable().getName());
                  column.setVariableLabel(variableByUuid2.getVariable().getLabel());
                  column.setDatatype(variableByUuid2.getVariable().getType());
               }
            }
         }
      }

      Collections.sort(decisionTable.getColumns(), new DecisionTableColumnComparator());
      Collections.sort(decisionTable.getRows(), new DecisionTableRowComparator());
   }

   @Override
   public boolean support(String name) {
      return name.equals("decision-table");
   }

   public void setColumnParser(ColumnParser columnParser) {
      this.columnParser = columnParser;
   }

   public void setRowParser(RowParser rowParser) {
      this.rowParser = rowParser;
   }

   public void setCellParser(CellParser cellParser) {
      this.cellParser = cellParser;
   }

   public void setRulesRebuilder(RulesRebuilder rulesRebuilder) {
      this.rulesRebuilder = rulesRebuilder;
   }

   public void setPredefinesParser(PredefinesParser predefinesParser) {
      this.predefinesParser = predefinesParser;
   }
}
