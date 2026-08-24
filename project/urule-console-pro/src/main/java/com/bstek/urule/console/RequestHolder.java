package com.bstek.urule.console;

import javax.servlet.http.HttpServletRequest;

public class RequestHolder {
   private static final ThreadLocal CURRENT_REQUEST = new ThreadLocal();

   public static void setRequest(HttpServletRequest request) {
      CURRENT_REQUEST.set(request);
   }

   public static HttpServletRequest getRequest() {
      return (HttpServletRequest)CURRENT_REQUEST.get();
   }

   public static void clean() {
      CURRENT_REQUEST.remove();
   }

   public void setSessionAttribute(String name, Object value) {
      getRequest().getSession().setAttribute(name, value);
   }

   public Object getSessionAttribute(String name) {
      return getRequest().getSession().getAttribute(name);
   }
}
