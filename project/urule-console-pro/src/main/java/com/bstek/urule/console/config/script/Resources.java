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
   private static ClassLoaderWrapper defaultClassLoader = new ClassLoaderWrapper();
   private static Charset charset;

   Resources() {
   }

   public static ClassLoader getDefaultClassLoader() {
      return Resources.defaultClassLoader.defaultClassLoader;
   }

   public static void setDefaultClassLoader(ClassLoader defaultClassLoader) {
      Resources.defaultClassLoader.defaultClassLoader = defaultClassLoader;
   }

   public static URL getResourceURL(String resource) throws IOException {
      return getResourceURL((ClassLoader)null, resource);
   }

   public static URL getResourceURL(ClassLoader loader, String resource) throws IOException {
      URL resourceAsURL = Resources.defaultClassLoader.getResourceAsURL(resource, loader);
      if (resourceAsURL == null) {
         throw new IOException("Could not find resource " + resource);
      } else {
         return resourceAsURL;
      }
   }

   public static InputStream getResourceAsStream(String resource) throws IOException {
      return getResourceAsStream((ClassLoader)null, resource);
   }

   public static InputStream getResourceAsStream(ClassLoader loader, String resource) throws IOException {
      InputStream resourceAsStream = Resources.defaultClassLoader.getResourceAsStream(resource, loader);
      if (resourceAsStream == null) {
         throw new IOException("Could not find resource " + resource);
      } else {
         return resourceAsStream;
      }
   }

   public static Properties getResourceAsProperties(String resource) throws IOException {
      Properties properties = new Properties();
      InputStream resourceAsStream = getResourceAsStream(resource);
      properties.load(resourceAsStream);
      resourceAsStream.close();
      return properties;
   }

   public static Properties getResourceAsProperties(ClassLoader loader, String resource) throws IOException {
      Properties properties = new Properties();
      InputStream resourceAsStream = getResourceAsStream(loader, resource);
      properties.load(resourceAsStream);
      resourceAsStream.close();
      return properties;
   }

   public static Reader getResourceAsReader(String resource) throws IOException {
      InputStreamReader inputStreamReader;
      if (Resources.charset == null) {
         inputStreamReader = new InputStreamReader(getResourceAsStream(resource));
      } else {
         inputStreamReader = new InputStreamReader(getResourceAsStream(resource), Resources.charset);
      }

      return inputStreamReader;
   }

   public static Reader getResourceAsReader(ClassLoader loader, String resource) throws IOException {
      InputStreamReader inputStreamReader;
      if (Resources.charset == null) {
         inputStreamReader = new InputStreamReader(getResourceAsStream(loader, resource));
      } else {
         inputStreamReader = new InputStreamReader(getResourceAsStream(loader, resource), Resources.charset);
      }

      return inputStreamReader;
   }

   public static File getResourceAsFile(String resource) throws IOException {
      return new File(getResourceURL(resource).getFile());
   }

   public static File getResourceAsFile(ClassLoader loader, String resource) throws IOException {
      return new File(getResourceURL(loader, resource).getFile());
   }

   public static InputStream getUrlAsStream(String urlString) throws IOException {
      URL uRL = new URL(urlString);
      URLConnection uRLConnection = uRL.openConnection();
      return uRLConnection.getInputStream();
   }

   public static Reader getUrlAsReader(String urlString) throws IOException {
      InputStreamReader inputStreamReader;
      if (Resources.charset == null) {
         inputStreamReader = new InputStreamReader(getUrlAsStream(urlString));
      } else {
         inputStreamReader = new InputStreamReader(getUrlAsStream(urlString), Resources.charset);
      }

      return inputStreamReader;
   }

   public static Properties getUrlAsProperties(String urlString) throws IOException {
      Properties properties = new Properties();
      InputStream urlAsStream = getUrlAsStream(urlString);
      properties.load(urlAsStream);
      urlAsStream.close();
      return properties;
   }

   public static Class classForName(String className) throws ClassNotFoundException {
      return Resources.defaultClassLoader.classForName(className);
   }

   public static Charset getCharset() {
      return Resources.charset;
   }

   public static void setCharset(Charset charset) {
      Resources.charset = charset;
   }
}
