package com.bstek.urule.console.editor.ruleset;

import com.bstek.urule.model.rule.lhs.Criterion;

public class CopyCriterion {
   private Criterion criterion;
   private String libs;

   public CopyCriterion(Criterion criterion, String libs) {
      this.criterion = criterion;
      this.libs = libs;
   }

   public Criterion getCriterion() {
      return this.criterion;
   }

   public String getLibs() {
      return this.libs;
   }
}
