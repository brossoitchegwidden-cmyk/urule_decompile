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

public class RestServletHandler extends AnonymousServletHandler {
   public static final String REST_URL = "/rest";
   private static final String a = "desc";
   private static final String e = "mock";
   private static ObjectMapper f = JsonMapper.builder().build();

   public void execute(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      HashMap var3 = new HashMap();
      String var4 = var1.getMethod();
      if (!"POST".equals(var4)) {
         var2.sendError(400);
         var3.put("error", "非法请求");
         this.a((HttpServletResponse)var2, (Object)var3);
      } else {
         String var5 = var1.getContextPath() + "/urule";
         String var6 = var1.getRequestURI();
         String var7 = var6.substring(var5.length());
         int var8 = var7.lastIndexOf("/");
         String var9 = var7.substring(var8 + 1, var7.length()).trim();
         if (StringUtils.isBlank(var9)) {
            var3.put("error", "请指定要调用的知识包ID");
            this.a((HttpServletResponse)var2, (Object)var3);
         } else {
            PacketData var10 = PacketCache.ins.getPacket(var9);
            if (var10 == null) {
               long var11 = 0L;

               try {
                  var11 = Long.valueOf(var9);
               } catch (NumberFormatException var17) {
                  throw new RuleException("Package [" + var9 + "] not exist");
               }

               var10 = PacketCache.ins.getPacket(var11);
            }

            if (var10 == null) {
               var3.put("error", "知识包【" + var9 + "】不存在或未发布");
               this.a((HttpServletResponse)var2, (Object)var3);
            } else {
               PacketConfig var19 = var10.getPacket();
               if (!var19.isEnable()) {
                  var3.put("error", "知识包【" + var9 + "】已停用");
                  this.a((HttpServletResponse)var2, (Object)var3);
               } else if (!var19.isRestEnable()) {
                  var3.put("error", "知识包【" + var9 + "】未暴露Rest服务");
                  this.a((HttpServletResponse)var2, (Object)var3);
               } else {
                  try {
                     String var12 = var1.getQueryString();
                     if (var12 != null) {
                        if ("desc".equals(var12)) {
                           List var20 = var10.getKnowledgePackageWrapper().getKnowledgePackage().getVariableCategories();
                           Map var21 = JsonBuilder.getInstance().buildVariableCategoriesMap(var20);
                           String var22 = var1.getRequestURL().toString();
                           var22 = Utils.decodeURL(var22);
                           var3.put("url", var22);
                           var3.put("authentication", var19.isRestSecurityEnable());
                           var3.put("input", this.a(var19.getRestInput(), var21, false));
                           var3.put("output", this.a(var19.getRestOutput(), var21, false));
                           this.a((HttpServletResponse)var2, (Object)var3);
                           return;
                        }

                        if ("mock".equals(var12)) {
                           this.a(var10, var1, var2);
                           return;
                        }

                        var3.put("error", "Unknow parameter [" + var12 + "]");
                        this.a((HttpServletResponse)var2, (Object)var3);
                        return;
                     }

                     if (!var19.isEnable()) {
                        var3.put("error", "知识包【" + var9 + "】已停用");
                        this.a((HttpServletResponse)var2, (Object)var3);
                        return;
                     }

                     this.b(var10, var1, var2);
                  } catch (Exception var16) {
                     StringBuilder var13 = new StringBuilder();
                     Throwable var14 = this.b(var16, var13);
                     String var15 = this.a(var14, var13);
                     var3.put("error", var15);
                     this.a((HttpServletResponse)var2, (Object)var3);
                  }

               }
            }
         }
      }
   }

