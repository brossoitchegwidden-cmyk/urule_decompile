package com.bstek.urule.runtime.log;

public class ExecuteFunctionLog extends DataLog {
   private static final String b = "***执行函数：%s > %s";
   private static final String c = "***execute function：%s > %s";
   private String d;

   public ExecuteFunctionLog(String var1, Object var2) {
      this.d = var1;
      String var3 = this.a() ? "***execute function：%s > %s" : "***执行函数：%s > %s";
      this.a = String.format(var3, var1, var2 == null ? "" : var2.toString());
   }

   public String getFunctionName() {
      return this.d;
   }
}
