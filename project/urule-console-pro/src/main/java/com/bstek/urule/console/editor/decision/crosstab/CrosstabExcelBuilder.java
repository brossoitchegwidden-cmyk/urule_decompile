package com.bstek.urule.console.editor.decision.crosstab;

import com.bstek.urule.Utils;
import com.bstek.urule.console.InfoException;
import com.bstek.urule.console.editor.ExcelExportUtils;
import com.bstek.urule.console.editor.ExcelSupport;
import com.bstek.urule.console.util.StringUtils;
import com.bstek.urule.console.xml.DocumentHelper;
import com.bstek.urule.model.crosstab.BundleData;
import com.bstek.urule.model.crosstab.ConditionCrossCell;
import com.bstek.urule.model.crosstab.CrossCell;
import com.bstek.urule.model.crosstab.CrosstabDefinition;
import com.bstek.urule.model.crosstab.HeaderCell;
import com.bstek.urule.model.crosstab.LeftColumn;
import com.bstek.urule.model.crosstab.TopColumn;
import com.bstek.urule.model.crosstab.TopRow;
import com.bstek.urule.model.crosstab.ValueCrossCell;
import com.bstek.urule.model.library.variable.Variable;
import com.bstek.urule.model.library.variable.VariableCategory;
import com.bstek.urule.model.table.Condition;
import com.bstek.urule.parse.deserializer.CrosstableDeserializer;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.streaming.SXSSFDrawing;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFComment;
import org.dom4j.Document;

public class CrosstabExcelBuilder {
   private ExcelSupport excelSupport = new ExcelSupport();
   private CrosstableDeserializer crosstableDeserializer = (CrosstableDeserializer)Utils.getApplicationContext().getBean("urule.crosstableDeserializer");
   static final String PREDEFINE_BUNDLE_TYPE = "predefine";

   public CrosstabDefinition buildTable(String content) {
      try {
         Document text = DocumentHelper.parseText(content);
         return this.crosstableDeserializer.deserialize(text.getRootElement());
      } catch (Exception exception) {
         throw new InfoException(exception);
      }
   }

   private CrossCell findCrossCell(List items, int number, int number2) {
      CrossCell crossCell = null;

      for(CrossCell crossCell2 : (Iterable<CrossCell>)(Iterable<?>)(items)) {
         if (crossCell2.getCol() == number2 + 1 && crossCell2.getRow() == number + 1) {
            crossCell = crossCell2;
            break;
         }
      }

      return crossCell;
   }

