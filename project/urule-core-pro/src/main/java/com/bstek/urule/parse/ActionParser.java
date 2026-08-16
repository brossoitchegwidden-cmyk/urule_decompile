package com.bstek.urule.parse;

import com.bstek.urule.action.Action;

public abstract class ActionParser extends AbstractParser<Action> {
   protected ValueParser a;

   public void setValueParser(ValueParser var1) {
      this.a = var1;
   }
}
