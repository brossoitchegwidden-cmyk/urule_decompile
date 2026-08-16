package com.bstek.urule.runtime;

import com.bstek.urule.ClassUtils;
import com.bstek.urule.SystemUtils;
import com.bstek.urule.Utils;
import com.bstek.urule.exception.RuleException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLClassLoader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;

public class DynamicSpringConfigLoaderImpl implements DynamicSpringConfigLoader, ApplicationContextAware {
   private Logger a = Logger.getGlobal();
   private static final long b = new Date().getTime();
   private static long c;
   private String d;
   private static String e;
   private static long f;
   private static String g;
   private static String h;
   private static String i;
   private String j;
   private String k;
   private String l;
   private String m;
   private ApplicationContext n;
   private ClassLoader o;
   private URLClassLoader p;
   private DynamicJarCreator q;
   private BuiltInActionLibraryBuilder r;
   private RemoteDynamicJarsBuilder s;

   public void setApplicationContext(ApplicationContext var1) throws BeansException {
      this.o = var1.getClassLoader();
      this.n = var1;
      Collection var2 = var1.getBeansOfType(DynamicJarCreator.class).values();
      if (var2.size() > 0) {
         this.q = (DynamicJarCreator)var2.iterator().next();
      }

      try {
         if (Utils.getApplicationContext() == null) {
            Utils.resetApplicationContext(var1);
         }

         this.a();
      } catch (Exception var4) {
         throw new RuleException(var4);
      }
   }

   private void a() throws Exception {
      String var1 = this.buildDynamicJarsStoreDirectPath();
      File var2 = new File(this.k);
      this.a(var2, true);
      boolean var3 = false;
      if (StringUtils.isNotBlank(this.s.getResporityServerUrl())) {
         try {
            var3 = this.s.requestRemoteJars(var1);
            this.s.startIntervalLoadRemoteJars(this);
            System.out.println("Load hot deployed jars successfully from server : " + this.s.getResporityServerUrl());
         } catch (Exception var5) {
            var5.printStackTrace();
            System.out.println("Load hot deployed jars was fail from server : " + this.s.getResporityServerUrl());
         }
      } else if (this.q != null) {
         var3 = this.q.doCreate(var1);
      }

      if (var3) {
         this.loadDynamicJars(var1);
      }

      i = Secret.b.a();
      this.b();
      this.d();
   }

   private void b() {
      String var1 = Secret.b.c();
      String var2 = this.c();
      this.d = Secret.b.b(var2);
      String var3 = Secret.b.a(var1);
      String var4 = Secret.b.a(var1, var2);
      StringBuilder var5 = new StringBuilder();
      var5.append("{");
      var5.append("\"" + Secret.b.a("a2V5", true) + "\":\"" + var3 + "\",");
      var5.append("\"" + Secret.b.a("ZGF0YQ==", true) + "\":\"" + var4 + "\"");
      var5.append("}");

      try {
         g = Base64.getEncoder().encodeToString(var5.toString().getBytes("UTF-8"));
         Secret.b.c(g);
      } catch (UnsupportedEncodingException var7) {
         throw new RuleException(var7);
      }
   }

   private String c() {
      String var1 = SystemUtils.OS_NAME;
      String var2 = SystemUtils.OS_VERSION;
      String var3 = SystemUtils.JAVA_VENDOR;
      String var4 = SystemUtils.JAVA_VERSION;
      return Secret.b.a(var1, var2, var3, var4);
   }

   private void d() throws Exception {
      boolean var1 = false;
      ObjectMapper var2 = JsonMapper.builder().build();
      String var3 = Secret.b.a("dXJ1bGUtbGljZW5zZQ==", true);
      String var4 = var3 + Secret.b.a("LnR4dA==", true);

      for (int var5 = 0; var5 <= 10; var5++) {
         if (var5 > 0) {
            var4 = var3 + var5 + Secret.b.a("LnR4dA==", true);
         }

         String var6 = this.a(var4);
         if (var6 != null) {
            var1 = this.a(var6, var2, var4);
            if (var1) {
               break;
            }
         }
      }

      if (!var1) {
         Secret.b.b();
      }
   }

