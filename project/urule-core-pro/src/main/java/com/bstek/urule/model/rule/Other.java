package com.bstek.urule.model.rule;

import com.bstek.urule.action.Action;
import java.util.ArrayList;
import java.util.List;

public class Other {
   private List<Action> actions;

   public List<Action> getActions() {
      return this.actions;
   }

   public void setActions(List<Action> actions) {
      this.actions = actions;
   }

   public void addAction(Action action) {
      if (this.actions == null) {
         this.actions = new ArrayList<>();
      }

      this.actions.add(action);
   }
}
