package com.bstek.urule.model.scorecard.runtime;

import com.bstek.urule.runtime.rete.Context;

public interface ScoringStrategy {
   /**计算得分方法*/
   Object calculate(Scorecard scorecard, Context context);
}
