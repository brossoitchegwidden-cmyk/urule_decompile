package com.bstek.urule.console;

import javax.servlet.http.HttpServletRequest;

public class RequestHolder {
   private static final ThreadLocal a = new ThreadLocal();

   public static void setRequest(HttpServletRequest var0) {
      a.set(var0);
   }

   public static HttpServletRequest getRequest() {
      return (HttpServletRequest)a.get();
   }

   public static void clean() {
      a.remove();
   }

   public void setSessionAttribute(String var1, Object var2) {
      getRequest().getSession().setAttribute(var1, var2);
   }

   public Object getSessionAttribute(String var1) {
      return getRequest().getSession().getAttribute(var1);
   }
}
