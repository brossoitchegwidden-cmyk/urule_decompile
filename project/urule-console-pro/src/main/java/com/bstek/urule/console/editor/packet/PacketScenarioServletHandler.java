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
   private ScenarioTestExecuting e = new ScenarioTestExecuting();

   public void doTest(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      TestScenario var3 = this.c(var1);
      String var4 = var1.getParameter("packageName");
      String var5 = var1.getParameter("enableLog");
      boolean var6 = false;
      if (StringUtils.isNotBlank(var5)) {
         var6 = Boolean.valueOf(var5);
      }

      var4 = Utils.decodeURL(var4);
      List var7 = null;
      KnowledgePackage var8 = this.f(var1);
      if (var8 != null) {
         var7 = var8.getVariableCategories();
      } else {
         KnowledgeBase var9 = this.e(var1);
         var8 = var9.getKnowledgePackage();
         var7 = var9.getResourceLibrary().getVariableCategories();
      }

      Map var13 = JsonBuilder.getInstance().buildVariableCategoriesMap(var7);
      ResultWrapper var10 = this.e.doTest(var3, var8, var13, var6);
      var10.setPackageName(var4);
      var10.setScenarioName(var3.getName());
      this.a(var2, var10);
   }

   private TestScenario c(HttpServletRequest var1) throws Exception {
      long var2 = Long.valueOf(var1.getParameter("id"));
      Scenario var4 = ScenarioManager.ins.load(var2);
      TestScenario var5 = new TestScenario();
      var5.setName(var4.getName());
      var5.setId(var4.getId());
      var5.setCreateUser(var4.getCreateUser());
      var5.setCreateDate(var4.getCreateDate());
      var5.setDesc(var4.getDesc());
      ObjectMapper var6 = JsonMapper.builder().build();
      List var7 = (List)var6.readValue(var4.getInputData(), ArrayList.class);
      List var8 = (List)var6.readValue(var4.getOutputData(), ArrayList.class);
      ArrayList var9 = new ArrayList();
      ArrayList var10 = new ArrayList();
      var5.setInputData(var9);
      var5.setOutputData(var10);

      for(Map var12 : (Iterable<Map>)(Iterable<?>)(var7)) {
         SimulateData var13 = new SimulateData();
         var13.setUuid((String)var12.get("categoryUuid"));
         var13.setName((String)var12.get("name"));
         ArrayList var14 = new ArrayList();
         var13.setFields(var14);
         var9.add(var13);

         for(Map var16 : (Iterable<Map>)(Iterable<?>)((List)var12.get("fields"))) {
            DataField var17 = new DataField();
            var17.setUuid((String)var16.get("uuid"));
            var17.setName((String)var16.get("name"));
            var17.setLabel((String)var16.get("label"));
            var17.setDatatype(Datatype.valueOf((String)var16.get("datatype")));
            var14.add(var17);
         }
      }

      for(Map var19 : (Iterable<Map>)(Iterable<?>)(var8)) {
         SimulateData var20 = new SimulateData();
         var20.setUuid((String)var19.get("categoryUuid"));
         var20.setName((String)var19.get("name"));
         ArrayList var21 = new ArrayList();
         var20.setFields(var21);
         var10.add(var20);

         for(Map var23 : (Iterable<Map>)(Iterable<?>)((List)var19.get("fields"))) {
            DataField var24 = new DataField();
            var24.setUuid((String)var23.get("uuid"));
            var24.setName((String)var23.get("name"));
            var24.setLabel((String)var23.get("label"));
            var24.setDatatype(Datatype.valueOf((String)var23.get("datatype")));
            var24.setOp(Op.valueOf((String)var23.get("op")));
            var21.add(var24);
         }
      }

      return var5;
   }

   public void load(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      Long var3 = Long.valueOf(var1.getParameter("packetId"));
      List var4 = ScenarioManager.ins.newQuery().packetId(var3).list();
      this.a(var2, var4);
   }

   @URuleAuthorization(
      authType = "project",
      code = "manager",
      model = "rule_knowledge"
   )
   public void add(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      Scenario var3 = new Scenario();
      var3.setName(var1.getParameter("name"));
      var3.setDesc(var1.getParameter("desc"));
      var3.setProjectId(ContextHolder.getProjectId());
      var3.setInputData(var1.getParameter("inputData"));
      var3.setOutputData(var1.getParameter("outputData"));
      var3.setPacketId(Long.valueOf(var1.getParameter("packetId")));
      var3.setCreateUser(SecurityUtils.getLoginUsername(var1));
      var3.setUpdateUser(SecurityUtils.getLoginUsername(var1));
      Scenario var4 = ScenarioManager.ins.add(var3);
      this.a(var2, var4);
   }

   @URuleAuthorization(
      authType = "project",
      code = "manager",
      model = "rule_knowledge"
   )
   public void update(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      long var3 = Long.valueOf(var1.getParameter("id"));
      Scenario var5 = ScenarioManager.ins.load(var3);
      var5.setName(var1.getParameter("name"));
      var5.setDesc(var1.getParameter("desc"));
      var5.setInputData(var1.getParameter("inputData"));
      var5.setOutputData(var1.getParameter("outputData"));
      var5.setUpdateUser(SecurityUtils.getLoginUsername(var1));
      ScenarioManager.ins.update(var5);
   }

   @URuleAuthorization(
      authType = "project",
      code = "manager",
      model = "rule_knowledge"
   )
   public void delete(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      ScenarioManager.ins.delete(Long.valueOf(var1.getParameter("id")));
   }

   @URuleAuthorization(
      authType = "project",
      code = "manager",
      model = "rule_knowledge"
   )
   public void uploadExcel(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      Long var3 = Long.valueOf(var1.getParameter("id"));
      UploadFile var4 = FileUtils.uploadFile(var1);
      InputStream var5 = var4.getInputStream();
      byte[] var6 = IOUtils.toByteArray(var5);
      var5.close();
      String var7 = SecurityUtils.getLoginUsername(var1);
      ScenarioManager.ins.uploadExcel(var3, var7, var6);
      Scenario var8 = ScenarioManager.ins.load(var3);
      var8.setExcelFileName(var4.getName());
      ScenarioManager.ins.update(var8);
   }

   @URuleAuthorization(
      authType = "project",
      code = "manager",
      model = "rule_knowledge"
   )
   public void downloadExcel(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      Long var3 = Long.valueOf(var1.getParameter("id"));
      Scenario var4 = ScenarioManager.ins.load(var3);
      byte[] var5 = ScenarioManager.ins.loadExcelFile(var3);
      ByteArrayInputStream var6 = new ByteArrayInputStream(var5);
      FileUtils.downloadFile(var4.getExcelFileName(), var6, var2);
      var6.close();
   }

   public void loadLibs(HttpServletRequest var1, HttpServletResponse var2) throws ServletException, IOException {
      KnowledgePackage var3 = this.f(var1);
      if (var3 != null) {
         this.a(var2, var3.getVariableCategories());
      } else {
         List var4 = this.d(var1);
         this.a(var2, var4);
      }
   }

   private List d(HttpServletRequest var1) throws IOException {
      KnowledgePackage var2 = this.f(var1);
      if (var2 != null) {
         return var2.getVariableCategories();
      } else {
         KnowledgeBase var3 = this.e(var1);
         List var4 = var3.getResourceLibrary().getVariableCategories();
         return var4;
      }
   }

   private KnowledgeBase e(HttpServletRequest var1) throws IOException {
      String var2 = var1.getParameter("files");
      var2 = Utils.decodeURL(var2);
      KnowledgeBuilder var3 = ServiceUtils.getKnowledgeBuilder();
      ResourceBase var4 = var3.newResourceBase();
      String[] var5 = var2.split(";");

      for(String var9 : var5) {
         var4.addResource(var9);
      }

      KnowledgeBase var11 = var3.buildKnowledgeBase(var4);
      return var11;
   }

   public void generateTemplateExcel(HttpServletRequest var1, HttpServletResponse var2) throws ServletException, IOException {
      String var3 = var1.getParameter("input");
      String var4 = var1.getParameter("output");
      var3 = URLDecoder.decode(var3, "utf-8");
      var4 = URLDecoder.decode(var4, "utf-8");
      ObjectMapper var5 = new ObjectMapper();
      List var6 = (List)var5.readValue(var3, ArrayList.class);
      List var7 = (List)var5.readValue(var4, ArrayList.class);
      SXSSFWorkbook var8 = new SXSSFWorkbook();
      this.a(var6, var8);
      this.b(var7, var8);
      var2.setContentType("application/x-xls");
      var2.setHeader("Content-Disposition", "attachment; filename=urule-scenario-test-template.xlsx");
      ServletOutputStream var9 = var2.getOutputStream();
      var8.write(var9);
      ((OutputStream)var9).flush();
      ((OutputStream)var9).close();
      var8.close();
   }

   private void a(List var1, SXSSFWorkbook var2) {
      XSSFCellStyle var3 = (XSSFCellStyle)var2.createCellStyle();
      var3.setFillPattern(FillPatternType.SOLID_FOREGROUND);
      var3.setFillForegroundColor(new XSSFColor(new Color(147, 228, 15)));
      this.a(var3);
      XSSFCellStyle var4 = (XSSFCellStyle)var2.createCellStyle();
      var4.setFillPattern(FillPatternType.SOLID_FOREGROUND);
      var4.setFillForegroundColor(new XSSFColor(new Color(111, 208, 215)));
      this.a(var4);
      SXSSFSheet var5 = var2.createSheet("场景");
      Row var6 = var5.createRow(0);
      var6.createCell(0);
      var6.createCell(1);
      int var7 = 2;
      int var8 = 0;

      for(Map var10 : (Iterable<Map>)(Iterable<?>)(var1)) {
         XSSFCellStyle var11 = var3;
         if (var8 % 2 == 0) {
            var11 = var4;
         }

         ++var8;
         Cell var12 = var6.createCell(var7);
         var12.setCellStyle(var11);
         String var13 = (String)var10.get("name");
         var12.setCellValue(var13);
         List var14 = (List)var10.get("fields");
         if (var14.size() > 1) {
            for(int var15 = 0; var15 < var14.size() - 1; ++var15) {
               int var16 = var15 + var7 + 1;
               var6.createCell(var16).setCellStyle(var11);
            }

            CellRangeAddress var33 = new CellRangeAddress(0, 0, var7, var7 + var14.size() - 1);
            var5.addMergedRegion(var33);
         }

         var7 += var14.size();
      }

      XSSFCellStyle var26 = (XSSFCellStyle)var2.createCellStyle();
      XSSFColor var27 = new XSSFColor(new Color(197, 218, 115));
      var26.setFillPattern(FillPatternType.SOLID_FOREGROUND);
      var26.setFillForegroundColor(var27);
      this.a(var26);
      Row var28 = var5.createRow(1);
      Cell var29 = var28.createCell(0);
      var29.setCellStyle(var26);
      var29.setCellValue("方案标识");
      Cell var30 = var28.createCell(1);
      var30.setCellStyle(var26);
      var30.setCellValue("描述");
      var5.setColumnWidth(1, 3000);
      var7 = 2;
      var8 = 0;

      for(Map var34 : (Iterable<Map>)(Iterable<?>)(var1)) {
         XSSFCellStyle var36 = var3;
         if (var8 % 2 == 0) {
            var36 = var4;
         }

         ++var8;

         for(Map var19 : (Iterable<Map>)(Iterable<?>)((List)var34.get("fields"))) {
            Cell var20 = var28.createCell(var7);
            var20.setCellStyle(var36);
            var5.setColumnWidth(var7, 3300);
            String var21 = (String)var19.get("label");
            var20.setCellValue(var21);
            ++var7;
         }
      }

      Row var32 = var5.createRow(2);
      XSSFCellStyle var35 = (XSSFCellStyle)var2.createCellStyle();
      this.a(var35);
      Cell var37 = var32.createCell(0);
      var37.setCellStyle(var35);
      this.a(var35);
      var37.setCellValue("1");
      Cell var38 = var32.createCell(1);
      var38.setCellStyle(var35);
      var38.setCellValue("描述内容");
      var7 = 2;

      for(Map var40 : (Iterable<Map>)(Iterable<?>)(var1)) {
         List var41 = (List)var40.get("fields");

         for(int var42 = 0; var42 < var41.size(); ++var42) {
            Cell var22 = var32.createCell(var7);
            var22.setCellStyle(var35);
            ++var7;
         }
      }

   }

   private void b(List var1, SXSSFWorkbook var2) {
      XSSFCellStyle var3 = (XSSFCellStyle)var2.createCellStyle();
      var3.setFillPattern(FillPatternType.SOLID_FOREGROUND);
      var3.setFillForegroundColor(new XSSFColor(new Color(255, 235, 69)));
      this.a(var3);
      XSSFCellStyle var4 = (XSSFCellStyle)var2.createCellStyle();
      var4.setFillPattern(FillPatternType.SOLID_FOREGROUND);
      var4.setFillForegroundColor(new XSSFColor(new Color(248, 255, 217)));
      this.a(var4);
      SXSSFSheet var5 = var2.createSheet("预期结果");
      Row var6 = var5.createRow(0);
      var6.createCell(0);
      int var7 = 1;
      int var8 = 0;

      for(Map var10 : (Iterable<Map>)(Iterable<?>)(var1)) {
         XSSFCellStyle var11 = var3;
         if (var8 % 2 == 0) {
            var11 = var4;
         }

         ++var8;
         Cell var12 = var6.createCell(var7);
         var12.setCellStyle(var11);
         String var13 = (String)var10.get("name");
         var12.setCellValue(var13);
         List var14 = (List)var10.get("fields");
         if (var14.size() > 1) {
            for(int var15 = 0; var15 < var14.size() - 1; ++var15) {
               int var16 = var15 + var7 + 1;
               var6.createCell(var16).setCellStyle(var11);
            }

            CellRangeAddress var35 = new CellRangeAddress(0, 0, var7, var7 + var14.size() - 1);
            var5.addMergedRegion(var35);
         }

         var7 += var14.size();
      }

      XSSFCellStyle var27 = (XSSFCellStyle)var2.createCellStyle();
      XSSFColor var28 = new XSSFColor(new Color(197, 218, 115));
      var27.setFillPattern(FillPatternType.SOLID_FOREGROUND);
      var27.setFillForegroundColor(var28);
      this.a(var27);
      Row var29 = var5.createRow(1);
      Cell var30 = var29.createCell(0);
      var30.setCellStyle(var27);
      var30.setCellValue("方案标识");
      var7 = 1;
      var8 = 0;

      for(Map var33 : (Iterable<Map>)(Iterable<?>)(var1)) {
         XSSFCellStyle var36 = var3;
         if (var8 % 2 == 0) {
            var36 = var4;
         }

         ++var8;

         for(Map var18 : (Iterable<Map>)(Iterable<?>)((List)var33.get("fields"))) {
            Cell var19 = var29.createCell(var7);
            var19.setCellStyle(var36);
            var5.setColumnWidth(var7, 4000);
            String var20 = (String)var18.get("label");
            String var21 = (String)var18.get("op");
            Op var22 = Op.valueOf(var21);
            String var23 = "\"" + var20 + "\"" + var22.toString();
            var19.setCellValue(var23);
            ++var7;
         }
      }

      Row var32 = var5.createRow(2);
      XSSFCellStyle var34 = (XSSFCellStyle)var2.createCellStyle();
      this.a(var34);
      Cell var37 = var32.createCell(0);
      var37.setCellStyle(var34);
      var37.setCellValue("1");
      var7 = 1;

      for(Map var40 : (Iterable<Map>)(Iterable<?>)(var1)) {
         List var41 = (List)var40.get("fields");

         for(int var42 = 0; var42 < var41.size(); ++var42) {
            Cell var43 = var32.createCell(var7);
            var43.setCellStyle(var34);
            ++var7;
         }
      }

   }

   private void a(XSSFCellStyle var1) {
      var1.setBorderLeft(BorderStyle.THIN);
      var1.setBorderRight(BorderStyle.THIN);
      var1.setBorderTop(BorderStyle.THIN);
      var1.setBorderBottom(BorderStyle.THIN);
      var1.setWrapText(true);
   }

   private KnowledgePackage f(HttpServletRequest var1) {
      String var2 = var1.getParameter("packetId");
      if (StringUtils.isNotBlank(var2)) {
         Packet var3 = PacketManager.ins.load(Long.valueOf(var2));
         if (var3.getType().equals(PacketType.upload)) {
            PacketPackage var4 = var3.getPacketPackage();
            if (var4 != null && var4.getId() != 0L) {
               String var5 = PacketPackageManager.ins.loadContent(var4.getId());
               if (StringUtils.isBlank(var5)) {
                  throw new InfoException("请先上传知识包");
               }

               KnowledgePackage var6 = Utils.stringToKnowledgePackage(var5);
               return var6;
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
