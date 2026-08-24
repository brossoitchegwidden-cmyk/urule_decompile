package com.bstek.urule.console.editor.packet;

import com.bstek.urule.Utils;
import com.bstek.urule.builder.KnowledgeBase;
import com.bstek.urule.builder.KnowledgeBuilder;
import com.bstek.urule.builder.ResourceBase;
import com.bstek.urule.console.ApiServletHandler;
import com.bstek.urule.console.ContextHolder;
import com.bstek.urule.console.InfoException;
import com.bstek.urule.console.database.manager.packet.PacketManager;
import com.bstek.urule.console.database.manager.packet.packge.PacketPackageManager;
import com.bstek.urule.console.database.manager.packet.scenario.ScenarioManager;
import com.bstek.urule.console.database.model.Packet;
import com.bstek.urule.console.database.model.PacketPackage;
import com.bstek.urule.console.database.model.PacketType;
import com.bstek.urule.console.database.model.Scenario;
import com.bstek.urule.console.editor.execute.JsonBuilder;
import com.bstek.urule.console.editor.packet.scenario.DataField;
import com.bstek.urule.console.editor.packet.scenario.ResultWrapper;
import com.bstek.urule.console.editor.packet.scenario.ScenarioTestExecuting;
import com.bstek.urule.console.editor.packet.scenario.SimulateData;
import com.bstek.urule.console.editor.packet.scenario.TestScenario;
import com.bstek.urule.console.security.SecurityUtils;
import com.bstek.urule.console.security.URuleAuthorization;
import com.bstek.urule.console.util.FileUtils;
import com.bstek.urule.console.util.ServiceUtils;
import com.bstek.urule.console.util.StringUtils;
import com.bstek.urule.console.util.UploadFile;
import com.bstek.urule.model.library.Datatype;
import com.bstek.urule.model.rule.Op;
import com.bstek.urule.runtime.KnowledgePackage;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import java.awt.Color;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;

public class PacketScenarioServletHandler extends ApiServletHandler {
   private ScenarioTestExecuting scenarioTestExecuting = new ScenarioTestExecuting();

   public void doTest(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      TestScenario testScenario = this.resolveTestScenario(req);
      String parameter = req.getParameter("packageName");
      String parameter2 = req.getParameter("enableLog");
      boolean flag = false;
      if (StringUtils.isNotBlank(parameter2)) {
         flag = Boolean.valueOf(parameter2);
      }

      parameter = Utils.decodeURL(parameter);
      List variableCategories = null;
      KnowledgePackage knowledgePackage = this.resolveKnowledgePackage(req);
      if (knowledgePackage != null) {
         variableCategories = knowledgePackage.getVariableCategories();
      } else {
         KnowledgeBase knowledgeBase = this.resolveKnowledgeBase(req);
         knowledgePackage = knowledgeBase.getKnowledgePackage();
         variableCategories = knowledgeBase.getResourceLibrary().getVariableCategories();
      }

      Map variableCategoriesMap = JsonBuilder.getInstance().buildVariableCategoriesMap(variableCategories);
      ResultWrapper resultWrapper = this.scenarioTestExecuting.doTest(testScenario, knowledgePackage, variableCategoriesMap, flag);
      resultWrapper.setPackageName(parameter);
      resultWrapper.setScenarioName(testScenario.getName());
      this.writeObjectToJson(resp, resultWrapper);
   }

