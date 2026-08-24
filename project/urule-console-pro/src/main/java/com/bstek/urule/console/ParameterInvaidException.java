package com.bstek.urule.console;

public class ParameterInvaidException extends RuntimeException {
   private static final long serialVersionUID = -422427375344153292L;

   public ParameterInvaidException() {
      super("Parameter invalid or missing!");
   }
}
