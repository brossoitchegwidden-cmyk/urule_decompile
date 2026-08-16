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
   private ExcelSupport a = new ExcelSupport();
   private DecisionTableDeserializer b = (DecisionTableDeserializer)Utils.getApplicationContext().getBean("urule.decisionTableDeserializer");

   public DecisionTable buildTable(String var1) {
      try {
         Document var2 = DocumentHelper.parseText(var1);
         return this.b.deserialize(var2.getRootElement());
      } catch (Exception var3) {
         throw new InfoException(var3);
      }
   }

   public void buildExcel(DecisionTable var1, OutputStream var2) throws IOException {
      SXSSFWorkbook var3 = new SXSSFWorkbook(100000);
      SXSSFSheet var4 = var3.createSheet();
      int var5 = 0;
      SXSSFRow var6 = var4.createRow(0);
      CellStyle var7 = var3.createCellStyle();
      var7.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
      var7.setFillPattern(FillPatternType.SOLID_FOREGROUND);
      var7.setBorderBottom(BorderStyle.THIN);
      var7.setBorderLeft(BorderStyle.THIN);
      var7.setBorderRight(BorderStyle.THIN);
      var7.setBorderTop(BorderStyle.THIN);
      Font var8 = var3.createFont();
      var8.setBold(true);
      var7.setFont(var8);
      CellStyle var9 = var3.createCellStyle();
      var9.setFillForegroundColor(IndexedColors.ORCHID.getIndex());
      var9.setFillPattern(FillPatternType.SOLID_FOREGROUND);
      var9.setBorderBottom(BorderStyle.THIN);
      var9.setBorderLeft(BorderStyle.THIN);
      var9.setBorderRight(BorderStyle.THIN);
      var9.setBorderTop(BorderStyle.THIN);
      var8 = var3.createFont();
      var8.setBold(true);
      var9.setFont(var8);
      CellStyle var10 = var3.createCellStyle();
      var10.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
      var10.setFillPattern(FillPatternType.SOLID_FOREGROUND);
      var10.setBorderBottom(BorderStyle.THIN);
      var10.setBorderLeft(BorderStyle.THIN);
      var10.setBorderRight(BorderStyle.THIN);
      var10.setBorderTop(BorderStyle.THIN);
      var8 = var3.createFont();
      var8.setBold(true);
      var10.setFont(var8);
      CellStyle var11 = var3.createCellStyle();
      var11.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());
      var11.setFillPattern(FillPatternType.SOLID_FOREGROUND);
      var11.setBorderBottom(BorderStyle.THIN);
      var11.setBorderLeft(BorderStyle.THIN);
      var11.setBorderRight(BorderStyle.THIN);
      var11.setBorderTop(BorderStyle.THIN);
      var8 = var3.createFont();
      var8.setBold(true);
      var8.setColor(IndexedColors.WHITE.getIndex());
      var11.setFont(var8);
      CellStyle var12 = var3.createCellStyle();
      var12.setVerticalAlignment(VerticalAlignment.CENTER);
      var12.setBorderBottom(BorderStyle.THIN);
      var12.setBorderLeft(BorderStyle.THIN);
      var12.setBorderRight(BorderStyle.THIN);
      var12.setBorderTop(BorderStyle.THIN);
      var12.setWrapText(true);
      SXSSFDrawing var13 = var4.createDrawingPatriarch();

      for(Column var15 : var1.getColumns()) {
         double var16 = (double)var15.getWidth() * 1.3281472327365;
         Double var18 = (double)256.0F * (var16 / (double)7.0F);
         Cell var19 = var6.createCell(var5);
         if (ColumnType.ExecuteMethod == var15.getType()) {
            var19.setCellStyle(var9);
            var19.setCellValue("ExecuteMethod");
            XSSFComment var20 = (XSSFComment)var13.createCellComment(new XSSFClientAnchor(0, 0, 100, 100, (short)var15.getNum(), 0, (short)var15.getNum() + 1, 2));
            var20.setString("执行方法");
            var19.setCellComment(var20);
         } else if (ColumnType.ConsolePrint == var15.getType()) {
            var19.setCellStyle(var7);
            var19.setCellValue("控制台输出");
            XSSFComment var32 = (XSSFComment)var13.createCellComment(new XSSFClientAnchor(0, 0, 100, 100, (short)var15.getNum(), 0, (short)var15.getNum() + 1, 2));
            var32.setString("out");
            var19.setCellComment(var32);
         } else if (ColumnType.Assignment == var15.getType()) {
            var19.setCellStyle(var10);
            if (var15.isPredefine()) {
               String var33 = var15.getPredefineName();
               if (StringUtils.isNotBlank(var15.getPredefinePropertyLabel())) {
                  var33 = var33 + "." + var15.getPredefinePropertyLabel();
               }

               var19.setCellValue(var33);
               XSSFComment var21 = (XSSFComment)var13.createCellComment(new XSSFClientAnchor(0, 0, 100, 100, (short)var15.getNum(), 0, (short)var15.getNum() + 1, 2));
               var21.setString("赋值:预定义变量");
               var19.setCellComment(var21);
            } else {
               if (ExcelSupport.isParameter(var15.getVariableCategory())) {
                  var19.setCellValue(this.a(var15));
               } else {
                  var19.setCellValue(var15.getVariableCategory() + "." + var15.getVariableLabel());
               }

               XSSFComment var34 = (XSSFComment)var13.createCellComment(new XSSFClientAnchor(0, 0, 0, 0, (short)var15.getNum(), 0, (short)var15.getNum() + 1, 2));
               var34.setString("赋值");
               var19.setCellComment(var34);
            }
         } else {
            var19.setCellStyle(var11);
            if (ExcelSupport.isParameter(var15.getVariableCategory())) {
               var19.setCellValue(this.a(var15));
            } else {
               var19.setCellValue(var15.getVariableCategory() + "." + var15.getVariableLabel());
            }

            if (var15.isPredefine()) {
               String var35 = var15.getPredefineName();
               if (StringUtils.isNotBlank(var15.getPredefinePropertyLabel())) {
                  var35 = var35 + "." + var15.getPredefinePropertyLabel();
               }

               var19.setCellValue(var35);
               XSSFComment var38 = (XSSFComment)var13.createCellComment(new XSSFClientAnchor(0, 0, 100, 100, (short)var15.getNum(), 0, (short)var15.getNum() + 1, 2));
               var38.setString("条件:预定义变量");
               var19.setCellComment(var38);
            } else {
               XSSFComment var36 = (XSSFComment)var13.createCellComment(new XSSFClientAnchor(0, 0, 100, 100, (short)var15.getNum(), 0, (short)var15.getNum() + 1, 2));
               var36.setString("条件");
               var19.setCellComment(var36);
            }
         }

         var4.setColumnWidth(var5++, Math.min(var18.intValue(), 65280));
      }

      int var27 = 0;

      for(Row var29 : var1.getRows()) {
         SXSSFRow var17 = var4.createRow(var27 + 1);
         var17.setHeight((short)(var29.getHeight() * 20));
         var5 = 0;

         for(Column var31 : var1.getColumns()) {
            com.bstek.urule.model.table.Cell var37 = (com.bstek.urule.model.table.Cell)var1.getCellMap().get(var27 + "," + var5);
            if (var37 != null) {
               if (var37.getRowspan() > 1) {
                  CellRangeAddress var39 = new CellRangeAddress(var37.getRow() + 1, var37.getRow() + var37.getRowspan(), var31.getNum(), var31.getNum());
                  var4.addMergedRegion(var39);
                  RegionUtil.setBorderTop(BorderStyle.THIN, var39, var4);
                  RegionUtil.setBorderBottom(BorderStyle.THIN, var39, var4);
                  RegionUtil.setBorderLeft(BorderStyle.THIN, var39, var4);
                  RegionUtil.setBorderRight(BorderStyle.THIN, var39, var4);
               }

               Cell var40 = var17.createCell(var5);
               var40.setCellStyle(var12);
               if (ColumnType.Criteria == var31.getType()) {
                  if (var37.getJoint() == null || var37.getJoint().getConditions() == null || var37.getJoint().getConditions().size() == 0) {
                     var40.setCellValue("");
                     ++var5;
                     continue;
                  }

                  String var22 = ExcelExportUtils.conditions2Label(this.a, var37.getJoint().getConditions(), var37.getJoint().getType());
                  var40.setCellValue(var22);
               } else if (ColumnType.Assignment == var31.getType()) {
                  if (var37.getValue() != null) {
                     var40.setCellValue(ExcelExportUtils.getLabelValue(this.a, (Condition)null, var37.getValue()));
                  }
               } else if (ColumnType.ConsolePrint == var31.getType()) {
                  if (var37.getValue() != null) {
                     var40.setCellValue(ExcelExportUtils.getLabelValue(this.a, (Condition)null, var37.getValue()));
                  }
               } else if (ColumnType.ExecuteMethod == var31.getType() && var37.getAction() != null) {
                  var40.setCellValue(this.a(var37.getAction()));
               }
            }

            ++var5;
         }

         ++var27;
      }

      if (var1.getPredefineGroup() != null && var1.getPredefineGroup().getPredefines().size() > 0) {
         ExcelExportUtils.exportPredefine(var3, var1.getPredefineGroup(), this.a);
      }

      ExcelExportUtils.exportProperties(var3, var1);
      var3.write(var2);
   }

   private String a(Column var1) {
      if (StringUtils.isNotBlank(var1.getKeyCategoryUuid())) {
         Variable var2 = null;
         VariableCategory var3 = this.a.findVariableCategoryByUUID(var1.getKeyCategoryUuid());
         if (var3 != null) {
            var2 = (Variable)var3.getVariableNames().get(var1.getVariableName());
         }

         if (var2 != null) {
            return "参数." + var1.getKeyLabel() + "." + var2.getLabel();
         }
      }

      return "参数." + var1.getVariableLabel();
   }

   private String a(Action var1) {
      String var2 = "";
      if (var1 instanceof ConsolePrintAction) {
         ConsolePrintAction var3 = (ConsolePrintAction)var1;
         var2 = var3.getValue() != null ? var3.getValue().toString() : "";
      } else if (var1 instanceof ExecuteCommonFunctionAction) {
         ExecuteCommonFunctionAction var5 = (ExecuteCommonFunctionAction)var1;
         var2 = var5.getLabel();
      } else if (var1 instanceof ExecuteMethodAction) {
         ExecuteMethodAction var6 = (ExecuteMethodAction)var1;
         var2 = var6.getBeanLabel() + "." + var6.getMethodLabel();
      } else if (var1 instanceof ScoringAction) {
         ScoringAction var7 = (ScoringAction)var1;
         var2 = var7.getName();
      } else if (var1 instanceof ScoringAction) {
         ScoringAction var8 = (ScoringAction)var1;
         var2 = var8.getName();
      } else if (var1 instanceof TemplateAction) {
         TemplateAction var9 = (TemplateAction)var1;
         var2 = var9.getName();
      } else if (var1 instanceof VariableAssignAction) {
         VariableAssignAction var10 = (VariableAssignAction)var1;
         var2 = var10.getVariableCategory() + "." + var10.getVariableLabel();
      } else {
         var2 = "";
      }

      return var2;
   }
}
