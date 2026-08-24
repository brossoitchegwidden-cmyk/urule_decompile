package com.bstek.urule.console.editor.execute.test;

import com.bstek.urule.Configure;
import com.bstek.urule.LocaleHolder;
import com.bstek.urule.Utils;
import com.bstek.urule.builder.KnowledgeBase;
import com.bstek.urule.builder.ResourceBase;
import com.bstek.urule.console.ApiServletHandler;
import com.bstek.urule.console.InfoException;
import com.bstek.urule.console.database.manager.packet.PacketManager;
import com.bstek.urule.console.database.manager.packet.packge.PacketPackageManager;
import com.bstek.urule.console.database.model.Packet;
import com.bstek.urule.console.database.model.PacketPackage;
import com.bstek.urule.console.database.model.PacketType;
import com.bstek.urule.console.editor.execute.JsonBuilder;
import com.bstek.urule.console.editor.execute.VariableCategoryNotFoundException;
import com.bstek.urule.console.util.ServiceUtils;
import com.bstek.urule.console.util.StringUtils;
import com.bstek.urule.model.GeneralEntity;
import com.bstek.urule.model.library.Datatype;
import com.bstek.urule.model.library.variable.Variable;
import com.bstek.urule.model.library.variable.VariableCategory;
import com.bstek.urule.runtime.KnowledgePackage;
import com.bstek.urule.runtime.KnowledgeSession;
import com.bstek.urule.runtime.KnowledgeSessionFactory;
import com.bstek.urule.runtime.response.ExecutionResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class QuickTestServletHandler extends ApiServletHandler {
   public static final String IMPORT_EXCEL_DATA = "_import_excel_data";

   public void init() {
      super.init();
   }

   public void doQuickTest(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      String parameter = req.getParameter("input");
      String parameter2 = req.getParameter("output");
      String parameter3 = req.getParameter("language");
      if ("zh".equals(parameter3)) {
         LocaleHolder.set(Locale.SIMPLIFIED_CHINESE);
      } else if ("en".equals(parameter3)) {
         LocaleHolder.set(Locale.ENGLISH);
      }

      ObjectMapper objectMapper = new ObjectMapper();
      SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Configure.getDateFormat());
      objectMapper.setDateFormat(simpleDateFormat);
      objectMapper.setDateFormat(simpleDateFormat);
      List variableCategories = null;
      Map flowMap = null;
      KnowledgePackage knowledgePackage = this.resolveKnowledgePackage(req);
      if (knowledgePackage != null) {
         flowMap = knowledgePackage.getFlowMap();
         variableCategories = knowledgePackage.getVariableCategories();
      } else {
         KnowledgeBase knowledgeBase = this.resolveKnowledgeBase(req);
         flowMap = knowledgeBase.getFlowMap();
         knowledgePackage = knowledgeBase.getKnowledgePackage();
         variableCategories = knowledgeBase.getResourceLibrary().getVariableCategories();
      }

      Map variableCategoriesMap = JsonBuilder.getInstance().buildVariableCategoriesMap(variableCategories);
      List items = (List)objectMapper.readValue(parameter, ArrayList.class);
      List items2 = (List)objectMapper.readValue(parameter2, ArrayList.class);
      List items3 = this.buildInputFacts(items, variableCategoriesMap);
      KnowledgeSession knowledgeSession = KnowledgeSessionFactory.newKnowledgeSession(knowledgePackage);
      Map valuesByKey = null;

      for(Map valuesByKey2 : (Iterable<Map>)(Iterable<?>)(items3)) {
         if (valuesByKey2 instanceof GeneralEntity) {
            knowledgeSession.insert(valuesByKey2);
         } else if (valuesByKey == null) {
            valuesByKey = valuesByKey2;
         } else {
            valuesByKey.putAll(valuesByKey2);
         }
      }

      Object objectValue = null;
      if (flowMap != null && flowMap.size() > 0) {
         String text = (String)flowMap.keySet().iterator().next();
         if (valuesByKey != null) {
            objectValue = knowledgeSession.startProcess(text, valuesByKey);
         } else {
            objectValue = knowledgeSession.startProcess(text);
         }
      } else if (valuesByKey != null) {
         objectValue = knowledgeSession.fireRules(valuesByKey);
      } else {
         objectValue = knowledgeSession.fireRules();
      }

      ConsoleLogWriter consoleLogWriter = new ConsoleLogWriter();
      List logs = knowledgeSession.getLogManager().getLogger().getLogs();
      consoleLogWriter.write(logs);
      List items4 = this.buildOutputValues(items2, items3, knowledgeSession.getParameters());
      HashMap valuesByKey3 = new HashMap();
      valuesByKey3.put("output", items4);
      valuesByKey3.put("time", ((ExecutionResponse)objectValue).getDuration());
      valuesByKey3.put("logs", consoleLogWriter.getLogMsg().toString());
      this.writeObjectToJson(resp, valuesByKey3);
   }

   private List buildOutputValues(List items, List items2, Map valuesByKey) throws Exception {
      ArrayList items3 = new ArrayList();

      for(Map valuesByKey2 : (Iterable<Map>)(Iterable<?>)(items)) {
         List fields = (List)valuesByKey2.get("fields");
         if (fields != null && fields.size() != 0) {
            String categoryName = (String)valuesByKey2.get("categoryName");
            if (categoryName.equals("参数")) {
               HashMap valuesByKey3 = new HashMap();
               items3.add(valuesByKey3);
               if (LocaleHolder.get() == Locale.ENGLISH) {
                  categoryName = "Parameter";
               }

               valuesByKey3.put("name", categoryName);
               ArrayList items4 = new ArrayList();
               valuesByKey3.put("fields", items4);

               for(Map valuesByKey4 : (Iterable<Map>)(Iterable<?>)(fields)) {
                  HashMap valuesByKey5 = new HashMap();
                  items4.add(valuesByKey5);
                  String name = (String)valuesByKey4.get("name");
                  Object objectProperty = Utils.getObjectProperty(valuesByKey, name);
                  valuesByKey5.put("name", (String)valuesByKey4.get("label"));
                  valuesByKey5.put("value", objectProperty);
               }
            } else {
               String categoryClass = (String)valuesByKey2.get("categoryClass");

               for(Map valuesByKey6 : (Iterable<Map>)(Iterable<?>)(items2)) {
                  if (valuesByKey6 instanceof GeneralEntity) {
                     String targetClass = ((GeneralEntity)valuesByKey6).getTargetClass();
                     if (categoryClass.equals(targetClass)) {
                        HashMap valuesByKey7 = new HashMap();
                        items3.add(valuesByKey7);
                        valuesByKey7.put("name", categoryName);
                        ArrayList items5 = new ArrayList();
                        valuesByKey7.put("fields", items5);

                        for(Map valuesByKey8 : (Iterable<Map>)(Iterable<?>)(fields)) {
                           HashMap valuesByKey9 = new HashMap();
                           items5.add(valuesByKey9);
                           String name2 = (String)valuesByKey8.get("name");
                           Object objectProperty2 = Utils.getObjectProperty(valuesByKey6, name2);
                           valuesByKey9.put("name", (String)valuesByKey8.get("label"));
                           valuesByKey9.put("value", objectProperty2);
                        }
                     }
                  }
               }
            }
         }
      }

      return items3;
   }

   private List buildInputFacts(List items, Map valuesByKey) throws Exception {
      HashSet uniqueItems = new HashSet();
      ArrayList items2 = new ArrayList();

      for(Map valuesByKey2 : (Iterable<Map>)(Iterable<?>)(items)) {
         Object generalEntity = null;
         String categoryName = (String)valuesByKey2.get("categoryName");
         if (categoryName.equals("参数")) {
            generalEntity = new HashMap();
            uniqueItems.add(categoryName);
         } else {
            String categoryClass = (String)valuesByKey2.get("categoryClass");
            generalEntity = new GeneralEntity(categoryClass);
            uniqueItems.add(categoryClass);
         }

         items2.add(generalEntity);
         List fields = (List)valuesByKey2.get("fields");
         this.populateFactFields(categoryName, fields, (Map)generalEntity, valuesByKey);
      }

      return items2;
   }

   private void populateFactFields(String text, List items, Map valuesByKey, Map valuesByKey2) throws Exception {
      if (items != null) {
         HashSet uniqueItems = new HashSet();

         for(Map valuesByKey3 : (Iterable<Map>)(Iterable<?>)(items)) {
            String name2 = (String)valuesByKey3.get("name");
            String text2 = (String)valuesByKey3.get("value");
            uniqueItems.add(name2);
            if (text2 != null) {
               Object complexObject = null;
               String type2 = (String)valuesByKey3.get("type");
               Datatype datatype = Datatype.parse(type2);
               if (datatype.equals(Datatype.String)) {
                  if (StringUtils.isNotEmpty(text2)) {
                     complexObject = datatype.convert(text2);
                  }
               } else if (!datatype.equals(Datatype.List) && !datatype.equals(Datatype.Object) && !datatype.equals(Datatype.Set) && !datatype.equals(Datatype.Map)) {
                  if (StringUtils.isNotBlank(text2)) {
                     complexObject = datatype.convert(text2);
                  }
               } else {
                  complexObject = JsonBuilder.getInstance().buildComplexObject(text2, valuesByKey2);
                  valuesByKey.put(name2, complexObject);
               }

               String[] parts = name2.split("\\.");
               Object objectProperty2 = valuesByKey;

               for(int index = 0; index < parts.length; ++index) {
                  String text3 = parts[index];
                  if (index == parts.length - 1) {
                     Utils.setObjectProperty(objectProperty2, text3, complexObject);
                     break;
                  }

                  Object objectProperty = Utils.getObjectProperty(objectProperty2, text3);
                  if (objectProperty == null) {
                     objectProperty = new HashMap();
                     Utils.setObjectProperty(objectProperty2, text3, objectProperty);
                  }

                  objectProperty2 = objectProperty;
               }
            }
         }

         VariableCategory variableCategory = (VariableCategory)valuesByKey2.get(text);
         if (variableCategory == null) {
            throw new VariableCategoryNotFoundException("变量对象【" + text + "】未定义!");
         } else {
            for(Variable variable : variableCategory.getVariables()) {
               String name = variable.getName();
               if (!uniqueItems.contains(name)) {
                  String defaultValue = variable.getDefaultValue();
                  if (defaultValue != null) {
                     Datatype type = variable.getType();
                     Object complexObject2;
                     if (type.equals(Datatype.String)) {
                        complexObject2 = type.convert(defaultValue);
                     } else if (!type.equals(Datatype.List) && !type.equals(Datatype.Object) && !type.equals(Datatype.Set) && !type.equals(Datatype.Map)) {
                        complexObject2 = type.convert(defaultValue);
                     } else {
                        complexObject2 = JsonBuilder.getInstance().buildComplexObject(defaultValue, valuesByKey2);
                     }

                     valuesByKey.put(name, complexObject2);
                  }
               }
            }

         }
      }
   }

   public void loadTestVariableCategories(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      KnowledgePackage knowledgePackage = this.resolveKnowledgePackage(req);
      if (knowledgePackage != null) {
         List variableCategories = knowledgePackage.getVariableCategories();
         this.writeObjectToJson(resp, variableCategories);
      } else {
         KnowledgeBase knowledgeBase = this.resolveKnowledgeBase(req);
         List variableCategories2 = knowledgeBase.getResourceLibrary().getVariableCategories();
         this.writeObjectToJson(resp, variableCategories2);
      }
   }

   private KnowledgePackage resolveKnowledgePackage(HttpServletRequest httpServletRequest) throws ServletException, IOException {
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

   private KnowledgeBase resolveKnowledgeBase(HttpServletRequest httpServletRequest) throws IOException {
      String parameter = httpServletRequest.getParameter("files");
      parameter = Utils.decodeURL(parameter);
      ResourceBase resourceBase = ServiceUtils.getKnowledgeBuilder().newResourceBase();
      String[] parts = parameter.split(";");

      for(String text : parts) {
         resourceBase.addResource(text);
      }

      KnowledgeBase knowledgeBase = ServiceUtils.getKnowledgeBuilder().buildKnowledgeBase(resourceBase);
      return knowledgeBase;
   }

   public String url() {
      return "/quicktest";
   }
}