   private TestScenario resolveTestScenario(HttpServletRequest httpServletRequest) throws Exception {
      long longValue = Long.valueOf(httpServletRequest.getParameter("id"));
      Scenario scenario = ScenarioManager.ins.load(longValue);
      TestScenario testScenario = new TestScenario();
      testScenario.setName(scenario.getName());
      testScenario.setId(scenario.getId());
      testScenario.setCreateUser(scenario.getCreateUser());
      testScenario.setCreateDate(scenario.getCreateDate());
      testScenario.setDesc(scenario.getDesc());
      ObjectMapper objectMapper = JsonMapper.builder().build();
      List items = (List)objectMapper.readValue(scenario.getInputData(), ArrayList.class);
      List items2 = (List)objectMapper.readValue(scenario.getOutputData(), ArrayList.class);
      ArrayList items3 = new ArrayList();
      ArrayList items4 = new ArrayList();
      testScenario.setInputData(items3);
      testScenario.setOutputData(items4);

      for(Map valuesByKey : (Iterable<Map>)(Iterable<?>)(items)) {
         SimulateData simulateData = new SimulateData();
         simulateData.setUuid((String)valuesByKey.get("categoryUuid"));
         simulateData.setName((String)valuesByKey.get("name"));
         ArrayList items5 = new ArrayList();
         simulateData.setFields(items5);
         items3.add(simulateData);

         for(Map valuesByKey2 : (Iterable<Map>)(Iterable<?>)((List)valuesByKey.get("fields"))) {
            DataField dataField = new DataField();
            dataField.setUuid((String)valuesByKey2.get("uuid"));
            dataField.setName((String)valuesByKey2.get("name"));
            dataField.setLabel((String)valuesByKey2.get("label"));
            dataField.setDatatype(Datatype.valueOf((String)valuesByKey2.get("datatype")));
            items5.add(dataField);
         }
      }

      for(Map valuesByKey3 : (Iterable<Map>)(Iterable<?>)(items2)) {
         SimulateData simulateData2 = new SimulateData();
         simulateData2.setUuid((String)valuesByKey3.get("categoryUuid"));
         simulateData2.setName((String)valuesByKey3.get("name"));
         ArrayList items6 = new ArrayList();
         simulateData2.setFields(items6);
         items4.add(simulateData2);

         for(Map valuesByKey4 : (Iterable<Map>)(Iterable<?>)((List)valuesByKey3.get("fields"))) {
            DataField dataField2 = new DataField();
            dataField2.setUuid((String)valuesByKey4.get("uuid"));
            dataField2.setName((String)valuesByKey4.get("name"));
            dataField2.setLabel((String)valuesByKey4.get("label"));
            dataField2.setDatatype(Datatype.valueOf((String)valuesByKey4.get("datatype")));
            dataField2.setOp(Op.valueOf((String)valuesByKey4.get("op")));
            items6.add(dataField2);
         }
      }

      return testScenario;
   }

   public void load(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      Long longValue = Long.valueOf(req.getParameter("packetId"));
      List items = ScenarioManager.ins.newQuery().packetId(longValue).list();
      this.writeObjectToJson(resp, items);
   }

   @URuleAuthorization(
      authType = "project",
      code = "manager",
      model = "rule_knowledge"
   )
   public void add(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      Scenario scenario = new Scenario();
      scenario.setName(req.getParameter("name"));
      scenario.setDesc(req.getParameter("desc"));
      scenario.setProjectId(ContextHolder.getProjectId());
      scenario.setInputData(req.getParameter("inputData"));
      scenario.setOutputData(req.getParameter("outputData"));
      scenario.setPacketId(Long.valueOf(req.getParameter("packetId")));
      scenario.setCreateUser(SecurityUtils.getLoginUsername(req));
      scenario.setUpdateUser(SecurityUtils.getLoginUsername(req));
      Scenario scenario2 = ScenarioManager.ins.add(scenario);
      this.writeObjectToJson(resp, scenario2);
   }

   @URuleAuthorization(
      authType = "project",
      code = "manager",
      model = "rule_knowledge"
   )
   public void update(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      long longValue = Long.valueOf(req.getParameter("id"));
      Scenario scenario = ScenarioManager.ins.load(longValue);
      scenario.setName(req.getParameter("name"));
      scenario.setDesc(req.getParameter("desc"));
      scenario.setInputData(req.getParameter("inputData"));
      scenario.setOutputData(req.getParameter("outputData"));
      scenario.setUpdateUser(SecurityUtils.getLoginUsername(req));
      ScenarioManager.ins.update(scenario);
   }

   @URuleAuthorization(
      authType = "project",
      code = "manager",
      model = "rule_knowledge"
   )
   public void delete(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      ScenarioManager.ins.delete(Long.valueOf(req.getParameter("id")));
   }

   @URuleAuthorization(
      authType = "project",
      code = "manager",
      model = "rule_knowledge"
   )
   public void uploadExcel(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      Long longValue = Long.valueOf(req.getParameter("id"));
      UploadFile uploadFile = FileUtils.uploadFile(req);
      InputStream inputStream = uploadFile.getInputStream();
      byte[] bytes = IOUtils.toByteArray(inputStream);
      inputStream.close();
      String loginUsername = SecurityUtils.getLoginUsername(req);
      ScenarioManager.ins.uploadExcel(longValue, loginUsername, bytes);
      Scenario scenario = ScenarioManager.ins.load(longValue);
      scenario.setExcelFileName(uploadFile.getName());
      ScenarioManager.ins.update(scenario);
   }

