package com.bstek.urule.console.editor.decision.crosstab;

import com.bstek.urule.Utils;
import com.bstek.urule.console.InfoException;
import com.bstek.urule.console.editor.ExcelImportUtils;
import com.bstek.urule.console.editor.ExcelSupport;
import com.bstek.urule.console.editor.decision.CellContent;
import com.bstek.urule.console.editor.decision.PredefineRow;
import com.bstek.urule.console.util.StringUtils;
import com.bstek.urule.console.xml.DocumentHelper;
import com.bstek.urule.dsl.DSLRuleSetBuilder;
import com.bstek.urule.model.crosstab.CrosstabDefinition;
import com.bstek.urule.model.library.variable.Variable;
import com.bstek.urule.model.library.variable.VariableCategory;
import com.bstek.urule.model.rule.ArithmeticType;
import com.bstek.urule.model.rule.ComplexArithmetic;
import com.bstek.urule.model.rule.SimpleValue;
import com.bstek.urule.model.rule.Value;
import com.bstek.urule.model.rule.VariableCategoryValue;
import com.bstek.urule.model.rule.VariableValue;
import com.bstek.urule.model.rule.lhs.Criteria;
import com.bstek.urule.model.rule.lhs.Criterion;
import com.bstek.urule.model.rule.lhs.Junction;
import com.bstek.urule.model.rule.lhs.Or;
import com.bstek.urule.parse.deserializer.CrosstableDeserializer;
import java.util.HashMap;
import java.util.List;
import org.apache.commons.lang.StringEscapeUtils;
import org.dom4j.Document;

/** Converts imported cross-table data into the canonical crosstab XML model. */
public class CrossTableXmlBuilder {
   private CrossData crossData;
   private ExcelSupport excelSupport;
   private DSLRuleSetBuilder dSLRuleSetBuilder;
   private CrosstableDeserializer crosstableDeserializer;

   public CrossTableXmlBuilder(CrossData data) {
      this.crossData = data;
      this.excelSupport = new ExcelSupport();
      this.dSLRuleSetBuilder = (DSLRuleSetBuilder)Utils.getApplicationContext().getBean("urule.dslRuleSetBuilder");
      this.crosstableDeserializer = (CrosstableDeserializer)Utils.getApplicationContext().getBean("urule.crosstableDeserializer");
   }

   public CrosstabDefinition doBuild() throws Exception {
      String xml = this.buildXml();
      Document document = DocumentHelper.parseText(xml);
      return this.crosstableDeserializer.deserialize(document.getRootElement());
   }

