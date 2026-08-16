package com.bstek.urule.runtime.rete;

import com.bstek.urule.model.rule.lhs.Criteria;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class AndActivityState extends ActivityState {
   private Map<Path, Set<Criteria>> a = new HashMap<>();
   private Map<Path, List<Map<String, Object>>> b = new HashMap<>();

   public AndActivityState(String var1) {
      super(var1);
   }

   public Map<Path, Set<Criteria>> getPathCriteriaMap() {
      return this.a;
   }

   public Map<Path, List<Map<String, Object>>> getPathFactMaps() {
      return this.b;
   }
}
