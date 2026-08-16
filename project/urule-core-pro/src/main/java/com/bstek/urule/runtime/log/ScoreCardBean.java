package com.bstek.urule.runtime.log;

public class ScoreCardBean extends DataLog {
   private static final String b = "---执行自定义评分卡得分计算Bean:";
   private static final String c = "---Perform custom scorecard score calculation beans:";
   private String d;

   public ScoreCardBean(String var1) {
      this.d = var1;
      this.a = this.a() ? "---Perform custom scorecard score calculation beans:" : "---执行自定义评分卡得分计算Bean:" + var1;
   }

   public String getBean() {
      return this.d;
   }
}
