package com.bstek.urule.console.config.script;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.Properties;

public class Resources {
   private static ClassLoaderWrapper a = new ClassLoaderWrapper();
   private static Charset b;

   Resources() {
   }

   public static ClassLoader getDefaultClassLoader() {
      return a.a;
   }

   public static void setDefaultClassLoader(ClassLoader var0) {
      a.a = var0;
   }

   public static URL getResourceURL(String var0) throws IOException {
      return getResourceURL((ClassLoader)null, var0);
   }

   public static URL getResourceURL(ClassLoader var0, String var1) throws IOException {
      URL var2 = a.getResourceAsURL(var1, var0);
      if (var2 == null) {
         throw new IOException("Could not find resource " + var1);
      } else {
         return var2;
      }
   }

   public static InputStream getResourceAsStream(String var0) throws IOException {
      return getResourceAsStream((ClassLoader)null, var0);
   }

   public static InputStream getResourceAsStream(ClassLoader var0, String var1) throws IOException {
      InputStream var2 = a.getResourceAsStream(var1, var0);
      if (var2 == null) {
         throw new IOException("Could not find resource " + var1);
      } else {
         return var2;
      }
   }

   public static Properties getResourceAsProperties(String var0) throws IOException {
      Properties var1 = new Properties();
      InputStream var2 = getResourceAsStream(var0);
      var1.load(var2);
      var2.close();
      return var1;
   }

   public static Properties getResourceAsProperties(ClassLoader var0, String var1) throws IOException {
      Properties var2 = new Properties();
      InputStream var3 = getResourceAsStream(var0, var1);
      var2.load(var3);
      var3.close();
      return var2;
   }

   public static Reader getResourceAsReader(String var0) throws IOException {
      InputStreamReader var1;
      if (b == null) {
         var1 = new InputStreamReader(getResourceAsStream(var0));
      } else {
         var1 = new InputStreamReader(getResourceAsStream(var0), b);
      }

      return var1;
   }

   public static Reader getResourceAsReader(ClassLoader var0, String var1) throws IOException {
      InputStreamReader var2;
      if (b == null) {
         var2 = new InputStreamReader(getResourceAsStream(var0, var1));
      } else {
         var2 = new InputStreamReader(getResourceAsStream(var0, var1), b);
      }

      return var2;
   }

   public static File getResourceAsFile(String var0) throws IOException {
      return new File(getResourceURL(var0).getFile());
   }

   public static File getResourceAsFile(ClassLoader var0, String var1) throws IOException {
      return new File(getResourceURL(var0, var1).getFile());
   }

   public static InputStream getUrlAsStream(String var0) throws IOException {
      URL var1 = new URL(var0);
      URLConnection var2 = var1.openConnection();
      return var2.getInputStream();
   }

   public static Reader getUrlAsReader(String var0) throws IOException {
      InputStreamReader var1;
      if (b == null) {
         var1 = new InputStreamReader(getUrlAsStream(var0));
      } else {
         var1 = new InputStreamReader(getUrlAsStream(var0), b);
      }

      return var1;
   }

   public static Properties getUrlAsProperties(String var0) throws IOException {
      Properties var1 = new Properties();
      InputStream var2 = getUrlAsStream(var0);
      var1.load(var2);
      var2.close();
      return var1;
   }

   public static Class classForName(String var0) throws ClassNotFoundException {
      return a.classForName(var0);
   }

   public static Charset getCharset() {
      return b;
   }

   public static void setCharset(Charset var0) {
      b = var0;
   }
}
