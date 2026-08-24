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

/** Converts Excel workbook rows into URule model objects and XML fragments. */
public class ExcelImportUtils {
   private static final String PREDEFINEGROUPPRIORITY = "predefineGroupPriority";

   public static List parseSheets(InputStream stream) throws IOException {
      XSSFWorkbook xSSFWorkbook = new XSSFWorkbook(stream);
      ArrayList sheets = new ArrayList();

      for(int number = 0; number < xSSFWorkbook.getNumberOfSheets(); ++number) {
         XSSFSheet sheetAt = xSSFWorkbook.getSheetAt(number);
         if (xSSFWorkbook.getSheetVisibility(number) == SheetVisibility.VISIBLE) {
            sheets.add(sheetAt);
         }
      }

      if (sheets.size() == 0) {
         xSSFWorkbook.close();
         throw new InfoException("导入Excel没有合法的Sheet！");
      } else {
         return sheets;
      }
   }

   public static XSSFSheet findDataSheet(XSSFWorkbook wb) {
      XSSFSheet sheetAt = wb.getSheetAt(wb.getActiveSheetIndex());
      if ("predefine".equals(sheetAt.getSheetName()) || "property".equals(sheetAt.getSheetName())) {
         for(int number = 0; number < wb.getNumberOfSheets(); ++number) {
            XSSFSheet sheetAt2 = wb.getSheetAt(number);
            String sheetName = sheetAt2.getSheetName();
            if (!"predefine".equals(sheetName) && !"property".equals(sheetName)) {
               sheetAt = sheetAt2;
               break;
            }
         }
      }

      return sheetAt;
   }

   public static Map parseVariables(List sheets) {
      HashMap variables = new HashMap();

      for(XSSFSheet xSSFSheet : (Iterable<XSSFSheet>)(Iterable<?>)(sheets)) {
         ArrayList items = new ArrayList();
         int lastRowNum = xSSFSheet.getLastRowNum();

         for(int number = 0; number <= lastRowNum; ++number) {
            XSSFRow row = xSSFSheet.getRow(number);
            PropertyItem propertyItem = new PropertyItem();
            items.add(propertyItem);
            XSSFCell cell = row.getCell(0);
            String text = getCellText(cell, true);
            propertyItem.setName(text);
            if (row.getLastCellNum() > 1) {
               cell = row.getCell(1);
               propertyItem.setLabel(getCellText(cell));
            }

            if (row.getLastCellNum() > 2) {
               cell = row.getCell(2);
               propertyItem.setDataType(parseFieldType(getCellText(cell)));
            }

            if (row.getLastCellNum() > 3) {
               cell = row.getCell(3);
               propertyItem.setAct(parseAct(getCellText(cell)));
            }
         }

         variables.put(xSSFSheet.getSheetName(), items);
      }

      return variables;
   }

   private static FieldType parseFieldType(String text) {
      if (StringUtils.isBlank(text)) {
         return FieldType.String;
      } else {
         String uppercaseText = text.trim().toUpperCase();
         if (FieldType.Integer.name().toUpperCase().equals(uppercaseText)) {
            return FieldType.Integer;
         } else if (FieldType.Double.name().toUpperCase().equals(uppercaseText)) {
            return FieldType.Double;
         } else if (FieldType.Long.name().toUpperCase().equals(uppercaseText)) {
            return FieldType.Long;
         } else if (FieldType.Float.name().toUpperCase().equals(uppercaseText)) {
            return FieldType.Float;
         } else if (FieldType.BigDecimal.name().toUpperCase().equals(uppercaseText)) {
            return FieldType.BigDecimal;
         } else if (FieldType.Short.name().toUpperCase().equals(uppercaseText)) {
            return FieldType.Short;
         } else if (FieldType.Boolean.name().toUpperCase().equals(uppercaseText)) {
            return FieldType.Boolean;
         } else {
            return FieldType.Date.name().toUpperCase().equals(uppercaseText) ? FieldType.Date : FieldType.String;
         }
      }
   }

   private static Act parseAct(String text) {
      if (StringUtils.isBlank(text)) {
         return Act.InOut;
      } else {
         String uppercaseText = text.trim().toUpperCase();
         if (Act.In.name().toUpperCase().equals(uppercaseText)) {
            return Act.In;
         } else {
            return Act.Out.name().toUpperCase().equals(uppercaseText) ? Act.Out : Act.InOut;
         }
      }
   }

