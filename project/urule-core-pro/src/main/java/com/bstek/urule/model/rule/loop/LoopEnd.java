package com.bstek.urule.model.rule.loop;

import com.bstek.urule.action.Action;
import java.util.List;

public class LoopEnd {
   private List<Action> actions;

   public List<Action> getActions() {
      return this.actions;
   }

   public void setActions(List<Action> actions) {
      this.actions = actions;
   }
}
