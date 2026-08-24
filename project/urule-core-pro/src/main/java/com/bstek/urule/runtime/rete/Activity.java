package com.bstek.urule.runtime.rete;

import java.util.List;
import java.util.Set;

public interface Activity extends Instance {
   List<Path> getPaths();

   boolean orNodeTokensExist(EvaluationContext context, Set<Integer> tokens);
}