   public static List parseParameters(List sheets) {
      ArrayList parameters = new ArrayList();
      ArrayList items = new ArrayList();

      for(XSSFSheet xSSFSheet : (Iterable<XSSFSheet>)(Iterable<?>)(sheets)) {
         int lastRowNum = xSSFSheet.getLastRowNum();

         for(int number = 0; number <= lastRowNum; ++number) {
            XSSFRow row = xSSFSheet.getRow(number);
            PropertyItem propertyItem = new PropertyItem();
            XSSFCell cell = row.getCell(0);
            String text = getCellText(cell, true);
            propertyItem.setName(text);
            if (!items.contains(text)) {
               parameters.add(propertyItem);
               items.add(text);
               if (row.getLastCellNum() > 1) {
                  cell = row.getCell(1);
                  propertyItem.setLabel(getCellText(cell));
               }

               if (row.getLastCellNum() > 2) {
                  cell = row.getCell(2);
                  propertyItem.setDataType(parseFieldType(getCellText(cell)));
               }

               if (row.getLastCellNum() > 3) {
                  cell = row.getCell(3);
                  propertyItem.setAct(parseAct(getCellText(cell)));
               }
            }
         }
      }

      return parameters;
   }

   private static String getCellText(XSSFCell cell) {
      return getCellText(cell, false);
   }

   private static String getCellText(XSSFCell xSSFCell, boolean integerOnly) {
      String text = "";
      if (CellType.NUMERIC == xSSFCell.getCellTypeEnum()) {
         if (integerOnly) {
            Double numericCellValue = xSSFCell.getNumericCellValue();
            text = numericCellValue.intValue() + "";
         } else {
            text = xSSFCell.getNumericCellValue() + "";
         }
      } else if (CellType.BOOLEAN == xSSFCell.getCellTypeEnum()) {
         text = xSSFCell.getBooleanCellValue() + "";
      } else if (CellType._NONE == xSSFCell.getCellTypeEnum()) {
         text = "";
      } else if (CellType.ERROR == xSSFCell.getCellTypeEnum()) {
         text = "ERROR";
      } else {
         text = xSSFCell.getStringCellValue();
      }

      return text;
   }

   public static List parsePredefines(XSSFWorkbook workbook) {
      ArrayList predefines = new ArrayList();
      XSSFSheet sheet = workbook.getSheet("predefine");
      if (sheet == null) {
         return predefines;
      } else {
         int lastRowNum = sheet.getLastRowNum();

         for(int number = 1; number <= lastRowNum; ++number) {
            PredefineRow predefineRow = new PredefineRow();
            XSSFRow row = sheet.getRow(number);
            predefineRow.setUuid(UUID.randomUUID().toString());
            predefineRow.setName(row.getCell(0).getStringCellValue());
            predefineRow.setFromOrIn(row.getCell(1).getStringCellValue());
            predefineRow.setType(row.getCell(2).getStringCellValue());
            predefineRow.setFromType(row.getCell(3).getStringCellValue());
            predefineRow.setFromCategory(row.getCell(4).getStringCellValue());
            predefineRow.setFromValue(row.getCell(5).getStringCellValue());
            predefineRow.setParams(row.getCell(6).getStringCellValue());
            predefineRow.setCondition(row.getCell(7).getStringCellValue());
            predefines.add(predefineRow);
         }

         return predefines;
      }
   }

   public static Integer getPredefineGroupPriority(Map properties) {
      Integer predefineGroupPriority = null;
      if (properties.containsKey("predefineGroupPriority")) {
         predefineGroupPriority = (Integer)properties.get("predefineGroupPriority");
      }

      return predefineGroupPriority;
   }

   public static ScoringType getScoringType(Map properties) {
      String text = null;
      if (properties.containsKey("scoringType")) {
         text = (String)properties.get("scoringType");
         return ScoringType.valueOf(text);
      } else {
         return ScoringType.sum;
      }
   }

   public static String getScoringBean(Map properties) {
      String scoringBean = null;
      if (properties.containsKey("scoringBean")) {
         scoringBean = (String)properties.get("scoringBean");
      }

      return scoringBean;
   }

