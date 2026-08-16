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
   private ExcelSupport b = new ExcelSupport();
   private CrosstableDeserializer c = (CrosstableDeserializer)Utils.getApplicationContext().getBean("urule.crosstableDeserializer");
   String a = "predefine";

   public CrosstabDefinition buildTable(String var1) {
      try {
         Document var2 = DocumentHelper.parseText(var1);
         return this.c.deserialize(var2.getRootElement());
      } catch (Exception var3) {
         throw new InfoException(var3);
      }
   }

   private CrossCell a(List var1, int var2, int var3) {
      CrossCell var4 = null;

      for(CrossCell var6 : (Iterable<CrossCell>)(Iterable<?>)(var1)) {
         if (var6.getCol() == var3 + 1 && var6.getRow() == var2 + 1) {
            var4 = var6;
            break;
         }
      }

      return var4;
   }

   public void buildExcel(CrosstabDefinition var1, OutputStream var2) throws IOException {
      SXSSFWorkbook var3 = new SXSSFWorkbook(100000);
      SXSSFSheet var4 = var3.createSheet();
      CellStyle var5 = var3.createCellStyle();
      var5.setBorderBottom(BorderStyle.THIN);
      var5.setBorderLeft(BorderStyle.THIN);
      var5.setBorderRight(BorderStyle.THIN);
      var5.setBorderTop(BorderStyle.THIN);
      CellStyle var6 = var3.createCellStyle();
      var6.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
      var6.setFillPattern(FillPatternType.SOLID_FOREGROUND);
      var6.setAlignment(HorizontalAlignment.CENTER);
      var6.setVerticalAlignment(VerticalAlignment.CENTER);
      var6.setBorderBottom(BorderStyle.THIN);
      var6.setBorderLeft(BorderStyle.THIN);
      var6.setBorderRight(BorderStyle.THIN);
      var6.setBorderTop(BorderStyle.THIN);
      Font var7 = var3.createFont();
      var7.setColor(IndexedColors.DARK_GREEN.index);
      var6.setFont(var7);
      CellStyle var8 = var3.createCellStyle();
      var8.setVerticalAlignment(VerticalAlignment.CENTER);
      var8.setBorderBottom(BorderStyle.THIN);
      var8.setBorderLeft(BorderStyle.THIN);
      var8.setBorderRight(BorderStyle.THIN);
      var8.setBorderTop(BorderStyle.THIN);
      var8.setWrapText(true);
      Row var9 = var4.createRow(0);
      HeaderCell var10 = var1.getHeaderCell();
      if (var10.getRowspan() > 1 || var10.getColspan() > 1) {
         CellRangeAddress var11 = new CellRangeAddress(0, var10.getRowspan() - 1, 0, var10.getColspan() - 1);
         var4.addMergedRegion(var11);
         RegionUtil.setBorderTop(BorderStyle.THIN, var11, var4);
         RegionUtil.setBorderBottom(BorderStyle.THIN, var11, var4);
         RegionUtil.setBorderLeft(BorderStyle.THIN, var11, var4);
         RegionUtil.setBorderRight(BorderStyle.THIN, var11, var4);
      }

      Cell var30 = var9.createCell(0);
      var30.setCellStyle(var6);
      var30.setCellValue(var10.getText());
      double var12 = 398.44416982095004;
      Double var14 = (double)256.0F * (var12 / (double)7.0F);
      var4.setColumnWidth(0, Math.min(var14.intValue(), 65280));
      SXSSFDrawing var15 = (SXSSFDrawing)var4.createDrawingPatriarch();
      ArrayList var16 = new ArrayList();

      for(int var17 = 0; var17 < var1.getRows().size(); ++var17) {
         Row var18 = var9;
         if (var17 > 0) {
            var18 = var4.createRow(var17);
         }

         var18.setHeight((short)1200);
         com.bstek.urule.model.crosstab.CrossRow var19 = (com.bstek.urule.model.crosstab.CrossRow)var1.getRows().get(var17);

         for(int var20 = 0; var20 < var1.getColumns().size(); ++var20) {
            com.bstek.urule.model.crosstab.CrossColumn var21 = (com.bstek.urule.model.crosstab.CrossColumn)var1.getColumns().get(var20);
            CrossCell var22 = this.a(var1.getCells(), var17, var20);
            if (var22 != null) {
               if (var22.getRowspan() > 1 || var22.getColspan() > 1) {
                  int var23 = var22.getRowspan() > 1 ? var22.getRow() + var22.getRowspan() - 1 : var22.getRow();
                  int var24 = var22.getColspan() > 1 ? var22.getCol() + var22.getColspan() - 1 : var22.getCol() - 1;
                  if (var23 > var22.getRow() || var24 > var22.getCol() - 1) {
                     if (var21 instanceof TopColumn) {
                        CellRangeAddress var25 = new CellRangeAddress(var22.getRow() - 1, var23 - 1, var22.getCol() - 1, var24 - 1);
                        var4.addMergedRegion(var25);
                        RegionUtil.setBorderTop(BorderStyle.THIN, var25, var4);
                        RegionUtil.setBorderBottom(BorderStyle.THIN, var25, var4);
                        RegionUtil.setBorderLeft(BorderStyle.THIN, var25, var4);
                        RegionUtil.setBorderRight(BorderStyle.THIN, var25, var4);
                     } else {
                        CellRangeAddress var34 = new CellRangeAddress(var22.getRow() - 1, var23 - 1, var22.getCol() - 1, var24);
                        var4.addMergedRegion(var34);
                        RegionUtil.setBorderTop(BorderStyle.THIN, var34, var4);
                        RegionUtil.setBorderBottom(BorderStyle.THIN, var34, var4);
                        RegionUtil.setBorderLeft(BorderStyle.THIN, var34, var4);
                        RegionUtil.setBorderRight(BorderStyle.THIN, var34, var4);
                     }
                  }
               }

               Cell var31 = var18.createCell(var20);
               if (var20 > 0) {
                  var4.setColumnWidth(var20, Math.min(var14.intValue() / 2, 65280));
               }

               if (var22 instanceof ConditionCrossCell) {
                  var31.setCellStyle(var6);
                  ConditionCrossCell var32 = (ConditionCrossCell)var22;
                  if (var32.getJoint() != null && var32.getJoint().getConditions() != null && var32.getJoint().getConditions().size() != 0) {
                     String var35 = ExcelExportUtils.conditions2Label(this.b, var32.getJoint().getConditions(), var32.getJoint().getType());
                     String var26 = "";
                     String var27 = "predefine";
                     if (var19 instanceof TopRow) {
                        TopRow var28 = (TopRow)var19;
                        var26 = this.a(var28);
                        if (!var16.contains(var19.getRowNumber() + "," + var21.getColumnNumber())) {
                           XSSFComment var29 = (XSSFComment)var15.createCellComment(new XSSFClientAnchor(0, 0, 0, 0, var21.getColumnNumber() - 1, var19.getRowNumber() - 1, var21.getColumnNumber(), var19.getRowNumber()));
                           var29.setString(var26);
                           if (var27.equals(var28.getBundleDataType())) {
                              var29.setString(var28.getBundleDataType() + ":" + var26);
                           }

                           var31.setCellComment(var29);
                           var16.add(var19.getRowNumber() + "," + var21.getColumnNumber());
                        }
                     } else if (var21 instanceof LeftColumn) {
                        LeftColumn var38 = (LeftColumn)var21;
                        var26 = this.a(var38);
                        if (!var16.contains(var19.getRowNumber() + "," + var38.getColumnNumber())) {
                           XSSFComment var39 = (XSSFComment)var15.createCellComment(new XSSFClientAnchor(0, 0, 0, 0, var21.getColumnNumber() - 1, var19.getRowNumber() - 1, var21.getColumnNumber(), var19.getRowNumber()));
                           var39.setString(var26);
                           if (var27.equals(var38.getBundleDataType())) {
                              var39.setString(var38.getBundleDataType() + ":" + var26);
                           }

                           var31.setCellComment(var39);
                           var16.add(var19.getRowNumber() + "," + var21.getColumnNumber());
                        }
                     }

                     var31.setCellValue(var35);
                  } else {
                     var31.setCellValue("");
                  }
               } else if (var22 instanceof ValueCrossCell) {
                  var31.setCellStyle(var8);
                  ValueCrossCell var33 = (ValueCrossCell)var22;
                  var31.setCellValue(ExcelExportUtils.getLabelValue(this.b, (Condition)null, var33.getValue()));
               }
            }
         }
      }

      if (var1.getPredefineGroup() != null && var1.getPredefineGroup().getPredefines().size() > 0) {
         ExcelExportUtils.exportPredefine(var3, var1.getPredefineGroup(), this.b);
      }

      ExcelExportUtils.exportProperties(var3, var1);
      var3.write(var2);
   }

   private String a(BundleData var1) {
      String var2 = "";
      if (this.a.equals(var1.getBundleDataType())) {
         var2 = var1.getPredefineName();
         if (StringUtils.isNotBlank(var1.getPredefinePropertyLabel())) {
            var2 = var2 + "." + var1.getPredefinePropertyLabel();
         }
      } else if (ExcelSupport.isParameter(var1.getVariableCategory())) {
         if (StringUtils.isNotBlank(var1.getKeyCategoryUuid())) {
            Variable var3 = null;
            VariableCategory var4 = this.b.findVariableCategoryByUUID(var1.getKeyCategoryUuid());
            if (var4 != null) {
               var3 = (Variable)var4.getVariableNames().get(var1.getVariableName());
            }

            if (var4 != null && var3 != null) {
               var2 = "参数." + var1.getKeyName() + "." + var3.getLabel();
            } else {
               var2 = "参数." + var1.getVariableLabel();
            }
         } else {
            var2 = "参数." + var1.getVariableLabel();
         }
      } else {
         var2 = var1.getVariableCategory() + "." + var1.getVariableLabel();
      }

      return var2;
   }
}
