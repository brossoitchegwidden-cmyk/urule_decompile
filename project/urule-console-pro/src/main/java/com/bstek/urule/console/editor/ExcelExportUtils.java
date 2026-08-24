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

/** Serializes URule definitions and display labels into Excel workbooks. */
public class ExcelExportUtils {
   public static String conditions2Label(ExcelSupport excelSupport, List conditions, JointType jointType) {
      String label = "";

      for(Condition condition : (Iterable<Condition>)(Iterable<?>)(conditions)) {
         if (condition.getValue() != null) {
            if (jointType == JointType.and) {
               if (StringUtils.isNotBlank(label)) {
                  label = label + " 并且 \n";
               }
            } else if (StringUtils.isNotBlank(label)) {
               label = label + " 或则 \n ";
            }

            label = label + getLabelValue(excelSupport, condition, condition.getValue());
         } else {
            label = label + condition.getOp().toString();
         }
      }

      return label;
   }

   public static String criterions2Label(ExcelSupport excelSupport, List criterions, String junctionType) {
      String label = "";

      for(Criterion criterion : (Iterable<Criterion>)(Iterable<?>)(criterions)) {
         if (junctionType.equals(JunctionType.and.name())) {
            if (StringUtils.isNotBlank(label)) {
               label = label + " 并且 ";
            }
         } else if (StringUtils.isNotBlank(label)) {
            label = label + " 或则 ";
         }

         if (criterion instanceof Junction) {
            Junction junction = (Junction)criterion;
            label = label + "(" + criterions2Label(excelSupport, junction.getCriterions(), junction.getJunctionType()) + ")";
         } else if (criterion instanceof Criteria) {
            Criteria criteria = (Criteria)criterion;
            LeftType type = criteria.getLeft().getType();
            LeftPart leftPart = criteria.getLeft().getLeftPart();
            if (type != LeftType.variable && type != LeftType.parameter) {
               if (type == LeftType.commonfunction) {
                  CommonFunctionLeftPart commonFunctionLeftPart = (CommonFunctionLeftPart)leftPart;
                  label = label + commonFunctionLeftPart.getLabel() + "(" + getLabelValue(excelSupport, (Condition)null, commonFunctionLeftPart.getParameter().getObjectParameter()) + ")";
               } else if (type == LeftType.function) {
                  FunctionLeftPart functionLeftPart = (FunctionLeftPart)leftPart;
                  label = label + functionLeftPart.getName() + "(" + getParameterValues(excelSupport, functionLeftPart.getParameters()) + ")";
               } else if (type == LeftType.method) {
                  MethodLeftPart methodLeftPart = (MethodLeftPart)leftPart;
                  label = label + methodLeftPart.getBeanLabel() + "." + methodLeftPart.getMethodLabel() + "(" + getParameterValues(excelSupport, methodLeftPart.getParameters()) + ")";
               } else if (type == LeftType.predefine) {
                  PredefineLeftPart predefineLeftPart = (PredefineLeftPart)leftPart;
                  label = label + predefineLeftPart.getName();
               }
            } else {
               VariableLeftPart variableLeftPart = (VariableLeftPart)leftPart;
               label = label + variableLeftPart.getVariableCategory() + "." + variableLeftPart.getVariableLabel();
            }

            label = label + " " + criteria.getOp().name();
            label = label + " " + getLabelValue(excelSupport, (Condition)null, criteria.getValue());
         }
      }

      return label;
   }

