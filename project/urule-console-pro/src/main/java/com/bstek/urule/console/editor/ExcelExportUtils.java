package com.bstek.urule.console.editor;

import com.bstek.urule.console.util.StringUtils;
import com.bstek.urule.model.crosstab.CrosstabDefinition;
import com.bstek.urule.model.library.variable.Variable;
import com.bstek.urule.model.library.variable.VariableCategory;
import com.bstek.urule.model.rule.AbstractValue;
import com.bstek.urule.model.rule.CommonFunctionValue;
import com.bstek.urule.model.rule.ComplexArithmetic;
import com.bstek.urule.model.rule.ConstantValue;
import com.bstek.urule.model.rule.MethodValue;
import com.bstek.urule.model.rule.Parameter;
import com.bstek.urule.model.rule.ParameterValue;
import com.bstek.urule.model.rule.ParenValue;
import com.bstek.urule.model.rule.Predefine;
import com.bstek.urule.model.rule.PredefineGroupDefinition;
import com.bstek.urule.model.rule.PredefineValue;
import com.bstek.urule.model.rule.PredefineValueType;
import com.bstek.urule.model.rule.SimpleValue;
import com.bstek.urule.model.rule.Value;
import com.bstek.urule.model.rule.VariableCategoryValue;
import com.bstek.urule.model.rule.VariableValue;
import com.bstek.urule.model.rule.lhs.CommonFunctionLeftPart;
import com.bstek.urule.model.rule.lhs.Criteria;
import com.bstek.urule.model.rule.lhs.Criterion;
import com.bstek.urule.model.rule.lhs.FunctionLeftPart;
import com.bstek.urule.model.rule.lhs.Junction;
import com.bstek.urule.model.rule.lhs.JunctionType;
import com.bstek.urule.model.rule.lhs.LeftPart;
import com.bstek.urule.model.rule.lhs.LeftType;
import com.bstek.urule.model.rule.lhs.MethodLeftPart;
import com.bstek.urule.model.rule.lhs.PredefineLeftPart;
import com.bstek.urule.model.rule.lhs.VariableLeftPart;
import com.bstek.urule.model.scorecard.ComplexScorecardDefinition;
import com.bstek.urule.model.scorecard.ScorecardDefinition;
import com.bstek.urule.model.scorecard.ScoringType;
import com.bstek.urule.model.table.Condition;
import com.bstek.urule.model.table.DecisionTable;
import com.bstek.urule.model.table.JointType;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

public class ExcelExportUtils {
   public static String conditions2Label(ExcelSupport var0, List var1, JointType var2) {
      String var3 = "";

      for(Condition var5 : (Iterable<Condition>)(Iterable<?>)(var1)) {
         if (var5.getValue() != null) {
            if (var2 == JointType.and) {
               if (StringUtils.isNotBlank(var3)) {
                  var3 = var3 + " 并且 \n";
               }
            } else if (StringUtils.isNotBlank(var3)) {
               var3 = var3 + " 或则 \n ";
            }

            var3 = var3 + getLabelValue(var0, var5, var5.getValue());
         } else {
            var3 = var3 + var5.getOp().toString();
         }
      }

      return var3;
   }

