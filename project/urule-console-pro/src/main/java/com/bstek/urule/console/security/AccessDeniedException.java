package com.bstek.urule.console.security;

import com.bstek.urule.exception.RuleException;

public class AccessDeniedException extends RuleException {
   private static final long serialVersionUID = 1L;

   public AccessDeniedException(String string) {
      super(string);
   }
}
