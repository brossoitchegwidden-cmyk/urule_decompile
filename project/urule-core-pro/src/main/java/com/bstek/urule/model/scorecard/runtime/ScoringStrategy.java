package com.bstek.urule.model.scorecard.runtime;

import com.bstek.urule.runtime.rete.Context;

public interface ScoringStrategy {
   Object calculate(Scorecard var1, Context var2);
}
