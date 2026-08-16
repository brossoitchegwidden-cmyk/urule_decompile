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
   private ExcelSupport a;
   private ScoreTableData b;
   private DSLRuleSetBuilder c;
   private List d;
   private ScorecardDeserializer e;
   private Map f = new HashMap();

   public ScoreTableBuilder(ScoreTableData var1) {
      this.b = var1;
      this.a = new ExcelSupport();
      this.d = VariableLoader.ins.load(ContextHolder.getGroupId(), ContextHolder.getProjectId());
      this.c = (DSLRuleSetBuilder)Utils.getApplicationContext().getBean("urule.dslRuleSetBuilder");
      this.e = (ScorecardDeserializer)Utils.getApplicationContext().getBean("urule.scorecardDeserializer");
   }

   public ScorecardDefinition buildTable() {
      try {
         String var1 = this.a();
         Document var2 = DocumentHelper.parseText(var1);
         return this.e.deserialize(var2.getRootElement());
      } catch (Exception var3) {
         throw new InfoException(var3);
      }
   }

   private String a() throws IOException {
      StringBuilder var1 = new StringBuilder();
      List var2 = this.b.getHeaders();
      TableHeader var3 = this.a(var2, false, false);
      String var4 = null;

      for(VariableInfo var6 : (Iterable<VariableInfo>)(Iterable<?>)(this.d)) {
         for(VariableCategory var8 : (Iterable<VariableCategory>)(Iterable<?>)(var6.getVariableCategories())) {
            if (var8.getName().contentEquals(var3.getName())) {
               var4 = var8.getUuid();
               break;
            }
         }
      }

      if (var4 == null) {
         throw new InfoException("名为【" + var3.getName() + "】的变量分类在当前项目的变量库中未定义");
      } else {
         boolean var26 = this.a(var2);
         String var27 = "从Excel中导入的评分卡";
         if (this.b.getProperties().containsKey("name")) {
            var27 = (String)this.b.getProperties().get("name");
         }

         var1.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
         var1.append("<scorecard weight-support=\"" + var26 + "\" name=\"" + var27 + "\" attr-col-width=\"200\" attr-col-name=\"属性\" attr-col-category=\"" + var3.getName() + "\" attr-col-category-uuid=\"" + var4 + "\" condition-col-width=\"220\" condition-col-name=\"条件\" score-col-width=\"180\" score-col-name=\"分值\"");
         ScoringType var28 = ExcelImportUtils.getScoringType(this.b.getProperties());
         var1.append(" scoring-type=\"" + var28.name() + "\" ");
         ExcelImportUtils.builderProperties(this.a, var1, this.b.getProperties(), true);
         String var29 = ExcelImportUtils.getScoringBean(this.b.getProperties());
         if (var28 == ScoringType.custom && StringUtils.isNotBlank(var29)) {
            var1.append(" custom-scoring-bean=\"" + var29 + "\" ");
         }

         var1.append(">");
         ExcelImportUtils.builderRemark(var1, this.b.getProperties());
         List var9 = this.b.getRows();
         int var10 = 2;

         for(RowData var12 : (Iterable<RowData>)(Iterable<?>)(var9)) {
            List var13 = var12.getCells();
            CellData var14 = null;

            for(CellData var16 : (Iterable<CellData>)(Iterable<?>)(var13)) {
               TableHeader var17 = var16.getHeader();
               if (!var17.isCondition() && !var17.isScore() && !var17.isCustom()) {
                  var14 = var16;
                  break;
               }
            }

            if (var14 != null) {
               var1.append("<attribute-row row-number=\"" + var10 + "\">");
               int var42 = var14.getSpan();

               for(int var44 = 1; var44 < var42; ++var44) {
                  ++var10;
                  var1.append("<condition-row row-number=\"" + var10 + "\"/>");
               }

               var1.append("</attribute-row>");
               ++var10;
            }
         }

         var10 = 2;

         for(int var31 = 0; var31 < var9.size(); ++var31) {
            RowData var33 = (RowData)var9.get(var31);

            for(CellData var43 : (Iterable<CellData>)(Iterable<?>)(var33.getCells())) {
               TableHeader var45 = var43.getHeader();
               String var46 = var43.getContent();
               int var18 = var43.getSpan();
               if (var18 == 0) {
                  var18 = 1;
               }

               if (var45.isCustom()) {
                  var1.append("<card-cell type=\"custom\" row=\"" + var10 + "\" col=\"" + (var43.getCol() + 1) + "\">");
                  var1.append("<value content=\"" + var46 + "\" type=\"Input\"/>");
                  var1.append("</card-cell>");
               } else if (!var45.isCondition() && !var45.isScore()) {
                  String[] var48 = new String[]{var45.getName(), var46};
                  String var20 = "";
                  if (var26) {
                     String[] var21 = var46.split("\n");
                     var48[1] = var21[0].trim();
                     var21[1] = var21[1].trim().toLowerCase();
                     if (var21[1].startsWith("权重:")) {
                        var20 = var21[1].substring("权重:".length());
                     } else if (var21[1].startsWith("权重:")) {
                        var20 = var21[1].substring("权重：".length());
                     } else if (var21[1].startsWith("weight:")) {
                        var20 = var21[1].substring("weight:".length());
                     } else if (var21[1].startsWith("weight：")) {
                        var20 = var21[1].substring("weight：".length());
                     }
                  } else if (var46.indexOf(".") > 0) {
                     var48[1] = var46.split("\\.")[0];
                  }

                  Variable var49 = null;
                  Variable var22 = null;
                  if (ExcelSupport.isParameter(var45.getName())) {
                     String[] var23 = var46.split("\\.");
                     String[] var24 = new String[]{"参数", var23[0]};
                     var22 = this.a(var24);
                     if (var22 != null) {
                        VariableCategory var25 = this.a(var22.getDataType());
                        if (var25 != null) {
                           var49 = (Variable)var25.getVariableLabels().get(var23[1]);
                        }
                     }
                  } else {
                     var49 = this.a(var48);
                  }

                  String var50 = "";
                  if (var26 && StringUtils.isNotBlank(var20)) {
                     var50 = " weight=\"" + var20 + "\"";
                  }

                  if (null != var22) {
                     if (null != var49) {
                        var1.append("<card-cell type=\"attribute\" row=\"" + var10 + "\" col=\"" + (var43.getCol() + 1) + "\"" + var50 + " var=\"" + var49.getName() + "\" var-label=\"" + var49.getLabel() + "\" datatype=\"" + var49.getDataType() + "\" uuid=\"" + var49.getUuid() + "\" key-label=\"" + var22.getLabel() + "\" key-name=\"" + var22.getName() + "\" key-uuid=\"" + var22.getUuid() + "\"");
                     } else {
                        var1.append("<card-cell type=\"attribute\" row=\"" + var10 + "\" col=\"" + (var43.getCol() + 1) + "\"" + var50 + " var=\"" + var22.getName() + "\" var-label=\"" + var22.getLabel() + "\" uuid=\"" + var22.getUuid() + "\" datatype=\"" + var22.getDataType() + "\"");
                     }
                  } else {
                     if (var49 == null) {
                        throw new RuleException(String.format("属性列[%s]不存在", var45.getName()));
                     }

                     var1.append("<card-cell type=\"attribute\" row=\"" + var10 + "\" col=\"" + (var43.getCol() + 1) + "\"" + var50 + " var=\"" + var49.getName() + "\" var-label=\"" + var49.getLabel() + "\" uuid=\"" + var49.getUuid() + "\" datatype=\"" + var49.getDataType() + "\"");
                  }

                  var1.append("/>");
               } else if (var45.isScore()) {
                  var1.append("<card-cell type=\"score\" row=\"" + var10 + "\" col=\"" + (var43.getCol() + 1) + "\">");
                  var1.append("<value content=\"" + var46 + "\" type=\"Input\"/>");
                  var1.append("</card-cell>");
               } else {
                  if (!var45.isCondition()) {
                     throw new InfoException("无法识别的单元格：" + var46 + "");
                  }

                  var1.append("<card-cell type=\"condition\" row=\"" + var10 + "\" col=\"" + (var43.getCol() + 1) + "\">");
                  Criterion var19 = this.c.buildCriterion(var46);
                  var1.append(this.a(var19));
                  var1.append("</card-cell>");
               }
            }

            ++var10;
         }

         int var32 = 0;

         for(TableHeader var38 : (Iterable<TableHeader>)(Iterable<?>)(var2)) {
            ++var32;
            if (var38.isCustom()) {
               var1.append("<custom-col col-number=\"" + var32 + "\" name=\"" + var38.getName() + "\" width=\"160\"/>");
            }
         }

         for(VariableInfo var39 : (Iterable<VariableInfo>)(Iterable<?>)(this.a.getVariableInfos())) {
            this.f.put(var39.getId(), var39);
         }

         for(VariableInfo var40 : (Iterable<VariableInfo>)(Iterable<?>)(this.f.values())) {
            if (var40.getType().endsWith(ResourceType.VariableLibrary.name())) {
               var1.append("<import-variable-library id=\"" + var40.getId() + "\" path=\"" + var40.getPath() + "\"/>");
            } else {
               var1.append("<import-parameter-library id=\"" + var40.getId() + "\" path=\"" + var40.getPath() + "\"/>");
            }
         }

         var1.append("</scorecard>");
         return var1.toString();
      }
   }

   private TableHeader a(List var1, boolean var2, boolean var3) {
      for(TableHeader var5 : (Iterable<TableHeader>)(Iterable<?>)(var1)) {
         if (var2 && var5.isScore()) {
            return var5;
         }

         if (var3 && var5.isCondition()) {
            return var5;
         }

         if (!var2 && !var3 && !var5.isScore() && !var5.isCondition()) {
            return var5;
         }
      }

      throw new InfoException("列定义存在问题！");
   }

   private boolean a(List var1) {
      boolean var2 = false;

      for(TableHeader var4 : (Iterable<TableHeader>)(Iterable<?>)(var1)) {
         if (var4.isWeightWupport()) {
            var2 = true;
            break;
         }
      }

      return var2;
   }

   private VariableCategory a(String var1) {
      for(VariableInfo var3 : (Iterable<VariableInfo>)(Iterable<?>)(this.d)) {
         for(VariableCategory var6 : (Iterable<VariableCategory>)(Iterable<?>)(var3.getVariableCategories())) {
            if (var6.getUuid().equals(var1)) {
               this.f.put(var3.getId(), var3);
               return var6;
            }
         }
      }

      return null;
   }

   private Variable a(String[] var1) {
      String var2 = var1[0];
      String var3 = var1[1];

      for(VariableInfo var5 : (Iterable<VariableInfo>)(Iterable<?>)(this.d)) {
         for(VariableCategory var8 : (Iterable<VariableCategory>)(Iterable<?>)(var5.getVariableCategories())) {
            if (var8.getName().equals(var2)) {
               this.f.put(var5.getId(), var5);
               List var9 = var8.getVariables();
               if (var9 != null) {
                  for(Variable var11 : (Iterable<Variable>)(Iterable<?>)(var9)) {
                     if (var11.getLabel().equals(var3) || var11.getName().equals(var3)) {
                        return var11;
                     }
                  }
               }
            }
         }
      }

      throw new InfoException("变量[" + var2 + "." + var3 + "]在当前项目中未定义!");
   }

   private String a(Criterion var1) {
      StringBuilder var2 = new StringBuilder();
      if (var1 instanceof Junction) {
         Junction var3 = (Junction)var1;
         List var4 = var3.getCriterions();
         String var5 = "and";
         if (var3 instanceof Or) {
            var5 = "or";
         }

         var2.append("<joint type=\"" + var5 + "\">");
         if (var4 != null) {
            for(Criterion var7 : (Iterable<Criterion>)(Iterable<?>)(var4)) {
               if (var7 instanceof Criteria) {
                  Criteria var8 = (Criteria)var7;
                  var2.append("<condition op=\"" + var8.getOp().name() + "\">");
                  Value var9 = var8.getValue();
                  String var10 = this.a(var9);
                  if (var10 != null) {
                     var2.append(var10);
                  }

                  var2.append("</condition>");
               }
            }
         }
      } else {
         var2.append("<joint type=\"and\">");
         Criteria var11 = (Criteria)var1;
         var2.append("<condition op=\"" + var11.getOp().name() + "\">");
         Value var12 = var11.getValue();
         var2.append(this.a(var12));
         var2.append("</condition>");
      }

      var2.append("</joint>");
      return var2.toString();
   }

   private String a(Value var1) {
      if (var1 == null) {
         return null;
      } else {
         StringBuilder var2 = new StringBuilder();
         if (var1 instanceof SimpleValue) {
            SimpleValue var3 = (SimpleValue)var1;
            String var4 = StringEscapeUtils.escapeXml(var3.getContent());
            var2.append(ExcelImportUtils.buildContentXml(var4));
         } else if (var1 instanceof VariableCategoryValue) {
            VariableCategoryValue var6 = (VariableCategoryValue)var1;
            String var9 = var6.getVariableCategory();
            String var5 = StringEscapeUtils.escapeXml(var9);
            var2.append(ExcelImportUtils.buildContentXml(var5));
         } else if (var1 instanceof VariableValue) {
            VariableValue var7 = (VariableValue)var1;
            String var10 = var7.getVariableCategory() + "." + var7.getVariableLabel();
            var10 = StringEscapeUtils.escapeXml(var10);
            var2.append(ExcelImportUtils.buildContentXml(var10));
         } else {
            var2.append(ExcelImportUtils.buildContentXml(""));
         }

         ComplexArithmetic var8 = var1.getArithmetic();
         if (var8 == null) {
            var2.append("</value>");
            return var2.toString();
         } else {
            ArithmeticType var12 = var8.getType();
            var2.append("<complex-arith type=\"" + var12.name() + "\">");
            var2.append(this.a(var8.getValue()));
            var2.append("</complex-arith>");
            var2.append("</value>");
            return var2.toString();
         }
      }
   }
}