   private void a(PacketData var1, HttpServletRequest var2, HttpServletResponse var3) throws Exception {
      HashMap var4 = new HashMap();
      PacketConfig var5 = var1.getPacket();
      var4.put("security", var5.isRestSecurityEnable());
      var4.put("username", var5.getRestSecurityUser());
      var4.put("password", var5.getRestSecurityPassword());
      String var6 = var2.getRequestURL().toString();
      var6 = Utils.decodeURL(var6);
      var4.put("url", var6);
      ObjectMapper var7 = new ObjectMapper();
      List var8 = var1.getKnowledgePackageWrapper().getKnowledgePackage().getVariableCategories();
      Map var9 = JsonBuilder.getInstance().buildVariableCategoriesMap(var8);
      String var10 = var7.writeValueAsString(this.a(var5.getRestInput(), var9));
      var4.put("input", var10);
      this.a((HttpServletResponse)var3, (Object)var4);
   }

   private List a(List var1, Map var2) {
      ArrayList var3 = new ArrayList();
      var1 = this.a(var1, var2, true);
      HashSet var4 = new HashSet();
      HashMap var5 = new HashMap();

      for(MonitorObject var7 : (Iterable<MonitorObject>)(Iterable<?>)(var1)) {
         Map var8 = this.a((MonitorObject)var7, (Set)var4, false);
         var5.put(var7.getName(), var8);
      }

      for(MonitorObject var11 : (Iterable<MonitorObject>)(Iterable<?>)(var1)) {
         if (!var4.contains(var11.getName())) {
            var3.add(var5.get(var11.getName()));
         }
      }

      return var3;
   }

   private Map a(MonitorObject var1, Set var2, boolean var3) {
      if (var3) {
         var2.add(var1.getName());
      }

      HashMap var4 = new HashMap();
      var4.put("name", var1.getName());
      var4.put("class", var1.getClazz());
      HashMap var5 = new HashMap();
      var4.put("fields", var5);

      for(MonitorObjectField var7 : var1.getFields()) {
         String var8 = var7.getName();
         String var9 = var7.getType();
         if (var9 == null) {
            var5.put(var8, "");
         } else if (var9.equals("String")) {
            var5.put(var8, "");
         } else if (var9.equals("Integer")) {
            var5.put(var8, 0);
         } else if (var9.equals("Char")) {
            var5.put(var8, 0);
         } else if (var9.equals("Double")) {
            var5.put(var8, 0);
         } else if (var9.equals("Long")) {
            var5.put(var8, 0);
         } else if (var9.equals("Float")) {
            var5.put(var8, 0);
         } else if (var9.equals("BigDecimal")) {
            var5.put(var8, 0);
         } else if (var9.equals("Boolean")) {
            var5.put(var8, false);
         } else if (var9.equals("Date")) {
            var5.put(var8, "2020-01-01 12:12:12");
         } else if (var9.equals("List")) {
            MonitorObject var10 = var7.get_value();
            ArrayList var11 = new ArrayList();
            if (var10 != null) {
               var11.add(this.a(var10, var2, true));
            }

            var5.put(var8, var11);
         } else if (var9.equals("Object")) {
            var5.put(var8, new HashMap());
         } else if (var9.equals("Set")) {
            var5.put(var8, new ArrayList());
         } else if (var9.equals("Map")) {
            var5.put(var8, new HashMap());
         } else if (var9.equals("Enum")) {
            var5.put(var8, "");
         } else {
            var5.put(var8, "");
         }
      }

      return var4;
   }

