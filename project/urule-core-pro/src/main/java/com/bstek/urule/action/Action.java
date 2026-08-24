package com.bstek.urule.action;

import com.bstek.urule.runtime.rete.Context;
import java.util.Map;

public interface Action extends Comparable<Action> {
   ActionValue execute(Context context, Map<String, Object> factMap);

   ActionType getActionType();

   int getPriority();

   void setDebug(boolean debug);
}
