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
   private ExcelSupport a = new ExcelSupport();
   private ComplexScorecardDeserializer b = (ComplexScorecardDeserializer)Utils.getApplicationContext().getBean("urule.complexScorecardDeserializer");

   public ComplexScorecardDefinition buildTable(RuleFile var1) {
      try {
         Document var2 = DocumentHelper.parseText(var1.getContent());
         return this.b.deserialize(var2.getRootElement());
      } catch (Exception var3) {
         throw new DeserializeException(var3);
      }
   }

   public void buildExcel(ComplexScorecardDefinition var1, ServletOutputStream var2) throws IOException {
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
      SXSSFDrawing var8 = (SXSSFDrawing)var4.createDrawingPatriarch();

      for(ComplexColumn var10 : var1.getColumns()) {
         Cell var11 = var5.createCell(var10.getNum());
         var11.setCellStyle(var6);
         if (ComplexColumnType.Criteria == var10.getType()) {
            var11.setCellValue(var10.getVariableCategory());
         } else if (ComplexColumnType.Score == var10.getType()) {
            var11.setCellValue("分值");
         } else if (ComplexColumnType.Custom == var10.getType()) {
            var11.setCellValue(var10.getCustomLabel());
         }

         this.a(var4, var10.getNum(), String.valueOf(var10.getWidth()));
         if (ComplexColumnType.Custom == var10.getType()) {
            XSSFComment var12 = (XSSFComment)var8.createCellComment(new XSSFClientAnchor(0, 0, 0, 0, var10.getNum(), 0, var10.getNum() + 1, 3));
            var12.setString("custom");
            var11.setCellComment(var12);
         }
      }

      CellStyle var20 = var3.createCellStyle();
      var20.setVerticalAlignment(VerticalAlignment.CENTER);
      var20.setBorderBottom(BorderStyle.THIN);
      var20.setBorderLeft(BorderStyle.THIN);
      var20.setBorderRight(BorderStyle.THIN);
      var20.setBorderTop(BorderStyle.THIN);
      var20.setWrapText(true);

      for(com.bstek.urule.model.table.Row var22 : var1.getRows()) {
         int var23 = var22.getNum();
         Row var13 = var4.createRow(var23 + 1);
         var13.setHeight((new Integer(var22.getHeight() * 20)).shortValue());

         for(ComplexColumn var15 : var1.getColumns()) {
            com.bstek.urule.model.table.Cell var16 = (com.bstek.urule.model.table.Cell)var1.getCellMap().get(var23 + "," + var15.getNum());
            if (var16 != null) {
               if (var16.getRowspan() > 1) {
                  CellRangeAddress var17 = new CellRangeAddress(var23 + 1, var23 + var16.getRowspan(), var15.getNum(), var15.getNum());
                  var4.addMergedRegion(var17);
                  RegionUtil.setBorderTop(BorderStyle.THIN, var17, var4);
                  RegionUtil.setBorderBottom(BorderStyle.THIN, var17, var4);
                  RegionUtil.setBorderLeft(BorderStyle.THIN, var17, var4);
                  RegionUtil.setBorderRight(BorderStyle.THIN, var17, var4);
               }

               Cell var24 = var13.createCell(var15.getNum());
               var24.setCellStyle(var20);
               if (ComplexColumnType.Criteria == var15.getType()) {
                  if (var16.getJoint() != null && var16.getJoint().getConditions() != null && var16.getJoint().getConditions().size() != 0) {
                     String var18 = ExcelExportUtils.conditions2Label(this.a, var16.getJoint().getConditions(), var16.getJoint().getType());
                     String var19 = null;
                     if (StringUtils.isNotBlank(var16.getKeyLabel())) {
                        var19 = var16.getKeyLabel() + "." + var16.getVariableLabel();
                     } else {
                        var19 = var16.getVariableLabel();
                     }

                     var24.setCellValue(var19 + "\n" + var18);
                  } else {
                     var24.setCellValue("");
                  }
               } else if (ComplexColumnType.Score == var15.getType()) {
                  var24.setCellValue(ExcelExportUtils.getLabelValue(this.a, (Condition)null, var16.getValue()));
               } else {
                  var24.setCellValue(ExcelExportUtils.getLabelValue(this.a, (Condition)null, var16.getValue()));
               }
            }
         }
      }

      ExcelExportUtils.exportProperties(var3, var1);
      var3.write(var2);
   }

   private void a(Sheet var1, int var2, String var3) {
      int var4 = (new Double(Double.parseDouble(var3))).intValue();
      Double var5 = (double)var4 * 1.3281472327365;
      Double var6 = (double)256.0F * (var5 / (double)7.0F);
      var1.setColumnWidth(var2, Math.min(var6.intValue(), 65280));
   }
}
