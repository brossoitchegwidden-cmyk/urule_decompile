package com.bstek.urule.console.config.exception;

public class SetupException extends RuntimeException {
   private static final long serialVersionUID = 2581947117309088737L;

   public SetupException(Exception ex) {
      super(ex);
   }

   public SetupException(String msg) {
      super(msg);
   }
}
