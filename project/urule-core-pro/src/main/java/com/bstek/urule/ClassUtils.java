package com.bstek.urule;

import com.bstek.urule.model.Label;
import com.bstek.urule.model.library.Datatype;
import com.bstek.urule.model.library.variable.Act;
import com.bstek.urule.model.library.variable.Variable;
import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;

public class ClassUtils {
   private static ConcurrentHashMap<String, Class<?>> a = new ConcurrentHashMap<>();
   private static ConcurrentHashMap<String, String> b = new ConcurrentHashMap<>();

   public static void classToXml(Class<?> var0, File var1) {
      if (!var1.exists()) {
         try {
            var1.createNewFile();
         } catch (IOException var12) {
            throw new RuntimeException(var12);
         }
      }

      FileOutputStream var2 = null;

      try {
         var2 = new FileOutputStream(var1);
         List var3 = classToVariables(var0);
         StringBuffer var4 = new StringBuffer();
         var4.append("<variables clazz=\"" + var0.getName() + "\">");

         for (Variable var6 : (Iterable<Variable>)(Iterable<?>)(var3)) {
            var4.append("<variable ");
            var4.append("name=\"" + var6.getName() + "\" ");
            if (var6.getLabel() != null) {
               var4.append("label=\"" + var6.getLabel() + "\" ");
            }

            if (var6.getDefaultValue() != null) {
               var4.append("defaultValue=\"" + var6.getDefaultValue() + "\" ");
            }

            if (var6.getType() != null) {
               var4.append("type=\"" + var6.getType() + "\" ");
            }

            if (var6.getAct() != null) {
               var4.append("act=\"" + var6.getAct() + "\" ");
            }

            var4.append(">");
            var4.append("</variable>");
         }

         var4.append("</variables>");
         Document var15 = DocumentHelper.parseText(var4.toString());
         OutputFormat var16 = OutputFormat.createPrettyPrint();
         var16.setEncoding("utf-8");
         XMLWriter var7 = new XMLWriter(var2, var16);
         var7.write(var15);
         var7.close();
         var2.close();
      } catch (Exception var13) {
         throw new RuntimeException(var13);
      } finally {
         IOUtils.closeQuietly(var2);
      }
   }

   public static List<Variable> classToVariables(Class<?> var0) {
      try {
         return a("", "", var0, new ArrayList<>());
      } catch (Exception var2) {
         throw new RuntimeException(var2);
      }
   }

   public static Class<?> doGetTargetClass(String var0, boolean var1) throws ClassNotFoundException {
      if (a.containsKey(var0)) {
         return a.get(var0);
      }

      Class var2 = null;
      if (b.contains(var0)) {
         if (var1) {
            throw new ClassNotFoundException(var0);
         } else {
            return null;
         }
      } else {
         if (a.contains(var0)) {
            return a.get(var0);
         }

         try {
            var2 = Class.forName(var0);
            a.put(var0, var2);
         } catch (ClassNotFoundException var6) {
            try {
               DefaultListableBeanFactory var4 = (DefaultListableBeanFactory)Utils.getApplicationContext().getAutowireCapableBeanFactory();
               var2 = var4.getBeanClassLoader().loadClass(var0);
               a.put(var0, var2);
            } catch (ClassNotFoundException var5) {
               b.put(var0, var0);
               if (var1) {
                  throw var5;
               }

               return null;
            }
         }

         a.put(var0, var2);
         return var2;
      }
   }

   public static Class<?> getTargetClass(String var0) throws ClassNotFoundException {
      return doGetTargetClass(var0, true);
   }

   public static Class<?> getTargetClassDefaultNull(String var0) throws ClassNotFoundException {
      return doGetTargetClass(var0, false);
   }

   private static List<Variable> a(String var0, String var1, Class<?> var2, Collection<Class<?>> var3) throws Exception {
      ArrayList var4 = new ArrayList();
      BeanInfo var5 = Introspector.getBeanInfo(var2, Object.class);
      PropertyDescriptor[] var6 = var5.getPropertyDescriptors();
      if (var6 != null && !var3.contains(var2)) {
         for (PropertyDescriptor var10 : var6) {
            Variable var11 = new Variable();
            Class var12 = var10.getPropertyType();
            Datatype var13 = a(var12);
            String var14 = var10.getName();
            String var15 = a(var2, var14);
            String var16 = var0 + var10.getName();
            var15 = var15 == null ? var16 : var15;
            String var17 = StringUtils.isBlank(var1) ? var15 : var1 + var15;
            var11.setName(var16);
            var11.setUuid(UUID.randomUUID().toString());
            var11.setLabel(var17);
            var11.setType(var13);
            var11.setAct(Act.InOut);
            if (!Datatype.Object.equals(var13) || var12.equals(Object.class)) {
               var4.add(var11);
            } else if (!var3.contains(var2) && !var3.contains(var12)) {
               var3.add(var2);
               var4.add(var11);
               var4.addAll(a(var0 + var10.getName() + ".", var17 + ".", var12, var3));
               var3.remove(var2);
            }
         }
      }

      return var4;
   }

   private static String a(Class<?> var0, String var1) throws Exception {
      Field var2 = null;

      while (var2 == null) {
         try {
            var2 = var0.getDeclaredField(var1);
         } catch (NoSuchFieldException var4) {
            if (var0 == Object.class) {
               throw var4;
            }

            var0 = var0.getSuperclass();
         }
      }

      Label var3 = var2.getAnnotation(Label.class);
      return var3 != null ? var3.value() : null;
   }

   private static Datatype a(Class<?> var0) {
      if (String.class.isAssignableFrom(var0)) {
         return Datatype.String;
      } else if (Boolean.class.isAssignableFrom(var0) || boolean.class.isAssignableFrom(var0)) {
         return Datatype.Boolean;
      } else if (Integer.class.isAssignableFrom(var0) || int.class.isAssignableFrom(var0)) {
         return Datatype.Integer;
      } else if (Float.class.isAssignableFrom(var0) || float.class.isAssignableFrom(var0)) {
         return Datatype.Float;
      } else if (Long.class.isAssignableFrom(var0) || long.class.isAssignableFrom(var0)) {
         return Datatype.Long;
      } else if (BigDecimal.class.isAssignableFrom(var0)) {
         return Datatype.BigDecimal;
      } else if (Double.class.isAssignableFrom(var0) || double.class.isAssignableFrom(var0)) {
         return Datatype.Double;
      } else if (Date.class.isAssignableFrom(var0)) {
         return Datatype.Date;
      } else if (Date.class.isAssignableFrom(var0)) {
         return Datatype.Date;
      } else if (List.class.isAssignableFrom(var0)) {
         return Datatype.List;
      } else if (Map.class.isAssignableFrom(var0)) {
         return Datatype.Map;
      } else if (Set.class.isAssignableFrom(var0)) {
         return Datatype.Set;
      } else if (Enum.class.isAssignableFrom(var0)) {
         return Datatype.Enum;
      } else {
         return !Character.class.isAssignableFrom(var0) && !char.class.isAssignableFrom(var0) ? Datatype.Object : Datatype.Char;
      }
   }

   public static void cleanClassesCache() {
      a.clear();
      b.clear();
   }
}
