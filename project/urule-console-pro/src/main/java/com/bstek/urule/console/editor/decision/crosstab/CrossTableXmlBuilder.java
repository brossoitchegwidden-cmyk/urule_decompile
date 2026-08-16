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

public class CrossTableXmlBuilder {
   private CrossData a;
   private ExcelSupport b;
   private DSLRuleSetBuilder c;
   private CrosstableDeserializer d;

   public CrossTableXmlBuilder(CrossData var1) {
      this.a = var1;
      this.b = new ExcelSupport();
      this.c = (DSLRuleSetBuilder)Utils.getApplicationContext().getBean("urule.dslRuleSetBuilder");
      this.d = (CrosstableDeserializer)Utils.getApplicationContext().getBean("urule.crosstableDeserializer");
   }

   public CrosstabDefinition doBuild() throws Exception {
      String var1 = this.a();
      Document var2 = DocumentHelper.parseText(var1);
      return this.d.deserialize(var2.getRootElement());
   }

   private String a(Variable var1, VariableCategory var2, Variable var3) {
      StringBuilder var4 = new StringBuilder();
      var4.append(" bundle-data-type=\"parameter\" category-uuid=\"参数\" uuid=\"" + var1.getUuid() + "\" var-category=\"" + "参数" + "\"");
      var4.append(" var=\"" + var1.getName() + "\" var-label=\"" + var1.getLabel() + "\" dataType=\"" + var1.getDataType() + "\"");
      var4.append(" key-category-uuid=\"" + var2.getUuid() + "\" key-uuid=\"" + var3.getUuid() + "\" key-label=\"" + var3.getLabel() + "\" key-name=\"" + var3.getName() + "\"");
      return var4.toString();
   }

   private String a(Variable var1) {
      StringBuilder var2 = new StringBuilder();
      var2.append(" bundle-data-type=\"parameter\" category-uuid=\"参数\" uuid=\"" + var1.getUuid() + "\" var-category=\"" + "参数" + "\"");
      var2.append(" var=\"" + var1.getName() + "\" var-label=\"" + var1.getLabel() + "\" dataType=\"" + var1.getDataType() + "\"");
      return var2.toString();
   }

   private String a(String var1, String var2, Variable var3) {
      return " bundle-data-type=\"" + var1 + "\" var-category=\"" + var2 + "\" var=\"" + var3.getName() + "\" var-label=\"" + var3.getLabel() + "\" datatype=\"" + var3.getType().name() + "\"";
   }

   private String a(String var1, String var2) {
      if (StringUtils.isBlank(var2)) {
         var2 = "";
      }

      return " bundle-data-type=\"predefine\" predefine-uuid=\"" + var1 + "\" predefine-property-uuid=\"" + var2 + "\"";
   }