   public static String criterions2Label(ExcelSupport var0, List var1, String var2) {
      String var3 = "";

      for(Criterion var5 : (Iterable<Criterion>)(Iterable<?>)(var1)) {
         if (var2.equals(JunctionType.and.name())) {
            if (StringUtils.isNotBlank(var3)) {
               var3 = var3 + " 并且 ";
            }
         } else if (StringUtils.isNotBlank(var3)) {
            var3 = var3 + " 或则 ";
         }

         if (var5 instanceof Junction) {
            Junction var11 = (Junction)var5;
            var3 = var3 + "(" + criterions2Label(var0, var11.getCriterions(), var11.getJunctionType()) + ")";
         } else if (var5 instanceof Criteria) {
            Criteria var6 = (Criteria)var5;
            LeftType var7 = var6.getLeft().getType();
            LeftPart var8 = var6.getLeft().getLeftPart();
            if (var7 != LeftType.variable && var7 != LeftType.parameter) {
               if (var7 == LeftType.commonfunction) {
                  CommonFunctionLeftPart var12 = (CommonFunctionLeftPart)var8;
                  var3 = var3 + var12.getLabel() + "(" + getLabelValue(var0, (Condition)null, var12.getParameter().getObjectParameter()) + ")";
               } else if (var7 == LeftType.function) {
                  FunctionLeftPart var13 = (FunctionLeftPart)var8;
                  var3 = var3 + var13.getName() + "(" + getParameterValues(var0, var13.getParameters()) + ")";
               } else if (var7 == LeftType.method) {
                  MethodLeftPart var14 = (MethodLeftPart)var8;
                  var3 = var3 + var14.getBeanLabel() + "." + var14.getMethodLabel() + "(" + getParameterValues(var0, var14.getParameters()) + ")";
               } else if (var7 == LeftType.predefine) {
                  PredefineLeftPart var15 = (PredefineLeftPart)var8;
                  var3 = var3 + var15.getName();
               }
            } else {
               VariableLeftPart var9 = (VariableLeftPart)var8;
               var3 = var3 + var9.getVariableCategory() + "." + var9.getVariableLabel();
            }

            var3 = var3 + " " + var6.getOp().name();
            var3 = var3 + " " + getLabelValue(var0, (Condition)null, var6.getValue());
         }
      }

      return var3;
   }

   public static String getLabelValue(ExcelSupport var0, Condition var1, Value var2) {
      String var3 = "";
      if (var2 instanceof VariableCategoryValue) {
         var3 = a(var0, var2);
      } else if (var2 instanceof ConstantValue) {
         var3 = a(var0, var2);
      } else if (var2 instanceof SimpleValue) {
         var3 = a(var0, var2);
      } else if (var2 instanceof VariableValue) {
         var3 = a(var0, var2);
      } else if (var2 instanceof ParameterValue) {
         var3 = a(var0, var2);
      } else if (var2 instanceof PredefineValue) {
         var3 = a(var0, var2);
      } else if (var2 instanceof MethodValue) {
         var3 = a(var0, var2);
      } else if (var2 instanceof CommonFunctionValue) {
         var3 = a(var0, var2);
      } else if (var2 instanceof ParenValue) {
         ParenValue var4 = (ParenValue)var2;
         var3 = "(" + getLabelValue(var0, (Condition)null, var4.getValue()) + ")";
      } else if (var2 instanceof ComplexArithmetic) {
         ComplexArithmetic var6 = (ComplexArithmetic)var2;
         var3 = getLabelValue(var0, (Condition)null, var6.getValue());
      } else {
         var3 = "";
      }

      if (var2 instanceof AbstractValue && var2.getArithmetic() != null) {
         var3 = var3 + " " + var2.getArithmetic().getType() + " " + getLabelValue(var0, (Condition)null, var2.getArithmetic().getValue()) + " ";
      }

      if (var1 != null) {
         var3 = var1.getOp().toString() + " " + var3;
      }

      return var3;
   }

   public static String getParameterValues(ExcelSupport var0, CommonFunctionValue var1) {
      return getLabelValue(var0, (Condition)null, var1.getParameter().getObjectParameter()) + "." + var1.getParameter().getPropertyLabel();
   }

   public static String getParameterValues(ExcelSupport var0, List var1) {
      String var2 = "";

      for(Parameter var4 : (Iterable<Parameter>)(Iterable<?>)(var1)) {
         if (StringUtils.isNotBlank(var2)) {
            var2 = var2 + ",";
         }

         var2 = var2 + getLabelValue(var0, (Condition)null, var4.getValue());
      }

      return var2;
   }

