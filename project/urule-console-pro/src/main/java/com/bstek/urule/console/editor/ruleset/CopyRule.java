package com.bstek.urule.console.editor.ruleset;

import com.bstek.urule.model.rule.Rule;

public class CopyRule {
   private Rule a;
   private String b;

   public CopyRule(Rule var1, String var2) {
      this.a = var1;
      this.b = var2;
   }

   public String getLibs() {
      return this.b;
   }

   public Rule getRule() {
      return this.a;
   }
}
