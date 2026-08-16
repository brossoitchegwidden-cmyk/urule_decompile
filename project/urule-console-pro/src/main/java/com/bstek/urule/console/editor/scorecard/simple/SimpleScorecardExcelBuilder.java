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
   private ExcelSupport a = new ExcelSupport();
   private ScorecardDeserializer b = (ScorecardDeserializer)Utils.getApplicationContext().getBean("urule.scorecardDeserializer");

   public ScorecardDefinition buildTable(RuleFile var1) {
      try {
         Document var2 = DocumentHelper.parseText(var1.getContent());
         return this.b.deserialize(var2.getRootElement());
      } catch (Exception var3) {
         throw new DeserializeException(var3);
      }
   }

   public void buildExcel(ScorecardDefinition var1, ServletOutputStream var2) throws IOException {
      SXSSFWorkbook var3 = new SXSSFWorkbook(100000);
      SXSSFSheet var4 = var3.createSheet();
      Row var5 = var4.createRow(0);
      CellStyle var6 = var3.createCellStyle();
      var6.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
      var6.setFillPattern(FillPatternType.SOLID_FOREGROUND);
      var6.setBorderBottom(BorderStyle.THIN);
      var6.setBorderLeft(BorderStyle.THIN);
      var6.setBorderRight(BorderStyle.THIN);
      var6.setBorderTop(BorderStyle.THIN);
      Font var7 = var3.createFont();
      var7.setBold(true);
      var6.setFont(var7);
      Cell var8 = var5.createCell(0);
      var8.setCellStyle(var6);
      var8.setCellValue(var1.getAttributeColVariableCategory());
      this.a(var4, 0, var1.getAttributeColWidth());
      SXSSFDrawing var9 = (SXSSFDrawing)var4.createDrawingPatriarch();
      if (var1.isWeightSupport()) {
         XSSFComment var10 = (XSSFComment)var9.createCellComment(new XSSFClientAnchor(0, 0, 0, 0, 0, 0, 2, 2));
         var10.setString("WeightSupport");
         var8.setCellComment(var10);
      }

      var8 = var5.createCell(1);
      var8.setCellStyle(var6);
      var8.setCellValue(var1.getConditionColName());
      this.a(var4, 1, var1.getConditionColWidth());
      var8 = var5.createCell(2);
      var8.setCellStyle(var6);
      var8.setCellValue(var1.getScoreColName());
      this.a(var4, 2, var1.getScoreColWidth());
      if (var1.getCustomCols().size() > 0) {
         for(CustomCol var11 : var1.getCustomCols()) {
            var8 = var5.createCell(var11.getColNumber() - 1);
            var8.setCellStyle(var6);
            var8.setCellValue(var11.getName());
            this.a(var4, 2, var11.getWidth());
            XSSFComment var12 = (XSSFComment)var9.createCellComment(new XSSFClientAnchor(0, 0, 0, 0, var11.getColNumber() - 1, 0, var11.getColNumber() + 1, 2));
            var12.setString("custom");
            var8.setCellComment(var12);
         }
      }

      CellStyle var28 = var3.createCellStyle();
      var28.setVerticalAlignment(VerticalAlignment.CENTER);
      var28.setBorderBottom(BorderStyle.THIN);
      var28.setBorderLeft(BorderStyle.THIN);
      var28.setBorderRight(BorderStyle.THIN);
      var28.setBorderTop(BorderStyle.THIN);
      var28.setWrapText(true);
      int var29 = 0;

      for(AttributeRow var13 : var1.getRows()) {
         int var14 = var13.getRowNumber();
         Row var15 = var4.createRow(var14 - 1);
         if (var1.isWeightSupport()) {
            var15.setHeight((new Integer(1200)).shortValue());
         }

         CardCell var16 = this.a(var1, var14, 1);
         Cell var17 = var15.createCell(0);
         var17.setCellStyle(var28);
         Object var18 = null;
         String var32;
         if (ExcelSupport.isParameter(var1.getAttributeColVariableCategory()) && StringUtils.isNotBlank(var16.getKeyLabel())) {
            var32 = var16.getKeyLabel();
            if (StringUtils.isNotBlank(var16.getKeyUuid())) {
               Variable var19 = this.a.findParameterByUuid(var16.getKeyUuid());
               if (var19 != null) {
                  VariableCategory var20 = this.a.findVariableCategoryByUUID(var19.getDataType());
                  List var21 = var20.getVariables();
                  Variable var22 = null;

                  for(Variable var24 : (Iterable<Variable>)(Iterable<?>)(var21)) {
                     if (var24.getUuid().equals(var16.getUuid())) {
                        var22 = var24;
                     }
                  }

                  if (var22 != null) {
                     var32 = var16.getKeyLabel() + "." + var22.getLabel();
                  }
               }
            }
         } else {
            var32 = var16.getVariableLabel();
         }

         if (var1.isWeightSupport()) {
            var32 = var32 + " \n权重:" + var16.getWeight();
         }

         var17.setCellValue(var32);
         int var33 = this.a(var1, var29);
         if (var33 > 1) {
            int var34 = var14 - 1;
            int var36 = var34 + var33 - 1;
            CellRangeAddress var37 = new CellRangeAddress(var34, var36, 0, 0);
            var4.addMergedRegion(var37);
            RegionUtil.setBorderTop(BorderStyle.THIN, var37, var4);
            RegionUtil.setBorderBottom(BorderStyle.THIN, var37, var4);
            RegionUtil.setBorderLeft(BorderStyle.THIN, var37, var4);
            RegionUtil.setBorderRight(BorderStyle.THIN, var37, var4);
         }

         this.a(var1, var8, var28, var14, var15);
         if (var33 > 1) {
            for(int var35 = 0; var35 < var33 - 1; ++var35) {
               var15 = var4.createRow(var14 + var35);
               this.a(var1, var8, var28, var14 + var35 + 1, var15);
            }
         }

         ++var29;
      }

      ExcelExportUtils.exportProperties(var3, var1);
      var3.write(var2);
   }

   private int a(ScorecardDefinition var1, int var2) {
      List var3 = var1.getRows();
      int var4 = ((AttributeRow)var3.get(var2)).getRowNumber();
      return var3.size() > var2 + 1 ? ((AttributeRow)var3.get(var2 + 1)).getRowNumber() - var4 : ((CardCell)var1.getCells().get(var1.getCells().size() - 1)).getRow() - var4 + 1;
   }

   private void a(ScorecardDefinition var1, Cell var2, CellStyle var3, int var4, Row var5) {
      CardCell var6 = this.a(var1, var4, 2);
      Cell var7 = var5.createCell(1);
      var7.setCellStyle(var3);
      if (var6.getJoint() != null && var6.getJoint().getConditions() != null && var6.getJoint().getConditions().size() != 0) {
         String var8 = ExcelExportUtils.conditions2Label(this.a, var6.getJoint().getConditions(), var6.getJoint().getType());
         var7.setCellValue(var8);
      } else {
         var2.setCellValue("");
      }

      var6 = this.a(var1, var4, 3);
      var7 = var5.createCell(2);
      var7.setCellStyle(var3);
      var7.setCellValue(ExcelExportUtils.getLabelValue(this.a, (Condition)null, var6.getValue()));
      if (var1.getCustomCols().size() > 0) {
         for(CustomCol var9 : var1.getCustomCols()) {
            var6 = this.a(var1, var4, var9.getColNumber());
            var7 = var5.createCell(var9.getColNumber() - 1);
            var7.setCellStyle(var3);
            var7.setCellValue(ExcelExportUtils.getLabelValue(this.a, (Condition)null, var6.getValue()));
         }
      }

   }

   private CardCell a(ScorecardDefinition var1, int var2, int var3) {
      CardCell var4 = null;

      for(CardCell var6 : var1.getCells()) {
         if (var6.getRow() == var2 && var6.getCol() == var3) {
            var4 = var6;
            break;
         }
      }

      return var4;
   }

   private void a(Sheet var1, int var2, String var3) {
      int var4 = (new Double(Double.parseDouble(var3))).intValue();
      Double var5 = (double)var4 * 1.3281472327365;
      Double var6 = (double)256.0F * (var5 / (double)7.0F);
      var1.setColumnWidth(var2, Math.min(var6.intValue(), 65280));
   }
}
