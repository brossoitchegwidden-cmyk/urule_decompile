package com.bstek.urule.console.anonymous.rest;

import com.bstek.urule.Utils;
import com.bstek.urule.console.admin.log.SystemLogUtils;
import com.bstek.urule.console.admin.log.URuleLogService;
import com.bstek.urule.console.anonymous.AnonymousServletHandler;
import com.bstek.urule.console.cache.packet.PacketCache;
import com.bstek.urule.console.cache.packet.PacketConfig;
import com.bstek.urule.console.cache.packet.PacketData;
import com.bstek.urule.console.config.Configure;
import com.bstek.urule.console.database.model.KnowledgeLog;
import com.bstek.urule.console.editor.execute.JsonBuilder;
import com.bstek.urule.console.editor.execute.MultiData;
import com.bstek.urule.console.editor.execute.VariableCategoryNotFoundException;
import com.bstek.urule.console.editor.execute.test.ConsoleLogWriter;
import com.bstek.urule.console.util.StringUtils;
import com.bstek.urule.exception.RuleAssertException;
import com.bstek.urule.exception.RuleException;
import com.bstek.urule.model.GeneralEntity;
import com.bstek.urule.model.library.Datatype;
import com.bstek.urule.model.library.variable.Variable;
import com.bstek.urule.model.library.variable.VariableCategory;
import com.bstek.urule.runtime.KnowledgePackageImpl;
import com.bstek.urule.runtime.KnowledgeSession;
import com.bstek.urule.runtime.KnowledgeSessionFactory;
import com.bstek.urule.runtime.monitor.MonitorObject;
import com.bstek.urule.runtime.monitor.MonitorObjectField;
import com.bstek.urule.runtime.response.ExecutionResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;

/** Handles REST discovery, mock payload generation and package execution. */
public class RestServletHandler extends AnonymousServletHandler {
   public static final String REST_URL = "/rest";
   private static final String DESCRIPTION_QUERY = "desc";
   private static final String MOCK_QUERY = "mock";
   private static ObjectMapper objectMapper = JsonMapper.builder().build();

   public void execute(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      HashMap responsePayload = new HashMap();
      String method = req.getMethod();
      if (!"POST".equals(method)) {
         resp.sendError(400);
         responsePayload.put("error", "非法请求");
         this.writeObjectToJson(resp, responsePayload);
      } else {
         String text = req.getContextPath() + "/urule";
         String requestURI = req.getRequestURI();
         String substring = requestURI.substring(text.length());
         int number = substring.lastIndexOf("/");
         String trimmedText = substring.substring(number + 1, substring.length()).trim();
         if (StringUtils.isBlank(trimmedText)) {
            responsePayload.put("error", "请指定要调用的知识包ID");
            this.writeObjectToJson(resp, responsePayload);
         } else {
            PacketData packet = PacketCache.ins.getPacket(trimmedText);
            if (packet == null) {
               long longValue = 0L;

               try {
                  longValue = Long.valueOf(trimmedText);
               } catch (NumberFormatException numberFormatException) {
                  throw new RuleException("Package [" + trimmedText + "] not exist");
               }

               packet = PacketCache.ins.getPacket(longValue);
            }

            if (packet == null) {
               responsePayload.put("error", "知识包【" + trimmedText + "】不存在或未发布");
               this.writeObjectToJson(resp, responsePayload);
            } else {
               PacketConfig packet2 = packet.getPacket();
               if (!packet2.isEnable()) {
                  responsePayload.put("error", "知识包【" + trimmedText + "】已停用");
                  this.writeObjectToJson(resp, responsePayload);
               } else if (!packet2.isRestEnable()) {
                  responsePayload.put("error", "知识包【" + trimmedText + "】未暴露Rest服务");
                  this.writeObjectToJson(resp, responsePayload);
               } else {
                  try {
                     String queryString = req.getQueryString();
                     if (queryString != null) {
                        if ("desc".equals(queryString)) {
                           List variableCategories = packet.getKnowledgePackageWrapper().getKnowledgePackage().getVariableCategories();
                           Map variableCategoriesMap = JsonBuilder.getInstance().buildVariableCategoriesMap(variableCategories);
                           String text2 = req.getRequestURL().toString();
                           text2 = Utils.decodeURL(text2);
                           responsePayload.put("url", text2);
                           responsePayload.put("authentication", packet2.isRestSecurityEnable());
                           responsePayload.put("input", this.resolveMonitorObjects(packet2.getRestInput(), variableCategoriesMap, false));
                           responsePayload.put("output", this.resolveMonitorObjects(packet2.getRestOutput(), variableCategoriesMap, false));
                           this.writeObjectToJson(resp, responsePayload);
                           return;
                        }

                        if ("mock".equals(queryString)) {
                           this.writeMockResponse(packet, req, resp);
                           return;
                        }

                        responsePayload.put("error", "Unknow parameter [" + queryString + "]");
                        this.writeObjectToJson(resp, responsePayload);
                        return;
                     }

                     if (!packet2.isEnable()) {
                        responsePayload.put("error", "知识包【" + trimmedText + "】已停用");
                        this.writeObjectToJson(resp, responsePayload);
                        return;
                     }

                     this.executeRequest(packet, req, resp);
                  } catch (Exception exception) {
                     StringBuilder stringBuilder = new StringBuilder();
                     Throwable rootCause = this.findRootCauseAndCollectTips(exception, stringBuilder);
                     String errorDetails = this.appendStackTrace(rootCause, stringBuilder);
                     responsePayload.put("error", errorDetails);
                     this.writeObjectToJson(resp, responsePayload);
                  }

               }
            }
         }
      }
   }