   private String a() throws Exception {
      StringBuilder var1 = new StringBuilder();
      var1.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
      CrossHeader var2 = this.a.getHeader();
      var1.append("<crosstab");
      ExcelImportUtils.builderProperties(this.b, var1, this.a.getProperties(), true);
      var1.append(">");
      ExcelImportUtils.builderRemark(var1, this.a.getProperties());
      HashMap var3 = new HashMap();
      ExcelImportUtils.builderPredefineXml(var1, ExcelImportUtils.getPredefineGroupPriority(this.a.getProperties()), this.a.getPredefineRows(), this.a.getPredefineNameMap(), this.b, var3);
      String var4 = var2.getContent();
      var4 = var4 == null ? "表头" : var4;
      var1.append("<header rowspan=\"" + var2.getRowSpan() + "\" colspan=\"" + var2.getColSpan() + "\"><![CDATA[" + var4 + "]]></header>");

      for(CrossRow var6 : (Iterable<CrossRow>)(Iterable<?>)(this.a.getRows())) {
         var1.append("<row number=\"" + var6.getNumber() + "\" type=\"" + var6.getType() + "\"");
         String var7 = var6.getContent();
         if (StringUtils.isNotBlank(var7)) {
            boolean var8 = var6.isPredefine();
            if (var8) {
               String[] var9 = var7.split("\\.");
               PredefineRow var10 = (PredefineRow)this.a.getPredefineNameMap().get(var9[0]);
               if (var9.length == 2) {
                  var9[0] = var10.getType();
                  Variable var11 = this.b.findVariable(var9);
                  var1.append(this.a(var10.getUuid(), var11.getUuid()));
               } else {
                  var1.append(this.a(var10.getUuid(), ""));
               }
            } else {
               String[] var24 = this.a(var7);
               String var28 = var24[0];
               Variable var32 = this.b.findVariable(var24, true);
               VariableCategory var12 = null;
               if (ExcelSupport.isParameter(var28)) {
                  if (var24.length > 2) {
                     var12 = this.b.findVariableCategoryByUUID(var32.getDataType());
                  } else {
                     var32 = this.b.findVariable(var24, true);
                  }
               } else {
                  var32 = this.b.findVariable(var24, true);
               }

               String var13 = "variable";
               if (ExcelSupport.isParameter(var28)) {
                  var13 = "parameter";
                  if (var24.length > 2 && var12 != null) {
                     Variable var14 = (Variable)var12.getVariableLabels().get(var24[2]);
                     var1.append(this.a(var14, var12, var32));
                  } else {
                     var1.append(this.a(var32));
                  }
               } else {
                  var1.append(this.a(var13, var28, var32));
               }
            }
         }

         var1.append("/>");
      }

      for(CrossColumn var18 : (Iterable<CrossColumn>)(Iterable<?>)(this.a.getColumns())) {
         var1.append("<column number=\"" + var18.getNumber() + "\" type=\"" + var18.getType() + "\"");
         String var20 = var18.getContent();
         if (StringUtils.isNotBlank(var20)) {
            boolean var22 = var18.isPredefine();
            if (var22) {
               String[] var25 = var20.split("\\.");
               PredefineRow var29 = (PredefineRow)this.a.getPredefineNameMap().get(var25[0]);
               if (var25.length == 2) {
                  var25[0] = var29.getType();
                  Variable var33 = this.b.findVariable(var25);
                  var1.append(this.a(var29.getUuid(), var33.getUuid()));
               } else {
                  var1.append(this.a(var29.getUuid(), ""));
               }
            } else {
               String[] var26 = this.a(var20);
               String var30 = var26[0];
               Variable var34 = this.b.findVariable(var26, true);
               VariableCategory var36 = null;
               if (ExcelSupport.isParameter(var30)) {
                  if (var26.length > 2) {
                     var36 = this.b.findVariableCategoryByUUID(var34.getDataType());
                  } else {
                     var34 = this.b.findVariable(var26, true);
                  }
               } else {
                  var34 = this.b.findVariable(var26, true);
               }

               String var40 = "variable";
               if (ExcelSupport.isParameter(var30)) {
                  var40 = "parameter";
                  if (var26.length > 2 && var36 != null) {
                     Variable var43 = (Variable)var36.getVariableLabels().get(var26[2]);
                     var1.append(this.a(var43, var36, var34));
                  } else {
                     var1.append(this.a(var34));
                  }
               } else {
                  var1.append(this.a(var40, var30, var34));
               }
            }
         }

         var1.append("/>");
      }

      for(CellContent var19 : (Iterable<CellContent>)(Iterable<?>)(this.a.getCells())) {
         String var21 = var19.getType();
         String var23 = "condition-cell";
         if (var21.equals("value")) {
            var23 = "value-cell";
         }

         var1.append("<");
         var1.append(var23);
         int var27 = 1;
         int var31 = 1;
         boolean var35 = false;
         if (var19.getRow() <= this.a.getHeader().getRowSpan()) {
            var31 = var19.getSpan();
            var35 = true;
         }

         if (var19.getCol() <= this.a.getHeader().getColSpan()) {
            var27 = var19.getSpan();
            var35 = true;
         }

         var1.append(" row=\"" + var19.getRow() + "\" col=\"" + var19.getCol() + "\"");
         if (var35) {
            var1.append(" rowspan=\"" + var27 + "\" colspan=\"" + var31 + "\"");
         }

         var1.append(">");
         String var37 = var19.getContent();
         if (StringUtils.isNotBlank(var37)) {
            if (var35) {
               Criterion var42 = this.c.buildCriterion(var37);
               var1.append(this.a(var42));
            } else {
               var37 = StringEscapeUtils.escapeXml(var37);
               var1.append(ExcelImportUtils.buildContentXml(var37, true));
            }
         }

         var1.append("");
         var1.append("</");
         var1.append(var23);
         var1.append(">");
      }

      ExcelImportUtils.builderLibraryXml(var1, this.b);
      var1.append("");
      var1.append("");
      var1.append("");
      var1.append("");
      var1.append("");
      var1.append("</crosstab>");
      return var1.toString();
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
                  var2.append(this.a(var9));
                  var2.append("</condition>");
               }
            }
         }
      } else {
         var2.append("<joint type=\"and\">");
         Criteria var10 = (Criteria)var1;
         var2.append("<condition op=\"" + var10.getOp().name() + "\">");
         Value var11 = var10.getValue();
         String var12 = this.a(var11);
         if (var12 != null) {
            var2.append(var12);
         }

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
            var2.append(ExcelImportUtils.buildContentXml((String)null));
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

   private String[] a(String var1) {
      String[] var2 = var1.split("\\.");
      if (var2.length < 2) {
         throw new InfoException("表头[" + var1 + "]不合法！");
      } else {
         return var2;
      }
   }
}
