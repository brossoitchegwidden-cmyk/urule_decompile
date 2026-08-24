package com.bstek.urule.console.editor.action;

import com.bstek.urule.Utils;
import com.bstek.urule.console.ApiServletHandler;
import com.bstek.urule.model.ExposeAction;
import com.bstek.urule.model.library.Datatype;
import com.bstek.urule.model.library.action.Parameter;
import com.bstek.urule.runtime.ProxyUtils;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ActionServletHandler extends ApiServletHandler {
   public void loadMethods(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      String parameter = req.getParameter("beanId");
      Object objectValue = Utils.getApplicationContext().getBean(parameter);
      Object targetObject = ProxyUtils.getTargetObject(objectValue);
      ArrayList items = new ArrayList();
      Method[] methods = targetObject.getClass().getMethods();

      for(Method method : methods) {
         ExposeAction annotation = (ExposeAction)method.getAnnotation(ExposeAction.class);
         if (annotation != null) {
            String name = method.getName();
            com.bstek.urule.model.library.action.Method method2 = new com.bstek.urule.model.library.action.Method();
            method2.setMethodName(name);
            method2.setName(annotation.value());
            method2.setParameters(this.buildParameters(method, annotation.parameters()));
            items.add(method2);
         }
      }

      this.writeObjectToJson(resp, items);
   }

   private List buildParameters(Method method, String[] values) {
      ArrayList items = new ArrayList();
      Class[] parameterTypes = method.getParameterTypes();

      for(int index = 0; index < parameterTypes.length; ++index) {
         Class valueType = parameterTypes[index];
         Parameter parameter = new Parameter();
         if (index < values.length) {
            parameter.setName(values[index]);
         } else {
            parameter.setName("参数" + index);
         }

         parameter.setType(this.resolveDatatype(valueType));
         items.add(parameter);
      }

      return items;
   }

   private Datatype resolveDatatype(Class valueType) {
      if (valueType.equals(String.class)) {
         return Datatype.String;
      } else if (valueType.equals(BigDecimal.class)) {
         return Datatype.BigDecimal;
      } else if (valueType.equals(Boolean.class)) {
         return Datatype.Boolean;
      } else if (valueType.equals(Boolean.class)) {
         return Datatype.Boolean;
      } else if (valueType.equals(Boolean.TYPE)) {
         return Datatype.Boolean;
      } else if (valueType.equals(Date.class)) {
         return Datatype.Date;
      } else if (valueType.equals(Double.class)) {
         return Datatype.Double;
      } else if (valueType.equals(Double.TYPE)) {
         return Datatype.Double;
      } else if (Enum.class.isAssignableFrom(valueType)) {
         return Datatype.Enum;
      } else if (valueType.equals(Float.class)) {
         return Datatype.Float;
      } else if (valueType.equals(Float.TYPE)) {
         return Datatype.Float;
      } else if (valueType.equals(Integer.class)) {
         return Datatype.Integer;
      } else if (valueType.equals(Integer.TYPE)) {
         return Datatype.Integer;
      } else if (valueType.equals(Character.class)) {
         return Datatype.Char;
      } else if (valueType.equals(Character.TYPE)) {
         return Datatype.Char;
      } else if (List.class.isAssignableFrom(valueType)) {
         return Datatype.List;
      } else if (valueType.equals(Long.TYPE)) {
         return Datatype.Long;
      } else if (valueType.equals(Long.class)) {
         return Datatype.Long;
      } else if (Map.class.isAssignableFrom(valueType)) {
         return Datatype.Map;
      } else {
         return Set.class.isAssignableFrom(valueType) ? Datatype.Set : Datatype.Object;
      }
   }

   public String url() {
      return "/action";
   }
}