   @URuleAuthorization(
      authType = "project",
      code = "manager",
      model = "rule_knowledge"
   )
   public void downloadExcel(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      Long longValue = Long.valueOf(req.getParameter("id"));
      Scenario scenario = ScenarioManager.ins.load(longValue);
      byte[] excelFile = ScenarioManager.ins.loadExcelFile(longValue);
      ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(excelFile);
      FileUtils.downloadFile(scenario.getExcelFileName(), byteArrayInputStream, resp);
      byteArrayInputStream.close();
   }

   public void loadLibs(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
      KnowledgePackage knowledgePackage = this.resolveKnowledgePackage(req);
      if (knowledgePackage != null) {
         this.writeObjectToJson(resp, knowledgePackage.getVariableCategories());
      } else {
         List items = this.resolveVariableCategories(req);
         this.writeObjectToJson(resp, items);
      }
   }

   private List resolveVariableCategories(HttpServletRequest httpServletRequest) throws IOException {
      KnowledgePackage knowledgePackage = this.resolveKnowledgePackage(httpServletRequest);
      if (knowledgePackage != null) {
         return knowledgePackage.getVariableCategories();
      } else {
         KnowledgeBase knowledgeBase = this.resolveKnowledgeBase(httpServletRequest);
         List variableCategories = knowledgeBase.getResourceLibrary().getVariableCategories();
         return variableCategories;
      }
   }

   private KnowledgeBase resolveKnowledgeBase(HttpServletRequest httpServletRequest) throws IOException {
      String parameter = httpServletRequest.getParameter("files");
      parameter = Utils.decodeURL(parameter);
      KnowledgeBuilder knowledgeBuilder = ServiceUtils.getKnowledgeBuilder();
      ResourceBase resourceBase = knowledgeBuilder.newResourceBase();
      String[] parts = parameter.split(";");

      for(String text : parts) {
         resourceBase.addResource(text);
      }

      KnowledgeBase knowledgeBase = knowledgeBuilder.buildKnowledgeBase(resourceBase);
      return knowledgeBase;
   }

   public void generateTemplateExcel(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
      String parameter = req.getParameter("input");
      String parameter2 = req.getParameter("output");
      parameter = URLDecoder.decode(parameter, "utf-8");
      parameter2 = URLDecoder.decode(parameter2, "utf-8");
      ObjectMapper objectMapper = new ObjectMapper();
      List items = (List)objectMapper.readValue(parameter, ArrayList.class);
      List items2 = (List)objectMapper.readValue(parameter2, ArrayList.class);
      SXSSFWorkbook sXSSFWorkbook = new SXSSFWorkbook();
      this.buildInputSheet(items, sXSSFWorkbook);
      this.buildOutputSheet(items2, sXSSFWorkbook);
      resp.setContentType("application/x-xls");
      resp.setHeader("Content-Disposition", "attachment; filename=urule-scenario-test-template.xlsx");
      ServletOutputStream outputStream = resp.getOutputStream();
      sXSSFWorkbook.write(outputStream);
      ((OutputStream)outputStream).flush();
      ((OutputStream)outputStream).close();
      sXSSFWorkbook.close();
   }

