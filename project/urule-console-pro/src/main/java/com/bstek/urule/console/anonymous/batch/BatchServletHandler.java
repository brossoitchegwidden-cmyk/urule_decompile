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
   private static final String DESC = "desc";
   private static final String MOCK = "mock";
   public static final Log logger = LogFactory.getLog(BatchServletHandler.class);

   private void markFailed(BatchResult batchResult, String text) {
      batchResult.setStatus(BatchStatus.failed);
      batchResult.setMsg(text);
   }

   /**执行方案*/
   public void execute(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      String queryString = req.getQueryString();
      if ("callback".equals(queryString)) {
         ObjectMapper objectMapper = this.createObjectMapper();
         BatchResult batchResult = (BatchResult)objectMapper.readValue(req.getInputStream(), BatchResult.class);
         logger.debug("Received batch callback: " + batchResult);
      } else {
         String text = req.getContextPath() + "/urule";
         String requestURI = req.getRequestURI();
         String substring = requestURI.substring(text.length());
         int number = substring.lastIndexOf("/");
         String trimmedText = substring.substring(number + 1, substring.length()).trim();
         BatchResult batchResult2 = new BatchResult();
         batchResult2.setIp(IPUtils.getIpAddress(req));
         batchResult2.setUserAgent(RequestHolder.getRequest().getHeader("User-Agent"));
         if (StringUtils.isBlank(trimmedText)) {
            this.markFailed(batchResult2, "请指定要调用的批处理ID");
            this.writeObjectToJson(resp, batchResult2);
         } else {
            for(int index = trimmedText.length() - 1; index >= 0; --index) {
               char text2 = trimmedText.charAt(index);
               if (!Character.isDigit(text2)) {
                  this.markFailed(batchResult2, "调用的批处理ID必须是一个整数");
                  this.writeObjectToJson(resp, batchResult2);
                  return;
               }
            }

            Batch batch = null;

            try {
               logger.debug("Initialize URule Batch ...");
               batch = SchemeService.ins.getBatchData(Long.parseLong(trimmedText));
            } catch (Exception exception) {
               this.markFailed(batchResult2, "批处理【" + trimmedText + "】提取失败,可能是不存在或配置异常");
               return;
            }

            if ("desc".equals(queryString)) {
               String text3 = req.getRequestURL().toString();
               text3 = Utils.decodeURL(text3);
               HashMap valuesByKey = new HashMap();
               valuesByKey.put("url", text3);
               valuesByKey.put("authentication", batch.isRestSecurityEnable());
               valuesByKey.put("input", batch.getParams());
               valuesByKey.put("output", this.buildValuesByKey());
               this.writeObjectToJson(resp, valuesByKey);
            } else {
               if ("mock".equals(queryString)) {
                  this.writeMockRequest(batch, req, resp);
                  return;
               }

               boolean flag = this.validateInvocation(req, resp, trimmedText, batchResult2, batch);
               if (!flag) {
                  return;
               }

               Object objectValue = new HashMap();
               ServletInputStream inputStream = req.getInputStream();
               if (inputStream != null) {
                  String trimmedText2 = IOUtils.toString(inputStream, "utf-8");
                  if (StringUtils.isNotBlank(trimmedText2)) {
                     trimmedText2 = trimmedText2.trim();
                     objectValue = (Map)this.createObjectMapper().readValue(trimmedText2, HashMap.class);
                  }
               }

               BatchServiceManager.execute(batch, (Map)objectValue, (String)null, batchResult2);
               this.writeObjectToJson(resp, batchResult2);
            }

         }
      }
   }

   private boolean validateInvocation(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, String text, BatchResult batchResult, Batch batch) throws ServletException, IOException {
      if (!batch.isRestEnable()) {
         this.markFailed(batchResult, "批处理【" + text + "】未暴露Rest服务");
         this.writeObjectToJson(httpServletResponse, batchResult);
         return false;
      } else if (BatchStatus.started == batch.getStatus()) {
         this.markFailed(batchResult, "批处理【" + text + "】正在运行,无法重复调用");
         this.writeObjectToJson(httpServletResponse, batchResult);
         return false;
      } else {
         if (batch.isRestSecurityEnable()) {
            String header = httpServletRequest.getHeader("Username");
            String header2 = httpServletRequest.getHeader("Password");
            if (!batch.getRestSecurityUser().equals(header) || !batch.getRestSecurityPassword().equals(header2)) {
               this.markFailed(batchResult, "批处理【" + text + "】需要用户名密码验证，请正确提供用户名密码信息");
               this.writeObjectToJson(httpServletResponse, batchResult);
               return false;
            }
         }

         return true;
      }
   }

   private void writeMockRequest(Batch batch, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {
      HashMap valuesByKey = new HashMap();
      valuesByKey.put("security", batch.isRestSecurityEnable());
      valuesByKey.put("username", batch.getRestSecurityUser());
      valuesByKey.put("password", batch.getRestSecurityPassword());
      String text = httpServletRequest.getRequestURL().toString();
      text = Utils.decodeURL(text);
      valuesByKey.put("url", text);
      ObjectMapper objectMapper = new ObjectMapper();
      HashMap valuesByKey2 = new HashMap();

      for(DataParam dataParam : (Iterable<DataParam>)(Iterable<?>)(batch.getParams())) {
         String name = dataParam.getName();
         String dataType = dataParam.getDataType();
         if (dataType == null) {
            valuesByKey2.put(name, "");
         } else if (dataType.equals("String")) {
            valuesByKey2.put(name, "");
         } else if (dataType.equals("Integer")) {
            valuesByKey2.put(name, 0);
         } else if (dataType.equals("Char")) {
            valuesByKey2.put(name, 0);
         } else if (dataType.equals("Double")) {
            valuesByKey2.put(name, 0);
         } else if (dataType.equals("Long")) {
            valuesByKey2.put(name, 0);
         } else if (dataType.equals("Float")) {
            valuesByKey2.put(name, 0);
         } else if (dataType.equals("BigDecimal")) {
            valuesByKey2.put(name, 0);
         } else if (dataType.equals("Boolean")) {
            valuesByKey2.put(name, false);
         } else if (dataType.equals("Date")) {
            valuesByKey2.put(name, "2020-01-01 12:12:12");
         } else {
            valuesByKey2.put(name, "");
         }
      }

      String text2 = objectMapper.writeValueAsString(valuesByKey2);
      valuesByKey.put("input", text2);
      this.writeObjectToJson(httpServletResponse, valuesByKey);
   }

   private Map buildValuesByKey() {
      HashMap valuesByKey = new HashMap();
      valuesByKey.put("clazz", BatchResult.class.getName());
      ArrayList items = new ArrayList();
      HashMap valuesByKey2 = new HashMap();
      valuesByKey2.put("name", "batchId");
      valuesByKey2.put("label", "批处理ID");
      valuesByKey2.put("dataType", "Long");
      items.add(valuesByKey2);
      valuesByKey2 = new HashMap();
      valuesByKey2.put("name", "batchName");
      valuesByKey2.put("label", "批处理名称");
      valuesByKey2.put("dataType", "String");
      items.add(valuesByKey2);
      valuesByKey2 = new HashMap();
      valuesByKey2.put("name", "startTime");
      valuesByKey2.put("label", "开始时间");
      valuesByKey2.put("dataType", "DateTime");
      items.add(valuesByKey2);
      valuesByKey2 = new HashMap();
      valuesByKey2.put("name", "endTime");
      valuesByKey2.put("label", "结束时间");
      valuesByKey2.put("dataType", "DateTime");
      items.add(valuesByKey2);
      valuesByKey2 = new HashMap();
      valuesByKey2.put("name", "ip");
      valuesByKey2.put("label", "IP");
      valuesByKey2.put("dataType", "String");
      items.add(valuesByKey2);
      valuesByKey2 = new HashMap();
      valuesByKey2.put("name", "readCount");
      valuesByKey2.put("label", "读取的记录总数");
      valuesByKey2.put("dataType", "Integer");
      items.add(valuesByKey2);
      valuesByKey2 = new HashMap();
      valuesByKey2.put("name", "filterCount");
      valuesByKey2.put("label", "过滤记录数");
      valuesByKey2.put("dataType", "Integer");
      items.add(valuesByKey2);
      valuesByKey2 = new HashMap();
      valuesByKey2.put("name", "status");
      valuesByKey2.put("label", "状态");
      valuesByKey2.put("dataType", "String");
      items.add(valuesByKey2);
      valuesByKey2 = new HashMap();
      valuesByKey2.put("name", "msg");
      valuesByKey2.put("label", "信息");
      valuesByKey2.put("dataType", "String");
      items.add(valuesByKey2);
      ArrayList items2 = new ArrayList();
      HashMap valuesByKey3 = new HashMap();
      valuesByKey3.put("name", "itemResults");
      valuesByKey3.put("label", "保存项明细");
      valuesByKey3.put("dataType", "String");
      valuesByKey2 = new HashMap();
      valuesByKey2.put("name", "name");
      valuesByKey2.put("label", "保存项名称");
      valuesByKey2.put("dataType", "String");
      items2.add(valuesByKey2);
      valuesByKey2 = new HashMap();
      valuesByKey2.put("name", "readCount");
      valuesByKey2.put("label", "读取的记录总数");
      valuesByKey2.put("dataType", "Integer");
      items2.add(valuesByKey2);
      valuesByKey2 = new HashMap();
      valuesByKey2.put("name", "filterCount");
      valuesByKey2.put("label", "过滤记录数");
      valuesByKey2.put("dataType", "Integer");
      items2.add(valuesByKey2);
      valuesByKey2 = new HashMap();
      valuesByKey2.put("name", "writeCount");
      valuesByKey2.put("label", "写入记录总数");
      valuesByKey2.put("dataType", "Integer");
      items2.add(valuesByKey2);
      valuesByKey2 = new HashMap();
      valuesByKey2.put("name", "tableName");
      valuesByKey2.put("label", "物理表");
      valuesByKey2.put("dataType", "String");
      items2.add(valuesByKey2);
      valuesByKey2 = new HashMap();
      valuesByKey2.put("name", "updateMode");
      valuesByKey2.put("label", "更新类型");
      valuesByKey2.put("dataType", "String");
      items2.add(valuesByKey2);
      valuesByKey3.put("fields", items2);
      items.add(valuesByKey3);
      valuesByKey.put("fields", items);
      return valuesByKey;
   }

   public String url() {
      return "/batch";
   }
}