   public static Map parseProperties(XSSFWorkbook workbook) {
      HashMap properties = new HashMap();
      XSSFSheet sheet = workbook.getSheet("property");
      if (sheet == null) {
         return properties;
      } else {
         int lastRowNum = sheet.getLastRowNum();

         for(int number = 1; number <= lastRowNum; ++number) {
            XSSFRow row = sheet.getRow(number);
            String stringCellValue = row.getCell(0).getStringCellValue();
            if (!StringUtils.isBlank(stringCellValue)) {
               stringCellValue = stringCellValue.trim().toLowerCase();
               if ("debug".equals(stringCellValue) || "允许调试信息输出".equals(stringCellValue)) {
                  Boolean booleanCellValue = row.getCell(1).getBooleanCellValue();
                  properties.put("debug", booleanCellValue);
               }

               if ("enable".equals(stringCellValue) || "是否启用".equals(stringCellValue)) {
                  Boolean booleanCellValue2 = row.getCell(1).getBooleanCellValue();
                  properties.put("enable", booleanCellValue2);
               }

               if ("salience".equals(stringCellValue) || "优先级".equals(stringCellValue)) {
                  try {
                     Double numericCellValue = row.getCell(1).getNumericCellValue();
                     Integer number2 = numericCellValue.intValue();
                     properties.put("salience", number2);
                  } catch (Exception exception) {
                  }
               }

               SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
               if ("effectiveDate".toLowerCase().equals(stringCellValue) || "生效时间".equals(stringCellValue)) {
                  try {
                     String stringCellValue2 = row.getCell(1).getStringCellValue();
                     Date dateValue = simpleDateFormat.parse(stringCellValue2);
                     properties.put("effectiveDate", dateValue);
                  } catch (Exception exception2) {
                  }
               }

               if ("expiresDate".toLowerCase().equals(stringCellValue) || "失效时间".equals(stringCellValue)) {
                  try {
                     String stringCellValue3 = row.getCell(1).getStringCellValue();
                     Date dateValue2 = simpleDateFormat.parse(stringCellValue3);
                     properties.put("expiresDate", dateValue2);
                  } catch (Exception exception3) {
                  }
               }

               if ("mutexGroup".toLowerCase().equals(stringCellValue) || "互斥组".equals(stringCellValue)) {
                  String stringCellValue4 = row.getCell(1).getStringCellValue();
                  properties.put("mutexGroup", stringCellValue4);
               }

               if ("remark".equals(stringCellValue) || "备注".equals(stringCellValue)) {
                  String stringCellValue5 = row.getCell(1).getStringCellValue();
                  properties.put("remark", stringCellValue5);
               }

               if ("name".equals(stringCellValue) || "名称".equals(stringCellValue)) {
                  String stringCellValue6 = row.getCell(1).getStringCellValue();
                  properties.put("name", stringCellValue6);
               }

               if ("predefineGroupPriority".toLowerCase().equals(stringCellValue) || "预定义值优先级".equals(stringCellValue)) {
                  Double numericCellValue2 = row.getCell(1).getNumericCellValue();
                  properties.put("predefineGroupPriority", numericCellValue2.intValue());
               }

               if ("assignTargetType".toLowerCase().equals(stringCellValue) || "赋值目标类型".equals(stringCellValue)) {
                  String stringCellValue7 = row.getCell(1).getStringCellValue();
                  if (ExcelSupport.isParameter(stringCellValue7)) {
                     stringCellValue7 = "parameter";
                  } else if ("变量".equals(stringCellValue7)) {
                     stringCellValue7 = "variable";
                  }

                  properties.put("assignTargetType", stringCellValue7);
               }

               if ("assignTargetCategory".toLowerCase().equals(stringCellValue) || "赋值对象类型".equals(stringCellValue)) {
                  String stringCellValue8 = row.getCell(1).getStringCellValue();
                  properties.put("assignTargetCategory", stringCellValue8);
               }

               if ("assignTargetVariable".toLowerCase().equals(stringCellValue) || "赋值属性类型".equals(stringCellValue)) {
                  String stringCellValue9 = row.getCell(1).getStringCellValue();
                  properties.put("assignTargetVariable", stringCellValue9);
               }

               if ("scoringType".toLowerCase().equals(stringCellValue) || "得分计算方式".equals(stringCellValue)) {
                  String stringCellValue10 = row.getCell(1).getStringCellValue();
                  properties.put("scoringType", stringCellValue10);
               }

               if ("scoringBean".toLowerCase().equals(stringCellValue) || "自定义计算得分的Bean ID".toLowerCase().equals(stringCellValue)) {
                  String stringCellValue11 = row.getCell(1).getStringCellValue();
                  properties.put("scoringBean", stringCellValue11);
               }
            }
         }

         return properties;
      }
   }

