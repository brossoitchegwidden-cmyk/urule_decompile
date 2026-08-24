package com.bstek.urule.exception;

public class DeserializeException extends RuleException {
   private static final long serialVersionUID = 3674373421453353666L;

   public DeserializeException(String msg) {
      super(msg);
   }

   public DeserializeException(Exception ex) {
      super(ex);
   }
}
