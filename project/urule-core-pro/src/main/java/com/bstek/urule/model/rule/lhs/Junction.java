package com.bstek.urule.model.rule.lhs;

import java.util.ArrayList;
import java.util.List;

public abstract class Junction extends BaseCriterion {
   private List<Criterion> criterions;

   public List<Criterion> getCriterions() {
      return this.criterions;
   }

   public void addCriterion(Criterion var1) {
      if (this.criterions == null) {
         this.criterions = new ArrayList<>();
      }

      var1.setParent(this);
      this.criterions.add(var1);
   }

   public void setCriterions(List<Criterion> var1) {
      for (Criterion var3 : var1) {
         var3.setParent(this);
      }

      this.criterions = var1;
   }

   public abstract String getJunctionType();
}
