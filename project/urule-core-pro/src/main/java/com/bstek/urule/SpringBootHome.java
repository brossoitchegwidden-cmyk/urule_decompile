package com.bstek.urule;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.security.CodeSource;
import java.security.ProtectionDomain;
import java.util.Enumeration;
import java.util.jar.JarFile;
import java.util.jar.Manifest;
import org.springframework.util.StringUtils;

public class SpringBootHome {
   public File findSpringbootJarHomeDir(Class<?> sourceClass) {
      File file = this.locateApplicationHome(sourceClass != null ? sourceClass : this.resolveClass());
      return this.locateApplicationHome(file);
   }

   private File locateApplicationHome(File file) {
      File parentFile = file;
      parentFile = parentFile != null ? parentFile : this.locateApplicationHome();
      if (parentFile.isFile()) {
         parentFile = parentFile.getParentFile();
      }

      parentFile = parentFile.exists() ? parentFile : new File(".");
      return parentFile.getAbsoluteFile();
   }

   private File locateApplicationHome() {
      String property = System.getProperty("user.dir");
      return new File(StringUtils.hasLength(property) ? property : ".");
   }

   private Class<?> resolveClass() {
      try {
         ClassLoader classLoader = this.getClass().getClassLoader();
         return this.locateApplicationHome(classLoader.getResources("META-INF/MANIFEST.MF"));
      } catch (Exception exception) {
         return null;
      }
   }

   private Class<?> locateApplicationHome(Enumeration<URL> enumeration) {
      while (enumeration.hasMoreElements()) {
         try (InputStream inputStream = ((URL)enumeration.nextElement()).openStream()) {
            Manifest manifest = new Manifest(inputStream);
            String text = manifest.getMainAttributes().getValue("Start-Class");
            if (text != null) {
               return org.springframework.util.ClassUtils.forName(text, this.getClass().getClassLoader());
            }
         } catch (Exception exception) {
         }
      }

      return null;
   }

   private File locateApplicationHome(Class<?> valueType) {
      try {
         ProtectionDomain protectionDomain = valueType != null ? valueType.getProtectionDomain() : null;
         CodeSource codeSource = protectionDomain != null ? protectionDomain.getCodeSource() : null;
         URL uRL = codeSource != null ? codeSource.getLocation() : null;
         File file = uRL != null ? this.locateApplicationHome(uRL) : null;
         return file != null && file.exists() && !this.evaluateCondition() ? file.getAbsoluteFile() : null;
      } catch (Exception exception) {
         return null;
      }
   }

   private File locateApplicationHome(URL uRL) throws IOException {
      URLConnection uRLConnection = uRL.openConnection();
      return uRLConnection instanceof JarURLConnection ? this.locateApplicationHome(((JarURLConnection)uRLConnection).getJarFile()) : new File(uRL.getPath());
   }

   private File locateApplicationHome(JarFile jarFile) {
      String name = jarFile.getName();
      int number = name.indexOf("!/");
      if (number > 0) {
         name = name.substring(0, number);
      }

      return new File(name);
   }

   private boolean evaluateCondition() {
      try {
         StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();

         for (int index = stackTrace.length - 1; index >= 0; index--) {
            if (stackTrace[index].getClassName().startsWith("org.junit.")) {
               return true;
            }
         }
      } catch (Exception exception) {
      }

      return false;
   }
}
