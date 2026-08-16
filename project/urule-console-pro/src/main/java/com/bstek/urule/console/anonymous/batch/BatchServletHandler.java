package com.bstek.urule.console.anonymous.batch;

import com.bstek.urule.Utils;
import com.bstek.urule.console.RequestHolder;
import com.bstek.urule.console.anonymous.AnonymousServletHandler;
import com.bstek.urule.console.batch.BatchResult;
import com.bstek.urule.console.batch.BatchServiceManager;
import com.bstek.urule.console.batch.BatchStatus;
import com.bstek.urule.console.batch.SchemeService;
import com.bstek.urule.console.database.model.batch.Batch;
import com.bstek.urule.console.database.model.batch.DataParam;
import com.bstek.urule.console.util.IPUtils;
import com.bstek.urule.console.util.StringUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class BatchServletHandler extends AnonymousServletHandler {
   public static final String URL = "/batch";
   private static final String a = "desc";
   private static final String e = "mock";
   public static final Log logger = LogFactory.getLog(BatchServletHandler.class);

   private void a(BatchResult var1, String var2) {
      var1.setStatus(BatchStatus.failed);
      var1.setMsg(var2);
   }

   public void execute(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      String var3 = var1.getQueryString();
      if ("callback".equals(var3)) {
         ObjectMapper var16 = this.a();
         BatchResult var17 = (BatchResult)var16.readValue(var1.getInputStream(), BatchResult.class);
         System.out.println(var17);
      } else {
         String var4 = var1.getContextPath() + "/urule";
         String var5 = var1.getRequestURI();
         String var6 = var5.substring(var4.length());
         int var7 = var6.lastIndexOf("/");
         String var8 = var6.substring(var7 + 1, var6.length()).trim();
         BatchResult var9 = new BatchResult();
         var9.setIp(IPUtils.getIpAddress(var1));
         var9.setUserAgent(RequestHolder.getRequest().getHeader("User-Agent"));
         if (StringUtils.isBlank(var8)) {
            this.a(var9, "请指定要调用的批处理ID");
            this.a(var2, var9);
         } else {
            for(int var10 = var8.length() - 1; var10 >= 0; --var10) {
               char var11 = var8.charAt(var10);
               if (!Character.isDigit(var11)) {
                  this.a(var9, "调用的批处理ID必须是一个整数");
                  this.a(var2, var9);
                  return;
               }
            }

            Batch var19 = null;

            try {
               logger.debug("Initialize URule Batch ...");
               var19 = SchemeService.ins.getBatchData(Long.parseLong(var8));
            } catch (Exception var15) {
               this.a(var9, "批处理【" + var8 + "】提取失败,可能是不存在或配置异常");
               return;
            }

            if ("desc".equals(var3)) {
               String var20 = var1.getRequestURL().toString();
               var20 = Utils.decodeURL(var20);
               HashMap var12 = new HashMap();
               var12.put("url", var20);
               var12.put("authentication", var19.isRestSecurityEnable());
               var12.put("input", var19.getParams());
               var12.put("output", this.b());
               this.a(var2, var12);
            } else {
               if ("mock".equals(var3)) {
                  this.a(var19, var1, var2);
                  return;
               }

               boolean var22 = this.a(var1, var2, var8, var9, var19);
               if (!var22) {
                  return;
               }

               Object var23 = new HashMap();
               ServletInputStream var13 = var1.getInputStream();
               if (var13 != null) {
                  String var14 = IOUtils.toString(var13, "utf-8");
                  if (StringUtils.isNotBlank(var14)) {
                     var14 = var14.trim();
                     var23 = (Map)this.a().readValue(var14, HashMap.class);
                  }
               }

               BatchServiceManager.execute(var19, (Map)var23, (String)null, var9);
               this.a(var2, var9);
            }

         }
      }
   }

   private boolean a(HttpServletRequest var1, HttpServletResponse var2, String var3, BatchResult var4, Batch var5) throws ServletException, IOException {
      if (!var5.isRestEnable()) {
         this.a(var4, "批处理【" + var3 + "】未暴露Rest服务");
         this.a(var2, var4);
         return false;
      } else if (BatchStatus.started == var5.getStatus()) {
         this.a(var4, "批处理【" + var3 + "】正在运行,无法重复调用");
         this.a(var2, var4);
         return false;
      } else {
         if (var5.isRestSecurityEnable()) {
            String var6 = var1.getHeader("Username");
            String var7 = var1.getHeader("Password");
            if (!var5.getRestSecurityUser().equals(var6) || !var5.getRestSecurityPassword().equals(var7)) {
               this.a(var4, "批处理【" + var3 + "】需要用户名密码验证，请正确提供用户名密码信息");
               this.a(var2, var4);
               return false;
            }
         }

         return true;
      }
   }

   private void a(Batch var1, HttpServletRequest var2, HttpServletResponse var3) throws Exception {
      HashMap var4 = new HashMap();
      var4.put("security", var1.isRestSecurityEnable());
      var4.put("username", var1.getRestSecurityUser());
      var4.put("password", var1.getRestSecurityPassword());
      String var5 = var2.getRequestURL().toString();
      var5 = Utils.decodeURL(var5);
      var4.put("url", var5);
      ObjectMapper var6 = new ObjectMapper();
      HashMap var7 = new HashMap();

      for(DataParam var9 : (Iterable<DataParam>)(Iterable<?>)(var1.getParams())) {
         String var10 = var9.getName();
         String var11 = var9.getDataType();
         if (var11 == null) {
            var7.put(var10, "");
         } else if (var11.equals("String")) {
            var7.put(var10, "");
         } else if (var11.equals("Integer")) {
            var7.put(var10, 0);
         } else if (var11.equals("Char")) {
            var7.put(var10, 0);
         } else if (var11.equals("Double")) {
            var7.put(var10, 0);
         } else if (var11.equals("Long")) {
            var7.put(var10, 0);
         } else if (var11.equals("Float")) {
            var7.put(var10, 0);
         } else if (var11.equals("BigDecimal")) {
            var7.put(var10, 0);
         } else if (var11.equals("Boolean")) {
            var7.put(var10, false);
         } else if (var11.equals("Date")) {
            var7.put(var10, "2020-01-01 12:12:12");
         } else {
            var7.put(var10, "");
         }
      }

      String var13 = var6.writeValueAsString(var7);
      var4.put("input", var13);
      this.a(var3, var4);
   }

   private Map b() {
      HashMap var1 = new HashMap();
      var1.put("clazz", BatchResult.class.getName());
      ArrayList var2 = new ArrayList();
      HashMap var3 = new HashMap();
      var3.put("name", "batchId");
      var3.put("label", "批处理ID");
      var3.put("dataType", "Long");
      var2.add(var3);
      var3 = new HashMap();
      var3.put("name", "batchName");
      var3.put("label", "批处理名称");
      var3.put("dataType", "String");
      var2.add(var3);
      var3 = new HashMap();
      var3.put("name", "startTime");
      var3.put("label", "开始时间");
      var3.put("dataType", "DateTime");
      var2.add(var3);
      var3 = new HashMap();
      var3.put("name", "endTime");
      var3.put("label", "结束时间");
      var3.put("dataType", "DateTime");
      var2.add(var3);
      var3 = new HashMap();
      var3.put("name", "ip");
      var3.put("label", "IP");
      var3.put("dataType", "String");
      var2.add(var3);
      var3 = new HashMap();
      var3.put("name", "readCount");
      var3.put("label", "读取的记录总数");
      var3.put("dataType", "Integer");
      var2.add(var3);
      var3 = new HashMap();
      var3.put("name", "filterCount");
      var3.put("label", "过滤记录数");
      var3.put("dataType", "Integer");
      var2.add(var3);
      var3 = new HashMap();
      var3.put("name", "status");
      var3.put("label", "状态");
      var3.put("dataType", "String");
      var2.add(var3);
      var3 = new HashMap();
      var3.put("name", "msg");
      var3.put("label", "信息");
      var3.put("dataType", "String");
      var2.add(var3);
      ArrayList var4 = new ArrayList();
      HashMap var5 = new HashMap();
      var5.put("name", "itemResults");
      var5.put("label", "保存项明细");
      var5.put("dataType", "String");
      var3 = new HashMap();
      var3.put("name", "name");
      var3.put("label", "保存项名称");
      var3.put("dataType", "String");
      var4.add(var3);
      var3 = new HashMap();
      var3.put("name", "readCount");
      var3.put("label", "读取的记录总数");
      var3.put("dataType", "Integer");
      var4.add(var3);
      var3 = new HashMap();
      var3.put("name", "filterCount");
      var3.put("label", "过滤记录数");
      var3.put("dataType", "Integer");
      var4.add(var3);
      var3 = new HashMap();
      var3.put("name", "writeCount");
      var3.put("label", "写入记录总数");
      var3.put("dataType", "Integer");
      var4.add(var3);
      var3 = new HashMap();
      var3.put("name", "tableName");
      var3.put("label", "物理表");
      var3.put("dataType", "String");
      var4.add(var3);
      var3 = new HashMap();
      var3.put("name", "updateMode");
      var3.put("label", "更新类型");
      var3.put("dataType", "String");
      var4.add(var3);
      var5.put("fields", var4);
      var2.add(var5);
      var1.put("fields", var2);
      return var1;
   }

   public String url() {
      return "/batch";
   }
}
