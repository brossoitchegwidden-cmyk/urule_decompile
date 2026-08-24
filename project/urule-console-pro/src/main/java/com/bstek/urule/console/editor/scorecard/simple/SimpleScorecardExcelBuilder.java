package com.bstek.urule.console.editor.scorecard.simple;

import com.bstek.urule.Utils;
import com.bstek.urule.console.database.model.RuleFile;
import com.bstek.urule.console.editor.ExcelExportUtils;
import com.bstek.urule.console.editor.ExcelSupport;
import com.bstek.urule.console.util.StringUtils;
import com.bstek.urule.console.xml.DocumentHelper;
import com.bstek.urule.exception.DeserializeException;
import com.bstek.urule.model.library.variable.Variable;
import com.bstek.urule.model.library.variable.VariableCategory;
import com.bstek.urule.model.scorecard.AttributeRow;
import com.bstek.urule.model.scorecard.CardCell;
import com.bstek.urule.model.scorecard.CustomCol;
import com.bstek.urule.model.scorecard.ScorecardDefinition;
import com.bstek.urule.model.table.Condition;
import com.bstek.urule.parse.deserializer.ScorecardDeserializer;
import java.io.IOException;
import java.util.List;
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

public class SimpleScorecardExcelBuilder {
   private ExcelSupport excelSupport = new ExcelSupport();
   private ScorecardDeserializer scorecardDeserializer = (ScorecardDeserializer)Utils.getApplicationContext().getBean("urule.scorecardDeserializer");

   public ScorecardDefinition buildTable(RuleFile ruleFile) {
      try {
         Document text = DocumentHelper.parseText(ruleFile.getContent());
         return this.scorecardDeserializer.deserialize(text.getRootElement());
      } catch (Exception exception) {
         throw new DeserializeException(exception);
      }
   }

   public void buildExcel(ScorecardDefinition table, ServletOutputStream outputStream) throws IOException {
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
      Cell cell = row.createCell(0);
      cell.setCellStyle(cellStyle);
      cell.setCellValue(table.getAttributeColVariableCategory());
      this.setColumnWidth(sheet, 0, table.getAttributeColWidth());
      SXSSFDrawing drawingPatriarch = (SXSSFDrawing)sheet.createDrawingPatriarch();
      if (table.isWeightSupport()) {
         XSSFComment cellComment = (XSSFComment)drawingPatriarch.createCellComment(new XSSFClientAnchor(0, 0, 0, 0, 0, 0, 2, 2));
         cellComment.setString("WeightSupport");
         cell.setCellComment(cellComment);
      }

      cell = row.createCell(1);
      cell.setCellStyle(cellStyle);
      cell.setCellValue(table.getConditionColName());
      this.setColumnWidth(sheet, 1, table.getConditionColWidth());
      cell = row.createCell(2);
      cell.setCellStyle(cellStyle);
      cell.setCellValue(table.getScoreColName());
      this.setColumnWidth(sheet, 2, table.getScoreColWidth());
      if (table.getCustomCols().size() > 0) {
         for(CustomCol customCol : table.getCustomCols()) {
            cell = row.createCell(customCol.getColNumber() - 1);
            cell.setCellStyle(cellStyle);
            cell.setCellValue(customCol.getName());
            this.setColumnWidth(sheet, 2, customCol.getWidth());
            XSSFComment xSSFComment = (XSSFComment)drawingPatriarch.createCellComment(new XSSFClientAnchor(0, 0, 0, 0, customCol.getColNumber() - 1, 0, customCol.getColNumber() + 1, 2));
            xSSFComment.setString("custom");
            cell.setCellComment(xSSFComment);
         }
      }

      CellStyle cellStyle2 = sXSSFWorkbook.createCellStyle();
      cellStyle2.setVerticalAlignment(VerticalAlignment.CENTER);
      cellStyle2.setBorderBottom(BorderStyle.THIN);
      cellStyle2.setBorderLeft(BorderStyle.THIN);
      cellStyle2.setBorderRight(BorderStyle.THIN);
      cellStyle2.setBorderTop(BorderStyle.THIN);
      cellStyle2.setWrapText(true);
      int number = 0;

      for(AttributeRow attributeRow : table.getRows()) {
         int rowNumber = attributeRow.getRowNumber();
         Row row2 = sheet.createRow(rowNumber - 1);
         if (table.isWeightSupport()) {
            row2.setHeight((new Integer(1200)).shortValue());
         }

         CardCell cardCell = this.findCardCell(table, rowNumber, 1);
         Cell cell2 = row2.createCell(0);
         cell2.setCellStyle(cellStyle2);
         Object objectValue = null;
         String text;
         if (ExcelSupport.isParameter(table.getAttributeColVariableCategory()) && StringUtils.isNotBlank(cardCell.getKeyLabel())) {
            text = cardCell.getKeyLabel();
            if (StringUtils.isNotBlank(cardCell.getKeyUuid())) {
               Variable parameterByUuid = this.excelSupport.findParameterByUuid(cardCell.getKeyUuid());
               if (parameterByUuid != null) {
                  VariableCategory variableCategoryByUUID = this.excelSupport.findVariableCategoryByUUID(parameterByUuid.getDataType());
                  List variables = variableCategoryByUUID.getVariables();
                  Variable variable = null;

                  for(Variable variable2 : (Iterable<Variable>)(Iterable<?>)(variables)) {
                     if (variable2.getUuid().equals(cardCell.getUuid())) {
                        variable = variable2;
                     }
                  }

                  if (variable != null) {
                     text = cardCell.getKeyLabel() + "." + variable.getLabel();
                  }
               }
            }
         } else {
            text = cardCell.getVariableLabel();
         }

         if (table.isWeightSupport()) {
            text = text + " \n权重:" + cardCell.getWeight();
         }

         cell2.setCellValue(text);
         int number2 = this.calculateAttributeRowSpan(table, number);
         if (number2 > 1) {
            int number3 = rowNumber - 1;
            int number4 = number3 + number2 - 1;
            CellRangeAddress cellRangeAddress = new CellRangeAddress(number3, number4, 0, 0);
            sheet.addMergedRegion(cellRangeAddress);
            RegionUtil.setBorderTop(BorderStyle.THIN, cellRangeAddress, sheet);
            RegionUtil.setBorderBottom(BorderStyle.THIN, cellRangeAddress, sheet);
            RegionUtil.setBorderLeft(BorderStyle.THIN, cellRangeAddress, sheet);
            RegionUtil.setBorderRight(BorderStyle.THIN, cellRangeAddress, sheet);
         }

         this.writeScorecardRow(table, cell, cellStyle2, rowNumber, row2);
         if (number2 > 1) {
            for(int index = 0; index < number2 - 1; ++index) {
               row2 = sheet.createRow(rowNumber + index);
               this.writeScorecardRow(table, cell, cellStyle2, rowNumber + index + 1, row2);
            }
         }

         ++number;
      }

      ExcelExportUtils.exportProperties(sXSSFWorkbook, table);
      sXSSFWorkbook.write(outputStream);
   }

