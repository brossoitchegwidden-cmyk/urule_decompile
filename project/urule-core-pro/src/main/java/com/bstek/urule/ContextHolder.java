package com.bstek.urule;

import java.util.HashMap;
import java.util.Map;

public class ContextHolder {
   private static final ThreadLocal<Map<String, Object>> a = new ThreadLocal<>();

   public static Object getData(String var0) {
      Map var1 = a.get();
      return var1 == null ? null : var1.get(var0);
   }

   public static void putData(String var0, Object var1) {
      Map var2 = a.get();
      if (var2 == null) {
         var2 = new HashMap();
         a.set(var2);
      }

      var2.put(var0, var1);
   }

   public static void clean() {
      a.remove();
   }
}