   private static String a(ExcelSupport var0, Value var1) {
      if (var1 instanceof VariableCategoryValue) {
         VariableCategoryValue var11 = (VariableCategoryValue)var1;
         return var11.getVariableCategory();
      } else if (var1 instanceof SimpleValue) {
         SimpleValue var10 = (SimpleValue)var1;
         return var10.getContent();
      } else if (var1 instanceof VariableValue) {
         VariableValue var9 = (VariableValue)var1;
         return var9.getVariableCategory() + "." + var9.getVariableLabel();
      } else if (var1 instanceof PredefineValue) {
         PredefineValue var8 = (PredefineValue)var1;
         return !StringUtils.isBlank(var8.getVariableCategory()) && !StringUtils.isBlank(var8.getPropertyLabel()) ? var8.getName() + "." + var8.getPropertyLabel() : var8.getName();
      } else if (var1 instanceof ParameterValue) {
         ParameterValue var7 = (ParameterValue)var1;
         if (StringUtils.isNotBlank(var7.getKeyCategoryUuid())) {
            Variable var13 = null;
            VariableCategory var4 = var0.findVariableCategoryByUUID(var7.getKeyCategoryUuid());
            if (var4 != null) {
               var13 = (Variable)var4.getVariableNames().get(var7.getVariableName());
            }

            if (var13 != null) {
               return "参数." + var7.getKeyLabel() + "." + var13.getLabel();
            }
         }

         return "参数." + var7.getVariableLabel();
      } else if (var1 instanceof ConstantValue) {
         ConstantValue var6 = (ConstantValue)var1;
         return var6.getConstantCategory() + "." + var6.getConstantLabel();
      } else if (var1 instanceof MethodValue) {
         MethodValue var5 = (MethodValue)var1;
         String var12 = "(" + getParameterValues(var0, var5.getParameters()) + ")";
         return var5.getBeanLabel() + "." + var5.getMethodLabel() + var12;
      } else if (var1 instanceof CommonFunctionValue) {
         CommonFunctionValue var2 = (CommonFunctionValue)var1;
         String var3 = "(" + getLabelValue(var0, (Condition)null, var2.getParameter().getObjectParameter()) + ")";
         return "函数." + var2.getLabel() + var3;
      } else {
         return "";
      }
   }

   public static void exportProperties(SXSSFWorkbook var0, CrosstabDefinition var1) {
      Integer var2 = Objects.isNull(var1.getPredefineGroup()) ? null : var1.getPredefineGroup().getPriority();
      String var3 = var1.getAssignTargetType();
      String var4 = var1.getAssignVariableCategory();
      String var5 = var1.getAssignVariableLabel();
      if (StringUtils.isNotBlank(var3)) {
         if ("variable".equals(var3)) {
            var3 = "变量";
         } else if ("parameter".equals(var3)) {
            var3 = "参数";
            var4 = var1.getKeyLabel();
         }
      }

      a(var0, (String)null, var2, var1.getDebug(), var1.getEnabled(), var1.getSalience(), var1.getEffectiveDate(), var1.getExpiresDate(), (String)null, var1.getRemark(), var3, var4, var5);
   }

   public static void exportProperties(SXSSFWorkbook var0, DecisionTable var1) {
      Integer var2 = Objects.isNull(var1.getPredefineGroup()) ? null : var1.getPredefineGroup().getPriority();
      a(var0, (String)null, var2, var1.getDebug(), var1.getEnabled(), var1.getSalience(), var1.getEffectiveDate(), var1.getExpiresDate(), var1.getMutexGroup(), var1.getRemark(), (String)null, (String)null, (String)null);
   }

   public static void exportProperties(SXSSFWorkbook var0, ScorecardDefinition var1) {
      String var2 = var1.getName();
      String var3 = var1.getAssignTargetType().name();
      String var4 = var1.getVariableCategory();
      String var5 = var1.getVariableLabel();
      if (StringUtils.isNotBlank(var3)) {
         if ("variable".equals(var3)) {
            var3 = "变量";
         } else if ("parameter".equals(var3)) {
            var3 = "参数";
            var4 = var1.getKeyLabel();
         }
      }

      int var6 = a(var0, var2, (Integer)null, var1.getDebug(), var1.getEnabled(), var1.getSalience(), var1.getEffectiveDate(), var1.getExpiresDate(), "", var1.getRemark(), var3, var4, var5);
      a(var0, var1.getScoringType(), var6, var1.getScoringBean());
   }

