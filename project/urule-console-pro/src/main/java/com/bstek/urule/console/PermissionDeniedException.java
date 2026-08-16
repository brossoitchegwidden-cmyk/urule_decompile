package com.bstek.urule.console;

import com.bstek.urule.exception.RuleException;

public class PermissionDeniedException extends RuleException {
   private static final long a = -423776275982862455L;

   public PermissionDeniedException() {
      super("Permission denied!");
   }

   public PermissionDeniedException(String var1) {
      super(var1);
   }
}