   public static void builderProperties(ExcelSupport excelSupport, StringBuilder sb, Map properties, boolean isCrossTable) {
      Boolean debug = false;
      if (properties.containsKey("debug")) {
         debug = (Boolean)properties.get("debug");
      }

      Boolean enabled = false;
      if (properties.containsKey("enable")) {
         enabled = (Boolean)properties.get("enable");
      }

      Integer salience = null;
      if (properties.containsKey("salience")) {
         salience = (Integer)properties.get("salience");
      }

      Date effectiveDate = null;
      if (properties.containsKey("effectiveDate")) {
         effectiveDate = (Date)properties.get("effectiveDate");
      }

      Date expiresDate = null;
      if (properties.containsKey("expiresDate")) {
         expiresDate = (Date)properties.get("expiresDate");
      }

      String mutexGroup = null;
      if (properties.containsKey("mutexGroup")) {
         mutexGroup = (String)properties.get("mutexGroup");
      }

      appendRuleAttributes(sb, debug, enabled, salience, effectiveDate, expiresDate, mutexGroup);
      if (isCrossTable) {
         String assignTargetType = null;
         if (properties.containsKey("assignTargetType")) {
            assignTargetType = (String)properties.get("assignTargetType");
         }

         String assignTargetCategory = null;
         if (properties.containsKey("assignTargetCategory")) {
            assignTargetCategory = (String)properties.get("assignTargetCategory");
         }

         String assignTargetVariable = null;
         if (properties.containsKey("assignTargetVariable")) {
            assignTargetVariable = (String)properties.get("assignTargetVariable");
         }

         appendAssignmentTarget(excelSupport, sb, assignTargetType, assignTargetCategory, assignTargetVariable);
      }

   }

   private static void appendRuleAttributes(StringBuilder stringBuilder, boolean debug, boolean enabled, Integer salience, Date effectiveDate, Date expiresDate, String mutexGroup) {
      if (debug) {
         stringBuilder.append(" debug=\"" + debug + "\"");
      }

      if (!enabled) {
         stringBuilder.append(" enabled=\"" + enabled + "\"");
      }

      if (null != salience && salience != 10) {
         stringBuilder.append(" salience=\"" + salience + "\"");
      }

      SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      if (null != effectiveDate) {
         stringBuilder.append(" effective-date=\"" + simpleDateFormat.format(effectiveDate) + "\"");
      }

      if (null != expiresDate) {
         stringBuilder.append(" expires-date=\"" + simpleDateFormat.format(expiresDate) + "\"");
      }

      if (StringUtils.isNotBlank(mutexGroup)) {
         stringBuilder.append(" mutex-group=\"" + mutexGroup + "\"");
      }

   }

   private static void appendAssignmentTarget(ExcelSupport excelSupport, StringBuilder stringBuilder, String targetType, String targetCategory, String targetVariable) {
      StringBuilder attributes = new StringBuilder("");
      if ("variable".equals(targetType)) {
         VariableCategory variableCategory = excelSupport.findVariableCategory(targetCategory, targetVariable);
         if (null == variableCategory) {
            return;
         }

         Variable variable = (Variable)variableCategory.getVariableLabels().get(targetVariable);
         attributes.append(" assign-target-type=\"variable\" category-uuid=\"" + variableCategory.getUuid() + "\" var-category=\"" + variableCategory.getName() + "\" var=\"" + variable.getName() + "\" var-label=\"" + variable.getLabel() + "\" datatype=\"" + variable.getType() + "\" uuid=\"" + variable.getUuid() + "\"");
      } else {
         if (!"parameter".equals(targetType)) {
            return;
         }

         attributes.append(" assign-target-type=\"parameter\" category-uuid=\"参数\" ");
         if (StringUtils.isBlank(targetCategory)) {
            Variable simpleParameterByLabel = excelSupport.findSimpleParameterByLabel(targetVariable);
            if (simpleParameterByLabel == null) {
               return;
            }

            attributes.append(" uuid=\"" + simpleParameterByLabel.getUuid() + "\" var-category=\"" + "参数" + "\" var=\"" + simpleParameterByLabel.getName() + "\" var-label=\"" + simpleParameterByLabel.getLabel() + "\" datatype=\"" + simpleParameterByLabel.getType() + "\"");
         } else {
            Variable parameterByLabel = excelSupport.findParameterByLabel(targetCategory, targetVariable);
            if (parameterByLabel == null) {
               return;
            }

            Variable targetProperty = null;
            VariableCategory variableCategoryByUUID = null;
            variableCategoryByUUID = excelSupport.findVariableCategoryByUUID(parameterByLabel.getDataType());
            targetProperty = (Variable)variableCategoryByUUID.getVariableLabels().get(targetVariable);
            if (null == targetProperty) {
               return;
            }

            attributes.append(" uuid=\"" + parameterByLabel.getUuid() + "\" var-category=\"" + "参数" + "\" var=\"" + targetProperty.getName() + "\" var-label=\"" + targetProperty.getLabel() + "\" datatype=\"" + targetProperty.getType() + "\"  key-category-uuid=\"" + variableCategoryByUUID.getUuid() + "\" key-uuid=\"" + targetProperty.getUuid() + "\" key-label=\"" + parameterByLabel.getLabel() + "\" key-name=\"" + parameterByLabel.getName() + "\"");
         }
      }

      stringBuilder.append(attributes.toString());
   }

