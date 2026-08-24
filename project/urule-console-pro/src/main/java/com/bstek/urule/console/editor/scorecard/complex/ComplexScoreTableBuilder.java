package com.bstek.urule.console.editor.scorecard.complex;

import com.bstek.urule.Utils;
import com.bstek.urule.builder.resource.ResourceType;
import com.bstek.urule.console.ContextHolder;
import com.bstek.urule.console.InfoException;
import com.bstek.urule.console.editor.ExcelImportUtils;
import com.bstek.urule.console.editor.ExcelSupport;
import com.bstek.urule.console.editor.lib.VariableInfo;
import com.bstek.urule.console.editor.lib.VariableLoader;
import com.bstek.urule.console.xml.DocumentHelper;
import com.bstek.urule.dsl.DSLRuleSetBuilder;
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
import com.bstek.urule.model.scorecard.ComplexScorecardDefinition;
import com.bstek.urule.model.scorecard.ScoringType;
import com.bstek.urule.parse.deserializer.ComplexScorecardDeserializer;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;

public class ComplexScoreTableBuilder {
   private ExcelSupport excelSupport;
   private ScoreTableData scoreTableData;
   private DSLRuleSetBuilder dSLRuleSetBuilder;
   private List variableLibraries;
   private ComplexScorecardDeserializer complexScorecardDeserializer;
   private Map importedLibrariesById = new HashMap();

   public ComplexScoreTableBuilder(ScoreTableData data) {
      this.scoreTableData = data;
      this.excelSupport = new ExcelSupport();
      this.variableLibraries = VariableLoader.ins.load(ContextHolder.getGroupId(), ContextHolder.getProjectId());
      this.dSLRuleSetBuilder = (DSLRuleSetBuilder)Utils.getApplicationContext().getBean("urule.dslRuleSetBuilder");
      this.complexScorecardDeserializer = (ComplexScorecardDeserializer)Utils.getApplicationContext().getBean("urule.complexScorecardDeserializer");
   }

   public ComplexScorecardDefinition buildTable() {
      try {
         String text2 = this.buildScorecardXml();
         Document text = DocumentHelper.parseText(text2);
         return this.complexScorecardDeserializer.deserialize(text.getRootElement());
      } catch (Exception exception) {
         throw new InfoException(exception);
      }
   }

