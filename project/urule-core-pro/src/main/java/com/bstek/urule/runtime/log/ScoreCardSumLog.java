package com.bstek.urule.runtime.log;

public class ScoreCardSumLog extends DataLog {
   private String cardName;
   private Object value;

   public ScoreCardSumLog(String cardName, Object value) {
      this.cardName = cardName;
      this.value = value;
      String text = this.isEnglishLanguage() ? "---scorecard %s, score: %s" : "---评分卡%s,得分：%s";
      this.msg = String.format(text, cardName, value);
   }

   public String getCardName() {
      return this.cardName;
   }

   public Object getValue() {
      return this.value;
   }
}
