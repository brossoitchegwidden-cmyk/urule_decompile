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
   private ExcelSupport a;
   private ScoreTableData b;
   private DSLRuleSetBuilder c;
   private List d;
   private ComplexScorecardDeserializer e;
   private Map f = new HashMap();

   public ComplexScoreTableBuilder(ScoreTableData var1) {
      this.b = var1;
      this.a = new ExcelSupport();
      this.d = VariableLoader.ins.load(ContextHolder.getGroupId(), ContextHolder.getProjectId());
      this.c = (DSLRuleSetBuilder)Utils.getApplicationContext().getBean("urule.dslRuleSetBuilder");
      this.e = (ComplexScorecardDeserializer)Utils.getApplicationContext().getBean("urule.complexScorecardDeserializer");
   }

   public ComplexScorecardDefinition buildTable() {
      try {
         String var1 = this.a();
         System.out.println(var1);
         Document var2 = DocumentHelper.parseText(var1);
         return this.e.deserialize(var2.getRootElement());
      } catch (Exception var3) {
         throw new InfoException(var3);
      }
   }

   private String a() throws IOException {
      StringBuilder var1 = new StringBuilder();
      var1.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
      var1.append("<complex-scorecard ");
      ScoringType var2 = ExcelImportUtils.getScoringType(this.b.getProperties());
      var1.append(" scoring-type=\"" + var2.name() + "\" ");
      ExcelImportUtils.builderProperties(this.a, var1, this.b.getProperties(), true);
      String var3 = ExcelImportUtils.getScoringBean(this.b.getProperties());
      if (var2 == ScoringType.custom && StringUtils.isNotBlank(var3)) {
         var1.append(" custom-scoring-bean=\"" + var3 + "\" ");
      }

      var1.append(">");
      ExcelImportUtils.builderRemark(var1, this.b.getProperties());
      List var4 = this.b.getHeaders();

      for(int var5 = 0; var5 < var4.size(); ++var5) {
         TableHeader var6 = (TableHeader)var4.get(var5);
         String var7 = var6.getName();
         if (var6.isScore()) {
            var1.append("<col num=\"" + var5 + "\" width=\"120\" type=\"Score\"/>");
         } else if (var6.isCustom()) {
            var1.append("<col num=\"" + var5 + "\" width=\"150\" type=\"Custom\" custom-label=\"" + var7 + "\"/>");
         } else {
            VariableCategory var8 = this.a(var7);
            var1.append("<col num=\"" + var5 + "\" width=\"120\" type=\"Criteria\" uuid=\"" + var8.getUuid() + "\" var-category=\"" + var7 + "\"/>");
         }
      }

      List var20 = this.b.getRows();

      for(int var21 = 0; var21 < var20.size(); ++var21) {
         var1.append("<row num=\"" + var21 + "\" height=\"40\"/>");
      }

      for(int var22 = 0; var22 < var20.size(); ++var22) {
         RowData var25 = (RowData)var20.get(var22);

         for(CellData var10 : (Iterable<CellData>)(Iterable<?>)(var25.getCells())) {
            TableHeader var11 = var10.getHeader();
            String var12 = var10.getContent();
            int var13 = var10.getSpan();
            if (var13 == 0) {
               var13 = 1;
            }

            var1.append("<cell row=\"" + var10.getRow() + "\" col=\"" + var10.getCol() + "\" rowspan=\"" + var13 + "\"");
            if (!var11.isScore() && !var11.isCustom()) {
               String var14 = var12;
               if (StringUtils.isNotBlank(var12)) {
                  int var15 = var12.indexOf("\n");
                  if (var15 > 0) {
                     var14 = var12.substring(0, var15);
                  }
               }

               Object var31 = null;
               Object var16 = null;
               if (ExcelSupport.isParameter(var11.getName())) {
                  String[] var17 = var14.split("\\.");
                  String[] var18 = new String[]{"参数", var17[0]};
                  Variable var36 = this.a(var18);
                  if (var36 != null) {
                     VariableCategory var19 = this.b(var36.getDataType());
                     if (var19 != null) {
                        Variable var32 = (Variable)var19.getVariableLabels().get(var17[1]);
                        var1.append(" var-label=\"" + var32.getLabel() + "\" var=\"" + var32.getName() + "\" datatype=\"" + var32.getDataType() + "\" uuid=\"" + var32.getUuid() + "\"");
                        var1.append(" key-label=\"" + var36.getLabel() + "\" key-name=\"" + var36.getName() + "\" key-uuid=\"" + var36.getUuid() + "\">");
                     } else {
                        var1.append(" var-label=\"" + var36.getLabel() + "\" var=\"" + var36.getName() + "\" datatype=\"" + var36.getDataType() + "\" uuid=\"" + var36.getUuid() + "\">");
                     }
                  } else {
                     var1.append(" var-label=\"" + var17[0] + "\" var=\"" + var17[0] + "\" datatype=\"String\" uuid=\"\">");
                  }
               } else {
                  String[] var37 = new String[]{var11.getName(), var14};
                  Variable var33 = this.a(var37, true);
                  if (var33 == null && var14.indexOf(".") > 0) {
                     var37[1] = var14.split("\\.")[0];
                     var33 = this.a(var37);
                  }

                  var1.append(" var-label=\"" + var33.getLabel() + "\" var=\"" + var33.getName() + "\" datatype=\"" + var33.getDataType() + "\" uuid=\"" + var33.getUuid() + "\">");
               }
            } else {
               var1.append(">");
            }

            if (StringUtils.isNotBlank(var12)) {
               if (!var11.isScore() && !var11.isCustom()) {
                  String var30 = var12;
                  if (StringUtils.isNotBlank(var12)) {
                     int var34 = var12.indexOf("\n");
                     if (var34 > 0) {
                        var30 = var12.substring(var34);
                     }
                  }

                  Criterion var35 = this.c.buildCriterion(var30);
                  var1.append(this.a(var35));
               } else {
                  var12 = StringEscapeUtils.escapeXml(var12);
                  var1.append("<value content=\"" + var12 + "\" type=\"Input\"/>");
               }
            }

            var1.append("</cell>");
         }
      }

      for(VariableInfo var26 : (Iterable<VariableInfo>)(Iterable<?>)(this.a.getVariableInfos())) {
         this.f.put(var26.getId(), var26);
      }

      for(VariableInfo var27 : (Iterable<VariableInfo>)(Iterable<?>)(this.f.values())) {
         if (var27.getType().endsWith(ResourceType.VariableLibrary.name())) {
            var1.append("<import-variable-library id=\"" + var27.getId() + "\" path=\"" + var27.getPath() + "\"/>");
         } else {
            var1.append("<import-parameter-library id=\"" + var27.getId() + "\" path=\"" + var27.getPath() + "\"/>");
         }
      }

      var1.append("</complex-scorecard>");
      return var1.toString();
   }

   private VariableCategory a(String var1) {
      for(VariableInfo var3 : (Iterable<VariableInfo>)(Iterable<?>)(this.d)) {
         for(VariableCategory var6 : (Iterable<VariableCategory>)(Iterable<?>)(var3.getVariableCategories())) {
            if (var6.getName().equals(var1)) {
               this.f.put(var3.getId(), var3);
               return var6;
            }
         }
      }

      throw new InfoException("变量分类[" + var1 + "]在当前项目中未定义!");
   }

   private VariableCategory b(String var1) {
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
      return this.a(var1, false);
   }

   private Variable a(String[] var1, boolean var2) {
      String var3 = var1[0];
      String var4 = var1[1];

      for(VariableInfo var6 : (Iterable<VariableInfo>)(Iterable<?>)(this.d)) {
         for(VariableCategory var9 : (Iterable<VariableCategory>)(Iterable<?>)(var6.getVariableCategories())) {
            if (var9.getName().equals(var3)) {
               this.f.put(var6.getId(), var6);
               List var10 = var9.getVariables();
               if (var10 != null) {
                  for(Variable var12 : (Iterable<Variable>)(Iterable<?>)(var10)) {
                     if (var12.getLabel().equals(var4) || var12.getName().equals(var4)) {
                        return var12;
                     }
                  }
               }
            }
         }
      }

      if (!var2) {
         throw new InfoException("变量[" + var3 + "." + var4 + "]在当前项目中未定义!");
      } else {
         return null;
      }
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
