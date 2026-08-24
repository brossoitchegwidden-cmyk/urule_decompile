package com.bstek.urule;

import java.util.HashMap;
import java.util.Map;

public class ContextHolder {
   private static final ThreadLocal<Map<String, Object>> CONTEXT_DATA = new ThreadLocal<>();

   public static Object getData(String key) {
      Map contextData = CONTEXT_DATA.get();
      return contextData == null ? null : contextData.get(key);
   }

   public static void putData(String key, Object value) {
      Map contextData = CONTEXT_DATA.get();
      if (contextData == null) {
         contextData = new HashMap();
         CONTEXT_DATA.set(contextData);
      }

      contextData.put(key, value);
   }

   public static void clean() {
      CONTEXT_DATA.remove();
   }
}
