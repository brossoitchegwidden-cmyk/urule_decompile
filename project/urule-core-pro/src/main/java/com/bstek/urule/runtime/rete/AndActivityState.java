package com.bstek.urule.runtime.rete;

import com.bstek.urule.model.rule.lhs.Criteria;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class AndActivityState extends ActivityState {
   private Map<Path, Set<Criteria>> pathCriteriaMap = new HashMap<>();
   private Map<Path, List<Map<String, Object>>> pathFactMaps = new HashMap<>();

   public AndActivityState(String id) {
      super(id);
   }

   public Map<Path, Set<Criteria>> getPathCriteriaMap() {
      return this.pathCriteriaMap;
   }

   public Map<Path, List<Map<String, Object>>> getPathFactMaps() {
      return this.pathFactMaps;
   }
}
