package com.bstek.urule.console.editor.scorecard.complex;

import com.bstek.urule.Utils;
import com.bstek.urule.console.database.model.RuleFile;
import com.bstek.urule.console.editor.ExcelExportUtils;
import com.bstek.urule.console.editor.ExcelSupport;
import com.bstek.urule.console.util.StringUtils;
import com.bstek.urule.console.xml.DocumentHelper;
import com.bstek.urule.exception.DeserializeException;
import com.bstek.urule.model.scorecard.ComplexColumn;
import com.bstek.urule.model.scorecard.ComplexColumnType;
import com.bstek.urule.model.scorecard.ComplexScorecardDefinition;
import com.bstek.urule.model.table.Condition;
import com.bstek.urule.parse.deserializer.ComplexScorecardDeserializer;
import java.io.IOException;
import javax.servlet.ServletOutputStream;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.streaming.SXSSFDrawing;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFComment;
import org.dom4j.Document;

public class ComplexScorecardExcelBuilder {
   private ExcelSupport excelSupport = new ExcelSupport();
   private ComplexScorecardDeserializer complexScorecardDeserializer = (ComplexScorecardDeserializer)Utils.getApplicationContext().getBean("urule.complexScorecardDeserializer");

   public ComplexScorecardDefinition buildTable(RuleFile ruleFile) {
      try {
         Document text = DocumentHelper.parseText(ruleFile.getContent());
         return this.complexScorecardDeserializer.deserialize(text.getRootElement());
      } catch (Exception exception) {
         throw new DeserializeException(exception);
      }
   }

   public void buildExcel(ComplexScorecardDefinition table, ServletOutputStream outputStream) throws IOException {
      SXSSFWorkbook sXSSFWorkbook = new SXSSFWorkbook(100000);
      SXSSFSheet sheet = sXSSFWorkbook.createSheet();
      Row row = sheet.createRow(0);
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
      SXSSFDrawing drawingPatriarch = (SXSSFDrawing)sheet.createDrawingPatriarch();

      for(ComplexColumn complexColumn : table.getColumns()) {
         Cell cell = row.createCell(complexColumn.getNum());
         cell.setCellStyle(cellStyle);
         if (ComplexColumnType.Criteria == complexColumn.getType()) {
            cell.setCellValue(complexColumn.getVariableCategory());
         } else if (ComplexColumnType.Score == complexColumn.getType()) {
            cell.setCellValue("分值");
         } else if (ComplexColumnType.Custom == complexColumn.getType()) {
            cell.setCellValue(complexColumn.getCustomLabel());
         }

         this.setColumnWidth(sheet, complexColumn.getNum(), String.valueOf(complexColumn.getWidth()));
         if (ComplexColumnType.Custom == complexColumn.getType()) {
            XSSFComment cellComment = (XSSFComment)drawingPatriarch.createCellComment(new XSSFClientAnchor(0, 0, 0, 0, complexColumn.getNum(), 0, complexColumn.getNum() + 1, 3));
            cellComment.setString("custom");
            cell.setCellComment(cellComment);
         }
      }

      CellStyle cellStyle2 = sXSSFWorkbook.createCellStyle();
      cellStyle2.setVerticalAlignment(VerticalAlignment.CENTER);
      cellStyle2.setBorderBottom(BorderStyle.THIN);
      cellStyle2.setBorderLeft(BorderStyle.THIN);
      cellStyle2.setBorderRight(BorderStyle.THIN);
      cellStyle2.setBorderTop(BorderStyle.THIN);
      cellStyle2.setWrapText(true);

      for(com.bstek.urule.model.table.Row row2 : table.getRows()) {
         int num = row2.getNum();
         Row row3 = sheet.createRow(num + 1);
         row3.setHeight((new Integer(row2.getHeight() * 20)).shortValue());

         for(ComplexColumn complexColumn2 : table.getColumns()) {
            com.bstek.urule.model.table.Cell cell2 = (com.bstek.urule.model.table.Cell)table.getCellMap().get(num + "," + complexColumn2.getNum());
            if (cell2 != null) {
               if (cell2.getRowspan() > 1) {
                  CellRangeAddress cellRangeAddress = new CellRangeAddress(num + 1, num + cell2.getRowspan(), complexColumn2.getNum(), complexColumn2.getNum());
                  sheet.addMergedRegion(cellRangeAddress);
                  RegionUtil.setBorderTop(BorderStyle.THIN, cellRangeAddress, sheet);
                  RegionUtil.setBorderBottom(BorderStyle.THIN, cellRangeAddress, sheet);
                  RegionUtil.setBorderLeft(BorderStyle.THIN, cellRangeAddress, sheet);
                  RegionUtil.setBorderRight(BorderStyle.THIN, cellRangeAddress, sheet);
               }

               Cell cell3 = row3.createCell(complexColumn2.getNum());
               cell3.setCellStyle(cellStyle2);
               if (ComplexColumnType.Criteria == complexColumn2.getType()) {
                  if (cell2.getJoint() != null && cell2.getJoint().getConditions() != null && cell2.getJoint().getConditions().size() != 0) {
                     String text = ExcelExportUtils.conditions2Label(this.excelSupport, cell2.getJoint().getConditions(), cell2.getJoint().getType());
                     String variableLabel = null;
                     if (StringUtils.isNotBlank(cell2.getKeyLabel())) {
                        variableLabel = cell2.getKeyLabel() + "." + cell2.getVariableLabel();
                     } else {
                        variableLabel = cell2.getVariableLabel();
                     }

                     cell3.setCellValue(variableLabel + "\n" + text);
                  } else {
                     cell3.setCellValue("");
                  }
               } else if (ComplexColumnType.Score == complexColumn2.getType()) {
                  cell3.setCellValue(ExcelExportUtils.getLabelValue(this.excelSupport, (Condition)null, cell2.getValue()));
               } else {
                  cell3.setCellValue(ExcelExportUtils.getLabelValue(this.excelSupport, (Condition)null, cell2.getValue()));
               }
            }
         }
      }

      ExcelExportUtils.exportProperties(sXSSFWorkbook, table);
      sXSSFWorkbook.write(outputStream);
   }

   private void setColumnWidth(Sheet sheet, int number, String text) {
      int number2 = (new Double(Double.parseDouble(text))).intValue();
      Double doubleValue = (double)number2 * 1.3281472327365;
      Double doubleValue2 = (double)256.0F * (doubleValue / (double)7.0F);
      sheet.setColumnWidth(number, Math.min(doubleValue2.intValue(), 65280));
   }
}