   private void buildInputSheet(List items, SXSSFWorkbook sXSSFWorkbook) {
      XSSFCellStyle cellStyle = (XSSFCellStyle)sXSSFWorkbook.createCellStyle();
      cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
      cellStyle.setFillForegroundColor(new XSSFColor(new Color(147, 228, 15)));
      this.applyCellBorders(cellStyle);
      XSSFCellStyle xSSFCellStyle = (XSSFCellStyle)sXSSFWorkbook.createCellStyle();
      xSSFCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
      xSSFCellStyle.setFillForegroundColor(new XSSFColor(new Color(111, 208, 215)));
      this.applyCellBorders(xSSFCellStyle);
      SXSSFSheet sheet = sXSSFWorkbook.createSheet("场景");
      Row row = sheet.createRow(0);
      row.createCell(0);
      row.createCell(1);
      int number = 2;
      int number2 = 0;

      for(Map valuesByKey : (Iterable<Map>)(Iterable<?>)(items)) {
         XSSFCellStyle xSSFCellStyle2 = cellStyle;
         if (number2 % 2 == 0) {
            xSSFCellStyle2 = xSSFCellStyle;
         }

         ++number2;
         Cell cell = row.createCell(number);
         cell.setCellStyle(xSSFCellStyle2);
         String name = (String)valuesByKey.get("name");
         cell.setCellValue(name);
         List fields = (List)valuesByKey.get("fields");
         if (fields.size() > 1) {
            for(int index = 0; index < fields.size() - 1; ++index) {
               int number3 = index + number + 1;
               row.createCell(number3).setCellStyle(xSSFCellStyle2);
            }

            CellRangeAddress cellRangeAddress = new CellRangeAddress(0, 0, number, number + fields.size() - 1);
            sheet.addMergedRegion(cellRangeAddress);
         }

         number += fields.size();
      }

      XSSFCellStyle xSSFCellStyle3 = (XSSFCellStyle)sXSSFWorkbook.createCellStyle();
      XSSFColor xSSFColor = new XSSFColor(new Color(197, 218, 115));
      xSSFCellStyle3.setFillPattern(FillPatternType.SOLID_FOREGROUND);
      xSSFCellStyle3.setFillForegroundColor(xSSFColor);
      this.applyCellBorders(xSSFCellStyle3);
      Row row2 = sheet.createRow(1);
      Cell cell2 = row2.createCell(0);
      cell2.setCellStyle(xSSFCellStyle3);
      cell2.setCellValue("方案标识");
      Cell cell3 = row2.createCell(1);
      cell3.setCellStyle(xSSFCellStyle3);
      cell3.setCellValue("描述");
      sheet.setColumnWidth(1, 3000);
      number = 2;
      number2 = 0;

      for(Map valuesByKey2 : (Iterable<Map>)(Iterable<?>)(items)) {
         XSSFCellStyle xSSFCellStyle4 = cellStyle;
         if (number2 % 2 == 0) {
            xSSFCellStyle4 = xSSFCellStyle;
         }

         ++number2;

         for(Map valuesByKey3 : (Iterable<Map>)(Iterable<?>)((List)valuesByKey2.get("fields"))) {
            Cell cell4 = row2.createCell(number);
            cell4.setCellStyle(xSSFCellStyle4);
            sheet.setColumnWidth(number, 3300);
            String label = (String)valuesByKey3.get("label");
            cell4.setCellValue(label);
            ++number;
         }
      }

      Row row3 = sheet.createRow(2);
      XSSFCellStyle xSSFCellStyle5 = (XSSFCellStyle)sXSSFWorkbook.createCellStyle();
      this.applyCellBorders(xSSFCellStyle5);
      Cell cell5 = row3.createCell(0);
      cell5.setCellStyle(xSSFCellStyle5);
      this.applyCellBorders(xSSFCellStyle5);
      cell5.setCellValue("1");
      Cell cell6 = row3.createCell(1);
      cell6.setCellStyle(xSSFCellStyle5);
      cell6.setCellValue("描述内容");
      number = 2;

      for(Map valuesByKey4 : (Iterable<Map>)(Iterable<?>)(items)) {
         List fields2 = (List)valuesByKey4.get("fields");

         for(int index2 = 0; index2 < fields2.size(); ++index2) {
            Cell cell7 = row3.createCell(number);
            cell7.setCellStyle(xSSFCellStyle5);
            ++number;
         }
      }

   }

