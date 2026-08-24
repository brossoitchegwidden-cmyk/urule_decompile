package com.bstek.urule.model.rule.lhs;

import com.bstek.urule.runtime.rete.EvaluationContext;
import java.util.Map;

public interface BaseCriteria {
   EvaluateResponse evaluate(EvaluationContext context, Map<String, Object> factMap);

   String getId();
}