   private String buildScorecardXml() throws IOException {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
      stringBuilder.append("<complex-scorecard ");
      ScoringType scoringType = ExcelImportUtils.getScoringType(this.scoreTableData.getProperties());
      stringBuilder.append(" scoring-type=\"" + scoringType.name() + "\" ");
      ExcelImportUtils.builderProperties(this.excelSupport, stringBuilder, this.scoreTableData.getProperties(), true);
      String scoringBean = ExcelImportUtils.getScoringBean(this.scoreTableData.getProperties());
      if (scoringType == ScoringType.custom && StringUtils.isNotBlank(scoringBean)) {
         stringBuilder.append(" custom-scoring-bean=\"" + scoringBean + "\" ");
      }

      stringBuilder.append(">");
      ExcelImportUtils.builderRemark(stringBuilder, this.scoreTableData.getProperties());
      List headers = this.scoreTableData.getHeaders();

      for(int index = 0; index < headers.size(); ++index) {
         TableHeader tableHeader = (TableHeader)headers.get(index);
         String name = tableHeader.getName();
         if (tableHeader.isScore()) {
            stringBuilder.append("<col num=\"" + index + "\" width=\"120\" type=\"Score\"/>");
         } else if (tableHeader.isCustom()) {
            stringBuilder.append("<col num=\"" + index + "\" width=\"150\" type=\"Custom\" custom-label=\"" + name + "\"/>");
         } else {
            VariableCategory variableCategory = this.findVariableCategoryByName(name);
            stringBuilder.append("<col num=\"" + index + "\" width=\"120\" type=\"Criteria\" uuid=\"" + variableCategory.getUuid() + "\" var-category=\"" + name + "\"/>");
         }
      }

      List rows = this.scoreTableData.getRows();

      for(int index2 = 0; index2 < rows.size(); ++index2) {
         stringBuilder.append("<row num=\"" + index2 + "\" height=\"40\"/>");
      }

      for(int index3 = 0; index3 < rows.size(); ++index3) {
         RowData rowData = (RowData)rows.get(index3);

         for(CellData cellData : (Iterable<CellData>)(Iterable<?>)(rowData.getCells())) {
            TableHeader header = cellData.getHeader();
            String content = cellData.getContent();
            int span = cellData.getSpan();
            if (span == 0) {
               span = 1;
            }

            stringBuilder.append("<cell row=\"" + cellData.getRow() + "\" col=\"" + cellData.getCol() + "\" rowspan=\"" + span + "\"");
            if (!header.isScore() && !header.isCustom()) {
               String substring = content;
               if (StringUtils.isNotBlank(content)) {
                  int number = content.indexOf("\n");
                  if (number > 0) {
                     substring = content.substring(0, number);
                  }
               }

               Object objectValue = null;
               Object objectValue2 = null;
               if (ExcelSupport.isParameter(header.getName())) {
                  String[] parts = substring.split("\\.");
                  String[] values = new String[]{"参数", parts[0]};
                  Variable variable = this.findVariable(values);
                  if (variable != null) {
                     VariableCategory variableCategory2 = this.resolveVariableCategory(variable.getDataType());
                     if (variableCategory2 != null) {
                        Variable variable2 = (Variable)variableCategory2.getVariableLabels().get(parts[1]);
                        stringBuilder.append(" var-label=\"" + variable2.getLabel() + "\" var=\"" + variable2.getName() + "\" datatype=\"" + variable2.getDataType() + "\" uuid=\"" + variable2.getUuid() + "\"");
                        stringBuilder.append(" key-label=\"" + variable.getLabel() + "\" key-name=\"" + variable.getName() + "\" key-uuid=\"" + variable.getUuid() + "\">");
                     } else {
                        stringBuilder.append(" var-label=\"" + variable.getLabel() + "\" var=\"" + variable.getName() + "\" datatype=\"" + variable.getDataType() + "\" uuid=\"" + variable.getUuid() + "\">");
                     }
                  } else {
                     stringBuilder.append(" var-label=\"" + parts[0] + "\" var=\"" + parts[0] + "\" datatype=\"String\" uuid=\"\">");
                  }
               } else {
                  String[] values2 = new String[]{header.getName(), substring};
                  Variable variable3 = this.findVariable(values2, true);
                  if (variable3 == null && substring.indexOf(".") > 0) {
                     values2[1] = substring.split("\\.")[0];
                     variable3 = this.findVariable(values2);
                  }

                  stringBuilder.append(" var-label=\"" + variable3.getLabel() + "\" var=\"" + variable3.getName() + "\" datatype=\"" + variable3.getDataType() + "\" uuid=\"" + variable3.getUuid() + "\">");
               }
            } else {
               stringBuilder.append(">");
            }

            if (StringUtils.isNotBlank(content)) {
               if (!header.isScore() && !header.isCustom()) {
                  String substring2 = content;
                  if (StringUtils.isNotBlank(content)) {
                     int number2 = content.indexOf("\n");
                     if (number2 > 0) {
                        substring2 = content.substring(number2);
                     }
                  }

                  Criterion criterion = this.dSLRuleSetBuilder.buildCriterion(substring2);
                  stringBuilder.append(this.buildCriterionXml(criterion));
               } else {
                  content = StringEscapeUtils.escapeXml(content);
                  stringBuilder.append("<value content=\"" + content + "\" type=\"Input\"/>");
               }
            }

            stringBuilder.append("</cell>");
         }
      }

      for(VariableInfo variableInfo : (Iterable<VariableInfo>)(Iterable<?>)(this.excelSupport.getVariableInfos())) {
         this.importedLibrariesById.put(variableInfo.getId(), variableInfo);
      }

      for(VariableInfo variableInfo2 : (Iterable<VariableInfo>)(Iterable<?>)(this.importedLibrariesById.values())) {
         if (variableInfo2.getType().endsWith(ResourceType.VariableLibrary.name())) {
            stringBuilder.append("<import-variable-library id=\"" + variableInfo2.getId() + "\" path=\"" + variableInfo2.getPath() + "\"/>");
         } else {
            stringBuilder.append("<import-parameter-library id=\"" + variableInfo2.getId() + "\" path=\"" + variableInfo2.getPath() + "\"/>");
         }
      }

      stringBuilder.append("</complex-scorecard>");
      return stringBuilder.toString();
   }

