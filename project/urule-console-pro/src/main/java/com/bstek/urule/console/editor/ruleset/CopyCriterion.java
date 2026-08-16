package com.bstek.urule.console.editor.ruleset;

import com.bstek.urule.model.rule.lhs.Criterion;

public class CopyCriterion {
   private Criterion a;
   private String b;

   public CopyCriterion(Criterion var1, String var2) {
      this.a = var1;
      this.b = var2;
   }

   public Criterion getCriterion() {
      return this.a;
   }

   public String getLibs() {
      return this.b;
   }
}
