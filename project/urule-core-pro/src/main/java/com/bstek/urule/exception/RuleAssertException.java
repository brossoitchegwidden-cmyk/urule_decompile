package com.bstek.urule.exception;

public class RuleAssertException extends RuleException {
   private static final long serialVersionUID = -1345171815520647493L;

   public RuleAssertException(String msg, Exception ex) {
      super(msg, ex);
   }
}
