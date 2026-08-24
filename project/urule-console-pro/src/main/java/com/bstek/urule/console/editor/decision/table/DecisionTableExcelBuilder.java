package com.bstek.urule.console.editor.decision.table;

import com.bstek.urule.Utils;
import com.bstek.urule.action.Action;
import com.bstek.urule.action.ConsolePrintAction;
import com.bstek.urule.action.ExecuteCommonFunctionAction;
import com.bstek.urule.action.ExecuteMethodAction;
import com.bstek.urule.action.ScoringAction;
import com.bstek.urule.action.TemplateAction;
import com.bstek.urule.action.VariableAssignAction;
import com.bstek.urule.console.InfoException;
import com.bstek.urule.console.editor.ExcelExportUtils;
import com.bstek.urule.console.editor.ExcelSupport;
import com.bstek.urule.console.util.StringUtils;
import com.bstek.urule.console.xml.DocumentHelper;
import com.bstek.urule.model.library.variable.Variable;
import com.bstek.urule.model.library.variable.VariableCategory;
import com.bstek.urule.model.table.Column;
import com.bstek.urule.model.table.ColumnType;
import com.bstek.urule.model.table.Condition;
import com.bstek.urule.model.table.DecisionTable;
import com.bstek.urule.model.table.Row;
import com.bstek.urule.parse.deserializer.DecisionTableDeserializer;
import java.io.IOException;
import java.io.OutputStream;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.streaming.SXSSFDrawing;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFComment;
import org.dom4j.Document;

public class DecisionTableExcelBuilder {
   private ExcelSupport excelSupport = new ExcelSupport();
   private DecisionTableDeserializer decisionTableDeserializer = (DecisionTableDeserializer)Utils.getApplicationContext().getBean("urule.decisionTableDeserializer");

   public DecisionTable buildTable(String content) {
      try {
         Document text = DocumentHelper.parseText(content);
         return this.decisionTableDeserializer.deserialize(text.getRootElement());
      } catch (Exception exception) {
         throw new InfoException(exception);
      }
   }

   public void buildExcel(DecisionTable table, OutputStream outputStream) throws IOException {
      SXSSFWorkbook sXSSFWorkbook = new SXSSFWorkbook(100000);
      SXSSFSheet sheet = sXSSFWorkbook.createSheet();
      int number = 0;
      SXSSFRow row = sheet.createRow(0);
      CellStyle cellStyle = sXSSFWorkbook.createCellStyle();
      cellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
      cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
      cellStyle.setBorderBottom(BorderStyle.THIN);
      cellStyle.setBorderLeft(BorderStyle.THIN);
      cellStyle.setBorderRight(BorderStyle.THIN);
      cellStyle.setBorderTop(BorderStyle.THIN);
      Font font = sXSSFWorkbook.createFont();
      font.setBold(true);
      cellStyle.setFont(font);
      CellStyle cellStyle2 = sXSSFWorkbook.createCellStyle();
      cellStyle2.setFillForegroundColor(IndexedColors.ORCHID.getIndex());
      cellStyle2.setFillPattern(FillPatternType.SOLID_FOREGROUND);
      cellStyle2.setBorderBottom(BorderStyle.THIN);
      cellStyle2.setBorderLeft(BorderStyle.THIN);
      cellStyle2.setBorderRight(BorderStyle.THIN);
      cellStyle2.setBorderTop(BorderStyle.THIN);
      font = sXSSFWorkbook.createFont();
      font.setBold(true);
      cellStyle2.setFont(font);
      CellStyle cellStyle3 = sXSSFWorkbook.createCellStyle();
      cellStyle3.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
      cellStyle3.setFillPattern(FillPatternType.SOLID_FOREGROUND);
      cellStyle3.setBorderBottom(BorderStyle.THIN);
      cellStyle3.setBorderLeft(BorderStyle.THIN);
      cellStyle3.setBorderRight(BorderStyle.THIN);
      cellStyle3.setBorderTop(BorderStyle.THIN);
      font = sXSSFWorkbook.createFont();
      font.setBold(true);
      cellStyle3.setFont(font);
      CellStyle cellStyle4 = sXSSFWorkbook.createCellStyle();
      cellStyle4.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());
      cellStyle4.setFillPattern(FillPatternType.SOLID_FOREGROUND);
      cellStyle4.setBorderBottom(BorderStyle.THIN);
      cellStyle4.setBorderLeft(BorderStyle.THIN);
      cellStyle4.setBorderRight(BorderStyle.THIN);
      cellStyle4.setBorderTop(BorderStyle.THIN);
      font = sXSSFWorkbook.createFont();
      font.setBold(true);
      font.setColor(IndexedColors.WHITE.getIndex());
      cellStyle4.setFont(font);
      CellStyle cellStyle5 = sXSSFWorkbook.createCellStyle();
      cellStyle5.setVerticalAlignment(VerticalAlignment.CENTER);
      cellStyle5.setBorderBottom(BorderStyle.THIN);
      cellStyle5.setBorderLeft(BorderStyle.THIN);
      cellStyle5.setBorderRight(BorderStyle.THIN);
      cellStyle5.setBorderTop(BorderStyle.THIN);
      cellStyle5.setWrapText(true);
      SXSSFDrawing drawingPatriarch = sheet.createDrawingPatriarch();

