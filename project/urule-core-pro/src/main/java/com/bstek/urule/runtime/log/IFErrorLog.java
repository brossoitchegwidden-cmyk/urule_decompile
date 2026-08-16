package com.bstek.urule.runtime.log;

import com.bstek.urule.model.rule.Parameter;

public class IFErrorLog extends DataLog {
   private Exception b;
   private Parameter c;

   public IFErrorLog(Parameter var1, Exception var2) {
      this.c = var1;
      this.b = var2;
      StringBuilder var3 = new StringBuilder();
      var3.append("IFError：");
      var3.append(var1.getId());
      var3.append("=>");
      if (var2 instanceof NullPointerException) {
         var3.append(NullPointerException.class.getName());
      } else {
         var3.append(var2.getMessage());
      }

      this.a = var3.toString();
   }

   public Exception getException() {
      return this.b;
   }

   public Parameter getParam() {
      return this.c;
   }
}
