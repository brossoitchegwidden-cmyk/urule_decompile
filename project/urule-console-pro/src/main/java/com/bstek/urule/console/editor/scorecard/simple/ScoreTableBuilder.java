package com.bstek.urule.console.editor.scorecard.simple;

import com.bstek.urule.Utils;
import com.bstek.urule.builder.resource.ResourceType;
import com.bstek.urule.console.ContextHolder;
import com.bstek.urule.console.InfoException;
import com.bstek.urule.console.editor.ExcelImportUtils;
import com.bstek.urule.console.editor.ExcelSupport;
import com.bstek.urule.console.editor.lib.VariableInfo;
import com.bstek.urule.console.editor.lib.VariableLoader;
import com.bstek.urule.console.util.StringUtils;
import com.bstek.urule.console.xml.DocumentHelper;
import com.bstek.urule.dsl.DSLRuleSetBuilder;
import com.bstek.urule.exception.RuleException;
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
import com.bstek.urule.model.scorecard.ScorecardDefinition;
import com.bstek.urule.model.scorecard.ScoringType;
import com.bstek.urule.parse.deserializer.ScorecardDeserializer;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringEscapeUtils;
import org.dom4j.Document;

public class ScoreTableBuilder {
   private ExcelSupport excelSupport;
   private ScoreTableData scoreTableData;
   private DSLRuleSetBuilder dSLRuleSetBuilder;
   private List variableLibraries;
   private ScorecardDeserializer scorecardDeserializer;
   private Map importedLibrariesById = new HashMap();

   public ScoreTableBuilder(ScoreTableData data) {
      this.scoreTableData = data;
      this.excelSupport = new ExcelSupport();
      this.variableLibraries = VariableLoader.ins.load(ContextHolder.getGroupId(), ContextHolder.getProjectId());
      this.dSLRuleSetBuilder = (DSLRuleSetBuilder)Utils.getApplicationContext().getBean("urule.dslRuleSetBuilder");
      this.scorecardDeserializer = (ScorecardDeserializer)Utils.getApplicationContext().getBean("urule.scorecardDeserializer");
   }

   public ScorecardDefinition buildTable() {
      try {
         String text2 = this.buildScorecardXml();
         Document text = DocumentHelper.parseText(text2);
         return this.scorecardDeserializer.deserialize(text.getRootElement());
      } catch (Exception exception) {
         throw new InfoException(exception);
      }
   }