      for(Column column : table.getColumns()) {
         double doubleValue = (double)column.getWidth() * 1.3281472327365;
         Double doubleValue2 = (double)256.0F * (doubleValue / (double)7.0F);
         Cell cell = row.createCell(number);
         if (ColumnType.ExecuteMethod == column.getType()) {
            cell.setCellStyle(cellStyle2);
            cell.setCellValue("ExecuteMethod");
            XSSFComment cellComment = (XSSFComment)drawingPatriarch.createCellComment(new XSSFClientAnchor(0, 0, 100, 100, (short)column.getNum(), 0, (short)column.getNum() + 1, 2));
            cellComment.setString("执行方法");
            cell.setCellComment(cellComment);
         } else if (ColumnType.ConsolePrint == column.getType()) {
            cell.setCellStyle(cellStyle);
            cell.setCellValue("控制台输出");
            XSSFComment xSSFComment = (XSSFComment)drawingPatriarch.createCellComment(new XSSFClientAnchor(0, 0, 100, 100, (short)column.getNum(), 0, (short)column.getNum() + 1, 2));
            xSSFComment.setString("out");
            cell.setCellComment(xSSFComment);
         } else if (ColumnType.Assignment == column.getType()) {
            cell.setCellStyle(cellStyle3);
            if (column.isPredefine()) {
               String predefineName = column.getPredefineName();
               if (StringUtils.isNotBlank(column.getPredefinePropertyLabel())) {
                  predefineName = predefineName + "." + column.getPredefinePropertyLabel();
               }

               cell.setCellValue(predefineName);
               XSSFComment xSSFComment2 = (XSSFComment)drawingPatriarch.createCellComment(new XSSFClientAnchor(0, 0, 100, 100, (short)column.getNum(), 0, (short)column.getNum() + 1, 2));
               xSSFComment2.setString("赋值:预定义变量");
               cell.setCellComment(xSSFComment2);
            } else {
               if (ExcelSupport.isParameter(column.getVariableCategory())) {
                  cell.setCellValue(this.formatParameterColumnLabel(column));
               } else {
                  cell.setCellValue(column.getVariableCategory() + "." + column.getVariableLabel());
               }

               XSSFComment xSSFComment3 = (XSSFComment)drawingPatriarch.createCellComment(new XSSFClientAnchor(0, 0, 0, 0, (short)column.getNum(), 0, (short)column.getNum() + 1, 2));
               xSSFComment3.setString("赋值");
               cell.setCellComment(xSSFComment3);
            }
         } else {
            cell.setCellStyle(cellStyle4);
            if (ExcelSupport.isParameter(column.getVariableCategory())) {
               cell.setCellValue(this.formatParameterColumnLabel(column));
            } else {
               cell.setCellValue(column.getVariableCategory() + "." + column.getVariableLabel());
            }

            if (column.isPredefine()) {
               String predefineName2 = column.getPredefineName();
               if (StringUtils.isNotBlank(column.getPredefinePropertyLabel())) {
                  predefineName2 = predefineName2 + "." + column.getPredefinePropertyLabel();
               }

               cell.setCellValue(predefineName2);
               XSSFComment xSSFComment4 = (XSSFComment)drawingPatriarch.createCellComment(new XSSFClientAnchor(0, 0, 100, 100, (short)column.getNum(), 0, (short)column.getNum() + 1, 2));
               xSSFComment4.setString("条件:预定义变量");
               cell.setCellComment(xSSFComment4);
            } else {
               XSSFComment xSSFComment5 = (XSSFComment)drawingPatriarch.createCellComment(new XSSFClientAnchor(0, 0, 100, 100, (short)column.getNum(), 0, (short)column.getNum() + 1, 2));
               xSSFComment5.setString("条件");
               cell.setCellComment(xSSFComment5);
            }
         }

         sheet.setColumnWidth(number++, Math.min(doubleValue2.intValue(), 65280));
      }

      int number2 = 0;