   private void b(PacketData var1, HttpServletRequest var2, HttpServletResponse var3) throws Exception {
      PacketConfig var4 = var1.getPacket();
      if (var4.isRestSecurityEnable()) {
         HashMap var5 = new HashMap();
         String var6 = var2.getHeader("Username");
         String var7 = var2.getHeader("Password");
         if (!var4.getRestSecurityUser().equals(var6) || !var4.getRestSecurityPassword().equals(var7)) {
            var5.put("error", "知识包服务需要用户名密码验证，请正确提供用户名密码信息");
            this.a((HttpServletResponse)var3, (Object)var5);
            return;
         }
      }

      ServletInputStream var18 = var2.getInputStream();
      if (var18 == null) {
         throw new RuleException("Input data can not be null.");
      } else {
         String var19 = IOUtils.toString(var18, "utf-8");
         if (StringUtils.isBlank(var19)) {
            throw new RuleException("Input data can not be null.");
         } else {
            var19 = var19.trim();
            KnowledgePackageImpl var21 = (KnowledgePackageImpl)var1.getKnowledgePackageWrapper().getKnowledgePackage();
            KnowledgeSession var8 = KnowledgeSessionFactory.newKnowledgeSession(var21);
            List var9 = var1.getKnowledgePackageWrapper().getKnowledgePackage().getVariableCategories();
            Map var10 = JsonBuilder.getInstance().buildVariableCategoriesMap(var9);
            Object var11 = JsonBuilder.getInstance().buildComplexObject(var19, var10);
            if (var11 instanceof MultiData) {
               MultiData var12 = (MultiData)var11;
               List var13 = var12.getData();
               ExecutionResult var14 = new ExecutionResult();

               for(Object var16 : var13) {
                  Map var17 = this.a(var16, var8, var21, var1, var10);
                  var14.addDuration((Long)var17.get("duration"));
                  var14.addOutput((List)var17.get("output"));
               }

               this.a((HttpServletResponse)var3, (Object)var14);
            } else {
               Map var22 = this.a(var11, var8, var21, var1, var10);
               this.a((HttpServletResponse)var3, (Object)var22);
            }

         }
      }
   }

   private Map a(Object var1, KnowledgeSession var2, KnowledgePackageImpl var3, PacketData var4, Map var5) throws Exception {
      HashMap var6 = new HashMap();
      if (var1 instanceof List) {
         for(Object var9 : (List)var1) {
            if (var9 instanceof GeneralEntity) {
               var2.insert(var9);
            } else if (var9 instanceof Map) {
               var6.putAll((Map)var9);
            }
         }
      } else if (var1 instanceof Map) {
         if (var1 instanceof GeneralEntity) {
            var2.insert(var1);
         } else if (var1 instanceof Map) {
            var6.putAll((Map)var1);
         }
      }

      Date var17 = new Date();
      Object var18 = null;
      if (var3.getFlowMap().size() > 0) {
         String var20 = (String)var3.getFlowMap().keySet().iterator().next();
         var18 = var2.startProcess(var20, var6);
      } else {
         var18 = var2.fireRules(var6);
      }

      Date var21 = new Date();
      var2.writeLogFile();
      PacketConfig var10 = var4.getPacket();
      if (var10.isAuditEnable()) {
         ConsoleLogWriter var11 = new ConsoleLogWriter();
         List var12 = var2.getLogManager().getLogger().getLogs();
         var11.write(var12);
         String var13 = var11.getLogMsg().toString();
         List var14 = null;
         List var15 = null;
         var14 = this.a((List)var10.getAuditInput(), (Object)var1, (Map)var6, (Map)var5);
         var15 = this.a(var10.getAuditOutput(), var1, var2.getParameters(), var5);
         KnowledgeLog var16 = URuleLogService.ins.getKnowledgeLog();
         var16.setKnowledgeId(var10.getId());
         var16.setVersion(var3.getVersion());
         var16.setStartTime(var17);
         var16.setEndTime(var21);
         var16.setTime(var21.getTime() - var17.getTime());
         if (var10.isRestSecurityEnable()) {
            var16.setUserId(var10.getRestSecurityUser());
            var16.setUsername(var10.getRestSecurityUser());
         }

         var16.setLogs(var13);
         var16.setInParams(f.writeValueAsString(var14));
         var16.setOutParams(f.writeValueAsString(var15));
         SystemLogUtils.addKnowledgeLog(var16);
      }

      List var22 = this.a(var10.getRestOutput(), var1, var2.getParameters(), var5);
      HashMap var23 = new HashMap();
      var23.put("output", var22);
      var23.put("duration", ((ExecutionResponse)var18).getDuration());
      return var23;
   }

