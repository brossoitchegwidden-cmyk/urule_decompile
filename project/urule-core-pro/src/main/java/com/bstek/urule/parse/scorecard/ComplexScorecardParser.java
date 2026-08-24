package com.bstek.urule.parse.scorecard;

import com.bstek.urule.Configure;
import com.bstek.urule.builder.RulesRebuilder;
import com.bstek.urule.exception.RuleException;
import com.bstek.urule.model.library.Datatype;
import com.bstek.urule.model.library.ResourceLibrary;
import com.bstek.urule.model.library.variable.Variable;
import com.bstek.urule.model.library.variable.VariableCategory;
import com.bstek.urule.model.library.variable.VariableData;
import com.bstek.urule.model.rule.Library;
import com.bstek.urule.model.rule.Value;
import com.bstek.urule.model.scorecard.AssignTargetType;
import com.bstek.urule.model.scorecard.ComplexColumn;
import com.bstek.urule.model.scorecard.ComplexColumnType;
import com.bstek.urule.model.scorecard.ComplexScorecardDefinition;
import com.bstek.urule.model.scorecard.ScoringType;
import com.bstek.urule.model.table.Cell;
import com.bstek.urule.model.table.Condition;
import com.bstek.urule.parse.LibrariesParser;
import com.bstek.urule.parse.table.CellParser;
import com.bstek.urule.parse.table.RowParser;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

public class ComplexScorecardParser extends LibrariesParser<ComplexScorecardDefinition> {
   private RowParser rowParser;
   private ComplexColumnParser columnParser;
   private CellParser cellParser;
   private RulesRebuilder rulesRebuilder;

   public ComplexScorecardDefinition parse(Element element) {
      ComplexScorecardDefinition complexScorecardDefinition = new ComplexScorecardDefinition();
      complexScorecardDefinition.setScoringType(ScoringType.valueOf(element.attributeValue("scoring-type")));
      complexScorecardDefinition.setScoringBean(element.attributeValue("custom-scoring-bean"));
      complexScorecardDefinition.setAssignTargetType(AssignTargetType.valueOf(element.attributeValue("assign-target-type")));
      complexScorecardDefinition.setVariableCategory(element.attributeValue("var-category"));
      complexScorecardDefinition.setVariableName(element.attributeValue("var"));
      complexScorecardDefinition.setVariableLabel(element.attributeValue("var-label"));
      complexScorecardDefinition.setKeyLabel(element.attributeValue("key-label"));
      complexScorecardDefinition.setKeyName(element.attributeValue("key-name"));
      complexScorecardDefinition.setKeyUuid(element.attributeValue("key-uuid"));
      complexScorecardDefinition.setCategoryUuid(element.attributeValue("category-uuid"));
      complexScorecardDefinition.setUuid(element.attributeValue("uuid"));
      String text = element.attributeValue("datatype");
      if (StringUtils.isNotBlank(text)) {
         complexScorecardDefinition.setDatatype(Datatype.valueOf(text));
      }

      String text2 = element.attributeValue("salience");
      if (StringUtils.isNotEmpty(text2)) {
         complexScorecardDefinition.setSalience(Integer.valueOf(text2));
      }

      String text3 = element.attributeValue("effective-date");
      SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Configure.getDateFormat());
      if (StringUtils.isNotEmpty(text3)) {
         try {
            complexScorecardDefinition.setEffectiveDate(simpleDateFormat.parse(text3));
         } catch (ParseException parseException) {
            throw new RuleException(parseException);
         }
      }

      String text4 = element.attributeValue("expires-date");
      if (StringUtils.isNotEmpty(text4)) {
         try {
            complexScorecardDefinition.setExpiresDate(simpleDateFormat.parse(text4));
         } catch (ParseException parseException2) {
            throw new RuleException(parseException2);
         }
      }

      String text5 = element.attributeValue("enabled");
      if (StringUtils.isNotEmpty(text5)) {
         complexScorecardDefinition.setEnabled(Boolean.valueOf(text5));
      }

      String text6 = element.attributeValue("debug");
      if (StringUtils.isNotEmpty(text6)) {
         complexScorecardDefinition.setDebug(Boolean.valueOf(text6));
      }

      for (Object objectValue : element.elements()) {
         if (objectValue != null && objectValue instanceof Element) {
            Element element2 = (Element)objectValue;
            String name = element2.getName();
            if (this.rowParser.support(name)) {
               complexScorecardDefinition.addRow(this.rowParser.parse(element2));
            } else if (this.columnParser.support(name)) {
               complexScorecardDefinition.addColumn(this.columnParser.parse(element2));
            } else if (this.cellParser.support(name)) {
               complexScorecardDefinition.addCell(this.cellParser.parse(element2));
            }

            Library library = this.parseLibrary(element2);
            if (library != null) {
               complexScorecardDefinition.addLibrary(library);
            } else if (name.equals("quick-test-data")) {
               String textTrim = element2.getTextTrim();
               complexScorecardDefinition.setQuickTestData(textTrim);
            } else if (name.equals("remark")) {
               complexScorecardDefinition.setRemark(element2.getText());
            }
         }
      }

