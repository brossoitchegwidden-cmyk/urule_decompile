package com.bstek.urule.console;

public class ContextHolder {
   private static final ThreadLocal a = new ThreadLocal();
   private static final ThreadLocal b = new ThreadLocal();

   public static void setProjectId(Long var0) {
      a.set(var0);
   }

   public static Long getProjectId() {
      return (Long)a.get();
   }

   public static void setGroupId(String var0) {
      b.set(var0);
   }

   public static String getGroupId() {
      return (String)b.get();
   }

   public static void clear() {
      b.remove();
      a.remove();
   }
}
