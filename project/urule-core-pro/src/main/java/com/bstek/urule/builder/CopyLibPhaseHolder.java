package com.bstek.urule.builder;

public class CopyLibPhaseHolder {
   private static final ThreadLocal<Boolean> a = new ThreadLocal<>();

   public static void set() {
      a.set(true);
   }

   public static boolean isCopyLib() {
      Boolean var0 = a.get();
      return var0 == null ? false : var0;
   }

   public static void clean() {
      a.remove();
   }
}
