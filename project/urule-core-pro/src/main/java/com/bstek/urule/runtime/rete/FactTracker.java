package com.bstek.urule.runtime.rete;

import com.bstek.urule.model.rule.lhs.Criteria;
import com.bstek.urule.runtime.agenda.Activation;
import com.bstek.urule.runtime.agenda.ActivationImpl;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class FactTracker {
   private Path currentPath;
   private Activation activation;
   private Set<Integer> tokens = new HashSet<>();
   private Set<Criteria> criterias = new HashSet<>();
   private Map<String, Object> factMap = new HashMap<>();

   public Activation getActivation() {
      return this.activation;
   }

   public void setActivation(Activation activation) {
      ActivationImpl activationImpl = (ActivationImpl)activation;
      activationImpl.setCriterias(this.criterias);
      activationImpl.setFactMap(this.factMap);
      this.activation = activation;
   }

   public void addFactMap(Map<String, Object> map) {
      this.factMap.putAll(map);
   }

   public Map<String, Object> getFactMap() {
      return this.factMap;
   }

   public void addCriteria(Criteria criteria) {
      this.criterias.add(criteria);
   }

   public void addCriterias(Set<Criteria> list) {
      this.criterias.addAll(list);
   }

   public Set<Criteria> getCriterias() {
      return this.criterias;
   }

   public Set<Integer> getTokens() {
      return this.tokens;
   }

   public void setToken(Integer token) {
      this.tokens.clear();
      this.tokens.add(token);
   }

   public void setTokens(Set<Integer> set) {
      this.tokens.clear();
      this.tokens.addAll(set);
   }

   public void setCurrentPath(Path currentPath) {
      this.currentPath = currentPath;
   }

   public Path getCurrentPath() {
      return this.currentPath;
   }

   public FactTracker newSubFactTracker() {
      FactTracker factTracker = new FactTracker();
      factTracker.setTokens(this.tokens);
      factTracker.addCriterias(this.criterias);
      factTracker.addFactMap(this.factMap);
      return factTracker;
   }
}
