package com.bstek.urule.runtime.log;

public class ScoreCardLog extends DataLog {
   private static final String b = "复杂评分卡";
   private static final String c = "ComplexScorecard";
   private static final String d = "执行[%s]:%s";
   private static final String e = "execute [%s]:%s";
   private static final String f = "执行评分卡[%s]:%s";
   private static final String g = "execute scorecard [%s]:%s";
   private String h;
   private String i;

   public ScoreCardLog(String var1, String var2) {
      this.h = var1;
      this.i = var2;
      if (var2.endsWith(".scc")) {
         this.h = this.a() ? "ComplexScorecard" : "复杂评分卡";
         String var3 = this.a() ? "execute [%s]:%s" : "执行[%s]:%s";
         this.a = String.format(var3, this.h, var2);
      } else {
         String var4 = this.a() ? "execute scorecard [%s]:%s" : "执行评分卡[%s]:%s";
         this.a = String.format(var4, this.h, var2);
      }
   }

   public String getName() {
      return this.h;
   }

   public String getPath() {
      return this.i;
   }
}