   private void buildOutputSheet(List items, SXSSFWorkbook sXSSFWorkbook) {
      XSSFCellStyle cellStyle = (XSSFCellStyle)sXSSFWorkbook.createCellStyle();
      cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
      cellStyle.setFillForegroundColor(new XSSFColor(new Color(255, 235, 69)));
      this.applyCellBorders(cellStyle);
      XSSFCellStyle xSSFCellStyle = (XSSFCellStyle)sXSSFWorkbook.createCellStyle();
      xSSFCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
      xSSFCellStyle.setFillForegroundColor(new XSSFColor(new Color(248, 255, 217)));
      this.applyCellBorders(xSSFCellStyle);
      SXSSFSheet sheet = sXSSFWorkbook.createSheet("预期结果");
      Row row = sheet.createRow(0);
      row.createCell(0);
      int number = 1;
      int number2 = 0;

      for(Map valuesByKey : (Iterable<Map>)(Iterable<?>)(items)) {
         XSSFCellStyle xSSFCellStyle2 = cellStyle;
         if (number2 % 2 == 0) {
            xSSFCellStyle2 = xSSFCellStyle;
         }

         ++number2;
         Cell cell = row.createCell(number);
         cell.setCellStyle(xSSFCellStyle2);
         String name = (String)valuesByKey.get("name");
         cell.setCellValue(name);
         List fields = (List)valuesByKey.get("fields");
         if (fields.size() > 1) {
            for(int index = 0; index < fields.size() - 1; ++index) {
               int number3 = index + number + 1;
               row.createCell(number3).setCellStyle(xSSFCellStyle2);
            }

            CellRangeAddress cellRangeAddress = new CellRangeAddress(0, 0, number, number + fields.size() - 1);
            sheet.addMergedRegion(cellRangeAddress);
         }

         number += fields.size();
      }

      XSSFCellStyle xSSFCellStyle3 = (XSSFCellStyle)sXSSFWorkbook.createCellStyle();
      XSSFColor xSSFColor = new XSSFColor(new Color(197, 218, 115));
      xSSFCellStyle3.setFillPattern(FillPatternType.SOLID_FOREGROUND);
      xSSFCellStyle3.setFillForegroundColor(xSSFColor);
      this.applyCellBorders(xSSFCellStyle3);
      Row row2 = sheet.createRow(1);
      Cell cell2 = row2.createCell(0);
      cell2.setCellStyle(xSSFCellStyle3);
      cell2.setCellValue("方案标识");
      number = 1;
      number2 = 0;

      for(Map valuesByKey2 : (Iterable<Map>)(Iterable<?>)(items)) {
         XSSFCellStyle xSSFCellStyle4 = cellStyle;
         if (number2 % 2 == 0) {
            xSSFCellStyle4 = xSSFCellStyle;
         }

         ++number2;

         for(Map valuesByKey3 : (Iterable<Map>)(Iterable<?>)((List)valuesByKey2.get("fields"))) {
            Cell cell3 = row2.createCell(number);
            cell3.setCellStyle(xSSFCellStyle4);
            sheet.setColumnWidth(number, 4000);
            String label = (String)valuesByKey3.get("label");
            String op2 = (String)valuesByKey3.get("op");
            Op op = Op.valueOf(op2);
            String text = "\"" + label + "\"" + op.toString();
            cell3.setCellValue(text);
            ++number;
         }
      }

      Row row3 = sheet.createRow(2);
      XSSFCellStyle xSSFCellStyle5 = (XSSFCellStyle)sXSSFWorkbook.createCellStyle();
      this.applyCellBorders(xSSFCellStyle5);
      Cell cell4 = row3.createCell(0);
      cell4.setCellStyle(xSSFCellStyle5);
      cell4.setCellValue("1");
      number = 1;

      for(Map valuesByKey4 : (Iterable<Map>)(Iterable<?>)(items)) {
         List fields2 = (List)valuesByKey4.get("fields");

         for(int index2 = 0; index2 < fields2.size(); ++index2) {
            Cell cell5 = row3.createCell(number);
            cell5.setCellStyle(xSSFCellStyle5);
            ++number;
         }
      }

   }

   private void applyCellBorders(XSSFCellStyle xSSFCellStyle) {
      xSSFCellStyle.setBorderLeft(BorderStyle.THIN);
      xSSFCellStyle.setBorderRight(BorderStyle.THIN);
      xSSFCellStyle.setBorderTop(BorderStyle.THIN);
      xSSFCellStyle.setBorderBottom(BorderStyle.THIN);
      xSSFCellStyle.setWrapText(true);
   }

   private KnowledgePackage resolveKnowledgePackage(HttpServletRequest httpServletRequest) {
      String parameter = httpServletRequest.getParameter("packetId");
      if (StringUtils.isNotBlank(parameter)) {
         Packet packet = PacketManager.ins.load(Long.valueOf(parameter));
         if (packet.getType().equals(PacketType.upload)) {
            PacketPackage packetPackage = packet.getPacketPackage();
            if (packetPackage != null && packetPackage.getId() != 0L) {
               String content = PacketPackageManager.ins.loadContent(packetPackage.getId());
               if (StringUtils.isBlank(content)) {
                  throw new InfoException("请先上传知识包");
               }

               KnowledgePackage knowledgePackage = Utils.stringToKnowledgePackage(content);
               return knowledgePackage;
            }

            throw new InfoException("请先上传知识包");
         }
      }

      return null;
   }

   public String url() {
      return "/scenario";
   }
}