   private List a(List var1, Object var2, Map var3, Map var4) {
      ArrayList var5 = new ArrayList();
      var1 = this.a(var1, var4, true);

      for(MonitorObject var7 : (Iterable<MonitorObject>)(Iterable<?>)(var1)) {
         String var8 = var7.getName();
         String var9 = Configure.getConfigure().getRestParameterName();
         if (!var8.equals(var9) && !var8.equals("参数")) {
            VariableCategory var18 = this.a(var4, var8);
            if (var18 == null) {
               throw new VariableCategoryNotFoundException("变量对象【" + var8 + "】未定义!");
            }

            String var19 = var18.getClazz();

            for(GeneralEntity var22 : (Iterable<GeneralEntity>)(Iterable<?>)(this.a(var2, var19))) {
               HashMap var15 = new HashMap();
               var5.add(var15);
               var15.put("name", var8);
               var15.put("class", var19);
               Map var16 = this.a((String)var8, (Object)var22, (List)var7.getFields(), (List)var1);
               var15.put("fields", var16);
            }
         } else {
            HashMap var10 = new HashMap();
            var5.add(var10);
            HashMap var11 = new HashMap();
            if (var7.getFields() != null) {
               for(MonitorObjectField var13 : var7.getFields()) {
                  String var14 = var13.getName();
                  var11.put(var14, var3.get(var14));
               }
            }

            var10.put(var9, var11);
         }
      }

      return var5;
   }

   private Map a(String var1, Object var2, List var3, List var4) {
      HashMap var5 = new HashMap();
      if (var3 == null) {
         return var5;
      } else {
         MonitorObject var6 = this.a(var1, var4);

         for(MonitorObjectField var8 : (Iterable<MonitorObjectField>)(Iterable<?>)(var3)) {
            String var9 = var8.getName();
            Object var10 = Utils.getObjectProperty(var2, var9);
            MonitorObject var11 = this.a(var9, var6);
            if (var11 == null) {
               String[] var18 = var9.split("\\.");
               Object var19 = var5;

               for(int var20 = 0; var20 < var18.length; ++var20) {
                  String var21 = var18[var20];
                  if (var20 == var18.length - 1) {
                     Utils.setObjectProperty(var19, var21, var10);
                     break;
                  }

                  Object var22 = Utils.getObjectProperty(var19, var21);
                  if (var22 == null) {
                     var22 = new HashMap();
                     Utils.setObjectProperty(var19, var21, var22);
                  }

                  var19 = var22;
               }
            } else {
               ArrayList var12 = new ArrayList();

               for(Object var15 : (List)var10) {
                  HashMap var16 = new HashMap();
                  var16.put("name", var11.getName());
                  var16.put("class", var11.getClazz());
                  Map var17 = this.a(var11.getName(), var15, var11.getFields(), var4);
                  var16.put("fields", var17);
                  var12.add(var16);
               }

               Utils.setObjectProperty(var5, var9, var12);
            }
         }

         return var5;
      }
   }

   private MonitorObject a(String var1, MonitorObject var2) {
      if (var2 == null) {
         return null;
      } else {
         for(MonitorObjectField var4 : var2.getFields()) {
            if (var1.equals(var4.getName())) {
               return var4.get_value();
            }
         }

         return null;
      }
   }

   private List a(Object var1, String var2) {
      ArrayList var3 = new ArrayList();
      if (var1 instanceof GeneralEntity) {
         GeneralEntity var4 = (GeneralEntity)var1;
         if (var4.getTargetClass().equals(var2)) {
            var3.add(var4);
         }
      } else if (var1 instanceof List) {
         for(Object var5 : (List)var1) {
            if (var5 instanceof GeneralEntity) {
               GeneralEntity var6 = (GeneralEntity)var5;
               if (var6.getTargetClass().equals(var2)) {
                  var3.add(var6);
               }
            }
         }
      }

      return var3;
   }

   private VariableCategory a(Map var1, String var2) {
      VariableCategory var3 = (VariableCategory)var1.get(var2);
      if (Configure.getConfigure().getRestParameterName().equals(var2) && var3 == null) {
         var3 = (VariableCategory)var1.get("参数");
      }

      return var3;
   }

