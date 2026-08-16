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
   public void loadMethods(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      String var3 = var1.getParameter("beanId");
      Object var4 = Utils.getApplicationContext().getBean(var3);
      Object var5 = ProxyUtils.getTargetObject(var4);
      ArrayList var6 = new ArrayList();
      Method[] var7 = var5.getClass().getMethods();

      for(Method var11 : var7) {
         ExposeAction var12 = (ExposeAction)var11.getAnnotation(ExposeAction.class);
         if (var12 != null) {
            String var13 = var11.getName();
            com.bstek.urule.model.library.action.Method var14 = new com.bstek.urule.model.library.action.Method();
            var14.setMethodName(var13);
            var14.setName(var12.value());
            var14.setParameters(this.a(var11, var12.parameters()));
            var6.add(var14);
         }
      }

      this.a(var2, var6);
   }

   private List a(Method var1, String[] var2) {
      ArrayList var3 = new ArrayList();
      Class[] var4 = var1.getParameterTypes();

      for(int var5 = 0; var5 < var4.length; ++var5) {
         Class var6 = var4[var5];
         Parameter var7 = new Parameter();
         if (var5 < var2.length) {
            var7.setName(var2[var5]);
         } else {
            var7.setName("参数" + var5);
         }

         var7.setType(this.a(var6));
         var3.add(var7);
      }

      return var3;
   }

   private Datatype a(Class var1) {
      if (var1.equals(String.class)) {
         return Datatype.String;
      } else if (var1.equals(BigDecimal.class)) {
         return Datatype.BigDecimal;
      } else if (var1.equals(Boolean.class)) {
         return Datatype.Boolean;
      } else if (var1.equals(Boolean.class)) {
         return Datatype.Boolean;
      } else if (var1.equals(Boolean.TYPE)) {
         return Datatype.Boolean;
      } else if (var1.equals(Date.class)) {
         return Datatype.Date;
      } else if (var1.equals(Double.class)) {
         return Datatype.Double;
      } else if (var1.equals(Double.TYPE)) {
         return Datatype.Double;
      } else if (Enum.class.isAssignableFrom(var1)) {
         return Datatype.Enum;
      } else if (var1.equals(Float.class)) {
         return Datatype.Float;
      } else if (var1.equals(Float.TYPE)) {
         return Datatype.Float;
      } else if (var1.equals(Integer.class)) {
         return Datatype.Integer;
      } else if (var1.equals(Integer.TYPE)) {
         return Datatype.Integer;
      } else if (var1.equals(Character.class)) {
         return Datatype.Char;
      } else if (var1.equals(Character.TYPE)) {
         return Datatype.Char;
      } else if (List.class.isAssignableFrom(var1)) {
         return Datatype.List;
      } else if (var1.equals(Long.TYPE)) {
         return Datatype.Long;
      } else if (var1.equals(Long.class)) {
         return Datatype.Long;
      } else if (Map.class.isAssignableFrom(var1)) {
         return Datatype.Map;
      } else {
         return Set.class.isAssignableFrom(var1) ? Datatype.Set : Datatype.Object;
      }
   }

   public String url() {
      return "/action";
   }
}