   public static void builderRemark(StringBuilder sb, Map properties) {
      if (properties.containsKey("remark")) {
         String text = (String)properties.get("remark");
         sb.append("<remark><![CDATA[" + text + "]]></remark>");
      }

   }

   public static void builderPredefineXml(StringBuilder sb, Integer predefineGroupPriority, List predefineRows, Map predefineNameMap, ExcelSupport excelSupport, Map parameterVarMap) {
      if (predefineRows.size() != 0) {
         for(PredefineRow predefineRow : (Iterable<PredefineRow>)(Iterable<?>)(predefineRows)) {
            if ("Predefine".equals(predefineRow.getFromType()) && Datatype.isType(predefineRow.getType()) && StringUtils.isNotBlank(predefineRow.getFromCategory()) && StringUtils.isNotBlank(predefineRow.getFromValue())) {
               String[] text = new String[2];
               PredefineRow predefineRow2 = (PredefineRow)predefineNameMap.get(predefineRow.getFromCategory());
               if (predefineRow2 != null && ExcelSupport.isParameter(predefineRow2.getFromType())) {
                  text[0] = predefineRow2.getType();
                  text[1] = predefineRow.getFromValue();
                  VariableCategory variableCategory = excelSupport.findVariableCategory(text);
                  if (variableCategory != null) {
                     parameterVarMap.put(predefineRow2.getType(), variableCategory);
                  }
               }
            }
         }

         if (predefineGroupPriority == null) {
            predefineGroupPriority = 1;
         }

         sb.append("<predefine-group priority=\"" + predefineGroupPriority + "\">");

         for(PredefineRow predefineRow3 : (Iterable<PredefineRow>)(Iterable<?>)(predefineRows)) {
            String fromType = predefineRow3.getFromType();
            sb.append("<predefine uuid=\"" + predefineRow3.getUuid() + "\" name=\"" + predefineRow3.getName() + "\" ");
            if (StringUtils.isNotBlank(predefineRow3.getCondition())) {
            }

            if ("Variable".equals(fromType)) {
               String[] values = new String[]{predefineRow3.getFromCategory(), predefineRow3.getFromValue()};
               Variable variable = excelSupport.findVariable(values);
               VariableCategory variableCategory2 = excelSupport.findVariableCategory(values);
               VariableCategory variableCategory3 = excelSupport.findVariableCategory(predefineRow3.getType());
               if (variableCategory3 == null) {
                  sb.append(" type=\"" + predefineRow3.getType() + "\"");
               } else {
                  sb.append(" type=\"" + variableCategory3.getUuid() + "\"");
               }

               sb.append(" value-type=\"" + predefineRow3.getFromOrIn() + "\">");
               sb.append("<value category-uuid=\"" + variableCategory2.getUuid() + "\" var-category=\"" + variableCategory2.getName() + "\" var=\"" + variable.getName() + "\" var-label=\"" + predefineRow3.getFromValue() + "\" datatype=\"" + variable.getDataType() + "\" uuid=\"" + variable.getUuid() + "\" type=\"Variable\"/>");
            } else if ("VariableCategory".equals(fromType)) {
               VariableCategory variableCategory4 = excelSupport.findVariableCategory(predefineRow3.getType());
               if (variableCategory4 != null) {
                  sb.append(" type=\"" + variableCategory4.getUuid() + "\" value-type=\"" + predefineRow3.getFromOrIn() + "\">");
                  sb.append("<value category-uuid=\"" + variableCategory4.getUuid() + "\" var-category=\"" + variableCategory4.getName() + "\" type=\"VariableCategory\"/>");
               } else {
                  sb.append(" type=\"" + predefineRow3.getType() + "\" value-type=\"" + predefineRow3.getFromOrIn() + "\">");
               }
            } else if ("Constant".equals(fromType)) {
               String[] values2 = new String[]{predefineRow3.getFromCategory(), predefineRow3.getFromValue()};
               Constant constant = excelSupport.findConstant(values2, false);
               ConstantCategory constantCategory = excelSupport.findConstantCategory(values2);
               sb.append(" type=\"" + predefineRow3.getType() + "\" value-type=\"" + predefineRow3.getFromOrIn() + "\">");
               sb.append("<value category-uuid=\"" + constantCategory.getUuid() + "\" uuid=\"" + constant.getUuid() + "\" const-category=\"" + constantCategory.getLabel() + "\" const=\"" + constant.getName() + "\" const-label=\"" + constant.getLabel() + "\" data-type=\"" + constant.getType() + "\" type=\"Constant\"/>");
            } else if (!"Method".equals(fromType)) {
               if ("CommonFunction".equals(fromType)) {
                  sb.append(" type=\"" + predefineRow3.getType() + "\" value-type=\"" + predefineRow3.getFromOrIn() + "\">");
                  FunctionDescriptor function = excelSupport.getFunction(predefineRow3.getFromValue());
                  if (function == null) {
                     sb.append("<value content=\"函数:" + predefineRow3.getFromValue() + "\" type=\"Input\">");
                  } else {
                     sb.append("<value function-label=\"" + function.getLabel() + "\" function-name=\"" + function.getName() + "\" type=\"CommonFunction\">");
                     String params = predefineRow3.getParams();
                     if (StringUtils.isNotBlank(params)) {
                        String[] parts = params.split("\\.");
                        if (parts.length < 2) {
                           sb.append("<function-parameter name=\"对象\" property-name=\"" + params + "\" property-label=\"" + params + "\">");
                           sb.append("<value content=\"" + params + "\" type=\"Input\"></value>");
                           sb.append("</function-parameter>");
                        } else {
                           String[] values3 = new String[]{parts[0], parts[1]};
                           VariableCategory variableCategory5 = excelSupport.findVariableCategory(values3);
                           sb.append("<function-parameter name=\"对象\" property-name=\"" + parts[0] + "\" property-label=\"" + parts[1] + "\">");
                           sb.append("<value category-uuid=\"" + variableCategory5.getUuid() + "\" var-category=\"" + params + "\" type=\"VariableCategory\"></value>");
                           sb.append("</function-parameter>");
                        }
                     }
                  }

                  sb.append("</value>");
               } else if ("Parameter".equals(fromType)) {
                  String[] values4 = new String[]{"参数", StringUtils.isBlank(predefineRow3.getFromCategory()) ? predefineRow3.getFromValue() : predefineRow3.getFromCategory()};
                  Variable variable2 = excelSupport.findVariable(values4);
                  if (Datatype.isType(predefineRow3.getType())) {
                     sb.append(" type=\"" + predefineRow3.getType() + "\" value-type=\"" + predefineRow3.getFromOrIn() + "\">");
                  } else if (parameterVarMap.containsKey(predefineRow3.getType())) {
                     sb.append(" type=\"" + ((VariableCategory)parameterVarMap.get(predefineRow3.getType())).getUuid() + "\" value-type=\"" + predefineRow3.getFromOrIn() + "\">");
                  } else {
                     VariableCategory variableCategory6 = excelSupport.findVariableCategory(predefineRow3.getType());
                     if (variableCategory6 != null) {
                        sb.append(" type=\"" + variableCategory6.getUuid() + "\" value-type=\"" + predefineRow3.getFromOrIn() + "\">");
                     } else {
                        sb.append(" type=\"" + predefineRow3.getType() + "\" value-type=\"" + predefineRow3.getFromOrIn() + "\">");
                     }
                  }

                  String text2 = "<value category-uuid=\"参数\" uuid=\"" + variable2.getUuid() + "\" var-category=\"" + "参数" + "\" var=\"" + variable2.getName() + "\" var-label=\"" + variable2.getLabel() + "\" datatype=\"" + variable2.getType() + "\" type=\"Parameter\"/>";
                  if (Datatype.Object.name().equals(variable2.getType().name()) && StringUtils.isNotBlank(predefineRow3.getFromValue())) {
                     VariableCategory variableCategoryByUUID = excelSupport.findVariableCategoryByUUID(variable2.getDataType());
                     if (variableCategoryByUUID != null) {
                        Variable variable3 = (Variable)variableCategoryByUUID.getVariableLabels().get(predefineRow3.getFromValue());
                        if (variable3 != null) {
                           sb.append("<value category-uuid=\"参数\" uuid=\"" + variable2.getUuid() + "\" var-category=\"" + "参数" + "\" var=\"" + variable2.getName() + "\" var-label=\"" + variable2.getLabel() + "\" datatype=\"" + variable2.getType() + "\" key-category-uuid=\"" + variableCategoryByUUID.getUuid() + "\" key-uuid=\"" + variable3.getUuid() + "\" key-label=\"" + variable3.getLabel() + "\" key-name=\"" + variable3.getName() + "\" type=\"Parameter\"/>");
                        }
                     } else {
                        sb.append(text2);
                     }
                  } else {
                     sb.append(text2);
                  }
               } else if ("Predefine".equals(fromType)) {
                  PredefineRow predefineRow4 = (PredefineRow)predefineNameMap.get(predefineRow3.getFromCategory());
                  VariableCategory variableCategory7 = excelSupport.findVariableCategory(predefineRow3.getType());
                  if (StringUtils.isEmpty(predefineRow3.getFromValue())) {
                     if (variableCategory7 != null) {
                        sb.append(" type=\"" + variableCategory7.getUuid() + "\" value-type=\"" + predefineRow3.getFromOrIn() + "\">");
                        sb.append("<value uuid=\"" + predefineRow4.getUuid() + "\" type=\"Predefine\"/>");
                     } else {
                        sb.append(" type=\"" + predefineRow3.getType() + "\" value-type=\"" + predefineRow3.getFromOrIn() + "\">");
                        sb.append("<value uuid=\"" + predefineRow4.getUuid() + "\" type=\"Predefine\"/>");
                     }
                  } else {
                     String[] values5 = new String[]{predefineRow4.getType(), predefineRow3.getFromValue()};
                     Variable variable4 = excelSupport.findVariable(values5);
                     if (variableCategory7 != null) {
                        sb.append(" type=\"" + variableCategory7.getUuid() + "\" value-type=\"" + predefineRow3.getFromOrIn() + "\">");
                     } else {
                        sb.append(" type=\"" + predefineRow3.getType() + "\" value-type=\"" + predefineRow3.getFromOrIn() + "\">");
                     }

                     sb.append("<value uuid=\"" + predefineRow4.getUuid() + "\" property-uuid=\"" + variable4.getUuid() + "\" type=\"Predefine\"/>");
                  }
               } else if ("Input".equals(fromType)) {
                  sb.append(" type=\"" + predefineRow3.getType() + "\" value-type=\"" + predefineRow3.getFromOrIn() + "\">");
                  sb.append("<value content=\"" + predefineRow3.getFromValue() + "\" type=\"Input\"/>");
               }
            } else {
               sb.append(" type=\"" + predefineRow3.getType() + "\" value-type=\"" + predefineRow3.getFromOrIn() + "\">");
               SpringBean action = excelSupport.getAction(predefineRow3.getFromCategory(), predefineRow3.getFromValue());
               Method actionMethod = excelSupport.getActionMethod(predefineRow3.getFromCategory(), predefineRow3.getFromValue());
               if (action != null && actionMethod != null) {
                  sb.append("<value bean-name=\"" + action.getId() + "\" bean-label=\"" + action.getName() + "\" method-name=\"" + actionMethod.getMethodName() + "\" method-label=\"" + actionMethod.getName() + "\" type=\"Method\">");
                  String params2 = predefineRow3.getParams();
                  if (StringUtils.isNotBlank(params2)) {
                     String[] parts2 = params2.split(",");
                     int number = 0;

                     for(String text3 : parts2) {
                        Parameter parameter = (Parameter)actionMethod.getParameters().get(number);
                        sb.append("<parameter name=\"" + parameter.getName() + "\" type=\"" + parameter.getType().name() + "\">");
                        sb.append("<value content=\"" + text3 + "\" type=\"Input\"></value>");
                        sb.append("</parameter>");
                        ++number;
                     }
                  }
               } else {
                  sb.append("<value content=\"" + predefineRow3.getFromCategory() + "." + predefineRow3.getFromValue() + "\" type=\"Input\">");
               }

               sb.append("</value>");
            }

            sb.append("</predefine>");
         }

         sb.append("</predefine-group>");
      }
   }

