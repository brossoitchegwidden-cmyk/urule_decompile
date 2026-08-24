package com.bstek.urule.runtime.log;

public class ExecuteFunctionLog extends DataLog {
   private String functionName;

   public ExecuteFunctionLog(String functionName, Object object) {
      this.functionName = functionName;
      String text = this.isEnglishLanguage() ? "***execute function：%s > %s" : "***执行函数：%s > %s";
      this.msg = String.format(text, functionName, object == null ? "" : object.toString());
   }

   public String getFunctionName() {
      return this.functionName;
   }
}
