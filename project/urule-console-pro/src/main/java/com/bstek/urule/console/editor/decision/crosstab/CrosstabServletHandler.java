package com.bstek.urule.console.editor.decision.crosstab;

import com.bstek.urule.console.ApiServletHandler;
import com.bstek.urule.console.ContextHolder;
import com.bstek.urule.console.InfoException;
import com.bstek.urule.console.database.manager.file.FileManager;
import com.bstek.urule.console.database.manager.project.ProjectManager;
import com.bstek.urule.console.database.model.Project;
import com.bstek.urule.console.database.model.RuleFile;
import com.bstek.urule.console.editor.ExcelImportUtils;
import com.bstek.urule.console.editor.decision.CellContent;
import com.bstek.urule.console.util.FileUtils;
import com.bstek.urule.model.crosstab.CrosstabDefinition;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.NumberToTextConverter;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFComment;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class CrosstabServletHandler extends ApiServletHandler {
   public void excel(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      InputStream var3 = FileUtils.uploadFile(var1).getInputStream();
      List var4 = ExcelImportUtils.parseSheets(var3);
      CrossData var5 = this.a((XSSFSheet)var4.get(0));
      var3.close();
      CrossTableXmlBuilder var6 = new CrossTableXmlBuilder(var5);
      CrosstabDefinition var7 = var6.doBuild();
      this.a(var2, var7);
   }

   public void doExport(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      long var3 = Long.parseLong(var1.getParameter("id"));
      RuleFile var5 = FileManager.ins.get(var3);
      Project var6 = ProjectManager.ins.get(var5.getProjectId());
      ContextHolder.setGroupId(var6.getGroupId());
      ContextHolder.setProjectId(var6.getId());
      var5.setContent(FileManager.ins.loadContent(var3));
      String var7 = FileManager.ins.loadContent(var3);
      CrosstabExcelBuilder var8 = new CrosstabExcelBuilder();
      CrosstabDefinition var9 = var8.buildTable(var7);
      SimpleDateFormat var10 = new SimpleDateFormat("yyyyMMddHHmmss");
      String var11 = var5.getName() + "-" + var10.format(new Date()) + ".xlsx";
      var2.setContentType("application/octet-stream;charset=ISO8859-1");
      var11 = new String(var11.getBytes("UTF-8"), "ISO8859-1");
      var2.setHeader("Content-Disposition", "attachment;filename=\"" + var11 + "\"");
      ServletOutputStream var12 = var2.getOutputStream();
      var8.buildExcel(var9, var12);
      var12.flush();
      var12.close();
   }

   private CrossData a(XSSFSheet var1) throws Exception {
      XSSFWorkbook var2 = var1.getWorkbook();
      List var3 = ExcelImportUtils.parsePredefines(var2);
      Map var4 = ExcelImportUtils.parseProperties(var2);
      ArrayList var5 = new ArrayList();
      ArrayList var6 = new ArrayList();
      XSSFSheet var7 = ExcelImportUtils.findDataSheet(var2);
      CrossHeader var8 = this.b(var7);
      XSSFRow var9 = var7.getRow(0);
      short var10 = var9.getLastCellNum();
      XSSFRow var11 = var7.getRow(var8.getRowSpan());
      String var12 = "predefine:";
      String var13 = "预定义变量:";

      for(int var14 = 0; var14 < var10; ++var14) {
         CrossColumn var15 = new CrossColumn();
         var15.setNumber(var14 + 1);
         if (var14 < var8.getColSpan()) {
            var15.setType(Type.left);
            XSSFCell var16 = var11.getCell(var14);
            XSSFComment var17 = var16.getCellComment();
            if (var17 != null) {
               String var18 = var17.getString().toString().toLowerCase().trim();
               if (var18.startsWith(var12) || var18.startsWith(var13)) {
                  var15.setPredefine(true);
                  var18 = var18.substring(var12.length());
               }

               var15.setContent(var18);
            }
         } else {
            var15.setType(Type.top);
         }

         var6.add(var15);
      }

      int var23 = var7.getLastRowNum();

      for(int var24 = 0; var24 <= var23; ++var24) {
         XSSFRow var26 = var7.getRow(var24);
         CrossRow var29 = new CrossRow();
         var29.setNumber(var24 + 1);
         if (var24 < var8.getRowSpan()) {
            var29.setType(Type.top);
            XSSFCell var31 = var26.getCell(var8.getColSpan());
            XSSFComment var19 = var31.getCellComment();
            if (var19 != null) {
               String var20 = var19.getString().toString().toLowerCase().trim();
               if (var20.startsWith(var12) || var20.startsWith(var13)) {
                  var29.setPredefine(true);
                  var20 = var20.substring(var12.length());
               }

               var29.setContent(var20);
            }
         } else {
            var29.setType(Type.left);
         }

         var5.add(var29);
      }

      ArrayList var25 = new ArrayList();

      for(int var27 = 0; var27 <= var23; ++var27) {
         XSSFRow var30 = var7.getRow(var27);

         for(int var32 = 0; var32 < var10; ++var32) {
            if (var27 != 0 || var32 != 0) {
               XSSFCell var33 = var30.getCell(var32);
               if (var33 != null) {
                  Span var34 = this.a(var27, var32, var7);
                  if (var34 != null) {
                     String var21 = this.a(var33);
                     CellContent var22 = new CellContent();
                     var22.setCol(var32 + 1);
                     var22.setRow(var27 + 1);
                     var22.setContent(var21);
                     if (var27 < var8.getRowSpan()) {
                        var22.setType("condition");
                        var22.setSpan(var34.getCol());
                     }

                     if (var32 < var8.getColSpan()) {
                        var22.setType("condition");
                        var22.setSpan(var34.getRow());
                     }

                     var25.add(var22);
                  }
               }
            }
         }
      }

      var7.getWorkbook().close();
      CrossData var28 = new CrossData();
      var28.setProperties(var4);
      var28.setCells(var25);
      var28.setColumns(var6);
      var28.setRows(var5);
      var28.setHeader(var8);
      var28.setPredefineRows(var3);
      return var28;
   }

   private CrossHeader b(XSSFSheet var1) {
      Span var2 = this.a(0, 0, var1);
      if (var2 == null) {
         throw new InfoException("导入的Excel不合法!");
      } else {
         CrossHeader var3 = new CrossHeader();
         var3.setRowSpan(var2.getRow());
         var3.setColSpan(var2.getCol());
         XSSFRow var4 = var1.getRow(0);
         XSSFCell var5 = var4.getCell(0);
         if (var5 == null) {
            throw new InfoException("导入的Excel表头不合法!");
         } else {
            var3.setContent(this.a(var5));
            return var3;
         }
      }
   }

   private Span a(int var1, int var2, XSSFSheet var3) {
      for(CellRangeAddress var6 : var3.getMergedRegions()) {
         if (var6.getFirstColumn() == var2 && var6.getFirstRow() == var1) {
            int var7 = var6.getLastRow() - var6.getFirstRow();
            if (var7 > 0) {
               ++var7;
            }

            Span var8 = new Span();
            var8.setRow(var7);
            int var9 = var6.getLastColumn() - var6.getFirstColumn();
            if (var9 > 0) {
               ++var9;
            }

            var8.setCol(var9);
            return var8;
         }

         if (var2 >= var6.getFirstColumn() && var2 <= var6.getLastColumn() && var1 >= var6.getFirstRow() && var1 <= var6.getLastRow()) {
            return null;
         }
      }

      Span var10 = new Span();
      var10.setRow(1);
      var10.setCol(1);
      return var10;
   }

   private String a(XSSFCell var1) {
      String var2 = null;
      CellType var3 = var1.getCellTypeEnum();
      switch (var3) {
         case STRING:
            var2 = var1.getStringCellValue();
            break;
         case BOOLEAN:
            var2 = String.valueOf(var1.getBooleanCellValue());
            break;
         case NUMERIC:
            var2 = String.valueOf(NumberToTextConverter.toText(var1.getNumericCellValue()));
         case _NONE:
         case BLANK:
         case ERROR:
         case FORMULA:
      }

      return var2;
   }

   public String url() {
      return "/crosstab";
   }
}