   private String a(String var1) {
      String var2 = null;

      try {
         Resource var3 = this.n.getResource("classpath:" + var1);
         if (var3 != null) {
            InputStream var4 = var3.getInputStream();
            var2 = IOUtils.toString(var4);
            var4.close();
         }
      } catch (Exception var5) {
      }

      return var2;
   }

   private boolean a(String var1, ObjectMapper var2, String var3) throws Exception {
      HashMap var4 = this.b(var1, var2, var3);
      if (var4 == null) {
         return false;
      }

      KnowledgeSessionFactory.a(true);
      h = (String)var4.get(Secret.b.a("dG8=", true));
      KnowledgeSessionFactory.a(var4);
      f = Long.valueOf(var4.get(Secret.b.a("bGltaXQ=", true)).toString());
      if (f == -1L) {
         e = Secret.b.a("VW5saW1pdGVk", true);
      } else {
         Calendar var5 = Calendar.getInstance();
         var5.setTimeInMillis(f);
         Date var6 = var5.getTime();
         e = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(var6);
      }

      Secret.b.b(h, e);
      String var7 = "TGljZW5zZSBmaWxlWw==";
      String var8 = "XSBpcyB2YWxpZA==";
      System.out.println(Secret.b.a(var7, true) + var3 + Secret.b.a(var8, true));
      return true;
   }

   private HashMap<?, ?> b(String var1, ObjectMapper var2, String var3) {
      try {
         byte[] var4 = Base64.getDecoder().decode(var1);
         HashMap var5 = (HashMap)var2.readValue(var4, HashMap.class);
         byte[] var6 = Base64.getDecoder().decode((String)var5.get(Secret.b.a("a2V5", true)));
         byte[] var7 = Secret.b.a(var6);
         byte[] var8 = Base64.getDecoder().decode((String)var5.get(Secret.b.a("ZGF0YQ==", true)));
         byte[] var9 = Secret.b.a(var7, var8);
         HashMap var10 = (HashMap)var2.readValue(var9, HashMap.class);
         String var11 = Secret.b.a(var10);
         if (!this.d.contentEquals(var11)) {
            String var12 = "TGljZW5zZSBmaWxlWw==";
            String var13 = "XSBpcyBpbnZhbGlk";
            System.err.println(Secret.b.a(var12, true) + var3 + Secret.b.a(var13, true));
            return null;
         } else {
            return var10;
         }
      } catch (Exception var14) {
         System.err.println("License file[" + var3 + "] is broken.");
         return null;
      }
   }

   private void a(File var1, boolean var2) {
      for (File var6 : var1.listFiles()) {
         if (var6.isFile()) {
            var6.delete();
         } else {
            this.a(var6, false);
         }
      }

      if (!var2) {
         var1.delete();
      }
   }

   @Override
   public void loadDynamicJars(String var1) throws Exception {
      if (var1 == null) {
         this.a.warning("Dynamic jars store path not specify,so do not load jars...");
      } else {
         this.l = var1;
         AutowireCapableBeanFactory var2 = this.n.getAutowireCapableBeanFactory();
         if (!(var2 instanceof DefaultListableBeanFactory)) {
            this.a.warning("Current \"" + var2 + "\" is not DefaultListableBeanFactory type,so can not loading dynamic jars.");
         } else {
            System.out.println("Start loading dynamic jars,this will take a faw seconds...");
            File var3 = new File(this.l);
            File[] var4 = var3.listFiles();
            if (var4 == null) {
               this.a.warning("Dynamic dir [" + this.l + "] has no files.");
            } else {
               DefaultListableBeanFactory var5 = (DefaultListableBeanFactory)var2;
               ArrayList<URL> var6 = new ArrayList();
               ArrayList<UrlResource> var7 = new ArrayList();

               for (File var11 : var4) {
                  String var12 = var11.getName();
                  if (var12.toLowerCase().endsWith(".jar")) {
                     URL var13 = var11.toURI().toURL();
                     var6.add(var13);
                     String var14 = var11.getAbsolutePath() + "!/urule-spring-context.xml";
                     if (var14.startsWith("/")) {
                        var14 = var14.substring(1, var14.length());
                     }

                     String var15 = "jar:file:/" + var14;
                     UrlResource var16 = new UrlResource(new URL(var15));
                     if (var16.exists()) {
                        var7.add(var16);
                     }
                  }
               }

               URL[] var18 = var6.toArray(new URL[var6.size()]);
               URLClassLoader var19 = new URLClassLoader(var18, this.o);

               try {
                  var5.setBeanClassLoader(var19);

                  for (UrlResource var21 : (Iterable<UrlResource>)(Iterable<?>)(var7)) {
                     XmlBeanDefinitionReader var22 = new XmlBeanDefinitionReader(var5);
                     var22.loadBeanDefinitions(var21);
                  }
               } catch (Exception var17) {
                  var5.setBeanClassLoader(this.o);
                  throw new RuleException(var17);
               }

               if (this.p != null) {
                  this.p.close();
               }

               this.p = var19;
               System.out.println("Loading dynamic jars successfully...");
               this.r.buildActions(this.n);
               this.b(var3.getName());
               ClassUtils.cleanClassesCache();
            }
         }
      }
   }

