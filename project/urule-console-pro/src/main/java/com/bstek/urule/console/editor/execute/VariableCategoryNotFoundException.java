package com.bstek.urule.console.editor.execute;

import com.bstek.urule.exception.RuleException;

public class VariableCategoryNotFoundException extends RuleException {
   private static final long serialVersionUID = 8068678481696494126L;

   public VariableCategoryNotFoundException(String msg) {
      super(msg);
   }
}
