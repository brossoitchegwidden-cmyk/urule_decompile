package com.bstek.urule.runtime.service;

import com.bstek.urule.Utils;
import com.bstek.urule.exception.RuleException;
import com.bstek.urule.model.flow.FlowDefinition;
import com.bstek.urule.model.rete.JsonUtils;
import com.bstek.urule.runtime.ArgumentsProvider;
import com.bstek.urule.runtime.KnowledgePackage;
import com.bstek.urule.runtime.KnowledgePackageImpl;
import com.bstek.urule.runtime.KnowledgePackageWrapper;
import com.bstek.urule.runtime.RemoteDynamicJarsBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class RemoteServiceImpl implements RemoteService, ApplicationContextAware {
   private String a;
   private RemoteDynamicJarsBuilder b;
   private ArgumentsProvider c;
   private Logger d = Logger.getGlobal();

   @Override
   public KnowledgePackage getKnowledge(String var1, String var2) {
      if (StringUtils.isEmpty(this.a())) {
         return null;
      }

      this.d.info("Load knowledgepackage [" + var1 + "] from remote server 【" + this.a() + "】...");
      String var3 = this.a(var1, var2);
      if (StringUtils.isEmpty(var3)) {
         return null;
      }

      KnowledgePackageWrapper var4 = JsonUtils.parseKnowledgePackageWrapper(var3);
      KnowledgePackageImpl var5 = (KnowledgePackageImpl)var4.getKnowledgePackage();
      Map var6 = var5.getFlowMap();
      if (var6 != null && var6.size() > 0) {
         for (FlowDefinition var8 : (Iterable<FlowDefinition>)(Iterable<?>)(var6.values())) {
            var8.buildConnectionToNode();
         }
      }

      var5.resetTimestamp();
      var5.setPackageInfo(var1);
      return var5;
   }

   private String a(String var1, String var2) {
      HttpURLConnection var3 = null;
      OutputStreamWriter var4 = null;
      InputStream var5 = null;
      Object var6 = null;
      Object var7 = null;

      try {
         var1 = Utils.encodeURL(var1);
         var1 = Utils.encodeURL(var1);
         String var8 = "packageId=" + var1 + "";
         if (StringUtils.isNotEmpty(var2)) {
            var8 = var8 + "&timestamp=" + var2 + "";
         }

         var8 = var8 + "&_u=" + Utils.encodeURL(this.b.getUser()) + "";
         var8 = var8 + "&_p=" + Utils.encodeURL(this.b.getPwd()) + "";
         URL var9 = new URL(this.a());
         var3 = (HttpURLConnection)var9.openConnection();
         var3.setRequestMethod("POST");
         var3.setRequestProperty("Accept-Charset", "utf-8");
         var3.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
         var3.setRequestProperty("Content-Length", String.valueOf(var8.length()));
         var3.setUseCaches(false);
         var3.setDoOutput(true);
         var3.connect();
         var4 = new OutputStreamWriter(var3.getOutputStream());
         var4.write(var8);
         var4.flush();
         if (var3.getResponseCode() != 200) {
            String var21 = var3.getHeaderField("errorMsg");
            if (var21 != null) {
               var21 = URLDecoder.decode(var21, "utf-8");
            }

            String var11 = this.a(var3.getErrorStream());
            if (var11 != null) {
               var21 = var11;
            }

            throw new RuleException("Server request was failed : " + var21);
         } else {
            var5 = var3.getInputStream();
            byte[] var10 = IOUtils.toByteArray(var5);
            return Utils.uncompress(var10);
         }
      } catch (Exception var15) {
         throw new RuleException(var15);
      } finally {
         IOUtils.closeQuietly(var4);
         IOUtils.closeQuietly((Reader)var7);
         IOUtils.closeQuietly((Reader)var6);
         IOUtils.closeQuietly(var5);
         if (var3 != null) {
            var3.disconnect();
         }
      }
   }

   private String a(InputStream var1) {
      try {
         if (var1 != null) {
            ObjectMapper var2 = new ObjectMapper();
            Map var3 = (Map)var2.readValue(var1, HashMap.class);
            return (String)var3.get("stack");
         } else {
            return null;
         }
      } catch (Exception var8) {
         return null;
      } finally {
         IOUtils.closeQuietly(var1);
      }
   }

   private String a() {
      if (this.c != null && this.c.resporityServerUrl() != null) {
         String var1 = this.c.resporityServerUrl();
         if (var1.endsWith("/")) {
            var1 = var1 + "urule/loadknowledge";
         } else {
            var1 = var1 + "/urule/loadknowledge";
         }

         return var1;
      } else {
         return this.a;
      }
   }

   public void setResporityServerUrl(String var1) {
      System.out.println("urule.resporityServerUrl:" + var1);
      if (!StringUtils.isEmpty(var1) && !var1.equals("urule.resporityServerUrl")) {
         this.a(var1);
      }
   }

   private void a(String var1) {
      if (var1.endsWith("/")) {
         var1 = var1 + "urule/loadknowledge";
      } else {
         var1 = var1 + "/urule/loadknowledge";
      }

      this.a = var1;
   }

   public void setApplicationContext(ApplicationContext var1) throws BeansException {
      Collection var2 = var1.getBeansOfType(ArgumentsProvider.class).values();
      if (var2.size() != 0) {
         this.c = (ArgumentsProvider)var2.iterator().next();
      }
   }

   public void setRemoteDynamicJarsBuilder(RemoteDynamicJarsBuilder var1) {
      this.b = var1;
   }
}
