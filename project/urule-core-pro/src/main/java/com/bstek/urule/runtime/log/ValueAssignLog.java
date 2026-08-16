package com.bstek.urule.runtime.log;

public class ValueAssignLog extends DataLog {
   private static final String b = "###赋值：%s=%s";
   private static final String c = "###set value：%s=%s";
   private String d;
   private Object e;

   public ValueAssignLog(String var1, Object var2) {
      this.d = var1;
      String var3 = this.a() ? "###set value：%s=%s" : "###赋值：%s=%s";
      this.a = String.format(var3, var1, var2);
   }

   public String getLeft() {
      return this.d;
   }

   public Object getRight() {
      return this.e;
   }
}