   private void b(String var1) {
      try {
         File var2 = new File(this.k);
         File[] var9 = var2.listFiles();
         if (var9 == null) {
            return;
         }

         for (File var7 : var9) {
            if (!var7.getName().equals(var1)) {
               this.a(var7, false);
            }
         }
      } catch (Exception var8) {
         String var3 = var8.getMessage();
         if (var3 == null) {
            var3 = NullPointerException.class.getName();
         }

         this.a.warning("Clean dynamic jars store path was fail:" + var3);
      }
   }

   @Override
   public String buildDynamicJarsStoreDirectPath() {
      SimpleDateFormat var1 = new SimpleDateFormat("yyyy-MM-dd-HHmmss");
      return this.e() + "/" + var1.format(new Date());
   }

   private final String e() {
      if (this.k != null) {
         return this.k;
      }

      String var1 = this.j;
      if (StringUtils.isBlank(var1)) {
         var1 = System.getProperty("java.io.tmpdir");
         if (!var1.endsWith("/")) {
            var1 = var1 + "/";
         }

         var1 = var1 + "urule-jars";
      }

      String var2 = System.getProperty("urule.instance.id");
      if (StringUtils.isNotBlank(var2)) {
         var1 = var1 + "/" + var2;
      }

      File var3 = new File(var1);
      if (!var3.exists()) {
         var3.mkdirs();
      }

      this.k = var1;
      return var1;
   }

   @Override
   public byte[] zipDynamicJars() throws IOException, FileNotFoundException {
      String var1 = this.getDynamicJarsStoreDirectPath();
      if (var1 == null) {
         throw new RuleException("Current jars dir not exist.");
      }

      ByteArrayOutputStream var2 = new ByteArrayOutputStream();
      ZipOutputStream var3 = new ZipOutputStream(var2);
      File var4 = new File(var1);

      for (File var8 : var4.listFiles()) {
         var3.putNextEntry(new ZipEntry(var8.getName()));
         FileInputStream var9 = new FileInputStream(var8);
         IOUtils.copy(var9, var3);
         IOUtils.closeQuietly(var9);
      }

      var3.finish();
      var3.flush();
      var3.closeEntry();
      var3.close();
      byte[] var10 = var2.toByteArray();
      IOUtils.closeQuietly(var2);
      return var10;
   }

   public void setRemoteDynamicJarsBuilder(RemoteDynamicJarsBuilder var1) {
      this.s = var1;
   }

   @Override
   public String getDynamicJarsStoreDirectPath() {
      return this.l;
   }

   @Override
   public String getDynamicJarsIdDigest() {
      return this.m;
   }

   @Override
   public void resetDynamicJarsIdDigest(String var1) {
      this.m = var1;
   }

   public void setDynamicJarsPath(String var1) {
      this.j = var1;
   }

   public void setBuiltInActionLibraryBuilder(BuiltInActionLibraryBuilder var1) {
      this.r = var1;
   }

   public static String getAuthInfo() {
      return h;
   }

   public static long getLimit() {
      return f;
   }

   public static String getLimitDate() {
      return e;
   }

   public static String getLicenseKey() {
      return g;
   }

   public static String getProductVersion() {
      return i;
   }

   public static long getTrialExpired() {
      return c;
   }

   static {
      Random var0 = new Random();
      int var1 = var0.nextInt(20);
      long var2 = 86400000 * var1;
      if (var1 < 1) {
         var2 = 43200000L;
      }

      c = b + var2;
   }
}
