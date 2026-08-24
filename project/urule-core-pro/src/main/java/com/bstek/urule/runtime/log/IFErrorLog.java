package com.bstek.urule.runtime.log;

import com.bstek.urule.model.rule.Parameter;

public class IFErrorLog extends DataLog {
   private Exception exception;
   private Parameter param;

   public IFErrorLog(Parameter param, Exception ex) {
      this.param = param;
      this.exception = ex;
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("IFError：");
      stringBuilder.append(param.getId());
      stringBuilder.append("=>");
      if (ex instanceof NullPointerException) {
         stringBuilder.append(NullPointerException.class.getName());
      } else {
         stringBuilder.append(ex.getMessage());
      }

      this.msg = stringBuilder.toString();
   }

   public Exception getException() {
      return this.exception;
   }

   public Parameter getParam() {
      return this.param;
   }
}
