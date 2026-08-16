package com.bstek.urule.console.editor.decision.table;

import com.bstek.urule.console.ApiServletHandler;
import com.bstek.urule.console.ContextHolder;
import com.bstek.urule.console.database.manager.file.FileManager;
import com.bstek.urule.console.database.manager.project.ProjectManager;
import com.bstek.urule.console.database.model.Project;
import com.bstek.urule.console.database.model.RuleFile;
import com.bstek.urule.console.editor.ExcelImportUtils;
import com.bstek.urule.console.editor.decision.CellContent;
import com.bstek.urule.console.util.FileUtils;
import com.bstek.urule.model.table.DecisionTable;
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
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class TableServletHandler extends ApiServletHandler {
   public void excel(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      InputStream var3 = FileUtils.uploadFile(var1).getInputStream();
      List var4 = ExcelImportUtils.parseSheets(var3);
      TableData var5 = this.a((XSSFSheet)var4.get(0));
      var3.close();
      DecisionTableXmlBuilder var6 = new DecisionTableXmlBuilder(var5);
      DecisionTable var7 = var6.buildTable();
      this.a(var2, var7);
   }

   public void doExport(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      long var3 = Long.parseLong(var1.getParameter("id"));
      RuleFile var5 = FileManager.ins.get(var3);
      var5.setContent(FileManager.ins.loadContent(var3));
      String var6 = FileManager.ins.loadContent(var3);
      Project var7 = ProjectManager.ins.get(var5.getProjectId());
      ContextHolder.setGroupId(var7.getGroupId());
      ContextHolder.setProjectId(var7.getId());
      DecisionTableExcelBuilder var8 = new DecisionTableExcelBuilder();
      DecisionTable var9 = var8.buildTable(var6);
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

   private TableData a(XSSFSheet var1) throws Exception {
      ArrayList var2 = new ArrayList();
      XSSFWorkbook var3 = var1.getWorkbook();
      List var4 = ExcelImportUtils.parsePredefines(var3);
      Map var5 = ExcelImportUtils.parseProperties(var3);
      XSSFSheet var6 = ExcelImportUtils.findDataSheet(var3);
      int var7 = var6.getLastRowNum();
      List var8 = this.b(var6);

      for(int var9 = 1; var9 <= var7; ++var9) {
         XSSFRow var10 = var6.getRow(var9);
         ContentRow var11 = new ContentRow();
         var2.add(var11);
         ArrayList var12 = new ArrayList();
         var11.setContents(var12);

         for(int var13 = 0; var13 < var8.size(); ++var13) {
            XSSFCell var14 = var10.getCell(var13);
            if (var14 != null) {
               int var15 = this.a(var9, var13, var6);
               if (var15 != 0) {
                  Header var16 = (Header)var8.get(var13);
                  CellContent var17 = new CellContent();
                  if (var15 > 0) {
                     var17.setSpan(var15);
                  }

                  var17.setHeader(var16);
                  CellType var18 = var14.getCellTypeEnum();
                  switch (var18) {
                     case STRING:
                        var17.setContent(var14.getStringCellValue());
                        break;
                     case BOOLEAN:
                        var17.setContent(String.valueOf(var14.getBooleanCellValue()));
                        break;
                     case NUMERIC:
                        var17.setContent(String.valueOf(var14.getNumericCellValue()));
                     case _NONE:
                     case BLANK:
                     case ERROR:
                     case FORMULA:
                  }

                  var17.setRow(var9 - 1);
                  var17.setCol(var13);
                  var12.add(var17);
               }
            }
         }
      }

      var6.getWorkbook().close();
      return new TableData(var5, var8, var2, var4);
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
            Header var8 = new Header();
            var3.add(var8);
            var8.setName(var7);
            XSSFComment var9 = var6.getCellComment();
            if (var9 != null) {
               String var10 = var9.getString().toString().toLowerCase().trim();
               if (!var10.equals("赋值") && !var10.equals("assign") && !var10.equals("assign:predefine") && !var10.equals("赋值:预定义变量")) {
                  if (!var10.equals("控制台输出") && !var10.equals("out")) {
                     if (var10.equals("执行方法") || var10.equals("execute")) {
                        var8.setType(HeaderType.execute);
                     }
                  } else {
                     var8.setType(HeaderType.out);
                  }
               } else {
                  var8.setType(HeaderType.assign);
               }

               if (StringUtils.isNotBlank(var10) && (var10.indexOf("predefine") > 0 || var10.indexOf("预定义变量") > 0)) {
                  var8.setPredefine(true);
               }
            }
         }
      }

      return var3;
   }

   public String url() {
      return "/table";
   }
}
