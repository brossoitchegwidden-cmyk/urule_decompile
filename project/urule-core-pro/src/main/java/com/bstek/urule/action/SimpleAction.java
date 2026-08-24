package com.bstek.urule.action;

import com.bstek.urule.runtime.rete.Context;
import java.util.Map;

public class SimpleAction extends AbstractAction {
   public SimpleAction(Object value) {
   }

   @Override
   public ActionValue execute(Context context, Map<String, Object> factMap) {
      return null;
   }

   @Override
   public ActionType getActionType() {
      return ActionType.ConsolePrint;
   }
}
