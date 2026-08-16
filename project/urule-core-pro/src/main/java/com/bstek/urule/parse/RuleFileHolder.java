package com.bstek.urule.parse;

public class RuleFileHolder {
   private static final ThreadLocal<String> a = new ThreadLocal<>();

   public static void resetRuleFile(String var0) {
      a.set(var0);
   }

   public static void clean() {
      a.remove();
   }

   public static String getRuleFile() {
      return a.get();
   }
}
