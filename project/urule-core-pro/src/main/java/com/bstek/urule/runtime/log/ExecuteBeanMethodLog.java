package com.bstek.urule.runtime.log;

public class ExecuteBeanMethodLog extends DataLog {
   private String b;

   public ExecuteBeanMethodLog(String var1, String var2) {
      this.b = var1;
      this.a = var1 + "(" + var2 + ")";
   }

   public String getMethodInfo() {
      return this.b;
   }
}
