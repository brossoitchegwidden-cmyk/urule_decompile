package com.bstek.urule.runtime.log;

public class ExecuteBeanMethodLog extends DataLog {
   private String methodInfo;

   public ExecuteBeanMethodLog(String methodInfo, String parameterInfo) {
      this.methodInfo = methodInfo;
      this.msg = methodInfo + "(" + parameterInfo + ")";
   }

   public String getMethodInfo() {
      return this.methodInfo;
   }
}
