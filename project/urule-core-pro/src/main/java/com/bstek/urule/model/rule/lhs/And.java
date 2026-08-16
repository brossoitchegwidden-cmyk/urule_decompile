package com.bstek.urule.model.rule.lhs;

import com.bstek.urule.runtime.rete.EvaluationContext;

public class And extends Junction {
   @Override
   public boolean doEval(EvaluationContext var1, boolean var2) {
      for (Criterion var5 : this.getCriterions()) {
         if (!var5.doEval(var1, var2)) {
            return false;
         }
      }

      return true;
   }

   @Override
   public String getJunctionType() {
      return JunctionType.and.name();
   }
}