   public static void builderLibraryXml(StringBuilder sb, ExcelSupport excelSupport) {
      for(VariableInfo variableInfo : (Iterable<VariableInfo>)(Iterable<?>)(excelSupport.getVarLibraries().values())) {
         if (variableInfo.getType().endsWith(ResourceType.VariableLibrary.name())) {
            sb.append("<import-variable-library id=\"" + variableInfo.getId() + "\" path=\"" + variableInfo.getPath() + "\"/>");
         } else {
            sb.append("<import-parameter-library id=\"" + variableInfo.getId() + "\" path=\"" + variableInfo.getPath() + "\"/>");
         }
      }

      for(ConstantInfo constantInfo : (Iterable<ConstantInfo>)(Iterable<?>)(excelSupport.getContLibraries().values())) {
         sb.append("<import-constant-library id=\"" + constantInfo.getId() + "\" path=\"" + constantInfo.getPath() + "\"/>");
      }

   }

   public static String buildParameterXml(Variable variable) {
      return " var-category=\"参数\" var-label=\"" + variable.getLabel() + "\" var=\"" + variable.getName() + "\" datatype=\"" + variable.getType().name() + "\" category-uuid=\"" + "参数" + "\" uuid=\"" + variable.getUuid() + "\"";
   }

   public static String buildParameterXml(Variable parameter, VariableCategory category, Variable variable) {
      return " var-category=\"参数\" var-label=\"" + variable.getLabel() + "\" var=\"" + variable.getName() + "\" datatype=\"" + variable.getType().name() + "\" category-uuid=\"" + "参数" + "\" uuid=\"" + parameter.getUuid() + "\" key-uuid=\"" + variable.getUuid() + "\" key-category-uuid=\"" + category.getUuid() + "\" key-label=\"" + parameter.getLabel() + "\" key-name=\"" + parameter.getName() + "\"";
   }