   public static String getLabelValue(ExcelSupport excelSupport, Condition condition, Value value) {
      String labelValue = "";
      if (value instanceof VariableCategoryValue) {
         labelValue = formatSimpleValue(excelSupport, value);
      } else if (value instanceof ConstantValue) {
         labelValue = formatSimpleValue(excelSupport, value);
      } else if (value instanceof SimpleValue) {
         labelValue = formatSimpleValue(excelSupport, value);
      } else if (value instanceof VariableValue) {
         labelValue = formatSimpleValue(excelSupport, value);
      } else if (value instanceof ParameterValue) {
         labelValue = formatSimpleValue(excelSupport, value);
      } else if (value instanceof PredefineValue) {
         labelValue = formatSimpleValue(excelSupport, value);
      } else if (value instanceof MethodValue) {
         labelValue = formatSimpleValue(excelSupport, value);
      } else if (value instanceof CommonFunctionValue) {
         labelValue = formatSimpleValue(excelSupport, value);
      } else if (value instanceof ParenValue) {
         ParenValue parenValue = (ParenValue)value;
         labelValue = "(" + getLabelValue(excelSupport, (Condition)null, parenValue.getValue()) + ")";
      } else if (value instanceof ComplexArithmetic) {
         ComplexArithmetic complexArithmetic = (ComplexArithmetic)value;
         labelValue = getLabelValue(excelSupport, (Condition)null, complexArithmetic.getValue());
      } else {
         labelValue = "";
      }

      if (value instanceof AbstractValue && value.getArithmetic() != null) {
         labelValue = labelValue + " " + value.getArithmetic().getType() + " " + getLabelValue(excelSupport, (Condition)null, value.getArithmetic().getValue()) + " ";
      }

      if (condition != null) {
         labelValue = condition.getOp().toString() + " " + labelValue;
      }

      return labelValue;
   }

   public static String getParameterValues(ExcelSupport excelSupport, CommonFunctionValue value) {
      return getLabelValue(excelSupport, (Condition)null, value.getParameter().getObjectParameter()) + "." + value.getParameter().getPropertyLabel();
   }

   public static String getParameterValues(ExcelSupport excelSupport, List parameters) {
      String parameterValues = "";

      for(Parameter parameter : (Iterable<Parameter>)(Iterable<?>)(parameters)) {
         if (StringUtils.isNotBlank(parameterValues)) {
            parameterValues = parameterValues + ",";
         }

         parameterValues = parameterValues + getLabelValue(excelSupport, (Condition)null, parameter.getValue());
      }

      return parameterValues;
   }

   private static String formatSimpleValue(ExcelSupport excelSupport, Value value) {
      if (value instanceof VariableCategoryValue) {
         VariableCategoryValue variableCategoryValue = (VariableCategoryValue)value;
         return variableCategoryValue.getVariableCategory();
      } else if (value instanceof SimpleValue) {
         SimpleValue simpleValue = (SimpleValue)value;
         return simpleValue.getContent();
      } else if (value instanceof VariableValue) {
         VariableValue variableValue = (VariableValue)value;
         return variableValue.getVariableCategory() + "." + variableValue.getVariableLabel();
      } else if (value instanceof PredefineValue) {
         PredefineValue predefineValue = (PredefineValue)value;
         return !StringUtils.isBlank(predefineValue.getVariableCategory()) && !StringUtils.isBlank(predefineValue.getPropertyLabel()) ? predefineValue.getName() + "." + predefineValue.getPropertyLabel() : predefineValue.getName();
      } else if (value instanceof ParameterValue) {
         ParameterValue parameterValue = (ParameterValue)value;
         if (StringUtils.isNotBlank(parameterValue.getKeyCategoryUuid())) {
            Variable variable = null;
            VariableCategory variableCategoryByUUID = excelSupport.findVariableCategoryByUUID(parameterValue.getKeyCategoryUuid());
            if (variableCategoryByUUID != null) {
               variable = (Variable)variableCategoryByUUID.getVariableNames().get(parameterValue.getVariableName());
            }

            if (variable != null) {
               return "参数." + parameterValue.getKeyLabel() + "." + variable.getLabel();
            }
         }

         return "参数." + parameterValue.getVariableLabel();
      } else if (value instanceof ConstantValue) {
         ConstantValue constantValue = (ConstantValue)value;
         return constantValue.getConstantCategory() + "." + constantValue.getConstantLabel();
      } else if (value instanceof MethodValue) {
         MethodValue methodValue = (MethodValue)value;
         String text = "(" + getParameterValues(excelSupport, methodValue.getParameters()) + ")";
         return methodValue.getBeanLabel() + "." + methodValue.getMethodLabel() + text;
      } else if (value instanceof CommonFunctionValue) {
         CommonFunctionValue commonFunctionValue = (CommonFunctionValue)value;
         String text2 = "(" + getLabelValue(excelSupport, (Condition)null, commonFunctionValue.getParameter().getObjectParameter()) + ")";
         return "函数." + commonFunctionValue.getLabel() + text2;
      } else {
         return "";
      }
   }

