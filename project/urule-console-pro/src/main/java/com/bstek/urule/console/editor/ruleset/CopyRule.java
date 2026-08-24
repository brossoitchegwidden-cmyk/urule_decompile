package com.bstek.urule.console.editor.ruleset;

import com.bstek.urule.model.rule.Rule;

public class CopyRule {
   private Rule rule;
   private String libs;

   public CopyRule(Rule rule, String libs) {
      this.rule = rule;
      this.libs = libs;
   }

   public String getLibs() {
      return this.libs;
   }

   public Rule getRule() {
      return this.rule;
   }
}
