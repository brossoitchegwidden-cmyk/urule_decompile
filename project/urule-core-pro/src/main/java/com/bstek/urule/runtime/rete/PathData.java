package com.bstek.urule.runtime.rete;

import com.bstek.urule.model.rule.lhs.Criteria;
import java.util.Map;
import java.util.Set;

class PathData {
   private Set<Criteria> criterias;
   private Map<String, Object> factMap;

   public PathData(Set<Criteria> criterias2, Map<String, Object> valuesByKey) {
      this.criterias = criterias2;
      this.factMap = valuesByKey;
   }

   public Set<Criteria> getCriterias() {
      return this.criterias;
   }

   public void addCriterias(Set<Criteria> criterias2) {
      this.criterias.addAll(criterias2);
   }

   public void setCriterias(Set<Criteria> criterias2) {
      this.criterias = criterias2;
   }

   public Map<String, Object> getFactMap() {
      return this.factMap;
   }

   public void setFactMap(Map<String, Object> valuesByKey) {
      this.factMap = valuesByKey;
   }
}
