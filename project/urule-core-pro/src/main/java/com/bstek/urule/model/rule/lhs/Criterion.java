package com.bstek.urule.model.rule.lhs;

import com.bstek.urule.runtime.rete.EvaluationContext;

public interface Criterion {
   Junction getParent();

   boolean doEval(EvaluationContext context, boolean debug);

   void setParent(Junction parent);
}
