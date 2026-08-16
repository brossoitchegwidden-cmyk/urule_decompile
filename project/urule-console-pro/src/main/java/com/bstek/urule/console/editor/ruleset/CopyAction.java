package com.bstek.urule.console.editor.ruleset;

import com.bstek.urule.action.Action;

public class CopyAction {
   private Action a;
   private String b;

   public CopyAction(Action var1, String var2) {
      this.a = var1;
      this.b = var2;
   }

   public Action getAction() {
      return this.a;
   }

   public String getLibs() {
      return this.b;
   }
}