   public static void exportProperties(SXSSFWorkbook wb, CrosstabDefinition dt) {
      Integer number = Objects.isNull(dt.getPredefineGroup()) ? null : dt.getPredefineGroup().getPriority();
      String assignTargetType = dt.getAssignTargetType();
      String assignVariableCategory = dt.getAssignVariableCategory();
      String assignVariableLabel = dt.getAssignVariableLabel();
      if (StringUtils.isNotBlank(assignTargetType)) {
         if ("variable".equals(assignTargetType)) {
            assignTargetType = "变量";
         } else if ("parameter".equals(assignTargetType)) {
            assignTargetType = "参数";
            assignVariableCategory = dt.getKeyLabel();
         }
      }

      createPropertySheet(wb, (String)null, number, dt.getDebug(), dt.getEnabled(), dt.getSalience(), dt.getEffectiveDate(), dt.getExpiresDate(), (String)null, dt.getRemark(), assignTargetType, assignVariableCategory, assignVariableLabel);
   }

   public static void exportProperties(SXSSFWorkbook wb, DecisionTable dt) {
      Integer number = Objects.isNull(dt.getPredefineGroup()) ? null : dt.getPredefineGroup().getPriority();
      createPropertySheet(wb, (String)null, number, dt.getDebug(), dt.getEnabled(), dt.getSalience(), dt.getEffectiveDate(), dt.getExpiresDate(), dt.getMutexGroup(), dt.getRemark(), (String)null, (String)null, (String)null);
   }

   public static void exportProperties(SXSSFWorkbook wb, ScorecardDefinition sd) {
      String name = sd.getName();
      String text = sd.getAssignTargetType().name();
      String variableCategory = sd.getVariableCategory();
      String variableLabel = sd.getVariableLabel();
      if (StringUtils.isNotBlank(text)) {
         if ("variable".equals(text)) {
            text = "变量";
         } else if ("parameter".equals(text)) {
            text = "参数";
            variableCategory = sd.getKeyLabel();
         }
      }

      int nextRow = createPropertySheet(wb, name, (Integer)null, sd.getDebug(), sd.getEnabled(), sd.getSalience(), sd.getEffectiveDate(), sd.getExpiresDate(), "", sd.getRemark(), text, variableCategory, variableLabel);
      appendScoringProperties(wb, sd.getScoringType(), nextRow, sd.getScoringBean());
   }

   public static void exportProperties(SXSSFWorkbook wb, ComplexScorecardDefinition sd) {
      String text = sd.getAssignTargetType().name();
      String variableCategory = sd.getVariableCategory();
      String variableLabel = sd.getVariableLabel();
      if (StringUtils.isNotBlank(text)) {
         if ("variable".equals(text)) {
            text = "变量";
         } else if ("parameter".equals(text)) {
            text = "参数";
            variableCategory = sd.getKeyLabel();
         }
      }

      int nextRow = createPropertySheet(wb, (String)null, (Integer)null, sd.getDebug(), sd.getEnabled(), sd.getSalience(), sd.getEffectiveDate(), sd.getExpiresDate(), "", sd.getRemark(), text, variableCategory, variableLabel);
      appendScoringProperties(wb, sd.getScoringType(), nextRow, sd.getScoringBean());
   }

   private static void appendScoringProperties(SXSSFWorkbook workbook, ScoringType scoringType, int rowIndex, String scoringBean) {
      SXSSFSheet sheet = workbook.getSheet("property");
      if (!Objects.isNull(scoringType)) {
         writePropertyRow(sheet, rowIndex, "得分计算方式", scoringType.name());
         ++rowIndex;
      }

      if (StringUtils.isNotBlank(scoringBean)) {
         writePropertyRow(sheet, rowIndex, "自定义计算得分的Bean ID", scoringBean);
         ++rowIndex;
      }

   }