   public static String buildVariableXml(VariableCategory category, Variable variable) {
      return " var-category=\"" + category.getName() + "\" var-label=\"" + variable.getLabel() + "\" var=\"" + variable.getName() + "\" datatype=\"" + variable.getType().name() + "\" category-uuid=\"" + category.getUuid() + "\" uuid=\"" + variable.getUuid() + "\"";
   }

   public static String buildParameterValueXml(ParameterValue parameter, Variable variable) {
      return "<value category-uuid=\"参数\" uuid=\"" + variable.getUuid() + "\" var-category=\"" + "参数" + "\" var=\"" + parameter.getVariableName() + "\" var-label=\"" + variable.getLabel() + "\" datatype=\"" + variable.getType() + "\" type=\"Parameter\"/>>";
   }

   public static String buildParameterValueXml(ParameterValue parameter, VariableCategory category, Variable variable) {
      return "<value category-uuid=\"参数\" uuid=\"" + parameter.getUuid() + "\" var-category=\"" + "参数" + "\" var=\"" + parameter.getVariableName() + "\" var-label=\"" + parameter.getVariableLabel() + "\" datatype=\"" + variable.getType() + "\" key-category-uuid=\"" + category.getUuid() + "\" key-uuid=\"" + variable.getUuid() + "\" key-label=\"" + variable.getName() + "\" key-name=\"" + variable.getLabel() + "\" type=\"Parameter\"/>>";
   }

   public static String buildPredefineXml(String categoryUUid, String variableUuid) {
      return " predefine=\"true\" uuid=\"" + categoryUUid + "\" property-uuid=\"" + variableUuid + "\"";
   }

   public static String buildContentXml(String data) {
      return buildContentXml(data, false);
   }

   public static String buildContentXml(String data, boolean withEndFlag) {
      if (StringUtils.isBlank(data)) {
         data = "";
      }

      String text = "<value content=\"" + data + "\" type=\"Input\"";
      if (withEndFlag) {
         text = text + "/";
      }

      return text + ">";
   }
}