   private int calculateAttributeRowSpan(ScorecardDefinition scorecardDefinition, int number) {
      List rows = scorecardDefinition.getRows();
      int rowNumber = ((AttributeRow)rows.get(number)).getRowNumber();
      return rows.size() > number + 1 ? ((AttributeRow)rows.get(number + 1)).getRowNumber() - rowNumber : ((CardCell)scorecardDefinition.getCells().get(scorecardDefinition.getCells().size() - 1)).getRow() - rowNumber + 1;
   }

   private void writeScorecardRow(ScorecardDefinition scorecardDefinition, Cell cell, CellStyle cellStyle, int number, Row row) {
      CardCell cardCell = this.findCardCell(scorecardDefinition, number, 2);
      Cell cell2 = row.createCell(1);
      cell2.setCellStyle(cellStyle);
      if (cardCell.getJoint() != null && cardCell.getJoint().getConditions() != null && cardCell.getJoint().getConditions().size() != 0) {
         String text = ExcelExportUtils.conditions2Label(this.excelSupport, cardCell.getJoint().getConditions(), cardCell.getJoint().getType());
         cell2.setCellValue(text);
      } else {
         cell.setCellValue("");
      }

      cardCell = this.findCardCell(scorecardDefinition, number, 3);
      cell2 = row.createCell(2);
      cell2.setCellStyle(cellStyle);
      cell2.setCellValue(ExcelExportUtils.getLabelValue(this.excelSupport, (Condition)null, cardCell.getValue()));
      if (scorecardDefinition.getCustomCols().size() > 0) {
         for(CustomCol customCol : scorecardDefinition.getCustomCols()) {
            cardCell = this.findCardCell(scorecardDefinition, number, customCol.getColNumber());
            cell2 = row.createCell(customCol.getColNumber() - 1);
            cell2.setCellStyle(cellStyle);
            cell2.setCellValue(ExcelExportUtils.getLabelValue(this.excelSupport, (Condition)null, cardCell.getValue()));
         }
      }

   }

   private CardCell findCardCell(ScorecardDefinition scorecardDefinition, int number, int number2) {
      CardCell cardCell = null;

      for(CardCell cardCell2 : scorecardDefinition.getCells()) {
         if (cardCell2.getRow() == number && cardCell2.getCol() == number2) {
            cardCell = cardCell2;
            break;
         }
      }

      return cardCell;
   }

   private void setColumnWidth(Sheet sheet, int number, String text) {
      int number2 = (new Double(Double.parseDouble(text))).intValue();
      Double doubleValue = (double)number2 * 1.3281472327365;
      Double doubleValue2 = (double)256.0F * (doubleValue / (double)7.0F);
      sheet.setColumnWidth(number, Math.min(doubleValue2.intValue(), 65280));
   }
}
