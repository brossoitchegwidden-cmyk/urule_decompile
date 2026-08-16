package com.bstek.urule.console.security;

import com.bstek.urule.exception.RuleException;

public class AccessDeniedException extends RuleException {
   private static final long a = 1L;

   public AccessDeniedException(String var1) {
      super(var1);
   }
}
