package com.bstek.urule.runtime.log;

public class ExcecuteScoreCardLog extends DataLog {
   private int rowNumber;
   private Object value;

   public ExcecuteScoreCardLog(int rowNumber, Object value) {
      this.rowNumber = rowNumber;
      this.value = value;
      String text = this.isEnglishLanguage() ? "---row %s,score：%s" : "---行 %s,得分：%s";
      this.msg = String.format(text, rowNumber, value);
   }

   public int getRowNumber() {
      return this.rowNumber;
   }

   public Object getValue() {
      return this.value;
   }
}
