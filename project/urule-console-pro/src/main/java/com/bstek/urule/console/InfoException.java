package com.bstek.urule.console;

public class InfoException extends RuntimeException {
   private static final long serialVersionUID = 3652200305693521438L;

   public InfoException(String msg) {
      super(msg);
   }

   public InfoException(Exception ex) {
      super(ex);
   }
}
