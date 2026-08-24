package com.bstek.urule.console.editor.execute;

import com.bstek.urule.exception.RuleException;

public class VariableNotFoundException extends RuleException {
   private static final long serialVersionUID = -7938592241842080271L;

   public VariableNotFoundException(String msg) {
      super(msg);
   }
}