   private static int createPropertySheet(SXSSFWorkbook workbook, String name, Integer predefinePriority, Boolean debug, Boolean enabled, Integer salience, Date effectiveDate, Date expiresDate, String mutexGroup, String remark, String assignTargetType, String assignCategory, String assignVariable) {
      SXSSFSheet sheet = workbook.createSheet("property");
      CellStyle cellStyle = workbook.createCellStyle();
      cellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
      cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
      cellStyle.setBorderBottom(BorderStyle.THIN);
      cellStyle.setBorderLeft(BorderStyle.THIN);
      cellStyle.setBorderRight(BorderStyle.THIN);
      cellStyle.setBorderTop(BorderStyle.THIN);
      SXSSFRow row = sheet.createRow(0);
      Double doubleValue = 7314.285714285715;
      int number = Math.min(doubleValue.intValue(), 65280);
      Cell cell = row.createCell(0);
      cell.setCellStyle(cellStyle);
      sheet.setColumnWidth(0, number);
      cell.setCellValue("属性");
      cell = row.createCell(1);
      sheet.setColumnWidth(1, number);
      cell.setCellStyle(cellStyle);
      cell.setCellValue("值");
      writePropertyRow(sheet, 1, "允许调试信息输出", debug == null ? false : debug);
      writePropertyRow(sheet, 2, "是否启用", enabled == null ? true : enabled);
      SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      int nextRow = 3;
      if (null != salience) {
         writePropertyRow(sheet, nextRow, "优先级", salience);
         ++nextRow;
      }

      if (null != effectiveDate) {
         writePropertyRow(sheet, nextRow, "生效时间", simpleDateFormat.format(effectiveDate));
         ++nextRow;
      }

      if (null != expiresDate) {
         writePropertyRow(sheet, nextRow, "失效时间", simpleDateFormat.format(expiresDate));
         ++nextRow;
      }

      if (StringUtils.isNotBlank(mutexGroup)) {
         writePropertyRow(sheet, nextRow, "互斥组", mutexGroup);
         ++nextRow;
      }

      if (StringUtils.isNotBlank(remark)) {
         writePropertyRow(sheet, nextRow, "备注", remark);
         ++nextRow;
      }

      if (StringUtils.isNotBlank(assignTargetType)) {
         writePropertyRow(sheet, nextRow, "赋值目标类型", assignTargetType);
         ++nextRow;
         writePropertyRow(sheet, nextRow, "赋值对象类型", assignCategory);
         ++nextRow;
         writePropertyRow(sheet, nextRow, "赋值属性类型", assignVariable);
         ++nextRow;
      }

      if (StringUtils.isNotBlank(name)) {
         writePropertyRow(sheet, nextRow, "名称", name);
         ++nextRow;
      }

      if (!Objects.isNull(predefinePriority)) {
         writePropertyRow(sheet, nextRow, "预定义值优先级", predefinePriority);
         ++nextRow;
      }

      return nextRow;
   }

   private static void writePropertyRow(SXSSFSheet sheet, int rowIndex, String propertyName, Object propertyValue) {
      CellStyle cellStyle = sheet.getWorkbook().createCellStyle();
      cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
      cellStyle.setBorderBottom(BorderStyle.THIN);
      cellStyle.setBorderLeft(BorderStyle.THIN);
      cellStyle.setBorderRight(BorderStyle.THIN);
      cellStyle.setBorderTop(BorderStyle.THIN);
      cellStyle.setWrapText(true);
      SXSSFRow row = sheet.createRow(rowIndex);
      Cell cell = row.createCell(0);
      cell.setCellStyle(cellStyle);
      cell.setCellValue(propertyName);
      cell = row.createCell(1);
      cell.setCellStyle(cellStyle);
      if (propertyValue == null) {
         cell.setCellValue("");
      } else if (propertyValue instanceof Boolean) {
         cell.setCellValue((Boolean)propertyValue);
      } else if (propertyValue instanceof Integer) {
         cell.setCellValue((double)(Integer)propertyValue);
      } else {
         cell.setCellValue(propertyValue.toString());
      }

   }

