package com.bstek.urule.builder;

public class CopyLibPhaseHolder {
   private static final ThreadLocal<Boolean> COPY_LIBRARY_PHASE = new ThreadLocal<>();

   public static void set() {
      COPY_LIBRARY_PHASE.set(true);
   }

   public static boolean isCopyLib() {
      Boolean flag = COPY_LIBRARY_PHASE.get();
      return flag == null ? false : flag;
   }

   public static void clean() {
      COPY_LIBRARY_PHASE.remove();
   }
}
