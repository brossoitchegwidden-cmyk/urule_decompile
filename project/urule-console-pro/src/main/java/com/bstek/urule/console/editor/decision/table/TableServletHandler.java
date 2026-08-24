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
   public void excel(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      InputStream inputStream = FileUtils.uploadFile(req).getInputStream();
      List sheets = ExcelImportUtils.parseSheets(inputStream);
      TableData tableData = this.parseTableData((XSSFSheet)sheets.get(0));
      inputStream.close();
      DecisionTableXmlBuilder decisionTableXmlBuilder = new DecisionTableXmlBuilder(tableData);
      DecisionTable table = decisionTableXmlBuilder.buildTable();
      this.writeObjectToJson(resp, table);
   }

   public void doExport(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      long longValue = Long.parseLong(req.getParameter("id"));
      RuleFile ruleFile = FileManager.ins.get(longValue);
      ruleFile.setContent(FileManager.ins.loadContent(longValue));
      String content = FileManager.ins.loadContent(longValue);
      Project project = ProjectManager.ins.get(ruleFile.getProjectId());
      ContextHolder.setGroupId(project.getGroupId());
      ContextHolder.setProjectId(project.getId());
      DecisionTableExcelBuilder decisionTableExcelBuilder = new DecisionTableExcelBuilder();
      DecisionTable table = decisionTableExcelBuilder.buildTable(content);
      SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
      String string = ruleFile.getName() + "-" + simpleDateFormat.format(new Date()) + ".xlsx";
      resp.setContentType("application/octet-stream;charset=ISO8859-1");
      string = new String(string.getBytes("UTF-8"), "ISO8859-1");
      resp.setHeader("Content-Disposition", "attachment;filename=\"" + string + "\"");
      ServletOutputStream outputStream = resp.getOutputStream();
      decisionTableExcelBuilder.buildExcel(table, outputStream);
      outputStream.flush();
      outputStream.close();
   }

   private TableData parseTableData(XSSFSheet xSSFSheet) throws Exception {
      ArrayList items = new ArrayList();
      XSSFWorkbook workbook = xSSFSheet.getWorkbook();
      List predefines = ExcelImportUtils.parsePredefines(workbook);
      Map properties = ExcelImportUtils.parseProperties(workbook);
      XSSFSheet dataSheet = ExcelImportUtils.findDataSheet(workbook);
      int lastRowNum = dataSheet.getLastRowNum();
      List items2 = this.parseHeaderColumns(dataSheet);

      for(int index = 1; index <= lastRowNum; ++index) {
         XSSFRow row = dataSheet.getRow(index);
         ContentRow contentRow = new ContentRow();
         items.add(contentRow);
         ArrayList items3 = new ArrayList();
         contentRow.setContents(items3);

         for(int index2 = 0; index2 < items2.size(); ++index2) {
            XSSFCell cell = row.getCell(index2);
            if (cell != null) {
               int number = this.getMergedRowSpan(index, index2, dataSheet);
               if (number != 0) {
                  Header header = (Header)items2.get(index2);
                  CellContent cellContent = new CellContent();
                  if (number > 0) {
                     cellContent.setSpan(number);
                  }

                  cellContent.setHeader(header);
                  CellType cellTypeEnum = cell.getCellTypeEnum();
                  switch (cellTypeEnum) {
                     case STRING:
                        cellContent.setContent(cell.getStringCellValue());
                        break;
                     case BOOLEAN:
                        cellContent.setContent(String.valueOf(cell.getBooleanCellValue()));
                        break;
                     case NUMERIC:
                        cellContent.setContent(String.valueOf(cell.getNumericCellValue()));
                     case _NONE:
                     case BLANK:
                     case ERROR:
                     case FORMULA:
                  }

                  cellContent.setRow(index - 1);
                  cellContent.setCol(index2);
                  items3.add(cellContent);
               }
            }
         }
      }

      dataSheet.getWorkbook().close();
      return new TableData(properties, items2, items, predefines);
   }

   private int getMergedRowSpan(int number, int number2, XSSFSheet xSSFSheet) {
      for(CellRangeAddress cellRangeAddress : xSSFSheet.getMergedRegions()) {
         if (cellRangeAddress.getFirstColumn() == number2 && cellRangeAddress.getFirstRow() == number) {
            int number3 = cellRangeAddress.getLastRow() - cellRangeAddress.getFirstRow();
            ++number3;
            return number3;
         }

         if (number2 >= cellRangeAddress.getFirstColumn() && number2 <= cellRangeAddress.getLastColumn() && number >= cellRangeAddress.getFirstRow() && number <= cellRangeAddress.getLastRow()) {
            return 0;
         }
      }

      return -1;
   }

   private List parseHeaderColumns(XSSFSheet xSSFSheet) {
      XSSFRow row = xSSFSheet.getRow(0);
      ArrayList items = new ArrayList();
      short lastCellNum = row.getLastCellNum();

      for(int index = 0; index < lastCellNum; ++index) {
         XSSFCell cell = row.getCell(index);
         String stringCellValue = cell.getStringCellValue();
         if (!StringUtils.isBlank(stringCellValue)) {
            Header header = new Header();
            items.add(header);
            header.setName(stringCellValue);
            XSSFComment cellComment = cell.getCellComment();
            if (cellComment != null) {
               String trimmedText = cellComment.getString().toString().toLowerCase().trim();
               if (!trimmedText.equals("赋值") && !trimmedText.equals("assign") && !trimmedText.equals("assign:predefine") && !trimmedText.equals("赋值:预定义变量")) {
                  if (!trimmedText.equals("控制台输出") && !trimmedText.equals("out")) {
                     if (trimmedText.equals("执行方法") || trimmedText.equals("execute")) {
                        header.setType(HeaderType.execute);
                     }
                  } else {
                     header.setType(HeaderType.out);
                  }
               } else {
                  header.setType(HeaderType.assign);
               }

               if (StringUtils.isNotBlank(trimmedText) && (trimmedText.indexOf("predefine") > 0 || trimmedText.indexOf("预定义变量") > 0)) {
                  header.setPredefine(true);
               }
            }
         }
      }

      return items;
   }

   public String url() {
      return "/table";
   }
}