   public static void exportProperties(SXSSFWorkbook var0, ComplexScorecardDefinition var1) {
      String var2 = var1.getAssignTargetType().name();
      String var3 = var1.getVariableCategory();
      String var4 = var1.getVariableLabel();
      if (StringUtils.isNotBlank(var2)) {
         if ("variable".equals(var2)) {
            var2 = "变量";
         } else if ("parameter".equals(var2)) {
            var2 = "参数";
            var3 = var1.getKeyLabel();
         }
      }

      int var5 = a(var0, (String)null, (Integer)null, var1.getDebug(), var1.getEnabled(), var1.getSalience(), var1.getEffectiveDate(), var1.getExpiresDate(), "", var1.getRemark(), var2, var3, var4);
      a(var0, var1.getScoringType(), var5, var1.getScoringBean());
   }

   private static void a(SXSSFWorkbook var0, ScoringType var1, int var2, String var3) {
      SXSSFSheet var4 = var0.getSheet("property");
      if (!Objects.isNull(var1)) {
         a(var4, var2, "得分计算方式", var1.name());
         ++var2;
      }

      if (StringUtils.isNotBlank(var3)) {
         a(var4, var2, "自定义计算得分的Bean ID", var3);
         ++var2;
      }

   }

   private static int a(SXSSFWorkbook var0, String var1, Integer var2, Boolean var3, Boolean var4, Integer var5, Date var6, Date var7, String var8, String var9, String var10, String var11, String var12) {
      SXSSFSheet var13 = var0.createSheet("property");
      CellStyle var14 = var0.createCellStyle();
      var14.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
      var14.setFillPattern(FillPatternType.SOLID_FOREGROUND);
      var14.setBorderBottom(BorderStyle.THIN);
      var14.setBorderLeft(BorderStyle.THIN);
      var14.setBorderRight(BorderStyle.THIN);
      var14.setBorderTop(BorderStyle.THIN);
      SXSSFRow var15 = var13.createRow(0);
      Double var16 = 7314.285714285715;
      int var17 = Math.min(var16.intValue(), 65280);
      Cell var18 = var15.createCell(0);
      var18.setCellStyle(var14);
      var13.setColumnWidth(0, var17);
      var18.setCellValue("属性");
      var18 = var15.createCell(1);
      var13.setColumnWidth(1, var17);
      var18.setCellStyle(var14);
      var18.setCellValue("值");
      a(var13, 1, "允许调试信息输出", var3 == null ? false : var3);
      a(var13, 2, "是否启用", var4 == null ? true : var4);
      SimpleDateFormat var19 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      int var20 = 3;
      if (null != var5) {
         a(var13, var20, "优先级", var5);
         ++var20;
      }

      if (null != var6) {
         a(var13, var20, "生效时间", var19.format(var6));
         ++var20;
      }

      if (null != var7) {
         a(var13, var20, "失效时间", var19.format(var7));
         ++var20;
      }

      if (StringUtils.isNotBlank(var8)) {
         a(var13, var20, "互斥组", var8);
         ++var20;
      }

      if (StringUtils.isNotBlank(var9)) {
         a(var13, var20, "备注", var9);
         ++var20;
      }

      if (StringUtils.isNotBlank(var10)) {
         a(var13, var20, "赋值目标类型", var10);
         ++var20;
         a(var13, var20, "赋值对象类型", var11);
         ++var20;
         a(var13, var20, "赋值属性类型", var12);
         ++var20;
      }

      if (StringUtils.isNotBlank(var1)) {
         a(var13, var20, "名称", var1);
         ++var20;
      }

      if (!Objects.isNull(var2)) {
         a(var13, var20, "预定义值优先级", var2);
         ++var20;
      }

      return var20;
   }

   private static void a(SXSSFSheet var0, int var1, String var2, Object var3) {
      CellStyle var4 = var0.getWorkbook().createCellStyle();
      var4.setVerticalAlignment(VerticalAlignment.CENTER);
      var4.setBorderBottom(BorderStyle.THIN);
      var4.setBorderLeft(BorderStyle.THIN);
      var4.setBorderRight(BorderStyle.THIN);
      var4.setBorderTop(BorderStyle.THIN);
      var4.setWrapText(true);
      SXSSFRow var5 = var0.createRow(var1);
      Cell var6 = var5.createCell(0);
      var6.setCellStyle(var4);
      var6.setCellValue(var2);
      var6 = var5.createCell(1);
      var6.setCellStyle(var4);
      if (var3 == null) {
         var6.setCellValue("");
      } else if (var3 instanceof Boolean) {
         var6.setCellValue((Boolean)var3);
      } else if (var3 instanceof Integer) {
         var6.setCellValue((double)(Integer)var3);
      } else {
         var6.setCellValue(var3.toString());
      }

   }

