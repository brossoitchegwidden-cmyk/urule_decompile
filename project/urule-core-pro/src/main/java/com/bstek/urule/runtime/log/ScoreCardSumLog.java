package com.bstek.urule.runtime.log;

public class ScoreCardSumLog extends DataLog {
   private static final String b = "---评分卡%s,得分：%s";
   private static final String c = "---scorecard %s, score: %s";
   private String d;
   private Object e;

   public ScoreCardSumLog(String var1, Object var2) {
      this.d = var1;
      this.e = var2;
      String var3 = this.a() ? "---scorecard %s, score: %s" : "---评分卡%s,得分：%s";
      this.a = String.format(var3, var1, var2);
   }

   public String getCardName() {
      return this.d;
   }

   public Object getValue() {
      return this.e;
   }
}
