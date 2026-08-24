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
   public void excel(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      InputStream inputStream = FileUtils.uploadFile(req).getInputStream();
      List sheets = ExcelImportUtils.parseSheets(inputStream);
      ScoreTableData scoreTableData = this.parseScoreTableData((XSSFSheet)sheets.get(0));
      inputStream.close();
      ScoreTableBuilder scoreTableBuilder = new ScoreTableBuilder(scoreTableData);
      ScorecardDefinition table = scoreTableBuilder.buildTable();
      this.writeObjectToJson(resp, table);
   }

   public void doExport(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      long longValue = Long.parseLong(req.getParameter("id"));
      RuleFile ruleFile = FileManager.ins.get(longValue);
      ruleFile.setContent(FileManager.ins.loadContent(longValue));
      Project project = ProjectManager.ins.get(ruleFile.getProjectId());
      ContextHolder.setGroupId(project.getGroupId());
      ContextHolder.setProjectId(project.getId());
      SimpleScorecardExcelBuilder simpleScorecardExcelBuilder = new SimpleScorecardExcelBuilder();
      ScorecardDefinition table = simpleScorecardExcelBuilder.buildTable(ruleFile);
      SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
      String string = ruleFile.getName() + "-" + simpleDateFormat.format(new Date()) + ".xlsx";
      resp.setContentType("application/octet-stream;charset=ISO8859-1");
      string = new String(string.getBytes("UTF-8"), "ISO8859-1");
      resp.setHeader("Content-Disposition", "attachment;filename=\"" + string + "\"");
      ServletOutputStream outputStream = resp.getOutputStream();
      simpleScorecardExcelBuilder.buildExcel(table, outputStream);
      outputStream.flush();
      outputStream.close();
   }

   private ScoreTableData parseScoreTableData(XSSFSheet xSSFSheet) throws Exception {
      ArrayList items = new ArrayList();
      XSSFSheet sheetAt = xSSFSheet.getWorkbook().getSheetAt(xSSFSheet.getWorkbook().getActiveSheetIndex());
      int lastRowNum = sheetAt.getLastRowNum();
      List items2 = this.parseHeaderColumns(sheetAt);
      Map properties = ExcelImportUtils.parseProperties(xSSFSheet.getWorkbook());

      for(int index = 1; index <= lastRowNum; ++index) {
         XSSFRow row = sheetAt.getRow(index);
         RowData rowData = new RowData();
         items.add(rowData);
         ArrayList items3 = new ArrayList();
         rowData.setCells(items3);

         for(int index2 = 0; index2 < items2.size(); ++index2) {
            XSSFCell cell = row.getCell(index2);
            if (cell != null) {
               int number = this.getMergedRowSpan(index, index2, sheetAt);
               if (number != 0) {
                  TableHeader tableHeader = (TableHeader)items2.get(index2);
                  CellData cellData = new CellData();
                  if (number > 0) {
                     cellData.setSpan(number);
                  }

                  cellData.setHeader(tableHeader);
                  CellType cellTypeEnum = cell.getCellTypeEnum();
                  switch (cellTypeEnum) {
                     case STRING:
                        cellData.setContent(cell.getStringCellValue());
                        break;
                     case BOOLEAN:
                        cellData.setContent(String.valueOf(cell.getBooleanCellValue()));
                        break;
                     case NUMERIC:
                        cellData.setContent(String.valueOf(cell.getNumericCellValue()));
                     case _NONE:
                     case BLANK:
                     case ERROR:
                     case FORMULA:
                  }

                  cellData.setRow(index - 1);
                  cellData.setCol(index2);
                  items3.add(cellData);
               }
            }
         }
      }

      sheetAt.getWorkbook().close();
      ScoreTableData scoreTableData = new ScoreTableData(items2, items);
      scoreTableData.setProperties(properties);
      return scoreTableData;
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
            TableHeader tableHeader = new TableHeader();
            items.add(tableHeader);
            tableHeader.setName(stringCellValue);
            XSSFComment cellComment = cell.getCellComment();
            if (cellComment != null) {
               String trimmedText = cellComment.getString().toString().toLowerCase().trim();
               if (!trimmedText.equals("自定义") && !trimmedText.equals("custom")) {
                  if (trimmedText.equals("权重") || trimmedText.equals("weightsupport")) {
                     tableHeader.setWeightWupport(true);
                  }
               } else {
                  tableHeader.setCustom(true);
               }
            }
         }
      }

      return items;
   }

   public String url() {
      return "/scorecard";
   }
}
