package com.bstek.urule.model.rule.lhs;

import com.bstek.urule.runtime.rete.EvaluationContext;

public class Or extends Junction {
   @Override
   public boolean doEval(EvaluationContext var1, boolean var2) {
      for (Criterion var5 : this.getCriterions()) {
         if (var5.doEval(var1, var2)) {
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