   private String buildNestedParameterAttributes(Variable variable, VariableCategory keyCategory, Variable keyVariable) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(" bundle-data-type=\"parameter\" category-uuid=\"参数\" uuid=\"" + variable.getUuid() + "\" var-category=\"" + "参数" + "\"");
      stringBuilder.append(" var=\"" + variable.getName() + "\" var-label=\"" + variable.getLabel() + "\" dataType=\"" + variable.getDataType() + "\"");
      stringBuilder.append(" key-category-uuid=\"" + keyCategory.getUuid() + "\" key-uuid=\"" + keyVariable.getUuid() + "\" key-label=\"" + keyVariable.getLabel() + "\" key-name=\"" + keyVariable.getName() + "\"");
      return stringBuilder.toString();
   }

   private String buildParameterAttributes(Variable variable) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(" bundle-data-type=\"parameter\" category-uuid=\"参数\" uuid=\"" + variable.getUuid() + "\" var-category=\"" + "参数" + "\"");
      stringBuilder.append(" var=\"" + variable.getName() + "\" var-label=\"" + variable.getLabel() + "\" dataType=\"" + variable.getDataType() + "\"");
      return stringBuilder.toString();
   }

   private String buildVariableAttributes(String bundleDataType, String variableCategory, Variable variable) {
      return " bundle-data-type=\"" + bundleDataType + "\" var-category=\"" + variableCategory + "\" var=\"" + variable.getName() + "\" var-label=\"" + variable.getLabel() + "\" datatype=\"" + variable.getType().name() + "\"";
   }

   private String buildPredefineAttributes(String predefineUuid, String propertyUuid) {
      if (StringUtils.isBlank(propertyUuid)) {
         propertyUuid = "";
      }

      return " bundle-data-type=\"predefine\" predefine-uuid=\"" + predefineUuid + "\" predefine-property-uuid=\"" + propertyUuid + "\"";
   }

   private String buildXml() throws Exception {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
      CrossHeader header = this.crossData.getHeader();
      stringBuilder.append("<crosstab");
      ExcelImportUtils.builderProperties(this.excelSupport, stringBuilder, this.crossData.getProperties(), true);
      stringBuilder.append(">");
      ExcelImportUtils.builderRemark(stringBuilder, this.crossData.getProperties());
      HashMap valuesByKey = new HashMap();
      ExcelImportUtils.builderPredefineXml(stringBuilder, ExcelImportUtils.getPredefineGroupPriority(this.crossData.getProperties()), this.crossData.getPredefineRows(), this.crossData.getPredefineNameMap(), this.excelSupport, valuesByKey);
      String content = header.getContent();
      content = content == null ? "表头" : content;
      stringBuilder.append("<header rowspan=\"" + header.getRowSpan() + "\" colspan=\"" + header.getColSpan() + "\"><![CDATA[" + content + "]]></header>");

      for(CrossRow crossRow : (Iterable<CrossRow>)(Iterable<?>)(this.crossData.getRows())) {
         stringBuilder.append("<row number=\"" + crossRow.getNumber() + "\" type=\"" + crossRow.getType() + "\"");
         String content2 = crossRow.getContent();
         if (StringUtils.isNotBlank(content2)) {
            boolean flag = crossRow.isPredefine();
            if (flag) {
               String[] parts = content2.split("\\.");
               PredefineRow predefineRow = (PredefineRow)this.crossData.getPredefineNameMap().get(parts[0]);
               if (parts.length == 2) {
                  parts[0] = predefineRow.getType();
                  Variable variable = this.excelSupport.findVariable(parts);
                  stringBuilder.append(this.buildPredefineAttributes(predefineRow.getUuid(), variable.getUuid()));
               } else {
                  stringBuilder.append(this.buildPredefineAttributes(predefineRow.getUuid(), ""));
               }
            } else {
               String[] values = this.splitHeaderReference(content2);
               String text = values[0];
               Variable variable2 = this.excelSupport.findVariable(values, true);
               VariableCategory variableCategory = null;
               if (ExcelSupport.isParameter(text)) {
                  if (values.length > 2) {
                     variableCategory = this.excelSupport.findVariableCategoryByUUID(variable2.getDataType());
                  } else {
                     variable2 = this.excelSupport.findVariable(values, true);
                  }
               } else {
                  variable2 = this.excelSupport.findVariable(values, true);
               }

               String text2 = "variable";
               if (ExcelSupport.isParameter(text)) {
                  text2 = "parameter";
                  if (values.length > 2 && variableCategory != null) {
                     Variable variable3 = (Variable)variableCategory.getVariableLabels().get(values[2]);
                     stringBuilder.append(this.buildNestedParameterAttributes(variable3, variableCategory, variable2));
                  } else {
                     stringBuilder.append(this.buildParameterAttributes(variable2));
                  }
               } else {
                  stringBuilder.append(this.buildVariableAttributes(text2, text, variable2));
               }
            }
         }

         stringBuilder.append("/>");
      }

      for(CrossColumn crossColumn : (Iterable<CrossColumn>)(Iterable<?>)(this.crossData.getColumns())) {
         stringBuilder.append("<column number=\"" + crossColumn.getNumber() + "\" type=\"" + crossColumn.getType() + "\"");
         String content3 = crossColumn.getContent();
         if (StringUtils.isNotBlank(content3)) {
            boolean flag2 = crossColumn.isPredefine();
            if (flag2) {
               String[] parts2 = content3.split("\\.");
               PredefineRow predefineRow2 = (PredefineRow)this.crossData.getPredefineNameMap().get(parts2[0]);
               if (parts2.length == 2) {
                  parts2[0] = predefineRow2.getType();
                  Variable variable4 = this.excelSupport.findVariable(parts2);
                  stringBuilder.append(this.buildPredefineAttributes(predefineRow2.getUuid(), variable4.getUuid()));
               } else {
                  stringBuilder.append(this.buildPredefineAttributes(predefineRow2.getUuid(), ""));
               }
            } else {
               String[] values2 = this.splitHeaderReference(content3);
               String text3 = values2[0];
               Variable variable5 = this.excelSupport.findVariable(values2, true);
               VariableCategory variableCategoryByUUID = null;
               if (ExcelSupport.isParameter(text3)) {
                  if (values2.length > 2) {
                     variableCategoryByUUID = this.excelSupport.findVariableCategoryByUUID(variable5.getDataType());
                  } else {
                     variable5 = this.excelSupport.findVariable(values2, true);
                  }
               } else {
                  variable5 = this.excelSupport.findVariable(values2, true);
               }

               String text4 = "variable";
               if (ExcelSupport.isParameter(text3)) {
                  text4 = "parameter";
                  if (values2.length > 2 && variableCategoryByUUID != null) {
                     Variable variable6 = (Variable)variableCategoryByUUID.getVariableLabels().get(values2[2]);
                     stringBuilder.append(this.buildNestedParameterAttributes(variable6, variableCategoryByUUID, variable5));
                  } else {
                     stringBuilder.append(this.buildParameterAttributes(variable5));
                  }
               } else {
                  stringBuilder.append(this.buildVariableAttributes(text4, text3, variable5));
               }
            }
         }

         stringBuilder.append("/>");
      }

      for(CellContent cellContent : (Iterable<CellContent>)(Iterable<?>)(this.crossData.getCells())) {
         String type = cellContent.getType();
         String text5 = "condition-cell";
         if (type.equals("value")) {
            text5 = "value-cell";
         }

         stringBuilder.append("<");
         stringBuilder.append(text5);
         int span = 1;
         int span2 = 1;
         boolean flag3 = false;
         if (cellContent.getRow() <= this.crossData.getHeader().getRowSpan()) {
            span2 = cellContent.getSpan();
            flag3 = true;
         }

         if (cellContent.getCol() <= this.crossData.getHeader().getColSpan()) {
            span = cellContent.getSpan();
            flag3 = true;
         }

         stringBuilder.append(" row=\"" + cellContent.getRow() + "\" col=\"" + cellContent.getCol() + "\"");
         if (flag3) {
            stringBuilder.append(" rowspan=\"" + span + "\" colspan=\"" + span2 + "\"");
         }

         stringBuilder.append(">");
         String content4 = cellContent.getContent();
         if (StringUtils.isNotBlank(content4)) {
            if (flag3) {
               Criterion criterion = this.dSLRuleSetBuilder.buildCriterion(content4);
               stringBuilder.append(this.buildCriterionXml(criterion));
            } else {
               content4 = StringEscapeUtils.escapeXml(content4);
               stringBuilder.append(ExcelImportUtils.buildContentXml(content4, true));
            }
         }

         stringBuilder.append("");
         stringBuilder.append("</");
         stringBuilder.append(text5);
         stringBuilder.append(">");
      }

      ExcelImportUtils.builderLibraryXml(stringBuilder, this.excelSupport);
      stringBuilder.append("");
      stringBuilder.append("");
      stringBuilder.append("");
      stringBuilder.append("");
      stringBuilder.append("");
      stringBuilder.append("</crosstab>");
      return stringBuilder.toString();
   }

   private String buildCriterionXml(Criterion criterion) {
      StringBuilder stringBuilder = new StringBuilder();
      if (criterion instanceof Junction) {
         Junction junction = (Junction)criterion;
         List criterions = junction.getCriterions();
         String text = "and";
         if (junction instanceof Or) {
            text = "or";
         }

         stringBuilder.append("<joint type=\"" + text + "\">");
         if (criterions != null) {
            for(Criterion criterion2 : (Iterable<Criterion>)(Iterable<?>)(criterions)) {
               if (criterion2 instanceof Criteria) {
                  Criteria criteria = (Criteria)criterion2;
                  stringBuilder.append("<condition op=\"" + criteria.getOp().name() + "\">");
                  Value localValue = criteria.getValue();
                  stringBuilder.append(this.buildValueXml(localValue));
                  stringBuilder.append("</condition>");
               }
            }
         }
      } else {
         stringBuilder.append("<joint type=\"and\">");
         Criteria criteria2 = (Criteria)criterion;
         stringBuilder.append("<condition op=\"" + criteria2.getOp().name() + "\">");
         Value localValue2 = criteria2.getValue();
         String valueXml = this.buildValueXml(localValue2);
         if (valueXml != null) {
            stringBuilder.append(valueXml);
         }

         stringBuilder.append("</condition>");
      }

      stringBuilder.append("</joint>");
      return stringBuilder.toString();
   }

   private String buildValueXml(Value value) {
      if (value == null) {
         return null;
      } else {
         StringBuilder stringBuilder = new StringBuilder();
         if (value instanceof SimpleValue) {
            SimpleValue simpleValue = (SimpleValue)value;
            String text = StringEscapeUtils.escapeXml(simpleValue.getContent());
            stringBuilder.append(ExcelImportUtils.buildContentXml(text));
         } else if (value instanceof VariableCategoryValue) {
            VariableCategoryValue variableCategoryValue = (VariableCategoryValue)value;
            String variableCategory = variableCategoryValue.getVariableCategory();
            String text2 = StringEscapeUtils.escapeXml(variableCategory);
            stringBuilder.append(ExcelImportUtils.buildContentXml(text2));
         } else if (value instanceof VariableValue) {
            VariableValue variableValue = (VariableValue)value;
            String text3 = variableValue.getVariableCategory() + "." + variableValue.getVariableLabel();
            text3 = StringEscapeUtils.escapeXml(text3);
            stringBuilder.append(ExcelImportUtils.buildContentXml(text3));
         } else {
            stringBuilder.append(ExcelImportUtils.buildContentXml((String)null));
         }

         ComplexArithmetic arithmetic = value.getArithmetic();
         if (arithmetic == null) {
            stringBuilder.append("</value>");
            return stringBuilder.toString();
         } else {
            ArithmeticType type = arithmetic.getType();
            stringBuilder.append("<complex-arith type=\"" + type.name() + "\">");
            stringBuilder.append(this.buildValueXml(arithmetic.getValue()));
            stringBuilder.append("</complex-arith>");
            stringBuilder.append("</value>");
            return stringBuilder.toString();
         }
      }
   }

   private String[] splitHeaderReference(String headerText) {
      String[] parts = headerText.split("\\.");
      if (parts.length < 2) {
         throw new InfoException("表头[" + headerText + "]不合法！");
      } else {
         return parts;
      }
   }
}
