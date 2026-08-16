package com.bstek.urule.model.rule.lhs;

import com.bstek.urule.runtime.rete.EvaluationContext;

public interface Criterion {
   Junction getParent();

   boolean doEval(EvaluationContext var1, boolean var2);

   void setParent(Junction var1);
}
