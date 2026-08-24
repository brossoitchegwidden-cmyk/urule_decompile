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
   private List<SpringBean> builtInActions = new ArrayList<>();

   public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
      this.buildActions(applicationContext);
   }

   public List<SpringBean> getBuiltInActions() {
      return this.builtInActions;
   }

   public void buildActions(ApplicationContext applicationContext) {
      System.out.println("[URULE-CORE]Load built in actions...");
      this.builtInActions.clear();
      Map beansWithAnnotation = applicationContext.getBeansWithAnnotation(ActionBean.class);

      for (String text : (Iterable<String>)(Iterable<?>)(beansWithAnnotation.keySet())) {
         Object targetObject = beansWithAnnotation.get(text);
         targetObject = ProxyUtils.getTargetObject(targetObject);
         ActionBean annotation = targetObject.getClass().getAnnotation(ActionBean.class);
         if (annotation != null) {
            SpringBean springBean = new SpringBean();
            springBean.setId(text);
            springBean.setName(annotation.name());
            if (StringUtils.isBlank(annotation.ename())) {
               springBean.setEname(annotation.name());
            } else {
               springBean.setEname(annotation.ename());
            }

            springBean.setMethods(this.buildMethods(targetObject.getClass().getMethods()));
            this.builtInActions.add(springBean);
         }
      }

      this.sortBuiltInActions();
   }

   private void sortBuiltInActions() {
      Collections.sort(this.builtInActions, new SpringBeanNameComparator());
   }

   private List<Method> buildMethods(java.lang.reflect.Method[] method) {
      ArrayList items = new ArrayList();

      for (java.lang.reflect.Method method2 : method) {
         ActionMethod annotation = method2.getAnnotation(ActionMethod.class);
         if (annotation != null) {
            String text = annotation.name();
            String name = method2.getName();
            Method method3 = new Method();
            method3.setMethodName(name);
            method3.setName(text);
            items.add(method3);
            ActionMethodParameter annotation2 = method2.getAnnotation(ActionMethodParameter.class);
            ArrayList items2 = new ArrayList();
            ArrayList items3 = new ArrayList();
            if (annotation2 != null) {
               String[] values = annotation2.names();

               for (String text2 : values) {
                  items2.add(text2);
                  items3.add(text2);
               }

               String[] values2 = annotation2.enames();
               if (values2.length == values.length) {
                  items3.clear();

                  for (String text3 : values2) {
                     items3.add(text3);
                  }
               }
            }

            method3.setParameters(this.buildParameters(method2, items2, items3));
         }
      }

      this.sortMethods(items);
      return items;
   }

   private void sortMethods(List<Method> methods) {
      Collections.sort(methods, new ActionMethodNameComparator());
   }

   private List<Parameter> buildParameters(java.lang.reflect.Method method, List<String> strings, List<String> strings2) {
      ArrayList items = new ArrayList();
      Class[] parameterTypes = method.getParameterTypes();

      for (int index = 0; index < parameterTypes.length; index++) {
         Class valueType = parameterTypes[index];
         String text = "";
         String text2 = "";
         if (strings.size() > index) {
            text = (String)strings.get(index);
            text2 = text;
            if (strings2.size() == strings.size()) {
               text2 = (String)strings2.get(index);
            }
         }

         Parameter parameter = new Parameter();
         parameter.setName(text);
         parameter.setEname(text2);
         parameter.setType(this.buildDatatype(valueType));
         items.add(parameter);
      }

      return items;
   }

   private Datatype buildDatatype(Class<?> valueType) {
      if (valueType.getName().equals("java.lang.Object")) {
         return Datatype.Object;
      } else if (valueType.isAssignableFrom(Integer.class) || valueType.isAssignableFrom(int.class)) {
         return Datatype.Integer;
      } else if (valueType.isAssignableFrom(Long.class) || valueType.isAssignableFrom(long.class)) {
         return Datatype.Long;
      } else if (valueType.isAssignableFrom(Double.class) || valueType.isAssignableFrom(double.class)) {
         return Datatype.Double;
      } else if (valueType.isAssignableFrom(Float.class) || valueType.isAssignableFrom(float.class)) {
         return Datatype.Float;
      } else if (valueType.isAssignableFrom(BigDecimal.class)) {
         return Datatype.BigDecimal;
      } else if (valueType.isAssignableFrom(Boolean.class) || valueType.isAssignableFrom(boolean.class)) {
         return Datatype.Boolean;
      } else if (valueType.isAssignableFrom(Date.class)) {
         return Datatype.Date;
      } else if (valueType.isAssignableFrom(List.class)) {
         return Datatype.List;
      } else if (valueType.isAssignableFrom(Set.class)) {
         return Datatype.Set;
      } else if (valueType.isAssignableFrom(Enum.class)) {
         return Datatype.Enum;
      } else if (valueType.isAssignableFrom(Map.class)) {
         return Datatype.Map;
      } else if (valueType.isAssignableFrom(String.class)) {
         return Datatype.String;
      } else {
         return !valueType.isAssignableFrom(Character.class) && !valueType.isAssignableFrom(char.class) ? Datatype.Object : Datatype.Char;
      }
   }
}
