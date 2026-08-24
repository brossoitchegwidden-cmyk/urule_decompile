package com.bstek.urule.model.decisiontree;

import com.bstek.urule.action.Action;
import java.util.List;

public class ActionTreeNode extends TreeNode {
   private List<Action> actions;

   public List<Action> getActions() {
      return this.actions;
   }

   public void setActions(List<Action> actions) {
      this.actions = actions;
   }
}
