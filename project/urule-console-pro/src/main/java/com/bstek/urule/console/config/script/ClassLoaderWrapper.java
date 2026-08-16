package com.bstek.urule.console.config.script;

import java.io.InputStream;
import java.net.URL;

public class ClassLoaderWrapper {
   ClassLoader a;
   ClassLoader b;

   ClassLoaderWrapper() {
      try {
         this.b = ClassLoader.getSystemClassLoader();
      } catch (SecurityException var2) {
      }

   }

   public URL getResourceAsURL(String var1) {
      return this.b(var1, this.a((ClassLoader)null));
   }

   public URL getResourceAsURL(String var1, ClassLoader var2) {
      return this.b(var1, this.a(var2));
   }

   public InputStream getResourceAsStream(String var1) {
      return this.a(var1, this.a((ClassLoader)null));
   }

   public InputStream getResourceAsStream(String var1, ClassLoader var2) {
      return this.a(var1, this.a(var2));
   }

   public Class classForName(String var1) throws ClassNotFoundException {
      return this.c(var1, this.a((ClassLoader)null));
   }

   public Class classForName(String var1, ClassLoader var2) throws ClassNotFoundException {
      return this.c(var1, this.a(var2));
   }

   InputStream a(String var1, ClassLoader[] var2) {
      for(ClassLoader var6 : var2) {
         if (null != var6) {
            InputStream var7 = var6.getResourceAsStream(var1);
            if (null == var7) {
               var7 = var6.getResourceAsStream("/" + var1);
            }

            if (null != var7) {
               return var7;
            }
         }
      }

      return null;
   }

   URL b(String var1, ClassLoader[] var2) {
      for(ClassLoader var7 : var2) {
         if (null != var7) {
            URL var3 = var7.getResource(var1);
            if (null == var3) {
               var3 = var7.getResource("/" + var1);
            }

            if (null != var3) {
               return var3;
            }
         }
      }

      return null;
   }

   Class c(String var1, ClassLoader[] var2) throws ClassNotFoundException {
      for(ClassLoader var6 : var2) {
         if (null != var6) {
            try {
               Class var7 = Class.forName(var1, true, var6);
               if (null != var7) {
                  return var7;
               }
            } catch (ClassNotFoundException var8) {
            }
         }
      }

      throw new ClassNotFoundException("Cannot find class: " + var1);
   }

   ClassLoader[] a(ClassLoader var1) {
      return new ClassLoader[]{var1, this.a, Thread.currentThread().getContextClassLoader(), this.getClass().getClassLoader(), this.b};
   }
}
