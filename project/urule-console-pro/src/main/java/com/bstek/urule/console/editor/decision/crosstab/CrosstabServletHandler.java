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
   public void excel(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      InputStream inputStream = FileUtils.uploadFile(req).getInputStream();
      List sheets = ExcelImportUtils.parseSheets(inputStream);
      CrossData crossData = this.parseCrossData((XSSFSheet)sheets.get(0));
      inputStream.close();
      CrossTableXmlBuilder crossTableXmlBuilder = new CrossTableXmlBuilder(crossData);
      CrosstabDefinition crosstabDefinition = crossTableXmlBuilder.doBuild();
      this.writeObjectToJson(resp, crosstabDefinition);
   }

   public void doExport(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      long longValue = Long.parseLong(req.getParameter("id"));
      RuleFile ruleFile = FileManager.ins.get(longValue);
      Project project = ProjectManager.ins.get(ruleFile.getProjectId());
      ContextHolder.setGroupId(project.getGroupId());
      ContextHolder.setProjectId(project.getId());
      ruleFile.setContent(FileManager.ins.loadContent(longValue));
      String content = FileManager.ins.loadContent(longValue);
      CrosstabExcelBuilder crosstabExcelBuilder = new CrosstabExcelBuilder();
      CrosstabDefinition table = crosstabExcelBuilder.buildTable(content);
      SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
      String string = ruleFile.getName() + "-" + simpleDateFormat.format(new Date()) + ".xlsx";
      resp.setContentType("application/octet-stream;charset=ISO8859-1");
      string = new String(string.getBytes("UTF-8"), "ISO8859-1");
      resp.setHeader("Content-Disposition", "attachment;filename=\"" + string + "\"");
      ServletOutputStream outputStream = resp.getOutputStream();
      crosstabExcelBuilder.buildExcel(table, outputStream);
      outputStream.flush();
      outputStream.close();
   }

   private CrossData parseCrossData(XSSFSheet xSSFSheet) throws Exception {
      XSSFWorkbook workbook = xSSFSheet.getWorkbook();
      List predefines = ExcelImportUtils.parsePredefines(workbook);
      Map properties = ExcelImportUtils.parseProperties(workbook);
      ArrayList items = new ArrayList();
      ArrayList items2 = new ArrayList();
      XSSFSheet dataSheet = ExcelImportUtils.findDataSheet(workbook);
      CrossHeader crossHeader = this.resolveCrossHeader(dataSheet);
      XSSFRow row = dataSheet.getRow(0);
      short lastCellNum = row.getLastCellNum();
      XSSFRow row2 = dataSheet.getRow(crossHeader.getRowSpan());
      String text = "predefine:";
      String text2 = "预定义变量:";

      for(int index = 0; index < lastCellNum; ++index) {
         CrossColumn crossColumn = new CrossColumn();
         crossColumn.setNumber(index + 1);
         if (index < crossHeader.getColSpan()) {
            crossColumn.setType(Type.left);
            XSSFCell cell = row2.getCell(index);
            XSSFComment cellComment = cell.getCellComment();
            if (cellComment != null) {
               String substring = cellComment.getString().toString().toLowerCase().trim();
               if (substring.startsWith(text) || substring.startsWith(text2)) {
                  crossColumn.setPredefine(true);
                  substring = substring.substring(text.length());
               }

               crossColumn.setContent(substring);
            }
         } else {
            crossColumn.setType(Type.top);
         }

         items2.add(crossColumn);
      }

      int lastRowNum = dataSheet.getLastRowNum();

      for(int index2 = 0; index2 <= lastRowNum; ++index2) {
         XSSFRow row3 = dataSheet.getRow(index2);
         CrossRow crossRow = new CrossRow();
         crossRow.setNumber(index2 + 1);
         if (index2 < crossHeader.getRowSpan()) {
            crossRow.setType(Type.top);
            XSSFCell cell2 = row3.getCell(crossHeader.getColSpan());
            XSSFComment cellComment2 = cell2.getCellComment();
            if (cellComment2 != null) {
               String substring2 = cellComment2.getString().toString().toLowerCase().trim();
               if (substring2.startsWith(text) || substring2.startsWith(text2)) {
                  crossRow.setPredefine(true);
                  substring2 = substring2.substring(text.length());
               }

               crossRow.setContent(substring2);
            }
         } else {
            crossRow.setType(Type.left);
         }

         items.add(crossRow);
      }

      ArrayList items3 = new ArrayList();

      for(int index3 = 0; index3 <= lastRowNum; ++index3) {
         XSSFRow row4 = dataSheet.getRow(index3);

         for(int index4 = 0; index4 < lastCellNum; ++index4) {
            if (index3 != 0 || index4 != 0) {
               XSSFCell cell3 = row4.getCell(index4);
               if (cell3 != null) {
                  Span span = this.resolveCellSpan(index3, index4, dataSheet);
                  if (span != null) {
                     String text3 = this.getCellText(cell3);
                     CellContent cellContent = new CellContent();
                     cellContent.setCol(index4 + 1);
                     cellContent.setRow(index3 + 1);
                     cellContent.setContent(text3);
                     if (index3 < crossHeader.getRowSpan()) {
                        cellContent.setType("condition");
                        cellContent.setSpan(span.getCol());
                     }

                     if (index4 < crossHeader.getColSpan()) {
                        cellContent.setType("condition");
                        cellContent.setSpan(span.getRow());
                     }

                     items3.add(cellContent);
                  }
               }
            }
         }
      }

      dataSheet.getWorkbook().close();
      CrossData crossData = new CrossData();
      crossData.setProperties(properties);
      crossData.setCells(items3);
      crossData.setColumns(items2);
      crossData.setRows(items);
      crossData.setHeader(crossHeader);
      crossData.setPredefineRows(predefines);
      return crossData;
   }

   private CrossHeader resolveCrossHeader(XSSFSheet xSSFSheet) {
      Span span = this.resolveCellSpan(0, 0, xSSFSheet);
      if (span == null) {
         throw new InfoException("导入的Excel不合法!");
      } else {
         CrossHeader crossHeader = new CrossHeader();
         crossHeader.setRowSpan(span.getRow());
         crossHeader.setColSpan(span.getCol());
         XSSFRow row = xSSFSheet.getRow(0);
         XSSFCell cell = row.getCell(0);
         if (cell == null) {
            throw new InfoException("导入的Excel表头不合法!");
         } else {
            crossHeader.setContent(this.getCellText(cell));
            return crossHeader;
         }
      }
   }

   private Span resolveCellSpan(int number, int number2, XSSFSheet xSSFSheet) {
      for(CellRangeAddress cellRangeAddress : xSSFSheet.getMergedRegions()) {
         if (cellRangeAddress.getFirstColumn() == number2 && cellRangeAddress.getFirstRow() == number) {
            int number3 = cellRangeAddress.getLastRow() - cellRangeAddress.getFirstRow();
            if (number3 > 0) {
               ++number3;
            }

            Span span = new Span();
            span.setRow(number3);
            int number4 = cellRangeAddress.getLastColumn() - cellRangeAddress.getFirstColumn();
            if (number4 > 0) {
               ++number4;
            }

            span.setCol(number4);
            return span;
         }

         if (number2 >= cellRangeAddress.getFirstColumn() && number2 <= cellRangeAddress.getLastColumn() && number >= cellRangeAddress.getFirstRow() && number <= cellRangeAddress.getLastRow()) {
            return null;
         }
      }

      Span span2 = new Span();
      span2.setRow(1);
      span2.setCol(1);
      return span2;
   }

   private String getCellText(XSSFCell xSSFCell) {
      String stringCellValue = null;
      CellType cellTypeEnum = xSSFCell.getCellTypeEnum();
      switch (cellTypeEnum) {
         case STRING:
            stringCellValue = xSSFCell.getStringCellValue();
            break;
         case BOOLEAN:
            stringCellValue = String.valueOf(xSSFCell.getBooleanCellValue());
            break;
         case NUMERIC:
            stringCellValue = String.valueOf(NumberToTextConverter.toText(xSSFCell.getNumericCellValue()));
         case _NONE:
         case BLANK:
         case ERROR:
         case FORMULA:
      }

      return stringCellValue;
   }

   public String url() {
      return "/crosstab";
   }
}
