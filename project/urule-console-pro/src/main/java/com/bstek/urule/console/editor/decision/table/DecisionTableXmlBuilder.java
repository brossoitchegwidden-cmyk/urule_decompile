package com.bstek.urule.console.editor.decision.table;

import com.bstek.urule.Utils;
import com.bstek.urule.console.InfoException;
import com.bstek.urule.console.editor.ExcelImportUtils;
import com.bstek.urule.console.editor.ExcelSupport;
import com.bstek.urule.console.editor.decision.CellContent;
import com.bstek.urule.console.editor.decision.PredefineRow;
import com.bstek.urule.console.xml.DocumentHelper;
import com.bstek.urule.dsl.DSLRuleSetBuilder;
import com.bstek.urule.model.library.action.Method;
import com.bstek.urule.model.library.action.SpringBean;
import com.bstek.urule.model.library.constant.Constant;
import com.bstek.urule.model.library.constant.ConstantCategory;
import com.bstek.urule.model.library.variable.Variable;
import com.bstek.urule.model.library.variable.VariableCategory;
import com.bstek.urule.model.rule.AbstractValue;
import com.bstek.urule.model.rule.ArithmeticType;
import com.bstek.urule.model.rule.ComplexArithmetic;
import com.bstek.urule.model.rule.MethodValue;
import com.bstek.urule.model.rule.Parameter;
import com.bstek.urule.model.rule.ParameterValue;
import com.bstek.urule.model.rule.ParenValue;
import com.bstek.urule.model.rule.SimpleValue;
import com.bstek.urule.model.rule.Value;
import com.bstek.urule.model.rule.VariableCategoryValue;
import com.bstek.urule.model.rule.VariableValue;
import com.bstek.urule.model.rule.lhs.Criteria;
import com.bstek.urule.model.rule.lhs.Criterion;
import com.bstek.urule.model.rule.lhs.Junction;
import com.bstek.urule.model.rule.lhs.Or;
import com.bstek.urule.model.table.DecisionTable;
import com.bstek.urule.parse.deserializer.DecisionTableDeserializer;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;

/** Builds a decision-table model by converting imported table data to XML. */
public class DecisionTableXmlBuilder {
   private ExcelSupport excelSupport;
   private TableData tableData;
   private DSLRuleSetBuilder dSLRuleSetBuilder;
   private DecisionTableDeserializer decisionTableDeserializer;

   public DecisionTableXmlBuilder(TableData data) {
      this.tableData = data;
      this.excelSupport = new ExcelSupport();
      this.dSLRuleSetBuilder = (DSLRuleSetBuilder)Utils.getApplicationContext().getBean("urule.dslRuleSetBuilder");
      this.decisionTableDeserializer = (DecisionTableDeserializer)Utils.getApplicationContext().getBean("urule.decisionTableDeserializer");
   }

   public DecisionTable buildTable() {
      try {
         String xml = this.buildXml();
         Document text = DocumentHelper.parseText(xml);
         return this.decisionTableDeserializer.deserialize(text.getRootElement());
      } catch (Exception exception) {
         throw new InfoException(exception);
      }
   }

   private String buildColumnXml(int number, String text, String text2) {
      if (StringUtils.isBlank(text2)) {
         text2 = "";
      }

      return "<col num=\"" + number + "\" width=\"180\" type=\"" + text + "\"" + text2 + "/>";
   }

