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

   private static String t;
   private static boolean u;

   private void d() throws Exception {
      boolean var1 = false;
      ObjectMapper var2 = JsonMapper.builder().build();
      String var3 = Secret.b.a("dXJ1bGUtbGljZW5zZQ==", true);
      String var4 = var3 + Secret.b.a("LnR4dA==", true);
      t = null;

      String var5 = resolveLicenseHome();
      if (StringUtils.isNotBlank(var5)) {
         File var6 = new File(var5, var4);
         if (var6.isFile()) {
            String var7 = this.a(var6);
            if (var7 != null && this.a(var7, var2, var4)) {
               var1 = true;
               t = "home";
            }
         }
      }

      if (!var1) {
         for (int var8 = 0; var8 <= 10; var8++) {
            String var9 = var8 == 0 ? var4 : var3 + var8 + Secret.b.a("LnR4dA==", true);
            String var10 = this.a(var9);
            if (var10 != null && this.a(var10, var2, var9)) {
               var1 = true;
               t = "classpath:" + var9;
               break;
            }
         }
      }

      if (!var1) {
         Secret.b.b();
      }
   }

   private static String resolveLicenseHome() {
      String var0 = null;
      InputStream var1 = null;

      try {
         try {
            var1 = new FileInputStream("urule-init.properties");
         } catch (FileNotFoundException var8) {
            var1 = DynamicSpringConfigLoaderImpl.class.getClassLoader().getResourceAsStream("urule-init.properties");
         }

         if (var1 != null) {
            java.util.Properties var2 = new java.util.Properties();
            var2.load(var1);
            var0 = var2.getProperty("urule.home");
         }
      } catch (IOException var9) {
      } finally {
         IOUtils.closeQuietly(var1);
      }

      if (StringUtils.isBlank(var0)) {
         var0 = System.getProperty("urule.home");
      }

      if (StringUtils.isBlank(var0)) {
         var0 = System.getProperty("uruleHome");
      }

      if (StringUtils.isBlank(var0)) {
         var0 = System.getenv("URULE_HOME");
      }

      return var0;
   }

   private String a(File var1) {
      try {
         FileInputStream var2 = new FileInputStream(var1);

         try {
            return IOUtils.toString(var2, "UTF-8");
         } finally {
            var2.close();
         }
      } catch (Exception var5) {
         return null;
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

      Object var5 = var4.get(Secret.b.a("dG8=", true));
      Object var6 = var4.get(Secret.b.a("bGltaXQ=", true));
      if (!(var5 instanceof String) || StringUtils.isBlank((String)var5) || var6 == null) {
         return false;
      }

      long var7 = Long.parseLong(var6.toString());
      if (var7 < -1L) {
         return false;
      }

      KnowledgeSessionFactory.a(true);
      h = (String)var5;
      u = "portable".equals(var4.get("binding"));
      KnowledgeSessionFactory.a(var4);
      f = var7;
      if (f == -1L) {
         e = Secret.b.a("VW5saW1pdGVk", true);
      } else {
         Calendar var8 = Calendar.getInstance();
         var8.setTimeInMillis(f);
         Date var9 = var8.getTime();
         e = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(var9);
      }

      Secret.b.b(h, e);
      String var10 = "TGljZW5zZSBmaWxlWw==";
      String var11 = "XSBpcyB2YWxpZA==";
      System.out.println(Secret.b.a(var10, true) + var3 + Secret.b.a(var11, true));
      return true;
   }

   private HashMap<?, ?> b(String var1, ObjectMapper var2, String var3) {
      try {
         return d(var1, var2, this.d);
      } catch (Exception var5) {
         System.err.println("License file[" + var3 + "] is broken.");
         return null;
      }
   }

   private static HashMap<?, ?> d(String var0, ObjectMapper var1, String var2) throws Exception {
      Exception var3 = null;
      try {
         HashMap<?, ?> var4 = c(var0, var1, var2, null);
         if (var4 != null) {
            return var4;
         }
      } catch (Exception var8) {
         var3 = var8;
      }

      String var5 = supplementalPublicKey();
      if (StringUtils.isBlank(var5)) {
         if (var3 != null) {
            throw var3;
         }
         return null;
      }

      try {
         return c(var0, var1, var2, var5);
      } catch (Exception var7) {
         throw var7;
      }
   }

   private static HashMap<?, ?> c(String var0, ObjectMapper var1, String var2, String var3) throws Exception {
      byte[] var4 = Base64.getDecoder().decode(var0);
      HashMap var5 = (HashMap)var1.readValue(var4, HashMap.class);
      byte[] var6 = Base64.getDecoder().decode((String)var5.get(Secret.b.a("a2V5", true)));
      byte[] var7 = var3 == null ? Secret.b.b(var6) : Secret.b.b(var6, var3);
      byte[] var8 = Base64.getDecoder().decode((String)var5.get(Secret.b.a("ZGF0YQ==", true)));
      byte[] var9 = Secret.b.a(var7, var8);
      HashMap var10 = (HashMap)var1.readValue(var9, HashMap.class);
      if ("portable".equals(var10.get("binding"))) {
         if (var3 == null) {
            return null;
         }
         Object var11 = var10.get("licenseId");
         Object var12 = var10.get("issuedAt");
         Object var13 = var10.get("productVersion");
         if (!(var11 instanceof String) || StringUtils.isBlank((String)var11) || var12 == null || !Secret.b.a().equals(var13)) {
            return null;
         }
         long var14 = Long.parseLong(var12.toString());
         return var14 > 0L && var14 <= System.currentTimeMillis() + 300000L ? var10 : null;
      }
      String var15 = Secret.b.a(var10);
      return var2.contentEquals(var15) ? var10 : null;
   }

   private static String supplementalPublicKey() {
      String var0 = System.getProperty("urule.license.issuer.public-key");
      if (StringUtils.isBlank(var0)) {
         return null;
      }

      File var1 = new File(var0);
      if (!var1.isFile() || var1.length() < 1L || var1.length() > 4096L) {
         return null;
      }

      FileInputStream var2 = null;
      try {
         var2 = new FileInputStream(var1);
         String var3 = IOUtils.toString(var2, "UTF-8").trim();
         Base64.getDecoder().decode(var3);
         return var3;
      } catch (Exception var4) {
         return null;
      } finally {
         IOUtils.closeQuietly(var2);
      }
   }

   /**
    * Validates a license without changing the active license or session state.
    */
   public static LicenseValidationResult validateLicense(String var0) {
      if (StringUtils.isBlank(var0)) {
         return LicenseValidationResult.invalid("empty_license");
      }

      try {
         ObjectMapper var1 = JsonMapper.builder().build();
         String var2 = Secret.b.a(SystemUtils.OS_NAME, SystemUtils.OS_VERSION, SystemUtils.JAVA_VENDOR, SystemUtils.JAVA_VERSION);
         String var3 = Secret.b.b(var2);
         HashMap<?, ?> var4 = d(var0, var1, var3);
         if (var4 == null) {
            return LicenseValidationResult.invalid("signature_or_environment_invalid");
         }

         Object var5 = var4.get(Secret.b.a("dG8=", true));
         Object var6 = var4.get(Secret.b.a("bGltaXQ=", true));
         if (!(var5 instanceof String) || StringUtils.isBlank((String)var5) || var6 == null) {
            return LicenseValidationResult.invalid("required_fields_missing");
         }

         long var7 = Long.parseLong(var6.toString());
         if (var7 < -1L) {
            return LicenseValidationResult.invalid("invalid_limit");
         }
         return LicenseValidationResult.valid((String)var5, var7, "portable".equals(var4.get("binding")));
      } catch (Exception var9) {
         return LicenseValidationResult.invalid("signature_or_format_invalid");
      }
   }

   public static String getLicenseSource() {
      return t;
   }

   public static boolean isLicensePortable() {
      return u;
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