   public static void exportPredefine(SXSSFWorkbook var0, PredefineGroupDefinition var1, ExcelSupport var2) {
      SXSSFSheet var3 = var0.createSheet("predefine");
      SXSSFRow var4 = var3.createRow(0);
      Double var5 = 7314.285714285715;
      int var6 = Math.min(var5.intValue(), 65280);
      CellStyle var7 = var0.createCellStyle();
      var7.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
      var7.setFillPattern(FillPatternType.SOLID_FOREGROUND);
      var7.setBorderBottom(BorderStyle.THIN);
      var7.setBorderLeft(BorderStyle.THIN);
      var7.setBorderRight(BorderStyle.THIN);
      var7.setBorderTop(BorderStyle.THIN);
      CellStyle var8 = var0.createCellStyle();
      var8.setVerticalAlignment(VerticalAlignment.CENTER);
      var8.setBorderBottom(BorderStyle.THIN);
      var8.setBorderLeft(BorderStyle.THIN);
      var8.setBorderRight(BorderStyle.THIN);
      var8.setBorderTop(BorderStyle.THIN);
      var8.setWrapText(true);
      Cell var9 = var4.createCell(0);
      var9.setCellStyle(var7);
      var3.setColumnWidth(0, var6);
      var9.setCellValue("name");
      var9 = var4.createCell(1);
      var3.setColumnWidth(1, var6 / 2);
      var9.setCellStyle(var7);
      var9.setCellValue("from or in");
      var9 = var4.createCell(2);
      var9.setCellStyle(var7);
      var3.setColumnWidth(2, var6);
      var9.setCellValue("type");
      var9 = var4.createCell(3);
      var9.setCellStyle(var7);
      var3.setColumnWidth(3, var6);
      var9.setCellValue("from type");
      var9 = var4.createCell(4);
      var9.setCellStyle(var7);
      var3.setColumnWidth(4, var6);
      var9.setCellValue("from category");
      var9 = var4.createCell(5);
      var9.setCellStyle(var7);
      var3.setColumnWidth(5, var6);
      var9.setCellValue("from value");
      var9 = var4.createCell(6);
      var9.setCellStyle(var7);
      var3.setColumnWidth(6, var6 * 2);
      var9.setCellValue("params");
      var9 = var4.createCell(7);
      var9.setCellStyle(var7);
      var3.setColumnWidth(7, var6 * 3);
      var9.setCellValue("condition");
      int var10 = 1;

      for(Predefine var12 : var1.getPredefines()) {
         SXSSFRow var13 = var3.createRow(var10);
         Cell var14 = var13.createCell(0);
         var14.setCellStyle(var8);
         var14.setCellValue(var12.getName());
         var14 = var13.createCell(1);
         var14.setCellStyle(var8);
         var14.setCellValue(var12.getValueType().name());
         var14 = var13.createCell(2);
         var14.setCellStyle(var8);
         VariableCategory var15 = var2.findVariableCategoryByUUID(var12.getType());
         if (var15 != null) {
            var14.setCellValue(var15.getName());
         } else {
            var14.setCellValue(var12.getType());
         }

         Cell var16 = var13.createCell(3);
         var16.setCellStyle(var8);
         if (var12.getValue() instanceof VariableCategoryValue) {
            VariableCategoryValue var17 = (VariableCategoryValue)var12.getValue();
            var14 = var13.createCell(4);
            var14.setCellStyle(var8);
            var14.setCellValue(var17.getVariableCategory());
            var14 = var13.createCell(5);
            var14.setCellStyle(var8);
            var16.setCellValue(var17.getValueType().name());
            var14 = var13.createCell(6);
            var14.setCellStyle(var8);
         } else if (var12.getValue() instanceof SimpleValue) {
            SimpleValue var52 = (SimpleValue)var12.getValue();
            var14 = var13.createCell(4);
            var14.setCellStyle(var8);
            var14.setCellValue("静态值");
            var14 = var13.createCell(5);
            var14.setCellStyle(var8);
            var14.setCellValue(var52.getContent());
            var16.setCellValue(var52.getValueType().name());
            var14 = var13.createCell(6);
            var14.setCellStyle(var8);
         } else if (var12.getValue() instanceof VariableValue) {
            VariableValue var53 = (VariableValue)var12.getValue();
            var14 = var13.createCell(4);
            var14.setCellStyle(var8);
            var14.setCellValue(var53.getVariableCategory());
            var14 = var13.createCell(5);
            var14.setCellStyle(var8);
            var14.setCellValue(var53.getVariableLabel());
            var16.setCellValue(var53.getValueType().name());
            var14 = var13.createCell(6);
            var14.setCellStyle(var8);
         } else if (var12.getValue() instanceof PredefineValue) {
            PredefineValue var54 = (PredefineValue)var12.getValue();
            var14 = var13.createCell(4);
            var14.setCellStyle(var8);
            var14.setCellValue(var54.getName());
            var14 = var13.createCell(5);
            var14.setCellStyle(var8);
            var14.setCellValue(var54.getPropertyLabel());
            var16.setCellValue("Predefine");
            var14 = var13.createCell(6);
            var14.setCellStyle(var8);
         } else if (var12.getValue() instanceof ParameterValue) {
            ParameterValue var55 = (ParameterValue)var12.getValue();
            var14 = var13.createCell(4);
            var14.setCellStyle(var8);
            var14.setCellValue(var55.getKeyLabel());
            var14 = var13.createCell(5);
            var14.setCellStyle(var8);
            var14.setCellValue(var55.getVariableLabel());
            var16.setCellValue(var55.getValueType().name());
            var14 = var13.createCell(6);
            var14.setCellStyle(var8);
         } else if (var12.getValue() instanceof ConstantValue) {
            ConstantValue var56 = (ConstantValue)var12.getValue();
            var14 = var13.createCell(4);
            var14.setCellStyle(var8);
            var14.setCellValue(var56.getConstantCategory());
            var14 = var13.createCell(5);
            var14.setCellStyle(var8);
            var14.setCellValue(var56.getConstantLabel());
            var16.setCellValue(var56.getValueType().name());
            var14 = var13.createCell(6);
            var14.setCellStyle(var8);
         } else if (var12.getValue() instanceof MethodValue) {
            MethodValue var57 = (MethodValue)var12.getValue();
            var14 = var13.createCell(4);
            var14.setCellStyle(var8);
            var14.setCellValue(var57.getBeanLabel());
            var14 = var13.createCell(5);
            var14.setCellStyle(var8);
            var14.setCellValue(var57.getMethodLabel());
            var16.setCellValue(var57.getValueType().name());
            var14 = var13.createCell(6);
            var14.setCellStyle(var8);
            var14.setCellValue(getParameterValues(var2, var57.getParameters()));
         } else if (var12.getValue() instanceof CommonFunctionValue) {
            CommonFunctionValue var58 = (CommonFunctionValue)var12.getValue();
            var14 = var13.createCell(4);
            var14.setCellStyle(var8);
            var14.setCellValue("函数");
            var14 = var13.createCell(5);
            var14.setCellStyle(var8);
            var14.setCellValue(var58.getLabel());
            var16.setCellValue(var58.getValueType().name());
            var14 = var13.createCell(6);
            var14.setCellStyle(var8);
            var14.setCellValue(getParameterValues(var2, var58));
         }

         var14 = var13.createCell(7);
         var14.setCellStyle(var8);
         if (var12.getValueType() == PredefineValueType.in && var12.getJunction() != null) {
            var14.setCellValue(criterions2Label(var2, var12.getJunction().getCriterions(), var12.getJunction().getJunctionType()));
         }

         ++var10;
      }

   }
}
