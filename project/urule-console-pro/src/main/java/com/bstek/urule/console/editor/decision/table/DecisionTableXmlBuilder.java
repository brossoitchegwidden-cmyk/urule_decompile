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

public class DecisionTableXmlBuilder {
   private ExcelSupport a;
   private TableData b;
   private DSLRuleSetBuilder c;
   private DecisionTableDeserializer d;

   public DecisionTableXmlBuilder(TableData var1) {
      this.b = var1;
      this.a = new ExcelSupport();
      this.c = (DSLRuleSetBuilder)Utils.getApplicationContext().getBean("urule.dslRuleSetBuilder");
      this.d = (DecisionTableDeserializer)Utils.getApplicationContext().getBean("urule.decisionTableDeserializer");
   }

   public DecisionTable buildTable() {
      try {
         String var1 = this.a();
         Document var2 = DocumentHelper.parseText(var1);
         return this.d.deserialize(var2.getRootElement());
      } catch (Exception var3) {
         throw new InfoException(var3);
      }
   }

   private String a(int var1, String var2, String var3) {
      if (StringUtils.isBlank(var3)) {
         var3 = "";
      }

      return "<col num=\"" + var1 + "\" width=\"180\" type=\"" + var2 + "\"" + var3 + "/>";
   }

   private String a() throws IOException {
      StringBuilder var1 = new StringBuilder();
      var1.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
      var1.append("<decision-table");
      ExcelImportUtils.builderProperties(this.a, var1, this.b.getProperties(), false);
      var1.append(">");
      ExcelImportUtils.builderRemark(var1, this.b.getProperties());
      HashMap var2 = new HashMap();
      List var3 = this.b.getHeaders();

      for(int var4 = 0; var4 < var3.size(); ++var4) {
         Header var5 = (Header)var3.get(var4);
         HeaderType var6 = var5.getType();
         String var7 = var5.getName();
         if ((var6.equals(HeaderType.assign) || var6.equals(HeaderType.condition)) && var5.isPredefine()) {
            PredefineRow var8 = (PredefineRow)this.b.getPredefineNameMap().get(var7.split("\\.")[0]);
            if (ExcelSupport.isParameter(var8.getFromType())) {
               String[] var9 = var7.split("\\.");
               var9[0] = var8.getFromValue();
               Variable var10 = this.a.findParameterByLabel(var9[0], var9[1]);
               if (var10 == null) {
                  var9[0] = var8.getType();
                  VariableCategory var11 = this.a.findVariableCategory(var9);
                  if (var11 != null) {
                     var2.put(var8.getType(), var11);
                  }
               }
            }
         }
      }

      ExcelImportUtils.builderPredefineXml(var1, ExcelImportUtils.getPredefineGroupPriority(this.b.getProperties()), this.b.getPredefineRows(), this.b.getPredefineNameMap(), this.a, var2);

      for(int var15 = 0; var15 < var3.size(); ++var15) {
         Header var17 = (Header)var3.get(var15);
         HeaderType var20 = var17.getType();
         String var22 = var17.getName();
         if (var20.equals(HeaderType.assign)) {
            if (var17.isPredefine()) {
               PredefineRow var24 = (PredefineRow)this.b.getPredefineNameMap().get(var22.split("\\.")[0]);
               String var29 = "";
               if ("Variable".equals(var24.getFromType())) {
                  String[] var39 = new String[]{var24.getType(), this.a(var22)[1]};
                  Variable var54 = this.a.findVariable(var39);
                  var29 = ExcelImportUtils.buildPredefineXml(var24.getUuid(), var54.getUuid());
               } else if ("VariableCategory".equals(var24.getFromType())) {
                  String[] var40 = new String[]{var24.getType(), this.a(var22)[1]};
                  Variable var55 = this.a.findVariable(var40);
                  var29 = ExcelImportUtils.buildPredefineXml(var24.getUuid(), var55.getUuid());
               } else if ("Input".equals(var24.getFromType())) {
                  var29 = ExcelImportUtils.buildPredefineXml(var24.getUuid(), "");
               } else {
                  String[] var41 = new String[]{var24.getFromCategory(), var24.getFromValue()};
                  Variable var56 = this.a.findVariable(var41);
                  VariableCategory var12 = this.a.findVariableCategory(var41);
                  var29 = ExcelImportUtils.buildPredefineXml(var12.getUuid(), var56.getUuid());
               }

               var1.append(this.a(var15, "Assignment", var29));
            } else {
               String[] var25 = var22.split("\\.");
               if (ExcelSupport.isParameter(var25[0])) {
                  String var31 = "";
                  if (var25.length == 2) {
                     Variable var42 = this.a.findVariable(var25);
                     var31 = ExcelImportUtils.buildParameterXml(var42);
                  } else {
                     Variable var43 = this.a.findParameterByLabel(var25[1], var25[2]);
                     if (var43 == null) {
                        var43 = this.a.findVariable(var25);
                        var31 = ExcelImportUtils.buildParameterXml(var43);
                     } else {
                        VariableCategory var57 = this.a.findVariableCategoryByUUID(var43.getDataType());
                        Variable var65 = (Variable)var57.getVariableLabels().get(var25[2]);
                        var31 = ExcelImportUtils.buildParameterXml(var43, var57, var65);
                     }
                  }

                  var1.append(this.a(var15, "Assignment", var31));
               } else {
                  Variable var33 = this.a.findVariable(var25);
                  VariableCategory var45 = this.a.findVariableCategory(var25);
                  String var58 = ExcelImportUtils.buildVariableXml(var45, var33);
                  var1.append(this.a(var15, "Assignment", var58));
               }
            }
         } else if (var20.equals(HeaderType.condition)) {
            if (var17.isPredefine()) {
               PredefineRow var26 = (PredefineRow)this.b.getPredefineNameMap().get(var22.split("\\.")[0]);
               String var34 = "";
               if ("Input".equals(var26.getFromType())) {
                  var34 = ExcelImportUtils.buildPredefineXml(var26.getUuid(), "");
               } else if (ExcelSupport.isParameter(var26.getFromType())) {
                  String[] var48 = var22.split("\\.");
                  var48[0] = var26.getFromValue();
                  Variable var61 = this.a.findParameterByLabel(var48[0], var48[1]);
                  if (var61 == null) {
                     var48[0] = var26.getType();
                     Variable var67 = this.a.findVariable(var48);
                     if (null != var67) {
                        var34 = ExcelImportUtils.buildPredefineXml(var26.getUuid(), var67.getUuid());
                     } else {
                        var34 = ExcelImportUtils.buildPredefineXml(var26.getUuid(), "");
                     }
                  } else {
                     VariableCategory var68 = this.a.findVariableCategoryByUUID(var61.getDataType());
                     Variable var13 = (Variable)var68.getVariableLabels().get(var48[2]);
                     var34 = ExcelImportUtils.buildPredefineXml(var26.getUuid(), var13.getUuid());
                  }
               } else if (!"Variable".equals(var26.getFromType()) && !"Predefine".equals(var26.getFromType())) {
                  if ("VariableCategory".equals(var26.getFromType())) {
                     VariableCategory var47 = this.a.findVariableCategory(var26.getType());
                     String[] var60 = new String[]{var47.getName(), this.a(var22)[1]};
                     Variable var66 = this.a.findVariable(var60);
                     var34 = ExcelImportUtils.buildPredefineXml(var26.getUuid(), var66.getUuid());
                  } else if ("Method".equals(var26.getFromType()) || "CommonFunction".equals(var26.getFromType())) {
                     var34 = ExcelImportUtils.buildPredefineXml(var26.getUuid(), "");
                  }
               } else {
                  String[] var46 = new String[]{var26.getType(), this.a(var22)[1]};
                  Variable var59 = this.a.findVariable(var46);
                  var34 = ExcelImportUtils.buildPredefineXml(var26.getUuid(), var59.getUuid());
               }

               var1.append(this.a(var15, "Criteria", var34));
            } else {
               String[] var27 = var22.split("\\.");
               if (ExcelSupport.isParameter(var27[0])) {
                  String var35 = "";
                  if (var27.length == 2) {
                     Variable var49 = this.a.findVariable(var27);
                     var35 = ExcelImportUtils.buildParameterXml(var49);
                  } else {
                     Variable var50 = this.a.findParameterByLabel(var27[1], var27[2]);
                     if (var50 == null) {
                        var50 = this.a.findVariable(var27);
                        var35 = ExcelImportUtils.buildParameterXml(var50);
                     } else {
                        VariableCategory var62 = this.a.findVariableCategoryByUUID(var50.getDataType());
                        Variable var69 = (Variable)var62.getVariableLabels().get(var27[2]);
                        var35 = ExcelImportUtils.buildParameterXml(var50, var62, var69);
                     }
                  }

                  var1.append(this.a(var15, "Criteria", var35));
               } else {
                  Variable var37 = this.a.findVariable(var27);
                  VariableCategory var52 = this.a.findVariableCategory(var27);
                  String var63 = ExcelImportUtils.buildVariableXml(var52, var37);
                  var1.append(this.a(var15, "Criteria", var63));
               }
            }
         } else if (var20.equals(HeaderType.out)) {
            var1.append(this.a(var15, "ConsolePrint", ""));
         } else if (var20.equals(HeaderType.execute)) {
            var1.append(this.a(var15, "ExecuteMethod", ""));
         }
      }

      List var16 = this.b.getRows();

      for(int var18 = 0; var18 < var16.size(); ++var18) {
         var1.append("<row num=\"" + var18 + "\" height=\"40\"/>");
      }

      for(int var19 = 0; var19 < var16.size(); ++var19) {
         ContentRow var21 = (ContentRow)var16.get(var19);

         for(CellContent var38 : (Iterable<CellContent>)(Iterable<?>)(var21.getContents())) {
            Header var53 = var38.getHeader();
            HeaderType var64 = var53.getType();
            String var70 = var38.getContent();
            int var73 = var38.getSpan();
            if (var73 == 0) {
               var73 = 1;
            }

            var1.append("<cell row=\"" + var38.getRow() + "\" col=\"" + var38.getCol() + "\" rowspan=\"" + var73 + "\">");
            if (StringUtils.isNotBlank(var70)) {
               if (var64.equals(HeaderType.condition)) {
                  Criterion var14 = this.c.buildCriterion(var70);
                  var1.append(this.a(var14));
               } else if (var64.equals(HeaderType.assign)) {
                  AbstractValue var74 = this.c.buildValue(var70);
                  if (var74.getArithmetic() != null) {
                     var70 = StringEscapeUtils.escapeXml(var70);
                     var1.append("<value content=\"" + var70 + "\" type=\"Input\"/>");
                  } else {
                     var1.append(this.a((Value)var74));
                  }
               } else {
                  var70 = StringEscapeUtils.escapeXml(var70);
                  var1.append("<value content=\"" + var70 + "\" type=\"Input\"/>");
               }
            }

            var1.append("</cell>");
         }
      }

      ExcelImportUtils.builderLibraryXml(var1, this.a);
      var1.append("</decision-table>");
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
            var2.append("<value content=\"" + var4 + "\" type=\"Input\">");
         } else if (var1 instanceof VariableCategoryValue) {
            VariableCategoryValue var11 = (VariableCategoryValue)var1;
            String var17 = var11.getVariableCategory();
            String var5 = StringEscapeUtils.escapeXml(var17);
            var2.append("<value content=\"" + var5 + "\" type=\"Input\">");
         } else if (var1 instanceof VariableValue) {
            VariableValue var12 = (VariableValue)var1;
            PredefineRow var18 = (PredefineRow)this.b.getPredefineNameMap().get(var12.getVariableCategory());
            if (var18 != null) {
               String[] var24 = new String[]{var18.getType(), var12.getVariableLabel()};
               VariableCategory var6 = this.a.findVariableCategory(var24);
               if (var6 != null) {
                  Variable var7 = this.a.findVariable(var24);
                  var2.append("<value uuid=\"" + var18.getUuid() + "\" property-uuid=\"" + var7.getUuid() + "\" type=\"Predefine\">");
               } else {
                  String var32 = var12.getVariableCategory() + "." + var12.getVariableLabel();
                  var32 = StringEscapeUtils.escapeXml(var32);
                  var2.append(ExcelImportUtils.buildContentXml(var32));
               }
            } else {
               String[] var25 = new String[]{var12.getVariableCategory(), var12.getVariableLabel()};
               Variable var30 = this.a.findVariable(var25, true);
               Constant var34 = this.a.findConstant(var25, true);
               if (var30 != null) {
                  VariableCategory var8 = this.a.findVariableCategory(var25);
                  var2.append("<value category-uuid=\"" + var8.getUuid() + "\" var-category=\"" + var12.getVariableCategory() + "\" var=\"" + var30.getName() + "\" var-label=\"" + var12.getVariableLabel() + "\" datatype=\"" + var30.getDataType() + "\" uuid=\"" + var30.getUuid());
                  String var9 = var8.getName().toLowerCase();
                  if (ExcelSupport.isParameter(var9)) {
                     var2.append("\" type=\"Parameter\">");
                  } else {
                     var2.append("\" type=\"Variable\">");
                  }
               } else if (var34 != null) {
                  ConstantCategory var36 = this.a.findConstantCategory(var25);
                  var2.append("<value category-uuid=\"" + var36.getUuid() + "\" uuid=\"" + var34.getUuid() + "\" const-category=\"" + var36.getLabel() + "\" const=\"" + var34.getLabel() + "\" datatype=\"" + var34.getType().name() + "\" type=\"Constant\">");
               } else {
                  String var37 = var12.getVariableCategory() + "." + var12.getVariableLabel();
                  var37 = StringEscapeUtils.escapeXml(var37);
                  var2.append(ExcelImportUtils.buildContentXml(var37));
               }
            }
         } else if (var1 instanceof MethodValue) {
            MethodValue var13 = (MethodValue)var1;
            SpringBean var19 = this.a.getAction(var13.getBeanLabel(), var13.getMethodLabel());
            Method var26 = this.a.getActionMethod(var13.getBeanLabel(), var13.getMethodLabel());
            if (var19 != null && var26 != null) {
               var2.append("<value bean-name=\"" + var19.getId() + "\" bean-label=\"" + var13.getBeanLabel() + "\" method-name=\"" + var26.getMethodName() + "\" method-label=\"" + var13.getMethodLabel() + "\" type=\"Method\">");
               int var31 = 0;

               for(Parameter var39 : var13.getParameters()) {
                  com.bstek.urule.model.library.action.Parameter var40 = (com.bstek.urule.model.library.action.Parameter)var26.getParameters().get(var31);
                  var2.append("<parameter name=\"" + var40.getName() + "\" type=\"" + var40.getType().name() + "\">");
                  Value var10 = var39.getValue();
                  var2.append(this.a(var10));
                  var2.append("</parameter>");
                  ++var31;
               }
            } else {
               var2.append("<value content=\"" + var13.getBeanLabel() + "." + var13.getMethodLabel() + "\" type=\"Input\">");
            }
         } else if (var1 instanceof ParenValue) {
            ParenValue var14 = (ParenValue)var1;
            var2.append("<paren>");
            var2.append(this.a(var14.getValue()));
            ComplexArithmetic var20 = var14.getArithmetic();
            ArithmeticType var27 = var20.getType();
            var2.append("<complex-arith type=\"" + var27.name() + "\">");
            var2.append(this.a(var20.getValue()));
            var2.append("</complex-arith>");
            var2.append("</paren>");
         } else if (var1 instanceof ParameterValue) {
            ParameterValue var15 = (ParameterValue)var1;
            if (StringUtils.isNotBlank(var15.getKeyCategoryUuid())) {
               Variable var21 = null;
               VariableCategory var28 = this.a.findVariableCategoryByUUID(var15.getKeyCategoryUuid());
               if (var28 != null) {
                  var21 = (Variable)var28.getVariableNames().get(var15.getVariableName());
               }

               if (var21 != null) {
                  var2.append(ExcelImportUtils.buildParameterValueXml(var15, var28, var21));
               } else {
                  var2.append(ExcelImportUtils.buildContentXml(var15.getKeyName() + "." + var15.getVariableLabel()));
               }
            } else {
               String[] var22 = new String[]{"参数", var15.getVariableLabel()};
               Variable var29 = this.a.findVariable(var22, true);
               if (var29 != null) {
                  var2.append(ExcelImportUtils.buildParameterValueXml(var15, var29));
               } else {
                  var2.append(ExcelImportUtils.buildContentXml("参数." + var15.getVariableLabel()));
               }
            }
         } else {
            var2.append(ExcelImportUtils.buildContentXml((String)null));
         }

         ComplexArithmetic var16 = var1.getArithmetic();
         if (var16 == null) {
            var2.append("</value>");
            return var2.toString();
         } else {
            ArithmeticType var23 = var16.getType();
            var2.append("<complex-arith type=\"" + var23.name() + "\">");
            var2.append(this.a(var16.getValue()));
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
         String[] var3 = new String[]{var2[0], var1.substring(var2[0].length() + 1)};
         return var3;
      }
   }
}