   private void writeMockResponse(PacketData packetData, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {
      HashMap valuesByKey = new HashMap();
      PacketConfig packet = packetData.getPacket();
      valuesByKey.put("security", packet.isRestSecurityEnable());
      valuesByKey.put("username", packet.getRestSecurityUser());
      valuesByKey.put("password", packet.getRestSecurityPassword());
      String text = httpServletRequest.getRequestURL().toString();
      text = Utils.decodeURL(text);
      valuesByKey.put("url", text);
      ObjectMapper objectMapper = new ObjectMapper();
      List variableCategories = packetData.getKnowledgePackageWrapper().getKnowledgePackage().getVariableCategories();
      Map variableCategoriesMap = JsonBuilder.getInstance().buildVariableCategoriesMap(variableCategories);
      String text2 = objectMapper.writeValueAsString(this.buildMockInput(packet.getRestInput(), variableCategoriesMap));
      valuesByKey.put("input", text2);
      this.writeObjectToJson(httpServletResponse, valuesByKey);
   }

   private List buildMockInput(List items, Map valuesByKey) {
      ArrayList mockInput = new ArrayList();
      items = this.resolveMonitorObjects(items, valuesByKey, true);
      HashSet uniqueItems = new HashSet();
      HashMap valuesByKey2 = new HashMap();

      for(MonitorObject monitorObject : (Iterable<MonitorObject>)(Iterable<?>)(items)) {
         Map mockObject = this.buildMockObject(monitorObject, uniqueItems, false);
         valuesByKey2.put(monitorObject.getName(), mockObject);
      }

      for(MonitorObject monitorObject2 : (Iterable<MonitorObject>)(Iterable<?>)(items)) {
         if (!uniqueItems.contains(monitorObject2.getName())) {
            mockInput.add(valuesByKey2.get(monitorObject2.getName()));
         }
      }

      return mockInput;
   }

   private Map buildMockObject(MonitorObject monitorObject, Set uniqueItems, boolean nestedReference) {
      if (nestedReference) {
         uniqueItems.add(monitorObject.getName());
      }

      HashMap valuesByKey = new HashMap();
      valuesByKey.put("name", monitorObject.getName());
      valuesByKey.put("class", monitorObject.getClazz());
      HashMap valuesByKey2 = new HashMap();
      valuesByKey.put("fields", valuesByKey2);

      for(MonitorObjectField monitorObjectField : monitorObject.getFields()) {
         String name = monitorObjectField.getName();
         String type = monitorObjectField.getType();
         if (type == null) {
            valuesByKey2.put(name, "");
         } else if (type.equals("String")) {
            valuesByKey2.put(name, "");
         } else if (type.equals("Integer")) {
            valuesByKey2.put(name, 0);
         } else if (type.equals("Char")) {
            valuesByKey2.put(name, 0);
         } else if (type.equals("Double")) {
            valuesByKey2.put(name, 0);
         } else if (type.equals("Long")) {
            valuesByKey2.put(name, 0);
         } else if (type.equals("Float")) {
            valuesByKey2.put(name, 0);
         } else if (type.equals("BigDecimal")) {
            valuesByKey2.put(name, 0);
         } else if (type.equals("Boolean")) {
            valuesByKey2.put(name, false);
         } else if (type.equals("Date")) {
            valuesByKey2.put(name, "2020-01-01 12:12:12");
         } else if (type.equals("List")) {
            MonitorObject monitorObject2 = monitorObjectField.get_value();
            ArrayList items = new ArrayList();
            if (monitorObject2 != null) {
               items.add(this.buildMockObject(monitorObject2, uniqueItems, true));
            }

            valuesByKey2.put(name, items);
         } else if (type.equals("Object")) {
            valuesByKey2.put(name, new HashMap());
         } else if (type.equals("Set")) {
            valuesByKey2.put(name, new ArrayList());
         } else if (type.equals("Map")) {
            valuesByKey2.put(name, new HashMap());
         } else if (type.equals("Enum")) {
            valuesByKey2.put(name, "");
         } else {
            valuesByKey2.put(name, "");
         }
      }

      return valuesByKey;
   }

   private void executeRequest(PacketData packetData, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {
      PacketConfig packet = packetData.getPacket();
      if (packet.isRestSecurityEnable()) {
         HashMap valuesByKey = new HashMap();
         String header = httpServletRequest.getHeader("Username");
         String header2 = httpServletRequest.getHeader("Password");
         if (!packet.getRestSecurityUser().equals(header) || !packet.getRestSecurityPassword().equals(header2)) {
            valuesByKey.put("error", "知识包服务需要用户名密码验证，请正确提供用户名密码信息");
            this.writeObjectToJson(httpServletResponse, valuesByKey);
            return;
         }
      }

      ServletInputStream inputStream = httpServletRequest.getInputStream();
      if (inputStream == null) {
         throw new RuleException("Input data can not be null.");
      } else {
         String trimmedText = IOUtils.toString(inputStream, "utf-8");
         if (StringUtils.isBlank(trimmedText)) {
            throw new RuleException("Input data can not be null.");
         } else {
            trimmedText = trimmedText.trim();
            KnowledgePackageImpl knowledgePackage = (KnowledgePackageImpl)packetData.getKnowledgePackageWrapper().getKnowledgePackage();
            KnowledgeSession knowledgeSession = KnowledgeSessionFactory.newKnowledgeSession(knowledgePackage);
            List variableCategories = packetData.getKnowledgePackageWrapper().getKnowledgePackage().getVariableCategories();
            Map variableCategoriesMap = JsonBuilder.getInstance().buildVariableCategoriesMap(variableCategories);
            Object complexObject = JsonBuilder.getInstance().buildComplexObject(trimmedText, variableCategoriesMap);
            if (complexObject instanceof MultiData) {
               MultiData multiData = (MultiData)complexObject;
               List items = multiData.getData();
               ExecutionResult executionResult = new ExecutionResult();

               for(Object objectValue : items) {
                  Map valuesByKey2 = this.executeInput(objectValue, knowledgeSession, knowledgePackage, packetData, variableCategoriesMap);
                  executionResult.addDuration((Long)valuesByKey2.get("duration"));
                  executionResult.addOutput((List)valuesByKey2.get("output"));
               }

               this.writeObjectToJson(httpServletResponse, executionResult);
            } else {
               Map valuesByKey3 = this.executeInput(complexObject, knowledgeSession, knowledgePackage, packetData, variableCategoriesMap);
               this.writeObjectToJson(httpServletResponse, valuesByKey3);
            }

         }
      }
   }

   private Map executeInput(Object objectValue, KnowledgeSession knowledgeSession, KnowledgePackageImpl knowledgePackageImpl, PacketData packetData, Map valuesByKey) throws Exception {
      HashMap valuesByKey2 = new HashMap();
      if (objectValue instanceof List) {
         for(Object objectValue2 : (List)objectValue) {
            if (objectValue2 instanceof GeneralEntity) {
               knowledgeSession.insert(objectValue2);
            } else if (objectValue2 instanceof Map) {
               valuesByKey2.putAll((Map)objectValue2);
            }
         }
      } else if (objectValue instanceof Map) {
         if (objectValue instanceof GeneralEntity) {
            knowledgeSession.insert(objectValue);
         } else if (objectValue instanceof Map) {
            valuesByKey2.putAll((Map)objectValue);
         }
      }

      Date date = new Date();
      Object objectValue3 = null;
      if (knowledgePackageImpl.getFlowMap().size() > 0) {
         String text = (String)knowledgePackageImpl.getFlowMap().keySet().iterator().next();
         objectValue3 = knowledgeSession.startProcess(text, valuesByKey2);
      } else {
         objectValue3 = knowledgeSession.fireRules(valuesByKey2);
      }

      Date date2 = new Date();
      knowledgeSession.writeLogFile();
      PacketConfig packet = packetData.getPacket();
      if (packet.isAuditEnable()) {
         ConsoleLogWriter consoleLogWriter = new ConsoleLogWriter();
         List logs = knowledgeSession.getLogManager().getLogger().getLogs();
         consoleLogWriter.write(logs);
         String text2 = consoleLogWriter.getLogMsg().toString();
         List items = null;
         List output = null;
         items = this.buildOutput((List)packet.getAuditInput(), objectValue, valuesByKey2, valuesByKey);
         output = this.buildOutput(packet.getAuditOutput(), objectValue, knowledgeSession.getParameters(), valuesByKey);
         KnowledgeLog knowledgeLog = URuleLogService.ins.getKnowledgeLog();
         knowledgeLog.setKnowledgeId(packet.getId());
         knowledgeLog.setVersion(knowledgePackageImpl.getVersion());
         knowledgeLog.setStartTime(date);
         knowledgeLog.setEndTime(date2);
         knowledgeLog.setTime(date2.getTime() - date.getTime());
         if (packet.isRestSecurityEnable()) {
            knowledgeLog.setUserId(packet.getRestSecurityUser());
            knowledgeLog.setUsername(packet.getRestSecurityUser());
         }

         knowledgeLog.setLogs(text2);
         knowledgeLog.setInParams(RestServletHandler.objectMapper.writeValueAsString(items));
         knowledgeLog.setOutParams(RestServletHandler.objectMapper.writeValueAsString(output));
         SystemLogUtils.addKnowledgeLog(knowledgeLog);
      }

      List output2 = this.buildOutput(packet.getRestOutput(), objectValue, knowledgeSession.getParameters(), valuesByKey);
      HashMap executeInputResult = new HashMap();
      executeInputResult.put("output", output2);
      executeInputResult.put("duration", ((ExecutionResponse)objectValue3).getDuration());
      return executeInputResult;
   }

   private List buildOutput(List items, Object objectValue, Map valuesByKey, Map valuesByKey2) {
      ArrayList output = new ArrayList();
      items = this.resolveMonitorObjects(items, valuesByKey2, true);

      for(MonitorObject monitorObject : (Iterable<MonitorObject>)(Iterable<?>)(items)) {
         String name = monitorObject.getName();
         String restParameterName = Configure.getConfigure().getRestParameterName();
         if (!name.equals(restParameterName) && !name.equals("参数")) {
            VariableCategory variableCategory = this.findVariableCategory(valuesByKey2, name);
            if (variableCategory == null) {
               throw new VariableCategoryNotFoundException("变量对象【" + name + "】未定义!");
            }

            String clazz = variableCategory.getClazz();

            for(GeneralEntity generalEntity : (Iterable<GeneralEntity>)(Iterable<?>)(this.findEntitiesByClass(objectValue, clazz))) {
               HashMap valuesByKey3 = new HashMap();
               output.add(valuesByKey3);
               valuesByKey3.put("name", name);
               valuesByKey3.put("class", clazz);
               Map objectFields = this.buildObjectFields(name, generalEntity, monitorObject.getFields(), items);
               valuesByKey3.put("fields", objectFields);
            }
         } else {
            HashMap valuesByKey4 = new HashMap();
            output.add(valuesByKey4);
            HashMap valuesByKey5 = new HashMap();
            if (monitorObject.getFields() != null) {
               for(MonitorObjectField monitorObjectField : monitorObject.getFields()) {
                  String name2 = monitorObjectField.getName();
                  valuesByKey5.put(name2, valuesByKey.get(name2));
               }
            }

            valuesByKey4.put(restParameterName, valuesByKey5);
         }
      }

      return output;
   }

   private Map buildObjectFields(String text, Object objectValue, List items, List items2) {
      HashMap valuesByKey = new HashMap();
      if (items == null) {
         return valuesByKey;
      } else {
         MonitorObject monitorObject = this.findMonitorObject(text, items2);

         for(MonitorObjectField monitorObjectField : (Iterable<MonitorObjectField>)(Iterable<?>)(items)) {
            String name = monitorObjectField.getName();
            Object objectProperty = Utils.getObjectProperty(objectValue, name);
            MonitorObject nestedMonitorObject = this.findNestedMonitorObject(name, monitorObject);
            if (nestedMonitorObject == null) {
               String[] parts = name.split("\\.");
               Object valuesByKey2 = valuesByKey;

               for(int index = 0; index < parts.length; ++index) {
                  String text2 = parts[index];
                  if (index == parts.length - 1) {
                     Utils.setObjectProperty(valuesByKey2, text2, objectProperty);
                     break;
                  }

                  Object objectProperty2 = Utils.getObjectProperty(valuesByKey2, text2);
                  if (objectProperty2 == null) {
                     objectProperty2 = new HashMap();
                     Utils.setObjectProperty(valuesByKey2, text2, objectProperty2);
                  }

                  valuesByKey2 = objectProperty2;
               }
            } else {
               ArrayList items3 = new ArrayList();

               for(Object objectValue2 : (List)objectProperty) {
                  HashMap valuesByKey3 = new HashMap();
                  valuesByKey3.put("name", nestedMonitorObject.getName());
                  valuesByKey3.put("class", nestedMonitorObject.getClazz());
                  Map objectFields = this.buildObjectFields(nestedMonitorObject.getName(), objectValue2, nestedMonitorObject.getFields(), items2);
                  valuesByKey3.put("fields", objectFields);
                  items3.add(valuesByKey3);
               }

               Utils.setObjectProperty(valuesByKey, name, items3);
            }
         }

         return valuesByKey;
      }
   }

   private MonitorObject findNestedMonitorObject(String text, MonitorObject monitorObject) {
      if (monitorObject == null) {
         return null;
      } else {
         for(MonitorObjectField monitorObjectField : monitorObject.getFields()) {
            if (text.equals(monitorObjectField.getName())) {
               return monitorObjectField.get_value();
            }
         }

         return null;
      }
   }

   private List findEntitiesByClass(Object objectValue, String text) {
      ArrayList items = new ArrayList();
      if (objectValue instanceof GeneralEntity) {
         GeneralEntity generalEntity = (GeneralEntity)objectValue;
         if (generalEntity.getTargetClass().equals(text)) {
            items.add(generalEntity);
         }
      } else if (objectValue instanceof List) {
         for(Object objectValue2 : (List)objectValue) {
            if (objectValue2 instanceof GeneralEntity) {
               GeneralEntity generalEntity2 = (GeneralEntity)objectValue2;
               if (generalEntity2.getTargetClass().equals(text)) {
                  items.add(generalEntity2);
               }
            }
         }
      }

      return items;
   }

   private VariableCategory findVariableCategory(Map valuesByKey, String text) {
      VariableCategory variableCategory = (VariableCategory)valuesByKey.get(text);
      if (Configure.getConfigure().getRestParameterName().equals(text) && variableCategory == null) {
         variableCategory = (VariableCategory)valuesByKey.get("参数");
      }

      return variableCategory;
   }

   private List resolveMonitorObjects(List items, Map valuesByKey, boolean includeNested) {
      HashSet uniqueItems = new HashSet();

      for(MonitorObject monitorObject : (Iterable<MonitorObject>)(Iterable<?>)(items)) {
         VariableCategory variableCategory = this.findVariableCategory(valuesByKey, monitorObject.getName());
         if ("参数".equals(variableCategory.getName())) {
            String restParameterName = Configure.getConfigure().getRestParameterName();
            monitorObject.setName(restParameterName);
         }

         monitorObject.setClazz(variableCategory.getClazz());

         for(MonitorObjectField monitorObjectField : monitorObject.getFields()) {
            Variable variable = null;

            for(Variable variable2 : variableCategory.getVariables()) {
               if (variable2.getName().contentEquals(monitorObjectField.getName()) || variable2.getLabel().contentEquals(monitorObjectField.getLabel())) {
                  variable = variable2;
                  break;
               }
            }

            if (variable != null) {
               monitorObjectField.setType(variable.getType().name());
               monitorObjectField.set_value(this.buildChildMonitorObject(variable, valuesByKey, items, uniqueItems));
            }
         }
      }

      if (!includeNested) {
         ArrayList monitorObjects = new ArrayList();

         for(MonitorObject monitorObject2 : (Iterable<MonitorObject>)(Iterable<?>)(items)) {
            if (!uniqueItems.contains(monitorObject2.getName())) {
               monitorObjects.add(monitorObject2);
            }
         }

         return monitorObjects;
      } else {
         return items;
      }
   }

   private MonitorObject buildChildMonitorObject(Variable variable, Map valuesByKey, List items, Set uniqueItems) {
      if (variable.getType().equals(Datatype.List) && !StringUtils.isBlank(variable.getChildType())) {
         VariableCategory variableCategory = (VariableCategory)valuesByKey.get(variable.getChildType());
         if (variableCategory == null) {
            return null;
         } else {
            String restParameterName = Configure.getConfigure().getRestParameterName();
            if ("参数".equals(variableCategory.getName())) {
               uniqueItems.add(restParameterName);
            } else {
               uniqueItems.add(variableCategory.getName());
            }

            MonitorObject monitorObject = new MonitorObject();
            monitorObject.setName(variableCategory.getName());
            if ("参数".equals(variableCategory.getName())) {
               monitorObject.setName(restParameterName);
            }

            monitorObject.setClazz(variableCategory.getClazz());
            ArrayList items2 = new ArrayList();
            monitorObject.setFields(items2);
            MonitorObject monitorObject2 = this.findMonitorObject(variableCategory.getName(), items);

            for(Variable variable2 : variableCategory.getVariables()) {
               if (this.containsField(monitorObject2, variable2.getName())) {
                  MonitorObjectField monitorObjectField = new MonitorObjectField();
                  monitorObjectField.setName(variable2.getName());
                  monitorObjectField.setLabel(variable2.getLabel());
                  monitorObjectField.setType(variable2.getType().name());
                  items2.add(monitorObjectField);
                  monitorObjectField.set_value(this.buildChildMonitorObject(variable2, valuesByKey, items, uniqueItems));
               }
            }

            return monitorObject;
         }
      } else {
         return null;
      }
   }

   private boolean containsField(MonitorObject monitorObject, String text) {
      if (monitorObject == null) {
         return true;
      } else {
         for(MonitorObjectField monitorObjectField : monitorObject.getFields()) {
            if (monitorObjectField.getName().equals(text)) {
               return true;
            }
         }

         return false;
      }
   }

   private MonitorObject findMonitorObject(String text, List items) {
      for(MonitorObject monitorObject : (Iterable<MonitorObject>)(Iterable<?>)(items)) {
         if (monitorObject.getName().equals(text)) {
            return monitorObject;
         }
      }

      return null;
   }

   private String appendStackTrace(Throwable throwable, StringBuilder stringBuilder) {
      ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
      PrintStream printStream = new PrintStream(byteArrayOutputStream);
      throwable.printStackTrace(printStream);
      String string = new String(byteArrayOutputStream.toByteArray());
      IOUtils.closeQuietly(printStream);
      IOUtils.closeQuietly(byteArrayOutputStream);
      if (stringBuilder.length() > 0) {
      }

      stringBuilder.append(string);
      return stringBuilder.toString();
   }

   private Throwable findRootCauseAndCollectTips(Throwable throwable, StringBuilder stringBuilder) {
      if (throwable instanceof RuleAssertException) {
         RuleAssertException ruleAssertException = (RuleAssertException)throwable;
         String tipMsg = ruleAssertException.getTipMsg();
         if (tipMsg != null) {
            stringBuilder.append(tipMsg);
         }
      }

      return throwable.getCause() != null ? this.findRootCauseAndCollectTips(throwable.getCause(), stringBuilder) : throwable;
   }

   public String url() {
      return "/rest";
   }
}