   public void buildExcel(CrosstabDefinition table, OutputStream outputStream) throws IOException {
      SXSSFWorkbook sXSSFWorkbook = new SXSSFWorkbook(100000);
      SXSSFSheet sheet = sXSSFWorkbook.createSheet();
      CellStyle cellStyle = sXSSFWorkbook.createCellStyle();
      cellStyle.setBorderBottom(BorderStyle.THIN);
      cellStyle.setBorderLeft(BorderStyle.THIN);
      cellStyle.setBorderRight(BorderStyle.THIN);
      cellStyle.setBorderTop(BorderStyle.THIN);
      CellStyle cellStyle2 = sXSSFWorkbook.createCellStyle();
      cellStyle2.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
      cellStyle2.setFillPattern(FillPatternType.SOLID_FOREGROUND);
      cellStyle2.setAlignment(HorizontalAlignment.CENTER);
      cellStyle2.setVerticalAlignment(VerticalAlignment.CENTER);
      cellStyle2.setBorderBottom(BorderStyle.THIN);
      cellStyle2.setBorderLeft(BorderStyle.THIN);
      cellStyle2.setBorderRight(BorderStyle.THIN);
      cellStyle2.setBorderTop(BorderStyle.THIN);
      Font font = sXSSFWorkbook.createFont();
      font.setColor(IndexedColors.DARK_GREEN.index);
      cellStyle2.setFont(font);
      CellStyle cellStyle3 = sXSSFWorkbook.createCellStyle();
      cellStyle3.setVerticalAlignment(VerticalAlignment.CENTER);
      cellStyle3.setBorderBottom(BorderStyle.THIN);
      cellStyle3.setBorderLeft(BorderStyle.THIN);
      cellStyle3.setBorderRight(BorderStyle.THIN);
      cellStyle3.setBorderTop(BorderStyle.THIN);
      cellStyle3.setWrapText(true);
      Row row = sheet.createRow(0);
      HeaderCell headerCell = table.getHeaderCell();
      if (headerCell.getRowspan() > 1 || headerCell.getColspan() > 1) {
         CellRangeAddress cellRangeAddress = new CellRangeAddress(0, headerCell.getRowspan() - 1, 0, headerCell.getColspan() - 1);
         sheet.addMergedRegion(cellRangeAddress);
         RegionUtil.setBorderTop(BorderStyle.THIN, cellRangeAddress, sheet);
         RegionUtil.setBorderBottom(BorderStyle.THIN, cellRangeAddress, sheet);
         RegionUtil.setBorderLeft(BorderStyle.THIN, cellRangeAddress, sheet);
         RegionUtil.setBorderRight(BorderStyle.THIN, cellRangeAddress, sheet);
      }

      Cell cell = row.createCell(0);
      cell.setCellStyle(cellStyle2);
      cell.setCellValue(headerCell.getText());
      double doubleValue = 398.44416982095004;
      Double doubleValue2 = (double)256.0F * (doubleValue / (double)7.0F);
      sheet.setColumnWidth(0, Math.min(doubleValue2.intValue(), 65280));
      SXSSFDrawing drawingPatriarch = (SXSSFDrawing)sheet.createDrawingPatriarch();
      ArrayList items = new ArrayList();

      for(int index = 0; index < table.getRows().size(); ++index) {
         Row row2 = row;
         if (index > 0) {
            row2 = sheet.createRow(index);
         }

         row2.setHeight((short)1200);
         com.bstek.urule.model.crosstab.CrossRow crossRow = (com.bstek.urule.model.crosstab.CrossRow)table.getRows().get(index);

         for(int index2 = 0; index2 < table.getColumns().size(); ++index2) {
            com.bstek.urule.model.crosstab.CrossColumn crossColumn = (com.bstek.urule.model.crosstab.CrossColumn)table.getColumns().get(index2);
            CrossCell crossCell = this.findCrossCell(table.getCells(), index, index2);
            if (crossCell != null) {
               if (crossCell.getRowspan() > 1 || crossCell.getColspan() > 1) {
                  int number = crossCell.getRowspan() > 1 ? crossCell.getRow() + crossCell.getRowspan() - 1 : crossCell.getRow();
                  int number2 = crossCell.getColspan() > 1 ? crossCell.getCol() + crossCell.getColspan() - 1 : crossCell.getCol() - 1;
                  if (number > crossCell.getRow() || number2 > crossCell.getCol() - 1) {
                     if (crossColumn instanceof TopColumn) {
                        CellRangeAddress cellRangeAddress2 = new CellRangeAddress(crossCell.getRow() - 1, number - 1, crossCell.getCol() - 1, number2 - 1);
                        sheet.addMergedRegion(cellRangeAddress2);
                        RegionUtil.setBorderTop(BorderStyle.THIN, cellRangeAddress2, sheet);
                        RegionUtil.setBorderBottom(BorderStyle.THIN, cellRangeAddress2, sheet);
                        RegionUtil.setBorderLeft(BorderStyle.THIN, cellRangeAddress2, sheet);
                        RegionUtil.setBorderRight(BorderStyle.THIN, cellRangeAddress2, sheet);
                     } else {
                        CellRangeAddress cellRangeAddress3 = new CellRangeAddress(crossCell.getRow() - 1, number - 1, crossCell.getCol() - 1, number2);
                        sheet.addMergedRegion(cellRangeAddress3);
                        RegionUtil.setBorderTop(BorderStyle.THIN, cellRangeAddress3, sheet);
                        RegionUtil.setBorderBottom(BorderStyle.THIN, cellRangeAddress3, sheet);
                        RegionUtil.setBorderLeft(BorderStyle.THIN, cellRangeAddress3, sheet);
                        RegionUtil.setBorderRight(BorderStyle.THIN, cellRangeAddress3, sheet);
                     }
                  }
               }

               Cell cell2 = row2.createCell(index2);
               if (index2 > 0) {
                  sheet.setColumnWidth(index2, Math.min(doubleValue2.intValue() / 2, 65280));
               }

               if (crossCell instanceof ConditionCrossCell) {
                  cell2.setCellStyle(cellStyle2);
                  ConditionCrossCell conditionCrossCell = (ConditionCrossCell)crossCell;
                  if (conditionCrossCell.getJoint() != null && conditionCrossCell.getJoint().getConditions() != null && conditionCrossCell.getJoint().getConditions().size() != 0) {
                     String text = ExcelExportUtils.conditions2Label(this.excelSupport, conditionCrossCell.getJoint().getConditions(), conditionCrossCell.getJoint().getType());
                     String text2 = "";
                     String text3 = "predefine";
                     if (crossRow instanceof TopRow) {
                        TopRow topRow = (TopRow)crossRow;
                        text2 = this.buildBundleLabel(topRow);
                        if (!items.contains(crossRow.getRowNumber() + "," + crossColumn.getColumnNumber())) {
                           XSSFComment cellComment = (XSSFComment)drawingPatriarch.createCellComment(new XSSFClientAnchor(0, 0, 0, 0, crossColumn.getColumnNumber() - 1, crossRow.getRowNumber() - 1, crossColumn.getColumnNumber(), crossRow.getRowNumber()));
                           cellComment.setString(text2);
                           if (text3.equals(topRow.getBundleDataType())) {
                              cellComment.setString(topRow.getBundleDataType() + ":" + text2);
                           }

                           cell2.setCellComment(cellComment);
                           items.add(crossRow.getRowNumber() + "," + crossColumn.getColumnNumber());
                        }
                     } else if (crossColumn instanceof LeftColumn) {
                        LeftColumn leftColumn = (LeftColumn)crossColumn;
                        text2 = this.buildBundleLabel(leftColumn);
                        if (!items.contains(crossRow.getRowNumber() + "," + leftColumn.getColumnNumber())) {
                           XSSFComment xSSFComment = (XSSFComment)drawingPatriarch.createCellComment(new XSSFClientAnchor(0, 0, 0, 0, crossColumn.getColumnNumber() - 1, crossRow.getRowNumber() - 1, crossColumn.getColumnNumber(), crossRow.getRowNumber()));
                           xSSFComment.setString(text2);
                           if (text3.equals(leftColumn.getBundleDataType())) {
                              xSSFComment.setString(leftColumn.getBundleDataType() + ":" + text2);
                           }

                           cell2.setCellComment(xSSFComment);
                           items.add(crossRow.getRowNumber() + "," + crossColumn.getColumnNumber());
                        }
                     }

                     cell2.setCellValue(text);
                  } else {
                     cell2.setCellValue("");
                  }
               } else if (crossCell instanceof ValueCrossCell) {
                  cell2.setCellStyle(cellStyle3);
                  ValueCrossCell valueCrossCell = (ValueCrossCell)crossCell;
                  cell2.setCellValue(ExcelExportUtils.getLabelValue(this.excelSupport, (Condition)null, valueCrossCell.getValue()));
               }
            }
         }
      }

      if (table.getPredefineGroup() != null && table.getPredefineGroup().getPredefines().size() > 0) {
         ExcelExportUtils.exportPredefine(sXSSFWorkbook, table.getPredefineGroup(), this.excelSupport);
      }

      ExcelExportUtils.exportProperties(sXSSFWorkbook, table);
      sXSSFWorkbook.write(outputStream);
   }

