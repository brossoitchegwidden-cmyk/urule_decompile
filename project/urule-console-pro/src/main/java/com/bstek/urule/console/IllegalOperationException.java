package com.bstek.urule.console;

public class IllegalOperationException extends RuntimeException {
   private static final long serialVersionUID = 7712600502226215081L;

   public IllegalOperationException() {
      super("Illegal Operation!");
   }

   public IllegalOperationException(String message) {
      super(message);
   }
}
