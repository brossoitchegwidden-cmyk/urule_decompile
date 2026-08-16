package com.bstek.urule.console.editor.scorecard.simple;

import com.bstek.urule.console.ApiServletHandler;
import com.bstek.urule.console.ContextHolder;
import com.bstek.urule.console.database.manager.file.FileManager;
import com.bstek.urule.console.database.manager.project.ProjectManager;
import com.bstek.urule.console.database.model.Project;
import com.bstek.urule.console.database.model.RuleFile;
import com.bstek.urule.console.editor.ExcelImportUtils;
import com.bstek.urule.console.util.FileUtils;
import com.bstek.urule.model.scorecard.ScorecardDefinition;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFComment;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;

public class SimpleScorecardServletHandler extends ApiServletHandler {
   public void excel(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      InputStream var3 = FileUtils.uploadFile(var1).getInputStream();
      List var4 = ExcelImportUtils.parseSheets(var3);
      ScoreTableData var5 = this.a((XSSFSheet)var4.get(0));
      var3.close();
      ScoreTableBuilder var6 = new ScoreTableBuilder(var5);
      ScorecardDefinition var7 = var6.buildTable();
      this.a(var2, var7);
   }

   public void doExport(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      long var3 = Long.parseLong(var1.getParameter("id"));
      RuleFile var5 = FileManager.ins.get(var3);
      var5.setContent(FileManager.ins.loadContent(var3));
      Project var6 = ProjectManager.ins.get(var5.getProjectId());
      ContextHolder.setGroupId(var6.getGroupId());
      ContextHolder.setProjectId(var6.getId());
      SimpleScorecardExcelBuilder var7 = new SimpleScorecardExcelBuilder();
      ScorecardDefinition var8 = var7.buildTable(var5);
      SimpleDateFormat var9 = new SimpleDateFormat("yyyyMMddHHmmss");
      String var10 = var5.getName() + "-" + var9.format(new Date()) + ".xlsx";
      var2.setContentType("application/octet-stream;charset=ISO8859-1");
      var10 = new String(var10.getBytes("UTF-8"), "ISO8859-1");
      var2.setHeader("Content-Disposition", "attachment;filename=\"" + var10 + "\"");
      ServletOutputStream var11 = var2.getOutputStream();
      var7.buildExcel(var8, var11);
      var11.flush();
      var11.close();
   }

   private ScoreTableData a(XSSFSheet var1) throws Exception {
      ArrayList var2 = new ArrayList();
      XSSFSheet var3 = var1.getWorkbook().getSheetAt(var1.getWorkbook().getActiveSheetIndex());
      int var4 = var3.getLastRowNum();
      List var5 = this.b(var3);
      Map var6 = ExcelImportUtils.parseProperties(var1.getWorkbook());

      for(int var7 = 1; var7 <= var4; ++var7) {
         XSSFRow var8 = var3.getRow(var7);
         RowData var9 = new RowData();
         var2.add(var9);
         ArrayList var10 = new ArrayList();
         var9.setCells(var10);

         for(int var11 = 0; var11 < var5.size(); ++var11) {
            XSSFCell var12 = var8.getCell(var11);
            if (var12 != null) {
               int var13 = this.a(var7, var11, var3);
               if (var13 != 0) {
                  TableHeader var14 = (TableHeader)var5.get(var11);
                  CellData var15 = new CellData();
                  if (var13 > 0) {
                     var15.setSpan(var13);
                  }

                  var15.setHeader(var14);
                  CellType var16 = var12.getCellTypeEnum();
                  switch (var16) {
                     case STRING:
                        var15.setContent(var12.getStringCellValue());
                        break;
                     case BOOLEAN:
                        var15.setContent(String.valueOf(var12.getBooleanCellValue()));
                        break;
                     case NUMERIC:
                        var15.setContent(String.valueOf(var12.getNumericCellValue()));
                     case _NONE:
                     case BLANK:
                     case ERROR:
                     case FORMULA:
                  }

                  var15.setRow(var7 - 1);
                  var15.setCol(var11);
                  var10.add(var15);
               }
            }
         }
      }

      var3.getWorkbook().close();
      ScoreTableData var17 = new ScoreTableData(var5, var2);
      var17.setProperties(var6);
      return var17;
   }

   private int a(int var1, int var2, XSSFSheet var3) {
      for(CellRangeAddress var6 : var3.getMergedRegions()) {
         if (var6.getFirstColumn() == var2 && var6.getFirstRow() == var1) {
            int var7 = var6.getLastRow() - var6.getFirstRow();
            ++var7;
            return var7;
         }

         if (var2 >= var6.getFirstColumn() && var2 <= var6.getLastColumn() && var1 >= var6.getFirstRow() && var1 <= var6.getLastRow()) {
            return 0;
         }
      }

      return -1;
   }

   private List b(XSSFSheet var1) {
      XSSFRow var2 = var1.getRow(0);
      ArrayList var3 = new ArrayList();
      short var4 = var2.getLastCellNum();

      for(int var5 = 0; var5 < var4; ++var5) {
         XSSFCell var6 = var2.getCell(var5);
         String var7 = var6.getStringCellValue();
         if (!StringUtils.isBlank(var7)) {
            TableHeader var8 = new TableHeader();
            var3.add(var8);
            var8.setName(var7);
            XSSFComment var9 = var6.getCellComment();
            if (var9 != null) {
               String var10 = var9.getString().toString().toLowerCase().trim();
               if (!var10.equals("自定义") && !var10.equals("custom")) {
                  if (var10.equals("权重") || var10.equals("weightsupport")) {
                     var8.setWeightWupport(true);
                  }
               } else {
                  var8.setCustom(true);
               }
            }
         }
      }

      return var3;
   }

   public String url() {
      return "/scorecard";
   }
}
