package com.bstek.urule.console.editor.ruleset;

import com.bstek.urule.action.Action;

public class CopyAction {
   private Action action;
   private String libs;

   public CopyAction(Action action, String libs) {
      this.action = action;
      this.libs = libs;
   }

   public Action getAction() {
      return this.action;
   }

   public String getLibs() {
      return this.libs;
   }
}
