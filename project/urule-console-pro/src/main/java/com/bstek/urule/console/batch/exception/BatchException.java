package com.bstek.urule.console.batch.exception;

import com.bstek.urule.exception.RuleException;

public class BatchException extends RuleException {
   private static final long serialVersionUID = -8229714718832150974L;

   public BatchException(String msg) {
      super(msg);
   }

   public BatchException(String msg, Exception ex) {
      super(msg, ex);
   }
}
