package com.bstek.urule.runtime.log;

public class MetLog extends DataLog {
   private int met;
   private boolean only;
   private int matchedCount;

   public MetLog(int met, int matchedCount, boolean only) {
      this.met = met;
      this.only = only;
      this.matchedCount = matchedCount;
      StringBuffer stringBuffer = new StringBuffer("--");
      if (only) {
         stringBuffer.append("只有");
      } else {
         stringBuffer.append("至少");
      }

      stringBuffer.append(met + "个条件成立，");
      stringBuffer.append("实际成立条件" + matchedCount + "个，");
      if (only) {
         if (met == matchedCount) {
            stringBuffer.append("满足");
         } else {
            stringBuffer.append("不满足");
         }
      } else if (matchedCount >= met) {
         stringBuffer.append("满足");
      } else {
         stringBuffer.append("不满足");
      }

      this.msg = stringBuffer.toString();
   }

   public int getMet() {
      return this.met;
   }

   public boolean isOnly() {
      return this.only;
   }

   public int getMatchedCount() {
      return this.matchedCount;
   }
}