   private String buildXml() throws IOException {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
      stringBuilder.append("<decision-table");
      ExcelImportUtils.builderProperties(this.excelSupport, stringBuilder, this.tableData.getProperties(), false);
      stringBuilder.append(">");
      ExcelImportUtils.builderRemark(stringBuilder, this.tableData.getProperties());
      HashMap valuesByKey = new HashMap();
      List headers = this.tableData.getHeaders();

      for(int index = 0; index < headers.size(); ++index) {
         Header header = (Header)headers.get(index);
         HeaderType type = header.getType();
         String name = header.getName();
         if ((type.equals(HeaderType.assign) || type.equals(HeaderType.condition)) && header.isPredefine()) {
            PredefineRow predefineRow = (PredefineRow)this.tableData.getPredefineNameMap().get(name.split("\\.")[0]);
            if (ExcelSupport.isParameter(predefineRow.getFromType())) {
               String[] parts = name.split("\\.");
               parts[0] = predefineRow.getFromValue();
               Variable parameterByLabel = this.excelSupport.findParameterByLabel(parts[0], parts[1]);
               if (parameterByLabel == null) {
                  parts[0] = predefineRow.getType();
                  VariableCategory variableCategory = this.excelSupport.findVariableCategory(parts);
                  if (variableCategory != null) {
                     valuesByKey.put(predefineRow.getType(), variableCategory);
                  }
               }
            }
         }
      }

      ExcelImportUtils.builderPredefineXml(stringBuilder, ExcelImportUtils.getPredefineGroupPriority(this.tableData.getProperties()), this.tableData.getPredefineRows(), this.tableData.getPredefineNameMap(), this.excelSupport, valuesByKey);

      for(int index2 = 0; index2 < headers.size(); ++index2) {
         Header header2 = (Header)headers.get(index2);
         HeaderType type2 = header2.getType();
         String name2 = header2.getName();
         if (type2.equals(HeaderType.assign)) {
            if (header2.isPredefine()) {
               PredefineRow predefineRow2 = (PredefineRow)this.tableData.getPredefineNameMap().get(name2.split("\\.")[0]);
               String predefineXml = "";
               if ("Variable".equals(predefineRow2.getFromType())) {
                  String[] values = new String[]{predefineRow2.getType(), this.splitHeaderName(name2)[1]};
                  Variable variable = this.excelSupport.findVariable(values);
                  predefineXml = ExcelImportUtils.buildPredefineXml(predefineRow2.getUuid(), variable.getUuid());
               } else if ("VariableCategory".equals(predefineRow2.getFromType())) {
                  String[] values2 = new String[]{predefineRow2.getType(), this.splitHeaderName(name2)[1]};
                  Variable variable2 = this.excelSupport.findVariable(values2);
                  predefineXml = ExcelImportUtils.buildPredefineXml(predefineRow2.getUuid(), variable2.getUuid());
               } else if ("Input".equals(predefineRow2.getFromType())) {
                  predefineXml = ExcelImportUtils.buildPredefineXml(predefineRow2.getUuid(), "");
               } else {
                  String[] values3 = new String[]{predefineRow2.getFromCategory(), predefineRow2.getFromValue()};
                  Variable variable3 = this.excelSupport.findVariable(values3);
                  VariableCategory variableCategory2 = this.excelSupport.findVariableCategory(values3);
                  predefineXml = ExcelImportUtils.buildPredefineXml(variableCategory2.getUuid(), variable3.getUuid());
               }

               stringBuilder.append(this.buildColumnXml(index2, "Assignment", predefineXml));
            } else {
               String[] parts2 = name2.split("\\.");
               if (ExcelSupport.isParameter(parts2[0])) {
                  String parameterXml = "";
                  if (parts2.length == 2) {
                     Variable variable4 = this.excelSupport.findVariable(parts2);
                     parameterXml = ExcelImportUtils.buildParameterXml(variable4);
                  } else {
                     Variable variable5 = this.excelSupport.findParameterByLabel(parts2[1], parts2[2]);
                     if (variable5 == null) {
                        variable5 = this.excelSupport.findVariable(parts2);
                        parameterXml = ExcelImportUtils.buildParameterXml(variable5);
                     } else {
                        VariableCategory variableCategoryByUUID = this.excelSupport.findVariableCategoryByUUID(variable5.getDataType());
                        Variable variable6 = (Variable)variableCategoryByUUID.getVariableLabels().get(parts2[2]);
                        parameterXml = ExcelImportUtils.buildParameterXml(variable5, variableCategoryByUUID, variable6);
                     }
                  }

                  stringBuilder.append(this.buildColumnXml(index2, "Assignment", parameterXml));
               } else {
                  Variable variable7 = this.excelSupport.findVariable(parts2);
                  VariableCategory variableCategory3 = this.excelSupport.findVariableCategory(parts2);
                  String variableXml = ExcelImportUtils.buildVariableXml(variableCategory3, variable7);
                  stringBuilder.append(this.buildColumnXml(index2, "Assignment", variableXml));
               }
            }
         } else if (type2.equals(HeaderType.condition)) {
            if (header2.isPredefine()) {
               PredefineRow predefineRow3 = (PredefineRow)this.tableData.getPredefineNameMap().get(name2.split("\\.")[0]);
               String predefineXml2 = "";
               if ("Input".equals(predefineRow3.getFromType())) {
                  predefineXml2 = ExcelImportUtils.buildPredefineXml(predefineRow3.getUuid(), "");
               } else if (ExcelSupport.isParameter(predefineRow3.getFromType())) {
                  String[] parts3 = name2.split("\\.");
                  parts3[0] = predefineRow3.getFromValue();
                  Variable parameterByLabel2 = this.excelSupport.findParameterByLabel(parts3[0], parts3[1]);
                  if (parameterByLabel2 == null) {
                     parts3[0] = predefineRow3.getType();
                     Variable variable8 = this.excelSupport.findVariable(parts3);
                     if (null != variable8) {
                        predefineXml2 = ExcelImportUtils.buildPredefineXml(predefineRow3.getUuid(), variable8.getUuid());
                     } else {
                        predefineXml2 = ExcelImportUtils.buildPredefineXml(predefineRow3.getUuid(), "");
                     }
                  } else {
                     VariableCategory variableCategoryByUUID2 = this.excelSupport.findVariableCategoryByUUID(parameterByLabel2.getDataType());
                     Variable variable9 = (Variable)variableCategoryByUUID2.getVariableLabels().get(parts3[2]);
                     predefineXml2 = ExcelImportUtils.buildPredefineXml(predefineRow3.getUuid(), variable9.getUuid());
                  }
               } else if (!"Variable".equals(predefineRow3.getFromType()) && !"Predefine".equals(predefineRow3.getFromType())) {
                  if ("VariableCategory".equals(predefineRow3.getFromType())) {
                     VariableCategory variableCategory4 = this.excelSupport.findVariableCategory(predefineRow3.getType());
                     String[] values4 = new String[]{variableCategory4.getName(), this.splitHeaderName(name2)[1]};
                     Variable variable10 = this.excelSupport.findVariable(values4);
                     predefineXml2 = ExcelImportUtils.buildPredefineXml(predefineRow3.getUuid(), variable10.getUuid());
                  } else if ("Method".equals(predefineRow3.getFromType()) || "CommonFunction".equals(predefineRow3.getFromType())) {
                     predefineXml2 = ExcelImportUtils.buildPredefineXml(predefineRow3.getUuid(), "");
                  }
               } else {
                  String[] values5 = new String[]{predefineRow3.getType(), this.splitHeaderName(name2)[1]};
                  Variable variable11 = this.excelSupport.findVariable(values5);
                  predefineXml2 = ExcelImportUtils.buildPredefineXml(predefineRow3.getUuid(), variable11.getUuid());
               }

               stringBuilder.append(this.buildColumnXml(index2, "Criteria", predefineXml2));
            } else {
               String[] parts4 = name2.split("\\.");
               if (ExcelSupport.isParameter(parts4[0])) {
                  String parameterXml2 = "";
                  if (parts4.length == 2) {
                     Variable variable12 = this.excelSupport.findVariable(parts4);
                     parameterXml2 = ExcelImportUtils.buildParameterXml(variable12);
                  } else {
                     Variable variable13 = this.excelSupport.findParameterByLabel(parts4[1], parts4[2]);
                     if (variable13 == null) {
                        variable13 = this.excelSupport.findVariable(parts4);
                        parameterXml2 = ExcelImportUtils.buildParameterXml(variable13);
                     } else {
                        VariableCategory variableCategoryByUUID3 = this.excelSupport.findVariableCategoryByUUID(variable13.getDataType());
                        Variable variable14 = (Variable)variableCategoryByUUID3.getVariableLabels().get(parts4[2]);
                        parameterXml2 = ExcelImportUtils.buildParameterXml(variable13, variableCategoryByUUID3, variable14);
                     }
                  }

                  stringBuilder.append(this.buildColumnXml(index2, "Criteria", parameterXml2));
               } else {
                  Variable variable15 = this.excelSupport.findVariable(parts4);
                  VariableCategory variableCategory5 = this.excelSupport.findVariableCategory(parts4);
                  String variableXml2 = ExcelImportUtils.buildVariableXml(variableCategory5, variable15);
                  stringBuilder.append(this.buildColumnXml(index2, "Criteria", variableXml2));
               }
            }
         } else if (type2.equals(HeaderType.out)) {
            stringBuilder.append(this.buildColumnXml(index2, "ConsolePrint", ""));
         } else if (type2.equals(HeaderType.execute)) {
            stringBuilder.append(this.buildColumnXml(index2, "ExecuteMethod", ""));
         }
      }

      List rows = this.tableData.getRows();

      for(int index3 = 0; index3 < rows.size(); ++index3) {
         stringBuilder.append("<row num=\"" + index3 + "\" height=\"40\"/>");
      }

      for(int index4 = 0; index4 < rows.size(); ++index4) {
         ContentRow contentRow = (ContentRow)rows.get(index4);

         for(CellContent cellContent : (Iterable<CellContent>)(Iterable<?>)(contentRow.getContents())) {
            Header header3 = cellContent.getHeader();
            HeaderType type3 = header3.getType();
            String content = cellContent.getContent();
            int span = cellContent.getSpan();
            if (span == 0) {
               span = 1;
            }

            stringBuilder.append("<cell row=\"" + cellContent.getRow() + "\" col=\"" + cellContent.getCol() + "\" rowspan=\"" + span + "\">");
            if (StringUtils.isNotBlank(content)) {
               if (type3.equals(HeaderType.condition)) {
                  Criterion criterion = this.dSLRuleSetBuilder.buildCriterion(content);
                  stringBuilder.append(this.buildCriterionXml(criterion));
               } else if (type3.equals(HeaderType.assign)) {
                  AbstractValue abstractValue = this.dSLRuleSetBuilder.buildValue(content);
                  if (abstractValue.getArithmetic() != null) {
                     content = StringEscapeUtils.escapeXml(content);
                     stringBuilder.append("<value content=\"" + content + "\" type=\"Input\"/>");
                  } else {
                     stringBuilder.append(this.buildValueXml((Value)abstractValue));
                  }
               } else {
                  content = StringEscapeUtils.escapeXml(content);
                  stringBuilder.append("<value content=\"" + content + "\" type=\"Input\"/>");
               }
            }

            stringBuilder.append("</cell>");
         }
      }

      ExcelImportUtils.builderLibraryXml(stringBuilder, this.excelSupport);
      stringBuilder.append("</decision-table>");
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
                  String valueXml = this.buildValueXml(localValue);
                  if (valueXml != null) {
                     stringBuilder.append(valueXml);
                  }

                  stringBuilder.append("</condition>");
               }
            }
         }
      } else {
         stringBuilder.append("<joint type=\"and\">");
         Criteria criteria2 = (Criteria)criterion;
         stringBuilder.append("<condition op=\"" + criteria2.getOp().name() + "\">");
         Value localValue2 = criteria2.getValue();
         stringBuilder.append(this.buildValueXml(localValue2));
         stringBuilder.append("</condition>");
      }

      stringBuilder.append("</joint>");
      return stringBuilder.toString();
   }

   private String buildValueXml(Value localValue) {
      if (localValue == null) {
         return null;
      } else {
         StringBuilder stringBuilder = new StringBuilder();
         if (localValue instanceof SimpleValue) {
            SimpleValue simpleValue = (SimpleValue)localValue;
            String text = StringEscapeUtils.escapeXml(simpleValue.getContent());
            stringBuilder.append("<value content=\"" + text + "\" type=\"Input\">");
         } else if (localValue instanceof VariableCategoryValue) {
            VariableCategoryValue variableCategoryValue = (VariableCategoryValue)localValue;
            String variableCategory = variableCategoryValue.getVariableCategory();
            String text2 = StringEscapeUtils.escapeXml(variableCategory);
            stringBuilder.append("<value content=\"" + text2 + "\" type=\"Input\">");
         } else if (localValue instanceof VariableValue) {
            VariableValue variableValue = (VariableValue)localValue;
            PredefineRow predefineRow = (PredefineRow)this.tableData.getPredefineNameMap().get(variableValue.getVariableCategory());
            if (predefineRow != null) {
               String[] values = new String[]{predefineRow.getType(), variableValue.getVariableLabel()};
               VariableCategory variableCategory2 = this.excelSupport.findVariableCategory(values);
               if (variableCategory2 != null) {
                  Variable variable = this.excelSupport.findVariable(values);
                  stringBuilder.append("<value uuid=\"" + predefineRow.getUuid() + "\" property-uuid=\"" + variable.getUuid() + "\" type=\"Predefine\">");
               } else {
                  String text3 = variableValue.getVariableCategory() + "." + variableValue.getVariableLabel();
                  text3 = StringEscapeUtils.escapeXml(text3);
                  stringBuilder.append(ExcelImportUtils.buildContentXml(text3));
               }
            } else {
               String[] values2 = new String[]{variableValue.getVariableCategory(), variableValue.getVariableLabel()};
               Variable variable2 = this.excelSupport.findVariable(values2, true);
               Constant constant = this.excelSupport.findConstant(values2, true);
               if (variable2 != null) {
                  VariableCategory variableCategory3 = this.excelSupport.findVariableCategory(values2);
                  stringBuilder.append("<value category-uuid=\"" + variableCategory3.getUuid() + "\" var-category=\"" + variableValue.getVariableCategory() + "\" var=\"" + variable2.getName() + "\" var-label=\"" + variableValue.getVariableLabel() + "\" datatype=\"" + variable2.getDataType() + "\" uuid=\"" + variable2.getUuid());
                  String lowercaseText = variableCategory3.getName().toLowerCase();
                  if (ExcelSupport.isParameter(lowercaseText)) {
                     stringBuilder.append("\" type=\"Parameter\">");
                  } else {
                     stringBuilder.append("\" type=\"Variable\">");
                  }
               } else if (constant != null) {
                  ConstantCategory constantCategory = this.excelSupport.findConstantCategory(values2);
                  stringBuilder.append("<value category-uuid=\"" + constantCategory.getUuid() + "\" uuid=\"" + constant.getUuid() + "\" const-category=\"" + constantCategory.getLabel() + "\" const=\"" + constant.getLabel() + "\" datatype=\"" + constant.getType().name() + "\" type=\"Constant\">");
               } else {
                  String text4 = variableValue.getVariableCategory() + "." + variableValue.getVariableLabel();
                  text4 = StringEscapeUtils.escapeXml(text4);
                  stringBuilder.append(ExcelImportUtils.buildContentXml(text4));
               }
            }
         } else if (localValue instanceof MethodValue) {
            MethodValue methodValue = (MethodValue)localValue;
            SpringBean action = this.excelSupport.getAction(methodValue.getBeanLabel(), methodValue.getMethodLabel());
            Method actionMethod = this.excelSupport.getActionMethod(methodValue.getBeanLabel(), methodValue.getMethodLabel());
            if (action != null && actionMethod != null) {
               stringBuilder.append("<value bean-name=\"" + action.getId() + "\" bean-label=\"" + methodValue.getBeanLabel() + "\" method-name=\"" + actionMethod.getMethodName() + "\" method-label=\"" + methodValue.getMethodLabel() + "\" type=\"Method\">");
               int number = 0;

               for(Parameter parameter : methodValue.getParameters()) {
                  com.bstek.urule.model.library.action.Parameter parameter2 = (com.bstek.urule.model.library.action.Parameter)actionMethod.getParameters().get(number);
                  stringBuilder.append("<parameter name=\"" + parameter2.getName() + "\" type=\"" + parameter2.getType().name() + "\">");
                  Value localValue2 = parameter.getValue();
                  stringBuilder.append(this.buildValueXml(localValue2));
                  stringBuilder.append("</parameter>");
                  ++number;
               }
            } else {
               stringBuilder.append("<value content=\"" + methodValue.getBeanLabel() + "." + methodValue.getMethodLabel() + "\" type=\"Input\">");
            }
         } else if (localValue instanceof ParenValue) {
            ParenValue parenValue = (ParenValue)localValue;
            stringBuilder.append("<paren>");
            stringBuilder.append(this.buildValueXml(parenValue.getValue()));
            ComplexArithmetic arithmetic = parenValue.getArithmetic();
            ArithmeticType type = arithmetic.getType();
            stringBuilder.append("<complex-arith type=\"" + type.name() + "\">");
            stringBuilder.append(this.buildValueXml(arithmetic.getValue()));
            stringBuilder.append("</complex-arith>");
            stringBuilder.append("</paren>");
         } else if (localValue instanceof ParameterValue) {
            ParameterValue parameterValue = (ParameterValue)localValue;
            if (StringUtils.isNotBlank(parameterValue.getKeyCategoryUuid())) {
               Variable variable3 = null;
               VariableCategory variableCategoryByUUID = this.excelSupport.findVariableCategoryByUUID(parameterValue.getKeyCategoryUuid());
               if (variableCategoryByUUID != null) {
                  variable3 = (Variable)variableCategoryByUUID.getVariableNames().get(parameterValue.getVariableName());
               }

               if (variable3 != null) {
                  stringBuilder.append(ExcelImportUtils.buildParameterValueXml(parameterValue, variableCategoryByUUID, variable3));
               } else {
                  stringBuilder.append(ExcelImportUtils.buildContentXml(parameterValue.getKeyName() + "." + parameterValue.getVariableLabel()));
               }
            } else {
               String[] values3 = new String[]{"参数", parameterValue.getVariableLabel()};
               Variable variable4 = this.excelSupport.findVariable(values3, true);
               if (variable4 != null) {
                  stringBuilder.append(ExcelImportUtils.buildParameterValueXml(parameterValue, variable4));
               } else {
                  stringBuilder.append(ExcelImportUtils.buildContentXml("参数." + parameterValue.getVariableLabel()));
               }
            }
         } else {
            stringBuilder.append(ExcelImportUtils.buildContentXml((String)null));
         }

         ComplexArithmetic arithmetic2 = localValue.getArithmetic();
         if (arithmetic2 == null) {
            stringBuilder.append("</value>");
            return stringBuilder.toString();
         } else {
            ArithmeticType type2 = arithmetic2.getType();
            stringBuilder.append("<complex-arith type=\"" + type2.name() + "\">");
            stringBuilder.append(this.buildValueXml(arithmetic2.getValue()));
            stringBuilder.append("</complex-arith>");
            stringBuilder.append("</value>");
            return stringBuilder.toString();
         }
      }
   }

   private String[] splitHeaderName(String text) {
      String[] parts = text.split("\\.");
      if (parts.length < 2) {
         throw new InfoException("表头[" + text + "]不合法！");
      } else {
         String[] values = new String[]{parts[0], text.substring(parts[0].length() + 1)};
         return values;
      }
   }
}
