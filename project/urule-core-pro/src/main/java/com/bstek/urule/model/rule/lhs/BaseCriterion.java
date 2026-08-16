package com.bstek.urule.model.rule.lhs;

import com.fasterxml.jackson.annotation.JsonIgnore;

public abstract class BaseCriterion implements Criterion {
   @JsonIgnore
   private Junction parent;

   @Override
   public Junction getParent() {
      return this.parent;
   }

   @Override
   public void setParent(Junction var1) {
      this.parent = var1;
   }
}