   public static void exportPredefine(SXSSFWorkbook wb, PredefineGroupDefinition predefineGroup, ExcelSupport excelSupport) {
      SXSSFSheet sheet = wb.createSheet("predefine");
      SXSSFRow row = sheet.createRow(0);
      Double doubleValue = 7314.285714285715;
      int number = Math.min(doubleValue.intValue(), 65280);
      CellStyle cellStyle = wb.createCellStyle();
      cellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
      cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
      cellStyle.setBorderBottom(BorderStyle.THIN);
      cellStyle.setBorderLeft(BorderStyle.THIN);
      cellStyle.setBorderRight(BorderStyle.THIN);
      cellStyle.setBorderTop(BorderStyle.THIN);
      CellStyle cellStyle2 = wb.createCellStyle();
      cellStyle2.setVerticalAlignment(VerticalAlignment.CENTER);
      cellStyle2.setBorderBottom(BorderStyle.THIN);
      cellStyle2.setBorderLeft(BorderStyle.THIN);
      cellStyle2.setBorderRight(BorderStyle.THIN);
      cellStyle2.setBorderTop(BorderStyle.THIN);
      cellStyle2.setWrapText(true);
      Cell cell = row.createCell(0);
      cell.setCellStyle(cellStyle);
      sheet.setColumnWidth(0, number);
      cell.setCellValue("name");
      cell = row.createCell(1);
      sheet.setColumnWidth(1, number / 2);
      cell.setCellStyle(cellStyle);
      cell.setCellValue("from or in");
      cell = row.createCell(2);
      cell.setCellStyle(cellStyle);
      sheet.setColumnWidth(2, number);
      cell.setCellValue("type");
      cell = row.createCell(3);
      cell.setCellStyle(cellStyle);
      sheet.setColumnWidth(3, number);
      cell.setCellValue("from type");
      cell = row.createCell(4);
      cell.setCellStyle(cellStyle);
      sheet.setColumnWidth(4, number);
      cell.setCellValue("from category");
      cell = row.createCell(5);
      cell.setCellStyle(cellStyle);
      sheet.setColumnWidth(5, number);
      cell.setCellValue("from value");
      cell = row.createCell(6);
      cell.setCellStyle(cellStyle);
      sheet.setColumnWidth(6, number * 2);
      cell.setCellValue("params");
      cell = row.createCell(7);
      cell.setCellStyle(cellStyle);
      sheet.setColumnWidth(7, number * 3);
      cell.setCellValue("condition");
      int number2 = 1;

      for(Predefine predefine : predefineGroup.getPredefines()) {
         SXSSFRow row2 = sheet.createRow(number2);
         Cell cell2 = row2.createCell(0);
         cell2.setCellStyle(cellStyle2);
         cell2.setCellValue(predefine.getName());
         cell2 = row2.createCell(1);
         cell2.setCellStyle(cellStyle2);
         cell2.setCellValue(predefine.getValueType().name());
         cell2 = row2.createCell(2);
         cell2.setCellStyle(cellStyle2);
         VariableCategory variableCategoryByUUID = excelSupport.findVariableCategoryByUUID(predefine.getType());
         if (variableCategoryByUUID != null) {
            cell2.setCellValue(variableCategoryByUUID.getName());
         } else {
            cell2.setCellValue(predefine.getType());
         }

         Cell cell3 = row2.createCell(3);
         cell3.setCellStyle(cellStyle2);
         if (predefine.getValue() instanceof VariableCategoryValue) {
            VariableCategoryValue variableCategoryValue = (VariableCategoryValue)predefine.getValue();
            cell2 = row2.createCell(4);
            cell2.setCellStyle(cellStyle2);
            cell2.setCellValue(variableCategoryValue.getVariableCategory());
            cell2 = row2.createCell(5);
            cell2.setCellStyle(cellStyle2);
            cell3.setCellValue(variableCategoryValue.getValueType().name());
            cell2 = row2.createCell(6);
            cell2.setCellStyle(cellStyle2);
         } else if (predefine.getValue() instanceof SimpleValue) {
            SimpleValue simpleValue = (SimpleValue)predefine.getValue();
            cell2 = row2.createCell(4);
            cell2.setCellStyle(cellStyle2);
            cell2.setCellValue("静态值");
            cell2 = row2.createCell(5);
            cell2.setCellStyle(cellStyle2);
            cell2.setCellValue(simpleValue.getContent());
            cell3.setCellValue(simpleValue.getValueType().name());
            cell2 = row2.createCell(6);
            cell2.setCellStyle(cellStyle2);
         } else if (predefine.getValue() instanceof VariableValue) {
            VariableValue variableValue = (VariableValue)predefine.getValue();
            cell2 = row2.createCell(4);
            cell2.setCellStyle(cellStyle2);
            cell2.setCellValue(variableValue.getVariableCategory());
            cell2 = row2.createCell(5);
            cell2.setCellStyle(cellStyle2);
            cell2.setCellValue(variableValue.getVariableLabel());
            cell3.setCellValue(variableValue.getValueType().name());
            cell2 = row2.createCell(6);
            cell2.setCellStyle(cellStyle2);
         } else if (predefine.getValue() instanceof PredefineValue) {
            PredefineValue predefineValue = (PredefineValue)predefine.getValue();
            cell2 = row2.createCell(4);
            cell2.setCellStyle(cellStyle2);
            cell2.setCellValue(predefineValue.getName());
            cell2 = row2.createCell(5);
            cell2.setCellStyle(cellStyle2);
            cell2.setCellValue(predefineValue.getPropertyLabel());
            cell3.setCellValue("Predefine");
            cell2 = row2.createCell(6);
            cell2.setCellStyle(cellStyle2);
         } else if (predefine.getValue() instanceof ParameterValue) {
            ParameterValue parameterValue = (ParameterValue)predefine.getValue();
            cell2 = row2.createCell(4);
            cell2.setCellStyle(cellStyle2);
            cell2.setCellValue(parameterValue.getKeyLabel());
            cell2 = row2.createCell(5);
            cell2.setCellStyle(cellStyle2);
            cell2.setCellValue(parameterValue.getVariableLabel());
            cell3.setCellValue(parameterValue.getValueType().name());
            cell2 = row2.createCell(6);
            cell2.setCellStyle(cellStyle2);
         } else if (predefine.getValue() instanceof ConstantValue) {
            ConstantValue constantValue = (ConstantValue)predefine.getValue();
            cell2 = row2.createCell(4);
            cell2.setCellStyle(cellStyle2);
            cell2.setCellValue(constantValue.getConstantCategory());
            cell2 = row2.createCell(5);
            cell2.setCellStyle(cellStyle2);
            cell2.setCellValue(constantValue.getConstantLabel());
            cell3.setCellValue(constantValue.getValueType().name());
            cell2 = row2.createCell(6);
            cell2.setCellStyle(cellStyle2);
         } else if (predefine.getValue() instanceof MethodValue) {
            MethodValue methodValue = (MethodValue)predefine.getValue();
            cell2 = row2.createCell(4);
            cell2.setCellStyle(cellStyle2);
            cell2.setCellValue(methodValue.getBeanLabel());
            cell2 = row2.createCell(5);
            cell2.setCellStyle(cellStyle2);
            cell2.setCellValue(methodValue.getMethodLabel());
            cell3.setCellValue(methodValue.getValueType().name());
            cell2 = row2.createCell(6);
            cell2.setCellStyle(cellStyle2);
            cell2.setCellValue(getParameterValues(excelSupport, methodValue.getParameters()));
         } else if (predefine.getValue() instanceof CommonFunctionValue) {
            CommonFunctionValue commonFunctionValue = (CommonFunctionValue)predefine.getValue();
            cell2 = row2.createCell(4);
            cell2.setCellStyle(cellStyle2);
            cell2.setCellValue("函数");
            cell2 = row2.createCell(5);
            cell2.setCellStyle(cellStyle2);
            cell2.setCellValue(commonFunctionValue.getLabel());
            cell3.setCellValue(commonFunctionValue.getValueType().name());
            cell2 = row2.createCell(6);
            cell2.setCellStyle(cellStyle2);
            cell2.setCellValue(getParameterValues(excelSupport, commonFunctionValue));
         }

         cell2 = row2.createCell(7);
         cell2.setCellStyle(cellStyle2);
         if (predefine.getValueType() == PredefineValueType.in && predefine.getJunction() != null) {
            cell2.setCellValue(criterions2Label(excelSupport, predefine.getJunction().getCriterions(), predefine.getJunction().getJunctionType()));
         }

         ++number2;
      }

   }
}
