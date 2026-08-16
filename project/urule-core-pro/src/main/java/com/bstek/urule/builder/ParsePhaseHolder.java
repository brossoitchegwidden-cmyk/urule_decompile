package com.bstek.urule.builder;

public class ParsePhaseHolder {
   private static final ThreadLocal<Boolean> a = new ThreadLocal<>();

   public static void defineParsePhase() {
      a.set(true);
   }

   public static boolean isParsePhase() {
      Boolean var0 = a.get();
      return var0 != null;
   }

   public static void cleanParsePhase() {
      a.remove();
   }
}