   private VariableCategory findVariableCategoryByName(String text) {
      for(VariableInfo variableInfo : (Iterable<VariableInfo>)(Iterable<?>)(this.variableLibraries)) {
         for(VariableCategory variableCategory : (Iterable<VariableCategory>)(Iterable<?>)(variableInfo.getVariableCategories())) {
            if (variableCategory.getName().equals(text)) {
               this.importedLibrariesById.put(variableInfo.getId(), variableInfo);
               return variableCategory;
            }
         }
      }

      throw new InfoException("变量分类[" + text + "]在当前项目中未定义!");
   }

   private VariableCategory resolveVariableCategory(String text) {
      for(VariableInfo variableInfo : (Iterable<VariableInfo>)(Iterable<?>)(this.variableLibraries)) {
         for(VariableCategory variableCategory : (Iterable<VariableCategory>)(Iterable<?>)(variableInfo.getVariableCategories())) {
            if (variableCategory.getUuid().equals(text)) {
               this.importedLibrariesById.put(variableInfo.getId(), variableInfo);
               return variableCategory;
            }
         }
      }

      return null;
   }

   private Variable findVariable(String[] values) {
      return this.findVariable(values, false);
   }

   private Variable findVariable(String[] values, boolean flag) {
      String text = values[0];
      String text2 = values[1];

      for(VariableInfo variableInfo : (Iterable<VariableInfo>)(Iterable<?>)(this.variableLibraries)) {
         for(VariableCategory variableCategory : (Iterable<VariableCategory>)(Iterable<?>)(variableInfo.getVariableCategories())) {
            if (variableCategory.getName().equals(text)) {
               this.importedLibrariesById.put(variableInfo.getId(), variableInfo);
               List variables = variableCategory.getVariables();
               if (variables != null) {
                  for(Variable variable : (Iterable<Variable>)(Iterable<?>)(variables)) {
                     if (variable.getLabel().equals(text2) || variable.getName().equals(text2)) {
                        return variable;
                     }
                  }
               }
            }
         }
      }

      if (!flag) {
         throw new InfoException("变量[" + text + "." + text2 + "]在当前项目中未定义!");
      } else {
         return null;
      }
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
                  String text2 = this.buildValueXml(localValue);
                  if (text2 != null) {
                     stringBuilder.append(text2);
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
            stringBuilder.append(ExcelImportUtils.buildContentXml(text));
         } else if (localValue instanceof VariableCategoryValue) {
            VariableCategoryValue variableCategoryValue = (VariableCategoryValue)localValue;
            String variableCategory = variableCategoryValue.getVariableCategory();
            String text2 = StringEscapeUtils.escapeXml(variableCategory);
            stringBuilder.append(ExcelImportUtils.buildContentXml(text2));
         } else if (localValue instanceof VariableValue) {
            VariableValue variableValue = (VariableValue)localValue;
            String text3 = variableValue.getVariableCategory() + "." + variableValue.getVariableLabel();
            text3 = StringEscapeUtils.escapeXml(text3);
            stringBuilder.append(ExcelImportUtils.buildContentXml(text3));
         } else {
            stringBuilder.append(ExcelImportUtils.buildContentXml(""));
         }

         ComplexArithmetic arithmetic = localValue.getArithmetic();
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
}
