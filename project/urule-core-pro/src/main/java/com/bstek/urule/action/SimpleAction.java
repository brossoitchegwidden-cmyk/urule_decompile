package com.bstek.urule.action;

import com.bstek.urule.runtime.rete.Context;
import java.util.Map;

public class SimpleAction extends AbstractAction {
   public SimpleAction(Object var1) {
   }

   @Override
   public ActionValue execute(Context var1, Map<String, Object> var2) {
      return null;
   }

   @Override
   public ActionType getActionType() {
      return ActionType.ConsolePrint;
   }
}
