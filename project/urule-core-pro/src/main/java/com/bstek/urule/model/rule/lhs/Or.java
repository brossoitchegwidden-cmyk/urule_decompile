package com.bstek.urule.model.rule.lhs;

import com.bstek.urule.runtime.rete.EvaluationContext;

public class Or extends Junction {
   @Override
   public boolean doEval(EvaluationContext context, boolean debug) {
      for (Criterion criterion : this.getCriterions()) {
         if (criterion.doEval(context, debug)) {
            return true;
         }
      }

      return false;
   }

   @Override
   public String getJunctionType() {
      return JunctionType.or.name();
   }
}
