package com.bstek.urule.runtime.log;

public class ValueAssignLog extends DataLog {
   private String left;
   private Object right;

   public ValueAssignLog(String left, Object right) {
      this.left = left;
      String text = this.isEnglishLanguage() ? "###set value：%s=%s" : "###赋值：%s=%s";
      this.msg = String.format(text, left, right);
   }

   public String getLeft() {
      return this.left;
   }

   public Object getRight() {
      return this.right;
   }
}
