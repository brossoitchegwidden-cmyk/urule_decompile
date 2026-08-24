package com.bstek.urule.parse;

import com.bstek.urule.action.Action;

public abstract class ActionParser extends AbstractParser<Action> {
   protected ValueParser valueParser;

   public void setValueParser(ValueParser valueParser) {
      this.valueParser = valueParser;
   }
}