   private List a(List var1, Map var2, boolean var3) {
      HashSet var4 = new HashSet();

      for(MonitorObject var6 : (Iterable<MonitorObject>)(Iterable<?>)(var1)) {
         VariableCategory var7 = this.a(var2, var6.getName());
         if ("参数".equals(var7.getName())) {
            String var8 = Configure.getConfigure().getRestParameterName();
            var6.setName(var8);
         }

         var6.setClazz(var7.getClazz());

         for(MonitorObjectField var9 : var6.getFields()) {
            Variable var10 = null;

            for(Variable var12 : var7.getVariables()) {
               if (var12.getName().contentEquals(var9.getName()) || var12.getLabel().contentEquals(var9.getLabel())) {
                  var10 = var12;
                  break;
               }
            }

            if (var10 != null) {
               var9.setType(var10.getType().name());
               var9.set_value(this.a((Variable)var10, (Map)var2, (List)var1, (Set)var4));
            }
         }
      }

      if (!var3) {
         ArrayList var13 = new ArrayList();

         for(MonitorObject var15 : (Iterable<MonitorObject>)(Iterable<?>)(var1)) {
            if (!var4.contains(var15.getName())) {
               var13.add(var15);
            }
         }

         return var13;
      } else {
         return var1;
      }
   }

   private MonitorObject a(Variable var1, Map var2, List var3, Set var4) {
      if (var1.getType().equals(Datatype.List) && !StringUtils.isBlank(var1.getChildType())) {
         VariableCategory var5 = (VariableCategory)var2.get(var1.getChildType());
         if (var5 == null) {
            return null;
         } else {
            String var6 = Configure.getConfigure().getRestParameterName();
            if ("参数".equals(var5.getName())) {
               var4.add(var6);
            } else {
               var4.add(var5.getName());
            }

            MonitorObject var7 = new MonitorObject();
            var7.setName(var5.getName());
            if ("参数".equals(var5.getName())) {
               var7.setName(var6);
            }

            var7.setClazz(var5.getClazz());
            ArrayList var8 = new ArrayList();
            var7.setFields(var8);
            MonitorObject var9 = this.a(var5.getName(), var3);

            for(Variable var11 : var5.getVariables()) {
               if (this.a(var9, var11.getName())) {
                  MonitorObjectField var12 = new MonitorObjectField();
                  var12.setName(var11.getName());
                  var12.setLabel(var11.getLabel());
                  var12.setType(var11.getType().name());
                  var8.add(var12);
                  var12.set_value(this.a(var11, var2, var3, var4));
               }
            }

            return var7;
         }
      } else {
         return null;
      }
   }

   private boolean a(MonitorObject var1, String var2) {
      if (var1 == null) {
         return true;
      } else {
         for(MonitorObjectField var4 : var1.getFields()) {
            if (var4.getName().equals(var2)) {
               return true;
            }
         }

         return false;
      }
   }

   private MonitorObject a(String var1, List var2) {
      for(MonitorObject var4 : (Iterable<MonitorObject>)(Iterable<?>)(var2)) {
         if (var4.getName().equals(var1)) {
            return var4;
         }
      }

      return null;
   }

   private String a(Throwable var1, StringBuilder var2) {
      ByteArrayOutputStream var3 = new ByteArrayOutputStream();
      PrintStream var4 = new PrintStream(var3);
      var1.printStackTrace(var4);
      String var5 = new String(var3.toByteArray());
      IOUtils.closeQuietly(var4);
      IOUtils.closeQuietly(var3);
      if (var2.length() > 0) {
      }

      var2.append(var5);
      return var2.toString();
   }

   private Throwable b(Throwable var1, StringBuilder var2) {
      if (var1 instanceof RuleAssertException) {
         RuleAssertException var3 = (RuleAssertException)var1;
         String var4 = var3.getTipMsg();
         if (var4 != null) {
            var2.append(var4);
         }
      }

      return var1.getCause() != null ? this.b(var1.getCause(), var2) : var1;
   }

   public String url() {
      return "/rest";
   }
}
