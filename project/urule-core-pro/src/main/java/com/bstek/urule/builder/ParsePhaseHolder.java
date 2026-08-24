package com.bstek.urule.builder;

public class ParsePhaseHolder {
   private static final ThreadLocal<Boolean> PARSE_PHASE = new ThreadLocal<>();

   public static void defineParsePhase() {
      PARSE_PHASE.set(true);
   }

   public static boolean isParsePhase() {
      Boolean flag = PARSE_PHASE.get();
      return flag != null;
   }

   public static void cleanParsePhase() {
      PARSE_PHASE.remove();
   }
}
