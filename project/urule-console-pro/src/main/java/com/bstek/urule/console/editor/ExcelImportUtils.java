package com.bstek.urule.console.editor;

import com.bstek.urule.builder.resource.ResourceType;
import com.bstek.urule.console.InfoException;
import com.bstek.urule.console.database.model.datasource.FieldType;
import com.bstek.urule.console.editor.constant.ConstantInfo;
import com.bstek.urule.console.editor.decision.PredefineRow;
import com.bstek.urule.console.editor.lib.VariableInfo;
import com.bstek.urule.console.util.StringUtils;
import com.bstek.urule.model.function.FunctionDescriptor;
import com.bstek.urule.model.library.Datatype;
import com.bstek.urule.model.library.action.Method;
import com.bstek.urule.model.library.action.Parameter;
import com.bstek.urule.model.library.action.SpringBean;
import com.bstek.urule.model.library.constant.Constant;
import com.bstek.urule.model.library.constant.ConstantCategory;
import com.bstek.urule.model.library.variable.Act;
import com.bstek.urule.model.library.variable.Variable;
import com.bstek.urule.model.library.variable.VariableCategory;
import com.bstek.urule.model.rule.ParameterValue;
import com.bstek.urule.model.scorecard.ScoringType;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.SheetVisibility;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelImportUtils {
   private static final String a = "predefineGroupPriority";

   public static List parseSheets(InputStream var0) throws IOException {
      XSSFWorkbook var1 = new XSSFWorkbook(var0);
      ArrayList var2 = new ArrayList();

      for(int var3 = 0; var3 < var1.getNumberOfSheets(); ++var3) {
         XSSFSheet var4 = var1.getSheetAt(var3);
         if (var1.getSheetVisibility(var3) == SheetVisibility.VISIBLE) {
            var2.add(var4);
         }
      }

      if (var2.size() == 0) {
         var1.close();
         throw new InfoException("导入Excel没有合法的Sheet！");
      } else {
         return var2;
      }
   }

   public static XSSFSheet findDataSheet(XSSFWorkbook var0) {
      XSSFSheet var1 = var0.getSheetAt(var0.getActiveSheetIndex());
      if ("predefine".equals(var1.getSheetName()) || "property".equals(var1.getSheetName())) {
         for(int var2 = 0; var2 < var0.getNumberOfSheets(); ++var2) {
            XSSFSheet var3 = var0.getSheetAt(var2);
            String var4 = var3.getSheetName();
            if (!"predefine".equals(var4) && !"property".equals(var4)) {
               var1 = var3;
               break;
            }
         }
      }

      return var1;
   }

   public static Map parseVariables(List var0) {
      HashMap var1 = new HashMap();

      for(XSSFSheet var3 : (Iterable<XSSFSheet>)(Iterable<?>)(var0)) {
         ArrayList var4 = new ArrayList();
         int var5 = var3.getLastRowNum();

         for(int var6 = 0; var6 <= var5; ++var6) {
            XSSFRow var7 = var3.getRow(var6);
            PropertyItem var8 = new PropertyItem();
            var4.add(var8);
            XSSFCell var9 = var7.getCell(0);
            String var10 = a(var9, true);
            var8.setName(var10);
            if (var7.getLastCellNum() > 1) {
               var9 = var7.getCell(1);
               var8.setLabel(a(var9));
            }

            if (var7.getLastCellNum() > 2) {
               var9 = var7.getCell(2);
               var8.setDataType(a(a(var9)));
            }

            if (var7.getLastCellNum() > 3) {
               var9 = var7.getCell(3);
               var8.setAct(b(a(var9)));
            }
         }

         var1.put(var3.getSheetName(), var4);
      }

      return var1;
   }

   private static FieldType a(String var0) {
      if (StringUtils.isBlank(var0)) {
         return FieldType.String;
      } else {
         String var1 = var0.trim().toUpperCase();
         if (FieldType.Integer.name().toUpperCase().equals(var1)) {
            return FieldType.Integer;
         } else if (FieldType.Double.name().toUpperCase().equals(var1)) {
            return FieldType.Double;
         } else if (FieldType.Long.name().toUpperCase().equals(var1)) {
            return FieldType.Long;
         } else if (FieldType.Float.name().toUpperCase().equals(var1)) {
            return FieldType.Float;
         } else if (FieldType.BigDecimal.name().toUpperCase().equals(var1)) {
            return FieldType.BigDecimal;
         } else if (FieldType.Short.name().toUpperCase().equals(var1)) {
            return FieldType.Short;
         } else if (FieldType.Boolean.name().toUpperCase().equals(var1)) {
            return FieldType.Boolean;
         } else {
            return FieldType.Date.name().toUpperCase().equals(var1) ? FieldType.Date : FieldType.String;
         }
      }
   }

   private static Act b(String var0) {
      if (StringUtils.isBlank(var0)) {
         return Act.InOut;
      } else {
         String var1 = var0.trim().toUpperCase();
         if (Act.In.name().toUpperCase().equals(var1)) {
            return Act.In;
         } else {
            return Act.Out.name().toUpperCase().equals(var1) ? Act.Out : Act.InOut;
         }
      }
   }

   public static List parseParameters(List var0) {
      ArrayList var1 = new ArrayList();
      ArrayList var2 = new ArrayList();

      for(XSSFSheet var4 : (Iterable<XSSFSheet>)(Iterable<?>)(var0)) {
         int var5 = var4.getLastRowNum();

         for(int var6 = 0; var6 <= var5; ++var6) {
            XSSFRow var7 = var4.getRow(var6);
            PropertyItem var8 = new PropertyItem();
            XSSFCell var9 = var7.getCell(0);
            String var10 = a(var9, true);
            var8.setName(var10);
            if (!var2.contains(var10)) {
               var1.add(var8);
               var2.add(var10);
               if (var7.getLastCellNum() > 1) {
                  var9 = var7.getCell(1);
                  var8.setLabel(a(var9));
               }

               if (var7.getLastCellNum() > 2) {
                  var9 = var7.getCell(2);
                  var8.setDataType(a(a(var9)));
               }

               if (var7.getLastCellNum() > 3) {
                  var9 = var7.getCell(3);
                  var8.setAct(b(a(var9)));
               }
            }
         }
      }

      return var1;
   }

   private static String a(XSSFCell var0) {
      return a(var0, false);
   }

   private static String a(XSSFCell var0, boolean var1) {
      String var2 = "";
      if (CellType.NUMERIC == var0.getCellTypeEnum()) {
         if (var1) {
            Double var3 = var0.getNumericCellValue();
            var2 = var3.intValue() + "";
         } else {
            var2 = var0.getNumericCellValue() + "";
         }
      } else if (CellType.BOOLEAN == var0.getCellTypeEnum()) {
         var2 = var0.getBooleanCellValue() + "";
      } else if (CellType._NONE == var0.getCellTypeEnum()) {
         var2 = "";
      } else if (CellType.ERROR == var0.getCellTypeEnum()) {
         var2 = "ERROR";
      } else {
         var2 = var0.getStringCellValue();
      }

      return var2;
   }

   public static List parsePredefines(XSSFWorkbook var0) {
      ArrayList var1 = new ArrayList();
      XSSFSheet var2 = var0.getSheet("predefine");
      if (var2 == null) {
         return var1;
      } else {
         int var3 = var2.getLastRowNum();

         for(int var4 = 1; var4 <= var3; ++var4) {
            PredefineRow var5 = new PredefineRow();
            XSSFRow var6 = var2.getRow(var4);
            var5.setUuid(UUID.randomUUID().toString());
            var5.setName(var6.getCell(0).getStringCellValue());
            var5.setFromOrIn(var6.getCell(1).getStringCellValue());
            var5.setType(var6.getCell(2).getStringCellValue());
            var5.setFromType(var6.getCell(3).getStringCellValue());
            var5.setFromCategory(var6.getCell(4).getStringCellValue());
            var5.setFromValue(var6.getCell(5).getStringCellValue());
            var5.setParams(var6.getCell(6).getStringCellValue());
            var5.setCondition(var6.getCell(7).getStringCellValue());
            var1.add(var5);
         }

         return var1;
      }
   }

   public static Integer getPredefineGroupPriority(Map var0) {
      Integer var1 = null;
      if (var0.containsKey("predefineGroupPriority")) {
         var1 = (Integer)var0.get("predefineGroupPriority");
      }

      return var1;
   }

   public static ScoringType getScoringType(Map var0) {
      String var1 = null;
      if (var0.containsKey("scoringType")) {
         var1 = (String)var0.get("scoringType");
         return ScoringType.valueOf(var1);
      } else {
         return ScoringType.sum;
      }
   }

   public static String getScoringBean(Map var0) {
      String var1 = null;
      if (var0.containsKey("scoringBean")) {
         var1 = (String)var0.get("scoringBean");
      }

      return var1;
   }

   public static Map parseProperties(XSSFWorkbook var0) {
      HashMap var1 = new HashMap();
      XSSFSheet var2 = var0.getSheet("property");
      if (var2 == null) {
         return var1;
      } else {
         int var3 = var2.getLastRowNum();

         for(int var4 = 1; var4 <= var3; ++var4) {
            XSSFRow var5 = var2.getRow(var4);
            String var6 = var5.getCell(0).getStringCellValue();
            if (!StringUtils.isBlank(var6)) {
               var6 = var6.trim().toLowerCase();
               if ("debug".equals(var6) || "允许调试信息输出".equals(var6)) {
                  Boolean var7 = var5.getCell(1).getBooleanCellValue();
                  var1.put("debug", var7);
               }

               if ("enable".equals(var6) || "是否启用".equals(var6)) {
                  Boolean var14 = var5.getCell(1).getBooleanCellValue();
                  var1.put("enable", var14);
               }

               if ("salience".equals(var6) || "优先级".equals(var6)) {
                  try {
                     Double var15 = var5.getCell(1).getNumericCellValue();
                     Integer var8 = var15.intValue();
                     var1.put("salience", var8);
                  } catch (Exception var12) {
                  }
               }

               SimpleDateFormat var16 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
               if ("effectiveDate".toLowerCase().equals(var6) || "生效时间".equals(var6)) {
                  try {
                     String var17 = var5.getCell(1).getStringCellValue();
                     Date var9 = var16.parse(var17);
                     var1.put("effectiveDate", var9);
                  } catch (Exception var11) {
                  }
               }

               if ("expiresDate".toLowerCase().equals(var6) || "失效时间".equals(var6)) {
                  try {
                     String var18 = var5.getCell(1).getStringCellValue();
                     Date var28 = var16.parse(var18);
                     var1.put("expiresDate", var28);
                  } catch (Exception var10) {
                  }
               }

               if ("mutexGroup".toLowerCase().equals(var6) || "互斥组".equals(var6)) {
                  String var19 = var5.getCell(1).getStringCellValue();
                  var1.put("mutexGroup", var19);
               }

               if ("remark".equals(var6) || "备注".equals(var6)) {
                  String var20 = var5.getCell(1).getStringCellValue();
                  var1.put("remark", var20);
               }

               if ("name".equals(var6) || "名称".equals(var6)) {
                  String var21 = var5.getCell(1).getStringCellValue();
                  var1.put("name", var21);
               }

               if ("predefineGroupPriority".toLowerCase().equals(var6) || "预定义值优先级".equals(var6)) {
                  Double var22 = var5.getCell(1).getNumericCellValue();
                  var1.put("predefineGroupPriority", var22.intValue());
               }

               if ("assignTargetType".toLowerCase().equals(var6) || "赋值目标类型".equals(var6)) {
                  String var23 = var5.getCell(1).getStringCellValue();
                  if (ExcelSupport.isParameter(var23)) {
                     var23 = "parameter";
                  } else if ("变量".equals(var23)) {
                     var23 = "variable";
                  }

                  var1.put("assignTargetType", var23);
               }

               if ("assignTargetCategory".toLowerCase().equals(var6) || "赋值对象类型".equals(var6)) {
                  String var24 = var5.getCell(1).getStringCellValue();
                  var1.put("assignTargetCategory", var24);
               }

               if ("assignTargetVariable".toLowerCase().equals(var6) || "赋值属性类型".equals(var6)) {
                  String var25 = var5.getCell(1).getStringCellValue();
                  var1.put("assignTargetVariable", var25);
               }

               if ("scoringType".toLowerCase().equals(var6) || "得分计算方式".equals(var6)) {
                  String var26 = var5.getCell(1).getStringCellValue();
                  var1.put("scoringType", var26);
               }

               if ("scoringBean".toLowerCase().equals(var6) || "自定义计算得分的Bean ID".toLowerCase().equals(var6)) {
                  String var27 = var5.getCell(1).getStringCellValue();
                  var1.put("scoringBean", var27);
               }
            }
         }

         return var1;
      }
   }

   public static void builderProperties(ExcelSupport var0, StringBuilder var1, Map var2, boolean var3) {
      Boolean var4 = false;
      if (var2.containsKey("debug")) {
         var4 = (Boolean)var2.get("debug");
      }

      Boolean var5 = false;
      if (var2.containsKey("enable")) {
         var5 = (Boolean)var2.get("enable");
      }

      Integer var6 = null;
      if (var2.containsKey("salience")) {
         var6 = (Integer)var2.get("salience");
      }

      Date var7 = null;
      if (var2.containsKey("effectiveDate")) {
         var7 = (Date)var2.get("effectiveDate");
      }

      Date var8 = null;
      if (var2.containsKey("expiresDate")) {
         var8 = (Date)var2.get("expiresDate");
      }

      String var9 = null;
      if (var2.containsKey("mutexGroup")) {
         var9 = (String)var2.get("mutexGroup");
      }

      a(var1, var4, var5, var6, var7, var8, var9);
      if (var3) {
         String var10 = null;
         if (var2.containsKey("assignTargetType")) {
            var10 = (String)var2.get("assignTargetType");
         }

         String var11 = null;
         if (var2.containsKey("assignTargetCategory")) {
            var11 = (String)var2.get("assignTargetCategory");
         }

         String var12 = null;
         if (var2.containsKey("assignTargetVariable")) {
            var12 = (String)var2.get("assignTargetVariable");
         }

         a(var0, var1, var10, var11, var12);
      }

   }

   private static void a(StringBuilder var0, boolean var1, boolean var2, Integer var3, Date var4, Date var5, String var6) {
      if (var1) {
         var0.append(" debug=\"" + var1 + "\"");
      }

      if (!var2) {
         var0.append(" enabled=\"" + var2 + "\"");
      }

      if (null != var3 && var3 != 10) {
         var0.append(" salience=\"" + var3 + "\"");
      }

      SimpleDateFormat var7 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      if (null != var4) {
         var0.append(" effective-date=\"" + var7.format(var4) + "\"");
      }

      if (null != var5) {
         var0.append(" expires-date=\"" + var7.format(var5) + "\"");
      }

      if (StringUtils.isNotBlank(var6)) {
         var0.append(" mutex-group=\"" + var6 + "\"");
      }

   }

   private static void a(ExcelSupport var0, StringBuilder var1, String var2, String var3, String var4) {
      StringBuilder var5 = new StringBuilder("");
      if ("variable".equals(var2)) {
         VariableCategory var6 = var0.findVariableCategory(var3, var4);
         if (null == var6) {
            return;
         }

         Variable var7 = (Variable)var6.getVariableLabels().get(var4);
         var5.append(" assign-target-type=\"variable\" category-uuid=\"" + var6.getUuid() + "\" var-category=\"" + var6.getName() + "\" var=\"" + var7.getName() + "\" var-label=\"" + var7.getLabel() + "\" datatype=\"" + var7.getType() + "\" uuid=\"" + var7.getUuid() + "\"");
      } else {
         if (!"parameter".equals(var2)) {
            return;
         }

         var5.append(" assign-target-type=\"parameter\" category-uuid=\"参数\" ");
         if (StringUtils.isBlank(var3)) {
            Variable var9 = var0.findSimpleParameterByLabel(var4);
            if (var9 == null) {
               return;
            }

            var5.append(" uuid=\"" + var9.getUuid() + "\" var-category=\"" + "参数" + "\" var=\"" + var9.getName() + "\" var-label=\"" + var9.getLabel() + "\" datatype=\"" + var9.getType() + "\"");
         } else {
            Variable var10 = var0.findParameterByLabel(var3, var4);
            if (var10 == null) {
               return;
            }

            Variable var11 = null;
            VariableCategory var8 = null;
            var8 = var0.findVariableCategoryByUUID(var10.getDataType());
            var11 = (Variable)var8.getVariableLabels().get(var4);
            if (null == var11) {
               return;
            }

            var5.append(" uuid=\"" + var10.getUuid() + "\" var-category=\"" + "参数" + "\" var=\"" + var11.getName() + "\" var-label=\"" + var11.getLabel() + "\" datatype=\"" + var11.getType() + "\"  key-category-uuid=\"" + var8.getUuid() + "\" key-uuid=\"" + var11.getUuid() + "\" key-label=\"" + var10.getLabel() + "\" key-name=\"" + var10.getName() + "\"");
         }
      }

      var1.append(var5.toString());
   }

   public static void builderRemark(StringBuilder var0, Map var1) {
      if (var1.containsKey("remark")) {
         String var2 = (String)var1.get("remark");
         var0.append("<remark><![CDATA[" + var2 + "]]></remark>");
      }

   }

   public static void builderPredefineXml(StringBuilder var0, Integer var1, List var2, Map var3, ExcelSupport var4, Map var5) {
      if (var2.size() != 0) {
         for(PredefineRow var7 : (Iterable<PredefineRow>)(Iterable<?>)(var2)) {
            if ("Predefine".equals(var7.getFromType()) && Datatype.isType(var7.getType()) && StringUtils.isNotBlank(var7.getFromCategory()) && StringUtils.isNotBlank(var7.getFromValue())) {
               String[] var8 = new String[2];
               PredefineRow var9 = (PredefineRow)var3.get(var7.getFromCategory());
               if (var9 != null && ExcelSupport.isParameter(var9.getFromType())) {
                  var8[0] = var9.getType();
                  var8[1] = var7.getFromValue();
                  VariableCategory var10 = var4.findVariableCategory(var8);
                  if (var10 != null) {
                     var5.put(var9.getType(), var10);
                  }
               }
            }
         }

         if (var1 == null) {
            var1 = 1;
         }

         var0.append("<predefine-group priority=\"" + var1 + "\">");

         for(PredefineRow var20 : (Iterable<PredefineRow>)(Iterable<?>)(var2)) {
            String var21 = var20.getFromType();
            var0.append("<predefine uuid=\"" + var20.getUuid() + "\" name=\"" + var20.getName() + "\" ");
            if (StringUtils.isNotBlank(var20.getCondition())) {
            }

            if ("Variable".equals(var21)) {
               String[] var28 = new String[]{var20.getFromCategory(), var20.getFromValue()};
               Variable var34 = var4.findVariable(var28);
               VariableCategory var40 = var4.findVariableCategory(var28);
               VariableCategory var44 = var4.findVariableCategory(var20.getType());
               if (var44 == null) {
                  var0.append(" type=\"" + var20.getType() + "\"");
               } else {
                  var0.append(" type=\"" + var44.getUuid() + "\"");
               }

               var0.append(" value-type=\"" + var20.getFromOrIn() + "\">");
               var0.append("<value category-uuid=\"" + var40.getUuid() + "\" var-category=\"" + var40.getName() + "\" var=\"" + var34.getName() + "\" var-label=\"" + var20.getFromValue() + "\" datatype=\"" + var34.getDataType() + "\" uuid=\"" + var34.getUuid() + "\" type=\"Variable\"/>");
            } else if ("VariableCategory".equals(var21)) {
               VariableCategory var27 = var4.findVariableCategory(var20.getType());
               if (var27 != null) {
                  var0.append(" type=\"" + var27.getUuid() + "\" value-type=\"" + var20.getFromOrIn() + "\">");
                  var0.append("<value category-uuid=\"" + var27.getUuid() + "\" var-category=\"" + var27.getName() + "\" type=\"VariableCategory\"/>");
               } else {
                  var0.append(" type=\"" + var20.getType() + "\" value-type=\"" + var20.getFromOrIn() + "\">");
               }
            } else if ("Constant".equals(var21)) {
               String[] var26 = new String[]{var20.getFromCategory(), var20.getFromValue()};
               Constant var33 = var4.findConstant(var26, false);
               ConstantCategory var39 = var4.findConstantCategory(var26);
               var0.append(" type=\"" + var20.getType() + "\" value-type=\"" + var20.getFromOrIn() + "\">");
               var0.append("<value category-uuid=\"" + var39.getUuid() + "\" uuid=\"" + var33.getUuid() + "\" const-category=\"" + var39.getLabel() + "\" const=\"" + var33.getName() + "\" const-label=\"" + var33.getLabel() + "\" data-type=\"" + var33.getType() + "\" type=\"Constant\"/>");
            } else if (!"Method".equals(var21)) {
               if ("CommonFunction".equals(var21)) {
                  var0.append(" type=\"" + var20.getType() + "\" value-type=\"" + var20.getFromOrIn() + "\">");
                  FunctionDescriptor var25 = var4.getFunction(var20.getFromValue());
                  if (var25 == null) {
                     var0.append("<value content=\"函数:" + var20.getFromValue() + "\" type=\"Input\">");
                  } else {
                     var0.append("<value function-label=\"" + var25.getLabel() + "\" function-name=\"" + var25.getName() + "\" type=\"CommonFunction\">");
                     String var32 = var20.getParams();
                     if (StringUtils.isNotBlank(var32)) {
                        String[] var38 = var32.split("\\.");
                        if (var38.length < 2) {
                           var0.append("<function-parameter name=\"对象\" property-name=\"" + var32 + "\" property-label=\"" + var32 + "\">");
                           var0.append("<value content=\"" + var32 + "\" type=\"Input\"></value>");
                           var0.append("</function-parameter>");
                        } else {
                           String[] var43 = new String[]{var38[0], var38[1]};
                           VariableCategory var46 = var4.findVariableCategory(var43);
                           var0.append("<function-parameter name=\"对象\" property-name=\"" + var38[0] + "\" property-label=\"" + var38[1] + "\">");
                           var0.append("<value category-uuid=\"" + var46.getUuid() + "\" var-category=\"" + var32 + "\" type=\"VariableCategory\"></value>");
                           var0.append("</function-parameter>");
                        }
                     }
                  }

                  var0.append("</value>");
               } else if ("Parameter".equals(var21)) {
                  String[] var24 = new String[]{"参数", StringUtils.isBlank(var20.getFromCategory()) ? var20.getFromValue() : var20.getFromCategory()};
                  Variable var31 = var4.findVariable(var24);
                  if (Datatype.isType(var20.getType())) {
                     var0.append(" type=\"" + var20.getType() + "\" value-type=\"" + var20.getFromOrIn() + "\">");
                  } else if (var5.containsKey(var20.getType())) {
                     var0.append(" type=\"" + ((VariableCategory)var5.get(var20.getType())).getUuid() + "\" value-type=\"" + var20.getFromOrIn() + "\">");
                  } else {
                     VariableCategory var36 = var4.findVariableCategory(var20.getType());
                     if (var36 != null) {
                        var0.append(" type=\"" + var36.getUuid() + "\" value-type=\"" + var20.getFromOrIn() + "\">");
                     } else {
                        var0.append(" type=\"" + var20.getType() + "\" value-type=\"" + var20.getFromOrIn() + "\">");
                     }
                  }

                  String var37 = "<value category-uuid=\"参数\" uuid=\"" + var31.getUuid() + "\" var-category=\"" + "参数" + "\" var=\"" + var31.getName() + "\" var-label=\"" + var31.getLabel() + "\" datatype=\"" + var31.getType() + "\" type=\"Parameter\"/>";
                  if (Datatype.Object.name().equals(var31.getType().name()) && StringUtils.isNotBlank(var20.getFromValue())) {
                     VariableCategory var42 = var4.findVariableCategoryByUUID(var31.getDataType());
                     if (var42 != null) {
                        Variable var45 = (Variable)var42.getVariableLabels().get(var20.getFromValue());
                        if (var45 != null) {
                           var0.append("<value category-uuid=\"参数\" uuid=\"" + var31.getUuid() + "\" var-category=\"" + "参数" + "\" var=\"" + var31.getName() + "\" var-label=\"" + var31.getLabel() + "\" datatype=\"" + var31.getType() + "\" key-category-uuid=\"" + var42.getUuid() + "\" key-uuid=\"" + var45.getUuid() + "\" key-label=\"" + var45.getLabel() + "\" key-name=\"" + var45.getName() + "\" type=\"Parameter\"/>");
                        }
                     } else {
                        var0.append(var37);
                     }
                  } else {
                     var0.append(var37);
                  }
               } else if ("Predefine".equals(var21)) {
                  PredefineRow var23 = (PredefineRow)var3.get(var20.getFromCategory());
                  VariableCategory var30 = var4.findVariableCategory(var20.getType());
                  if (StringUtils.isEmpty(var20.getFromValue())) {
                     if (var30 != null) {
                        var0.append(" type=\"" + var30.getUuid() + "\" value-type=\"" + var20.getFromOrIn() + "\">");
                        var0.append("<value uuid=\"" + var23.getUuid() + "\" type=\"Predefine\"/>");
                     } else {
                        var0.append(" type=\"" + var20.getType() + "\" value-type=\"" + var20.getFromOrIn() + "\">");
                        var0.append("<value uuid=\"" + var23.getUuid() + "\" type=\"Predefine\"/>");
                     }
                  } else {
                     String[] var35 = new String[]{var23.getType(), var20.getFromValue()};
                     Variable var41 = var4.findVariable(var35);
                     if (var30 != null) {
                        var0.append(" type=\"" + var30.getUuid() + "\" value-type=\"" + var20.getFromOrIn() + "\">");
                     } else {
                        var0.append(" type=\"" + var20.getType() + "\" value-type=\"" + var20.getFromOrIn() + "\">");
                     }

                     var0.append("<value uuid=\"" + var23.getUuid() + "\" property-uuid=\"" + var41.getUuid() + "\" type=\"Predefine\"/>");
                  }
               } else if ("Input".equals(var21)) {
                  var0.append(" type=\"" + var20.getType() + "\" value-type=\"" + var20.getFromOrIn() + "\">");
                  var0.append("<value content=\"" + var20.getFromValue() + "\" type=\"Input\"/>");
               }
            } else {
               var0.append(" type=\"" + var20.getType() + "\" value-type=\"" + var20.getFromOrIn() + "\">");
               SpringBean var22 = var4.getAction(var20.getFromCategory(), var20.getFromValue());
               Method var29 = var4.getActionMethod(var20.getFromCategory(), var20.getFromValue());
               if (var22 != null && var29 != null) {
                  var0.append("<value bean-name=\"" + var22.getId() + "\" bean-label=\"" + var22.getName() + "\" method-name=\"" + var29.getMethodName() + "\" method-label=\"" + var29.getName() + "\" type=\"Method\">");
                  String var11 = var20.getParams();
                  if (StringUtils.isNotBlank(var11)) {
                     String[] var12 = var11.split(",");
                     int var13 = 0;

                     for(String var17 : var12) {
                        Parameter var18 = (Parameter)var29.getParameters().get(var13);
                        var0.append("<parameter name=\"" + var18.getName() + "\" type=\"" + var18.getType().name() + "\">");
                        var0.append("<value content=\"" + var17 + "\" type=\"Input\"></value>");
                        var0.append("</parameter>");
                        ++var13;
                     }
                  }
               } else {
                  var0.append("<value content=\"" + var20.getFromCategory() + "." + var20.getFromValue() + "\" type=\"Input\">");
               }

               var0.append("</value>");
            }

            var0.append("</predefine>");
         }

         var0.append("</predefine-group>");
      }
   }

   public static void builderLibraryXml(StringBuilder var0, ExcelSupport var1) {
      for(VariableInfo var3 : (Iterable<VariableInfo>)(Iterable<?>)(var1.getVarLibraries().values())) {
         if (var3.getType().endsWith(ResourceType.VariableLibrary.name())) {
            var0.append("<import-variable-library id=\"" + var3.getId() + "\" path=\"" + var3.getPath() + "\"/>");
         } else {
            var0.append("<import-parameter-library id=\"" + var3.getId() + "\" path=\"" + var3.getPath() + "\"/>");
         }
      }

      for(ConstantInfo var5 : (Iterable<ConstantInfo>)(Iterable<?>)(var1.getContLibraries().values())) {
         var0.append("<import-constant-library id=\"" + var5.getId() + "\" path=\"" + var5.getPath() + "\"/>");
      }

   }

   public static String buildParameterXml(Variable var0) {
      return " var-category=\"参数\" var-label=\"" + var0.getLabel() + "\" var=\"" + var0.getName() + "\" datatype=\"" + var0.getType().name() + "\" category-uuid=\"" + "参数" + "\" uuid=\"" + var0.getUuid() + "\"";
   }

   public static String buildParameterXml(Variable var0, VariableCategory var1, Variable var2) {
      return " var-category=\"参数\" var-label=\"" + var2.getLabel() + "\" var=\"" + var2.getName() + "\" datatype=\"" + var2.getType().name() + "\" category-uuid=\"" + "参数" + "\" uuid=\"" + var0.getUuid() + "\" key-uuid=\"" + var2.getUuid() + "\" key-category-uuid=\"" + var1.getUuid() + "\" key-label=\"" + var0.getLabel() + "\" key-name=\"" + var0.getName() + "\"";
   }

   public static String buildVariableXml(VariableCategory var0, Variable var1) {
      return " var-category=\"" + var0.getName() + "\" var-label=\"" + var1.getLabel() + "\" var=\"" + var1.getName() + "\" datatype=\"" + var1.getType().name() + "\" category-uuid=\"" + var0.getUuid() + "\" uuid=\"" + var1.getUuid() + "\"";
   }

   public static String buildParameterValueXml(ParameterValue var0, Variable var1) {
      return "<value category-uuid=\"参数\" uuid=\"" + var1.getUuid() + "\" var-category=\"" + "参数" + "\" var=\"" + var0.getVariableName() + "\" var-label=\"" + var1.getLabel() + "\" datatype=\"" + var1.getType() + "\" type=\"Parameter\"/>>";
   }

   public static String buildParameterValueXml(ParameterValue var0, VariableCategory var1, Variable var2) {
      return "<value category-uuid=\"参数\" uuid=\"" + var0.getUuid() + "\" var-category=\"" + "参数" + "\" var=\"" + var0.getVariableName() + "\" var-label=\"" + var0.getVariableLabel() + "\" datatype=\"" + var2.getType() + "\" key-category-uuid=\"" + var1.getUuid() + "\" key-uuid=\"" + var2.getUuid() + "\" key-label=\"" + var2.getName() + "\" key-name=\"" + var2.getLabel() + "\" type=\"Parameter\"/>>";
   }

   public static String buildPredefineXml(String var0, String var1) {
      return " predefine=\"true\" uuid=\"" + var0 + "\" property-uuid=\"" + var1 + "\"";
   }

   public static String buildContentXml(String var0) {
      return buildContentXml(var0, false);
   }

   public static String buildContentXml(String var0, boolean var1) {
      if (StringUtils.isBlank(var0)) {
         var0 = "";
      }

      String var2 = "<value content=\"" + var0 + "\" type=\"Input\"";
      if (var1) {
         var2 = var2 + "/";
      }

      return var2 + ">";
   }
}