      this.parseDefinition(complexScorecardDefinition);
      return complexScorecardDefinition;
   }

   private void parseDefinition(ComplexScorecardDefinition complexScorecardDefinition) {
      List libraries = complexScorecardDefinition.getLibraries();
      ResourceLibrary resourceLibrary = this.rulesRebuilder.getResourceLibraryBuilder().buildResourceLibrary(libraries, complexScorecardDefinition.getPredefines());
      if (complexScorecardDefinition.getAssignTargetType() == AssignTargetType.parameter && StringUtils.isBlank(complexScorecardDefinition.getKeyUuid())) {
         Variable parameterByUuid = resourceLibrary.getParameterByUuid(complexScorecardDefinition.getKeyUuid(), complexScorecardDefinition.getKeyName(), complexScorecardDefinition.getKeyLabel());
         if (parameterByUuid != null && parameterByUuid.getType() == Datatype.Object) {
            complexScorecardDefinition.setKeyUuid(parameterByUuid.getUuid());
         }
      }

      List columns = complexScorecardDefinition.getColumns();
      if (columns != null) {
         for (ComplexColumn complexColumn : (Iterable<ComplexColumn>)(Iterable<?>)(columns)) {
            ComplexColumnType type = complexColumn.getType();
            if (type.equals(ComplexColumnType.Criteria)) {
               VariableCategory variableCategoryByUuid = resourceLibrary.getVariableCategoryByUuid(complexColumn.getUuid());
               if (variableCategoryByUuid != null) {
                  complexColumn.setVariableCategory(variableCategoryByUuid.getName());
               }
            }
         }
      }

      for (Cell cell : complexScorecardDefinition.getCellMap().values()) {
         if (cell.getAction() != null) {
            this.rulesRebuilder.rebuildAction(cell.getAction(), resourceLibrary, false);
         } else if (cell.getValue() != null) {
            this.rulesRebuilder.rebuildValue(cell.getValue(), resourceLibrary, false);
         } else if (cell.getJoint() != null && cell.getJoint() != null && cell.getJoint().getJunction() != null) {
            List conditions = cell.getJoint().getConditions();
            if (conditions != null) {
               for (Condition condition : (Iterable<Condition>)(Iterable<?>)(conditions)) {
                  Value localValue = condition.getValue();
                  if (localValue != null) {
                     this.rulesRebuilder.rebuildValue(localValue, resourceLibrary, false);
                  }
               }
            }
         }

         if (cell.getUuid() != null) {
            String text = this.parseDefinition(columns, cell.getCol());
            VariableData variableData = null;
            if ("参数".equals(text)) {
               Variable parameterByUuid2 = resourceLibrary.getParameterByUuid(cell.getKeyUuid(), cell.getKeyName(), cell.getKeyLabel());
               if (parameterByUuid2 != null && parameterByUuid2.getType() == Datatype.Object) {
                  variableData = resourceLibrary.getVariableByUuid(parameterByUuid2.getDataType(), cell.getUuid());
                  if (StringUtils.isBlank(cell.getKeyUuid())) {
                     cell.setKeyUuid(parameterByUuid2.getUuid());
                  }
               }

               if (variableData == null) {
                  variableData = resourceLibrary.getVariableByName(text, cell.getVariableName());
               }

               if (variableData != null) {
                  cell.setDatatype(variableData.getVariable().getType());
                  cell.setVariableLabel(variableData.getVariable().getLabel());
                  cell.setVariableName(variableData.getVariable().getName());
               }
            } else {
               variableData = resourceLibrary.getVariableByUuid(text, cell.getUuid());
               if (variableData != null) {
                  if (cell.getKeyCategoryUuid() != null) {
                     VariableData variableByUuid = resourceLibrary.getVariableByUuid(cell.getKeyCategoryUuid(), cell.getKeyUuid());
                     cell.setDatatype(variableByUuid.getVariable().getType());
                     cell.setVariableName(variableByUuid.getVariable().getName());
                     cell.setVariableLabel(variableByUuid.getVariable().getLabel());
                     cell.setKeyLabel(variableData.getVariable().getLabel());
                     cell.setKeyName(variableData.getVariable().getName());
                  } else {
                     cell.setDatatype(variableData.getVariable().getType());
                     cell.setVariableName(variableData.getVariable().getName());
                     cell.setVariableLabel(variableData.getVariable().getLabel());
                  }
               }
            }
         }
      }

      Collections.sort(complexScorecardDefinition.getColumns(), new ComplexScorecardColumnComparator());
      Collections.sort(complexScorecardDefinition.getRows(), new ComplexScorecardRowComparator());
   }

   private String parseDefinition(List<ComplexColumn> complexColumns, int number) {
      if (complexColumns != null) {
         for (ComplexColumn complexColumn : complexColumns) {
            if (complexColumn.getNum() == number) {
               return complexColumn.getUuid();
            }
         }
      }

      return null;
   }

   @Override
   public boolean support(String name) {
      return name.equals("complex-scorecard");
   }

   public void setColumnParser(ComplexColumnParser columnParser) {
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
}
