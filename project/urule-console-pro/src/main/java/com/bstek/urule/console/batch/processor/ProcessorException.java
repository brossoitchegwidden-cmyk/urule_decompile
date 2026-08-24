package com.bstek.urule.console.batch.processor;

import com.bstek.urule.exception.RuleException;
import com.bstek.urule.model.GeneralEntity;

public class ProcessorException extends RuleException {
   private static final long serialVersionUID = -3045647109781785076L;
   private Object data;

   public ProcessorException(String msg, Exception ex, GeneralEntity data) {
      super(msg, ex);
      this.setData(data);
   }

   public Object getData() {
      return this.data;
   }

   public void setData(Object data) {
      this.data = data;
   }
}
