package com.bstek.urule.runtime.rete;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CriteriaActivityState extends ActivityState {
   private Set<String> classSet = new HashSet<>();
   private List<Map<String, Object>> factMapList = new ArrayList<>();

   public CriteriaActivityState(String id) {
      super(id);
   }

   public Set<String> getClassSet() {
      return this.classSet;
   }

   public List<Map<String, Object>> getFactMapList() {
      return this.factMapList;
   }
}
