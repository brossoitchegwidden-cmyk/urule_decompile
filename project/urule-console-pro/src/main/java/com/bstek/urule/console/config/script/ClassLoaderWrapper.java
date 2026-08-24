package com.bstek.urule.console.config.script;

import java.io.InputStream;
import java.net.URL;

public class ClassLoaderWrapper {
   ClassLoader defaultClassLoader;
   ClassLoader systemClassLoader;

   ClassLoaderWrapper() {
      try {
         this.systemClassLoader = ClassLoader.getSystemClassLoader();
      } catch (SecurityException securityException) {
      }

   }

   public URL getResourceAsURL(String resource) {
      return this.getResourceUrl(resource, this.getClassLoaders(null));
   }

   public URL getResourceAsURL(String resource, ClassLoader classLoader) {
      return this.getResourceUrl(resource, this.getClassLoaders(classLoader));
   }

   public InputStream getResourceAsStream(String resource) {
      return this.getResourceStream(resource, this.getClassLoaders(null));
   }

   public InputStream getResourceAsStream(String resource, ClassLoader classLoader) {
      return this.getResourceStream(resource, this.getClassLoaders(classLoader));
   }

   public Class classForName(String name) throws ClassNotFoundException {
      return this.loadClass(name, this.getClassLoaders(null));
   }

   public Class classForName(String name, ClassLoader classLoader) throws ClassNotFoundException {
      return this.loadClass(name, this.getClassLoaders(classLoader));
   }

   InputStream getResourceStream(String resourcePath, ClassLoader[] classLoaders) {
      for(ClassLoader classLoader2 : classLoaders) {
         if (null != classLoader2) {
            InputStream resourceAsStream = classLoader2.getResourceAsStream(resourcePath);
            if (null == resourceAsStream) {
               resourceAsStream = classLoader2.getResourceAsStream("/" + resourcePath);
            }

            if (null != resourceAsStream) {
               return resourceAsStream;
            }
         }
      }

      return null;
   }

   URL getResourceUrl(String resourcePath, ClassLoader[] classLoaders) {
      for(ClassLoader classLoader2 : classLoaders) {
         if (null != classLoader2) {
            URL resource = classLoader2.getResource(resourcePath);
            if (null == resource) {
               resource = classLoader2.getResource("/" + resourcePath);
            }

            if (null != resource) {
               return resource;
            }
         }
      }

      return null;
   }

   Class loadClass(String className, ClassLoader[] classLoaders) throws ClassNotFoundException {
      for(ClassLoader classLoader2 : classLoaders) {
         if (null != classLoader2) {
            try {
               Class valueType = Class.forName(className, true, classLoader2);
               if (null != valueType) {
                  return valueType;
               }
            } catch (ClassNotFoundException classNotFoundException) {
            }
         }
      }

      throw new ClassNotFoundException("Cannot find class: " + className);
   }

   ClassLoader[] getClassLoaders(ClassLoader preferredClassLoader) {
      return new ClassLoader[]{preferredClassLoader, this.defaultClassLoader, Thread.currentThread().getContextClassLoader(), this.getClass().getClassLoader(), this.systemClassLoader};
   }
}