   private String buildScorecardXml() throws IOException {
      StringBuilder stringBuilder = new StringBuilder();
      List headers = this.scoreTableData.getHeaders();
      TableHeader tableHeader = this.findHeader(headers, false, false);
      String uuid = null;

      for(VariableInfo variableInfo : (Iterable<VariableInfo>)(Iterable<?>)(this.variableLibraries)) {
         for(VariableCategory variableCategory : (Iterable<VariableCategory>)(Iterable<?>)(variableInfo.getVariableCategories())) {
            if (variableCategory.getName().contentEquals(tableHeader.getName())) {
               uuid = variableCategory.getUuid();
               break;
            }
         }
      }

      if (uuid == null) {
         throw new InfoException("名为【" + tableHeader.getName() + "】的变量分类在当前项目的变量库中未定义");
      } else {
         boolean flag = this.hasWeightSupport(headers);
         String name = "从Excel中导入的评分卡";
         if (this.scoreTableData.getProperties().containsKey("name")) {
            name = (String)this.scoreTableData.getProperties().get("name");
         }

         stringBuilder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
         stringBuilder.append("<scorecard weight-support=\"" + flag + "\" name=\"" + name + "\" attr-col-width=\"200\" attr-col-name=\"属性\" attr-col-category=\"" + tableHeader.getName() + "\" attr-col-category-uuid=\"" + uuid + "\" condition-col-width=\"220\" condition-col-name=\"条件\" score-col-width=\"180\" score-col-name=\"分值\"");
         ScoringType scoringType = ExcelImportUtils.getScoringType(this.scoreTableData.getProperties());
         stringBuilder.append(" scoring-type=\"" + scoringType.name() + "\" ");
         ExcelImportUtils.builderProperties(this.excelSupport, stringBuilder, this.scoreTableData.getProperties(), true);
         String scoringBean = ExcelImportUtils.getScoringBean(this.scoreTableData.getProperties());
         if (scoringType == ScoringType.custom && StringUtils.isNotBlank(scoringBean)) {
            stringBuilder.append(" custom-scoring-bean=\"" + scoringBean + "\" ");
         }

         stringBuilder.append(">");
         ExcelImportUtils.builderRemark(stringBuilder, this.scoreTableData.getProperties());
         List rows = this.scoreTableData.getRows();
         int number = 2;

         for(RowData rowData : (Iterable<RowData>)(Iterable<?>)(rows)) {
            List cells = rowData.getCells();
            CellData cellData = null;

            for(CellData cellData2 : (Iterable<CellData>)(Iterable<?>)(cells)) {
               TableHeader header = cellData2.getHeader();
               if (!header.isCondition() && !header.isScore() && !header.isCustom()) {
                  cellData = cellData2;
                  break;
               }
            }

            if (cellData != null) {
               stringBuilder.append("<attribute-row row-number=\"" + number + "\">");
               int span = cellData.getSpan();

               for(int index = 1; index < span; ++index) {
                  ++number;
                  stringBuilder.append("<condition-row row-number=\"" + number + "\"/>");
               }

               stringBuilder.append("</attribute-row>");
               ++number;
            }
         }

         number = 2;

         for(int index2 = 0; index2 < rows.size(); ++index2) {
            RowData rowData2 = (RowData)rows.get(index2);

            for(CellData cellData3 : (Iterable<CellData>)(Iterable<?>)(rowData2.getCells())) {
               TableHeader header2 = cellData3.getHeader();
               String content = cellData3.getContent();
               int span2 = cellData3.getSpan();
               if (span2 == 0) {
                  span2 = 1;
               }

               if (header2.isCustom()) {
                  stringBuilder.append("<card-cell type=\"custom\" row=\"" + number + "\" col=\"" + (cellData3.getCol() + 1) + "\">");
                  stringBuilder.append("<value content=\"" + content + "\" type=\"Input\"/>");
                  stringBuilder.append("</card-cell>");
               } else if (!header2.isCondition() && !header2.isScore()) {
                  String[] values = new String[]{header2.getName(), content};
                  String substring = "";
                  if (flag) {
                     String[] parts = content.split("\n");
                     values[1] = parts[0].trim();
                     parts[1] = parts[1].trim().toLowerCase();
                     if (parts[1].startsWith("权重:")) {
                        substring = parts[1].substring("权重:".length());
                     } else if (parts[1].startsWith("权重:")) {
                        substring = parts[1].substring("权重：".length());
                     } else if (parts[1].startsWith("weight:")) {
                        substring = parts[1].substring("weight:".length());
                     } else if (parts[1].startsWith("weight：")) {
                        substring = parts[1].substring("weight：".length());
                     }
                  } else if (content.indexOf(".") > 0) {
                     values[1] = content.split("\\.")[0];
                  }

                  Variable variable = null;
                  Variable variable2 = null;
                  if (ExcelSupport.isParameter(header2.getName())) {
                     String[] parts2 = content.split("\\.");
                     String[] values2 = new String[]{"参数", parts2[0]};
                     variable2 = this.findVariable(values2);
                     if (variable2 != null) {
                        VariableCategory variableCategory2 = this.findVariableCategoryByUuid(variable2.getDataType());
                        if (variableCategory2 != null) {
                           variable = (Variable)variableCategory2.getVariableLabels().get(parts2[1]);
                        }
                     }
                  } else {
                     variable = this.findVariable(values);
                  }

                  String text = "";
                  if (flag && StringUtils.isNotBlank(substring)) {
                     text = " weight=\"" + substring + "\"";
                  }

                  if (null != variable2) {
                     if (null != variable) {
                        stringBuilder.append("<card-cell type=\"attribute\" row=\"" + number + "\" col=\"" + (cellData3.getCol() + 1) + "\"" + text + " var=\"" + variable.getName() + "\" var-label=\"" + variable.getLabel() + "\" datatype=\"" + variable.getDataType() + "\" uuid=\"" + variable.getUuid() + "\" key-label=\"" + variable2.getLabel() + "\" key-name=\"" + variable2.getName() + "\" key-uuid=\"" + variable2.getUuid() + "\"");
                     } else {
                        stringBuilder.append("<card-cell type=\"attribute\" row=\"" + number + "\" col=\"" + (cellData3.getCol() + 1) + "\"" + text + " var=\"" + variable2.getName() + "\" var-label=\"" + variable2.getLabel() + "\" uuid=\"" + variable2.getUuid() + "\" datatype=\"" + variable2.getDataType() + "\"");
                     }
                  } else {
                     if (variable == null) {
                        throw new RuleException(String.format("属性列[%s]不存在", header2.getName()));
                     }

                     stringBuilder.append("<card-cell type=\"attribute\" row=\"" + number + "\" col=\"" + (cellData3.getCol() + 1) + "\"" + text + " var=\"" + variable.getName() + "\" var-label=\"" + variable.getLabel() + "\" uuid=\"" + variable.getUuid() + "\" datatype=\"" + variable.getDataType() + "\"");
                  }

                  stringBuilder.append("/>");
               } else if (header2.isScore()) {
                  stringBuilder.append("<card-cell type=\"score\" row=\"" + number + "\" col=\"" + (cellData3.getCol() + 1) + "\">");
                  stringBuilder.append("<value content=\"" + content + "\" type=\"Input\"/>");
                  stringBuilder.append("</card-cell>");
               } else {
                  if (!header2.isCondition()) {
                     throw new InfoException("无法识别的单元格：" + content + "");
                  }

                  stringBuilder.append("<card-cell type=\"condition\" row=\"" + number + "\" col=\"" + (cellData3.getCol() + 1) + "\">");
                  Criterion criterion = this.dSLRuleSetBuilder.buildCriterion(content);
                  stringBuilder.append(this.buildCriterionXml(criterion));
                  stringBuilder.append("</card-cell>");
               }
            }

            ++number;
         }

         int number2 = 0;

         for(TableHeader tableHeader2 : (Iterable<TableHeader>)(Iterable<?>)(headers)) {
            ++number2;
            if (tableHeader2.isCustom()) {
               stringBuilder.append("<custom-col col-number=\"" + number2 + "\" name=\"" + tableHeader2.getName() + "\" width=\"160\"/>");
            }
         }

         for(VariableInfo variableInfo2 : (Iterable<VariableInfo>)(Iterable<?>)(this.excelSupport.getVariableInfos())) {
         this.importedLibrariesById.put(variableInfo2.getId(), variableInfo2);
         }

         for(VariableInfo variableInfo3 : (Iterable<VariableInfo>)(Iterable<?>)(this.importedLibrariesById.values())) {
            if (variableInfo3.getType().endsWith(ResourceType.VariableLibrary.name())) {
               stringBuilder.append("<import-variable-library id=\"" + variableInfo3.getId() + "\" path=\"" + variableInfo3.getPath() + "\"/>");
            } else {
               stringBuilder.append("<import-parameter-library id=\"" + variableInfo3.getId() + "\" path=\"" + variableInfo3.getPath() + "\"/>");
            }
         }

         stringBuilder.append("</scorecard>");
         return stringBuilder.toString();
      }
   }

   private TableHeader findHeader(List items, boolean flag, boolean flag2) {
      for(TableHeader tableHeader : (Iterable<TableHeader>)(Iterable<?>)(items)) {
         if (flag && tableHeader.isScore()) {
            return tableHeader;
         }

         if (flag2 && tableHeader.isCondition()) {
            return tableHeader;
         }

         if (!flag && !flag2 && !tableHeader.isScore() && !tableHeader.isCondition()) {
            return tableHeader;
         }
      }

      throw new InfoException("列定义存在问题！");
   }

   private boolean hasWeightSupport(List items) {
      boolean flag = false;

      for(TableHeader tableHeader : (Iterable<TableHeader>)(Iterable<?>)(items)) {
         if (tableHeader.isWeightWupport()) {
            flag = true;
            break;
         }
      }

      return flag;
   }

   private VariableCategory findVariableCategoryByUuid(String text) {
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

      throw new InfoException("变量[" + text + "." + text2 + "]在当前项目中未定义!");
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
