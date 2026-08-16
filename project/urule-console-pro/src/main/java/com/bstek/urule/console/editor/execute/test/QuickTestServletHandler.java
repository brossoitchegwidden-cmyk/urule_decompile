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

   public void doQuickTest(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      String var3 = var1.getParameter("input");
      String var4 = var1.getParameter("output");
      String var5 = var1.getParameter("language");
      if ("zh".equals(var5)) {
         LocaleHolder.set(Locale.SIMPLIFIED_CHINESE);
      } else if ("en".equals(var5)) {
         LocaleHolder.set(Locale.ENGLISH);
      }

      ObjectMapper var6 = new ObjectMapper();
      SimpleDateFormat var7 = new SimpleDateFormat(Configure.getDateFormat());
      var6.setDateFormat(var7);
      var6.setDateFormat(var7);
      List var8 = null;
      Map var9 = null;
      KnowledgePackage var10 = this.c(var1);
      if (var10 != null) {
         var9 = var10.getFlowMap();
         var8 = var10.getVariableCategories();
      } else {
         KnowledgeBase var11 = this.d(var1);
         var9 = var11.getFlowMap();
         var10 = var11.getKnowledgePackage();
         var8 = var11.getResourceLibrary().getVariableCategories();
      }

      Map var24 = JsonBuilder.getInstance().buildVariableCategoriesMap(var8);
      List var12 = (List)var6.readValue(var3, ArrayList.class);
      List var13 = (List)var6.readValue(var4, ArrayList.class);
      List var14 = this.a(var12, var24);
      KnowledgeSession var15 = KnowledgeSessionFactory.newKnowledgeSession(var10);
      Map var16 = null;

      for(Map var18 : (Iterable<Map>)(Iterable<?>)(var14)) {
         if (var18 instanceof GeneralEntity) {
            var15.insert(var18);
         } else if (var16 == null) {
            var16 = var18;
         } else {
            var16.putAll(var18);
         }
      }

      Object var25 = null;
      if (var9 != null && var9.size() > 0) {
         String var27 = (String)var9.keySet().iterator().next();
         if (var16 != null) {
            var25 = var15.startProcess(var27, var16);
         } else {
            var25 = var15.startProcess(var27);
         }
      } else if (var16 != null) {
         var25 = var15.fireRules(var16);
      } else {
         var25 = var15.fireRules();
      }

      ConsoleLogWriter var28 = new ConsoleLogWriter();
      List var19 = var15.getLogManager().getLogger().getLogs();
      var28.write(var19);
      List var20 = this.a(var13, var14, var15.getParameters());
      HashMap var21 = new HashMap();
      var21.put("output", var20);
      var21.put("time", ((ExecutionResponse)var25).getDuration());
      var21.put("logs", var28.getLogMsg().toString());
      this.a(var2, var21);
   }

   private List a(List var1, List var2, Map var3) throws Exception {
      ArrayList var4 = new ArrayList();

      for(Map var6 : (Iterable<Map>)(Iterable<?>)(var1)) {
         List var7 = (List)var6.get("fields");
         if (var7 != null && var7.size() != 0) {
            String var8 = (String)var6.get("categoryName");
            if (var8.equals("参数")) {
               HashMap var20 = new HashMap();
               var4.add(var20);
               if (LocaleHolder.get() == Locale.ENGLISH) {
                  var8 = "Parameter";
               }

               var20.put("name", var8);
               ArrayList var21 = new ArrayList();
               var20.put("fields", var21);

               for(Map var23 : (Iterable<Map>)(Iterable<?>)(var7)) {
                  HashMap var24 = new HashMap();
                  var21.add(var24);
                  String var25 = (String)var23.get("name");
                  Object var26 = Utils.getObjectProperty(var3, var25);
                  var24.put("name", (String)var23.get("label"));
                  var24.put("value", var26);
               }
            } else {
               String var9 = (String)var6.get("categoryClass");

               for(Map var11 : (Iterable<Map>)(Iterable<?>)(var2)) {
                  if (var11 instanceof GeneralEntity) {
                     String var12 = ((GeneralEntity)var11).getTargetClass();
                     if (var9.equals(var12)) {
                        HashMap var13 = new HashMap();
                        var4.add(var13);
                        var13.put("name", var8);
                        ArrayList var14 = new ArrayList();
                        var13.put("fields", var14);

                        for(Map var16 : (Iterable<Map>)(Iterable<?>)(var7)) {
                           HashMap var17 = new HashMap();
                           var14.add(var17);
                           String var18 = (String)var16.get("name");
                           Object var19 = Utils.getObjectProperty(var11, var18);
                           var17.put("name", (String)var16.get("label"));
                           var17.put("value", var19);
                        }
                     }
                  }
               }
            }
         }
      }

      return var4;
   }

   private List a(List var1, Map var2) throws Exception {
      HashSet var3 = new HashSet();
      ArrayList var4 = new ArrayList();

      for(Map var6 : (Iterable<Map>)(Iterable<?>)(var1)) {
         Object var7 = null;
         String var8 = (String)var6.get("categoryName");
         if (var8.equals("参数")) {
            var7 = new HashMap();
            var3.add(var8);
         } else {
            String var9 = (String)var6.get("categoryClass");
            var7 = new GeneralEntity(var9);
            var3.add(var9);
         }

         var4.add(var7);
         List var11 = (List)var6.get("fields");
         this.a(var8, var11, (Map)var7, var2);
      }

      return var4;
   }

   private void a(String var1, List var2, Map var3, Map var4) throws Exception {
      if (var2 != null) {
         HashSet var5 = new HashSet();

         for(Map var7 : (Iterable<Map>)(Iterable<?>)(var2)) {
            String var8 = (String)var7.get("name");
            String var9 = (String)var7.get("value");
            var5.add(var8);
            if (var9 != null) {
               Object var10 = null;
               String var11 = (String)var7.get("type");
               Datatype var12 = Datatype.parse(var11);
               if (var12.equals(Datatype.String)) {
                  if (StringUtils.isNotEmpty(var9)) {
                     var10 = var12.convert(var9);
                  }
               } else if (!var12.equals(Datatype.List) && !var12.equals(Datatype.Object) && !var12.equals(Datatype.Set) && !var12.equals(Datatype.Map)) {
                  if (StringUtils.isNotBlank(var9)) {
                     var10 = var12.convert(var9);
                  }
               } else {
                  var10 = JsonBuilder.getInstance().buildComplexObject(var9, var4);
                  var3.put(var8, var10);
               }

               String[] var13 = var8.split("\\.");
               Object var14 = var3;

               for(int var15 = 0; var15 < var13.length; ++var15) {
                  String var16 = var13[var15];
                  if (var15 == var13.length - 1) {
                     Utils.setObjectProperty(var14, var16, var10);
                     break;
                  }

                  Object var17 = Utils.getObjectProperty(var14, var16);
                  if (var17 == null) {
                     var17 = new HashMap();
                     Utils.setObjectProperty(var14, var16, var17);
                  }

                  var14 = var17;
               }
            }
         }

         VariableCategory var18 = (VariableCategory)var4.get(var1);
         if (var18 == null) {
            throw new VariableCategoryNotFoundException("变量对象【" + var1 + "】未定义!");
         } else {
            for(Variable var20 : var18.getVariables()) {
               String var21 = var20.getName();
               if (!var5.contains(var21)) {
                  String var22 = var20.getDefaultValue();
                  if (var22 != null) {
                     Datatype var24 = var20.getType();
                     Object var23;
                     if (var24.equals(Datatype.String)) {
                        var23 = var24.convert(var22);
                     } else if (!var24.equals(Datatype.List) && !var24.equals(Datatype.Object) && !var24.equals(Datatype.Set) && !var24.equals(Datatype.Map)) {
                        var23 = var24.convert(var22);
                     } else {
                        var23 = JsonBuilder.getInstance().buildComplexObject(var22, var4);
                     }

                     var3.put(var21, var23);
                  }
               }
            }

         }
      }
   }

   public void loadTestVariableCategories(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      KnowledgePackage var3 = this.c(var1);
      if (var3 != null) {
         List var6 = var3.getVariableCategories();
         this.a(var2, var6);
      } else {
         KnowledgeBase var4 = this.d(var1);
         List var5 = var4.getResourceLibrary().getVariableCategories();
         this.a(var2, var5);
      }
   }

   private KnowledgePackage c(HttpServletRequest var1) throws ServletException, IOException {
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

   private KnowledgeBase d(HttpServletRequest var1) throws IOException {
      String var2 = var1.getParameter("files");
      var2 = Utils.decodeURL(var2);
      ResourceBase var3 = ServiceUtils.getKnowledgeBuilder().newResourceBase();
      String[] var4 = var2.split(";");

      for(String var8 : var4) {
         var3.addResource(var8);
      }

      KnowledgeBase var10 = ServiceUtils.getKnowledgeBuilder().buildKnowledgeBase(var3);
      return var10;
   }

   public String url() {
      return "/quicktest";
   }
}
