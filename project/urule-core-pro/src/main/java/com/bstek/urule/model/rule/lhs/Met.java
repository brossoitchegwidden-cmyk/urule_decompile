package com.bstek.urule.model.rule.lhs;

import com.bstek.urule.runtime.rete.EvaluationContext;
import java.util.List;

public class Met extends Junction {
   private int met;
   private boolean only;

   @Override
   public boolean doEval(EvaluationContext context, boolean debug) {
      List criterions = this.getCriterions();
      int number = 0;

      for (Criterion criterion : (Iterable<Criterion>)(Iterable<?>)(criterions)) {
         if (criterion.doEval(context, debug)) {
            number++;
         }
      }

      if (this.only) {
         if (number == this.met) {
            return true;
         }
      } else if (number >= this.met) {
         return true;
      }

      return false;
   }

   public int getMet() {
      return this.met;
   }

   public void setMet(int met) {
      this.met = met;
   }

   public boolean isOnly() {
      return this.only;
   }

   public void setOnly(boolean only) {
      this.only = only;
   }

   @Override
   public String getJunctionType() {
      return JunctionType.met.name();
   }
}
