package com.bstek.urule.console.batch.processor;

import com.bstek.urule.exception.RuleException;
import com.bstek.urule.model.GeneralEntity;

public class ProcessorException extends RuleException {
   private static final long a = -3045647109781785076L;
   private Object b;

   public ProcessorException(String var1, Exception var2, GeneralEntity var3) {
      super(var1, var2);
      this.setData(var3);
   }

   public Object getData() {
      return this.b;
   }

   public void setData(Object var1) {
      this.b = var1;
   }
}