   private String buildBundleLabel(BundleData bundleData) {
      String predefineName = "";
      if (PREDEFINE_BUNDLE_TYPE.equals(bundleData.getBundleDataType())) {
         predefineName = bundleData.getPredefineName();
         if (StringUtils.isNotBlank(bundleData.getPredefinePropertyLabel())) {
            predefineName = predefineName + "." + bundleData.getPredefinePropertyLabel();
         }
      } else if (ExcelSupport.isParameter(bundleData.getVariableCategory())) {
         if (StringUtils.isNotBlank(bundleData.getKeyCategoryUuid())) {
            Variable variable = null;
            VariableCategory variableCategoryByUUID = this.excelSupport.findVariableCategoryByUUID(bundleData.getKeyCategoryUuid());
            if (variableCategoryByUUID != null) {
               variable = (Variable)variableCategoryByUUID.getVariableNames().get(bundleData.getVariableName());
            }

            if (variableCategoryByUUID != null && variable != null) {
               predefineName = "参数." + bundleData.getKeyName() + "." + variable.getLabel();
            } else {
               predefineName = "参数." + bundleData.getVariableLabel();
            }
         } else {
            predefineName = "参数." + bundleData.getVariableLabel();
         }
      } else {
         predefineName = bundleData.getVariableCategory() + "." + bundleData.getVariableLabel();
      }

      return predefineName;
   }
}
