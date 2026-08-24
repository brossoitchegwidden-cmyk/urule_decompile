package com.bstek.urule.runtime.log;

public class ScoreCardBean extends DataLog {
   private static final String BEAN = "---执行自定义评分卡得分计算Bean:";
   private static final String PERFORM_CUSTOM_SCORECARD_SCORE_CALCULATION_BEANS = "---Perform custom scorecard score calculation beans:";
   private String bean;

   public ScoreCardBean(String bean) {
      this.bean = bean;
      this.msg = this.isEnglishLanguage() ? "---Perform custom scorecard score calculation beans:" : "---执行自定义评分卡得分计算Bean:" + bean;
   }

   public String getBean() {
      return this.bean;
   }
}
