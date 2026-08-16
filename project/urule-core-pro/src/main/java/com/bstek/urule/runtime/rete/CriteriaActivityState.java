package com.bstek.urule.runtime.rete;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CriteriaActivityState extends ActivityState {
   private Set<String> a = new HashSet<>();
   private List<Map<String, Object>> b = new ArrayList<>();

   public CriteriaActivityState(String var1) {
      super(var1);
   }

   public Set<String> getClassSet() {
      return this.a;
   }

   public List<Map<String, Object>> getFactMapList() {
      return this.b;
   }
}
