package com.bstek.urule.runtime;

import com.bstek.urule.model.library.Datatype;
import com.bstek.urule.model.library.action.Method;
import com.bstek.urule.model.library.action.Parameter;
import com.bstek.urule.model.library.action.SpringBean;
import com.bstek.urule.model.library.action.annotation.ActionBean;
import com.bstek.urule.model.library.action.annotation.ActionMethod;
import com.bstek.urule.model.library.action.annotation.ActionMethodParameter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class BuiltInActionLibraryBuilder implements ApplicationContextAware {
   public static final String BEAN_ID = "urule.builtInActionLibraryBuilder";
   private List<SpringBean> a = new ArrayList<>();

   public void setApplicationContext(ApplicationContext var1) throws BeansException {
      this.buildActions(var1);
   }

   public List<SpringBean> getBuiltInActions() {
      return this.a;
   }

   public void buildActions(ApplicationContext var1) {
      System.out.println("[URULE-CORE]Load built in actions...");
      this.a.clear();
      Map var2 = var1.getBeansWithAnnotation(ActionBean.class);

      for (String var4 : (Iterable<String>)(Iterable<?>)(var2.keySet())) {
         Object var5 = var2.get(var4);
         var5 = ProxyUtils.getTargetObject(var5);
         ActionBean var6 = var5.getClass().getAnnotation(ActionBean.class);
         if (var6 != null) {
            SpringBean var7 = new SpringBean();
            var7.setId(var4);
            var7.setName(var6.name());
            if (StringUtils.isBlank(var6.ename())) {
               var7.setEname(var6.name());
            } else {
               var7.setEname(var6.ename());
            }

            var7.setMethods(this.a(var5.getClass().getMethods()));
            this.a.add(var7);
         }
      }

      this.a();
   }

   private void a() {
      Collections.sort(this.a, new BuiltInActionLibraryBuilder$1(this));
   }

   private List<Method> a(java.lang.reflect.Method[] var1) {
      ArrayList var2 = new ArrayList();

      for (java.lang.reflect.Method var6 : var1) {
         ActionMethod var7 = var6.getAnnotation(ActionMethod.class);
         if (var7 != null) {
            String var8 = var7.name();
            String var9 = var6.getName();
            Method var10 = new Method();
            var10.setMethodName(var9);
            var10.setName(var8);
            var2.add(var10);
            ActionMethodParameter var11 = var6.getAnnotation(ActionMethodParameter.class);
            ArrayList var12 = new ArrayList();
            ArrayList var13 = new ArrayList();
            if (var11 != null) {
               String[] var14 = var11.names();

               for (String var18 : var14) {
                  var12.add(var18);
                  var13.add(var18);
               }

               String[] var20 = var11.enames();
               if (var20.length == var14.length) {
                  var13.clear();

                  for (String var19 : var20) {
                     var13.add(var19);
                  }
               }
            }

            var10.setParameters(this.a(var6, var12, var13));
         }
      }

      this.a(var2);
      return var2;
   }

   private void a(List<Method> var1) {
      Collections.sort(var1, new BuiltInActionLibraryBuilder$2(this));
   }

   private List<Parameter> a(java.lang.reflect.Method var1, List<String> var2, List<String> var3) {
      ArrayList var4 = new ArrayList();
      Class[] var5 = var1.getParameterTypes();

      for (int var6 = 0; var6 < var5.length; var6++) {
         Class var7 = var5[var6];
         String var8 = "";
         String var9 = "";
         if (var2.size() > var6) {
            var8 = (String)var2.get(var6);
            var9 = var8;
            if (var3.size() == var2.size()) {
               var9 = (String)var3.get(var6);
            }
         }

         Parameter var10 = new Parameter();
         var10.setName(var8);
         var10.setEname(var9);
         var10.setType(this.a(var7));
         var4.add(var10);
      }

      return var4;
   }

   private Datatype a(Class<?> var1) {
      if (var1.getName().equals("java.lang.Object")) {
         return Datatype.Object;
      } else if (var1.isAssignableFrom(Integer.class) || var1.isAssignableFrom(int.class)) {
         return Datatype.Integer;
      } else if (var1.isAssignableFrom(Long.class) || var1.isAssignableFrom(long.class)) {
         return Datatype.Long;
      } else if (var1.isAssignableFrom(Double.class) || var1.isAssignableFrom(double.class)) {
         return Datatype.Double;
      } else if (var1.isAssignableFrom(Float.class) || var1.isAssignableFrom(float.class)) {
         return Datatype.Float;
      } else if (var1.isAssignableFrom(BigDecimal.class)) {
         return Datatype.BigDecimal;
      } else if (var1.isAssignableFrom(Boolean.class) || var1.isAssignableFrom(boolean.class)) {
         return Datatype.Boolean;
      } else if (var1.isAssignableFrom(Date.class)) {
         return Datatype.Date;
      } else if (var1.isAssignableFrom(List.class)) {
         return Datatype.List;
      } else if (var1.isAssignableFrom(Set.class)) {
         return Datatype.Set;
      } else if (var1.isAssignableFrom(Enum.class)) {
         return Datatype.Enum;
      } else if (var1.isAssignableFrom(Map.class)) {
         return Datatype.Map;
      } else if (var1.isAssignableFrom(String.class)) {
         return Datatype.String;
      } else {
         return !var1.isAssignableFrom(Character.class) && !var1.isAssignableFrom(char.class) ? Datatype.Object : Datatype.Char;
      }
   }
}
