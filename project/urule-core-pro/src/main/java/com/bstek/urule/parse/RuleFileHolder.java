package com.bstek.urule.parse;

public class RuleFileHolder {
   private static final ThreadLocal<String> CURRENT_RULE_FILE = new ThreadLocal<>();

   public static void resetRuleFile(String filePath) {
      CURRENT_RULE_FILE.set(filePath);
   }

   public static void clean() {
      CURRENT_RULE_FILE.remove();
   }

   public static String getRuleFile() {
      return CURRENT_RULE_FILE.get();
   }
}
