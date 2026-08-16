package com.bstek.urule.runtime;

import com.bstek.urule.exception.RuleException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class RemoteDynamicJarsBuilder implements ApplicationContextAware {
   private String a;
   private String b;
   private String c;
   private String d;
   private int e;
   private String f;
   private Timer g;
   public static final String NONE = "n";
   private Logger h = Logger.getGlobal();
   private ArgumentsProvider i;
   public static final String BEAN_ID = "urule.remoteDynamicJarsBuilder";

   public boolean startIntervalLoadRemoteJars(DynamicSpringConfigLoader var1) throws Exception {
      if (this.e == 0) {
         return false;
      }

      if (this.g == null) {
         this.g = new Timer();
      }

      this.h.info("Start loading dynamic jars from server [" + this.getResporityServerUrl() + "] every " + this.e + " minutes.");
      int var2 = this.e * 1000 * 60;
      this.g.schedule(new IntervalTask(var1, this), var2, var2);
      return true;
   }

   public boolean requestRemoteJars(String var1) throws Exception {
      String var2 = this.a();
      if (var2.equals("n")) {
         this.f = var2;
         return false;
      }

      if (this.f != null && this.f.equals(var2)) {
         return false;
      }

      System.out.println("Start pull dynamic jars from server side.");
      this.f = var2;
      HttpURLConnection var3 = null;
      OutputStreamWriter var4 = null;
      InputStream var5 = null;
      java.io.Closeable var6 = null;
      java.io.Closeable var7 = null;

      try {
         String var8 = "_u=" + URLEncoder.encode(this.a, "utf-8") + "&_p=" + URLEncoder.encode(this.b, "utf-8") + "";
         URL var9 = new URL(this.getResporityServerUrl());
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
            throw new RuleException("Server request was failed, Response message : " + var3.getResponseMessage());
         }

         var5 = var3.getInputStream();
         this.unzipDynamicJars(var5, var1);
      } catch (Exception var17) {
         throw new RuleException(var17);
      } finally {
         try {
            if (var4 != null) {
               var4.close();
            }

            if (var7 != null) {
               var7.close();
            }

            if (var6 != null) {
               var6.close();
            }

            if (var5 != null) {
               var5.close();
            }
         } catch (IOException var16) {
            var16.printStackTrace();
         }

         if (var3 != null) {
            var3.disconnect();
         }
      }

      return true;
   }

   private String a() {
      try {
         String var1 = this.f == null ? "" : this.f;
         String var2 = "_u=" + this.a + "&_p=" + this.b + "&jarsId=" + var1 + "";
         URL var3 = new URL(this.b());
         HttpURLConnection var4 = (HttpURLConnection)var3.openConnection();
         var4.setRequestMethod("POST");
         var4.setRequestProperty("Accept-Charset", "utf-8");
         var4.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
         var4.setRequestProperty("Content-Length", String.valueOf(var2.length()));
         var4.setUseCaches(false);
         var4.setDoOutput(true);
         var4.connect();
         OutputStreamWriter var5 = new OutputStreamWriter(var4.getOutputStream());
         var5.write(var2);
         var5.flush();
         if (var4.getResponseCode() != 200) {
            throw new RuleException("Server request was failed, Response message : " + var4.getResponseMessage());
         }

         InputStream var6 = var4.getInputStream();
         ObjectMapper var7 = JsonMapper.builder().build();
         Map var8 = (Map)var7.readValue(var6, HashMap.class);
         IOUtils.closeQuietly(var6);
         var4.disconnect();
         var5.close();
         boolean var9 = (Boolean)var8.get("match");
         return var9 ? "n" : (String)var8.get("digest");
      } catch (Exception var11) {
         throw new RuleException("当前环境配置了【" + this.b() + "】参数，但访问对应的服务器时出现错误", var11);
      }
   }

   public void unzipDynamicJars(InputStream var1, String var2) throws Exception {
      File var3 = new File(var2);
      if (!var3.exists()) {
         var3.mkdirs();
      }

      ZipInputStream var4 = new ZipInputStream(var1);

      for (ZipEntry var5 = var4.getNextEntry(); var5 != null; var5 = var4.getNextEntry()) {
         String var6 = var2 + "/" + var5.getName();
         File var7 = new File(var6);
         FileOutputStream var8 = new FileOutputStream(var7);
         int var9 = 0;
         byte[] var10 = new byte[1024];

         while ((var9 = var4.read(var10)) != -1) {
            var8.write(var10, 0, var9);
         }

         IOUtils.closeQuietly(var8);
      }

      var4.closeEntry();
      var4.close();
   }

   public void destroy() {
      if (this.g != null) {
         this.g.cancel();
      }
   }

   private String b() {
      if (this.i != null && this.i.resporityServerUrl() != null) {
         String var1 = this.i.resporityServerUrl();
         if (var1.endsWith("/")) {
            var1 = var1 + "urule/dynamic/checkLatestJarsDir";
         } else {
            var1 = var1 + "/urule/dynamic/checkLatestJarsDir";
         }

         return var1;
      } else {
         return this.d;
      }
   }

   public String getResporityServerUrl() {
      if (this.i != null && this.i.resporityServerUrl() != null) {
         String var1 = this.i.resporityServerUrl();
         if (var1.endsWith("/")) {
            var1 = var1 + "urule/dynamic/loadDynamicJars";
         } else {
            var1 = var1 + "/urule/dynamic/loadDynamicJars";
         }

         return var1;
      } else {
         return this.c;
      }
   }

   public void setRemoteLoadInterval(int var1) {
      this.e = var1;
   }

   public void setResporityServerUrl(String var1) {
      if (!StringUtils.isEmpty(var1) && !var1.equals("urule.resporityServerUrl")) {
         this.a(var1);
      }
   }

   private void a(String var1) {
      if (var1.endsWith("/")) {
         this.d = var1 + "urule/dynamic/checkLatestJarsDir";
         var1 = var1 + "urule/dynamic/loadDynamicJars";
      } else {
         this.d = var1 + "/urule/dynamic/checkLatestJarsDir";
         var1 = var1 + "/urule/dynamic/loadDynamicJars";
      }

      this.c = var1;
   }

   public void setApplicationContext(ApplicationContext var1) throws BeansException {
      Collection var2 = var1.getBeansOfType(ArgumentsProvider.class).values();
      if (var2.size() != 0) {
         this.i = (ArgumentsProvider)var2.iterator().next();
      }
   }

   public String getUser() {
      return this.a;
   }

   public String getPwd() {
      return this.b;
   }

   public void setUser(String var1) {
      this.a = var1;
   }

   public void setPwd(String var1) {
      this.b = var1;
   }
}