      for(Row row2 : table.getRows()) {
         SXSSFRow row3 = sheet.createRow(number2 + 1);
         row3.setHeight((short)(row2.getHeight() * 20));
         number = 0;

         for(Column column2 : table.getColumns()) {
            com.bstek.urule.model.table.Cell cell2 = (com.bstek.urule.model.table.Cell)table.getCellMap().get(number2 + "," + number);
            if (cell2 != null) {
               if (cell2.getRowspan() > 1) {
                  CellRangeAddress cellRangeAddress = new CellRangeAddress(cell2.getRow() + 1, cell2.getRow() + cell2.getRowspan(), column2.getNum(), column2.getNum());
                  sheet.addMergedRegion(cellRangeAddress);
                  RegionUtil.setBorderTop(BorderStyle.THIN, cellRangeAddress, sheet);
                  RegionUtil.setBorderBottom(BorderStyle.THIN, cellRangeAddress, sheet);
                  RegionUtil.setBorderLeft(BorderStyle.THIN, cellRangeAddress, sheet);
                  RegionUtil.setBorderRight(BorderStyle.THIN, cellRangeAddress, sheet);
               }

               Cell cell3 = row3.createCell(number);
               cell3.setCellStyle(cellStyle5);
               if (ColumnType.Criteria == column2.getType()) {
                  if (cell2.getJoint() == null || cell2.getJoint().getConditions() == null || cell2.getJoint().getConditions().size() == 0) {
                     cell3.setCellValue("");
                     ++number;
                     continue;
                  }

                  String text = ExcelExportUtils.conditions2Label(this.excelSupport, cell2.getJoint().getConditions(), cell2.getJoint().getType());
                  cell3.setCellValue(text);
               } else if (ColumnType.Assignment == column2.getType()) {
                  if (cell2.getValue() != null) {
                     cell3.setCellValue(ExcelExportUtils.getLabelValue(this.excelSupport, (Condition)null, cell2.getValue()));
                  }
               } else if (ColumnType.ConsolePrint == column2.getType()) {
                  if (cell2.getValue() != null) {
                     cell3.setCellValue(ExcelExportUtils.getLabelValue(this.excelSupport, (Condition)null, cell2.getValue()));
                  }
               } else if (ColumnType.ExecuteMethod == column2.getType() && cell2.getAction() != null) {
                  cell3.setCellValue(this.formatActionLabel(cell2.getAction()));
               }
            }

            ++number;
         }

         ++number2;
      }

      if (table.getPredefineGroup() != null && table.getPredefineGroup().getPredefines().size() > 0) {
         ExcelExportUtils.exportPredefine(sXSSFWorkbook, table.getPredefineGroup(), this.excelSupport);
      }

      ExcelExportUtils.exportProperties(sXSSFWorkbook, table);
      sXSSFWorkbook.write(outputStream);
   }

   private String formatParameterColumnLabel(Column column) {
      if (StringUtils.isNotBlank(column.getKeyCategoryUuid())) {
         Variable variable = null;
         VariableCategory variableCategoryByUUID = this.excelSupport.findVariableCategoryByUUID(column.getKeyCategoryUuid());
         if (variableCategoryByUUID != null) {
            variable = (Variable)variableCategoryByUUID.getVariableNames().get(column.getVariableName());
         }

         if (variable != null) {
            return "参数." + column.getKeyLabel() + "." + variable.getLabel();
         }
      }

      return "参数." + column.getVariableLabel();
   }

   private String formatActionLabel(Action action) {
      String text = "";
      if (action instanceof ConsolePrintAction) {
         ConsolePrintAction consolePrintAction = (ConsolePrintAction)action;
         text = consolePrintAction.getValue() != null ? consolePrintAction.getValue().toString() : "";
      } else if (action instanceof ExecuteCommonFunctionAction) {
         ExecuteCommonFunctionAction executeCommonFunctionAction = (ExecuteCommonFunctionAction)action;
         text = executeCommonFunctionAction.getLabel();
      } else if (action instanceof ExecuteMethodAction) {
         ExecuteMethodAction executeMethodAction = (ExecuteMethodAction)action;
         text = executeMethodAction.getBeanLabel() + "." + executeMethodAction.getMethodLabel();
      } else if (action instanceof ScoringAction) {
         ScoringAction scoringAction = (ScoringAction)action;
         text = scoringAction.getName();
      } else if (action instanceof ScoringAction) {
         ScoringAction scoringAction2 = (ScoringAction)action;
         text = scoringAction2.getName();
      } else if (action instanceof TemplateAction) {
         TemplateAction templateAction = (TemplateAction)action;
         text = templateAction.getName();
      } else if (action instanceof VariableAssignAction) {
         VariableAssignAction variableAssignAction = (VariableAssignAction)action;
         text = variableAssignAction.getVariableCategory() + "." + variableAssignAction.getVariableLabel();
      } else {
         text = "";
      }

      return text;
   }
}
