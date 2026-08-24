package com.bstek.urule.runtime.rete;

import com.bstek.urule.runtime.WorkingMemory;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class EvaluationContextImpl extends ContextImpl implements EvaluationContext {
   private int nextToken;
   private Set<OrActivity> orActivities = new HashSet<>();
   private Set<MetActivity> metActivities = new HashSet<>();
   private Set<String> pathPassedSet = new HashSet<>();
   private Map<String, ActivityState> activityStatesById = new HashMap<>();

   public EvaluationContextImpl(WorkingMemory workingMemory, Map<String, String> variableCategoryMap) {
      super(workingMemory, variableCategoryMap);
   }

   @Override
   public void resetActivitiesState() {
      this.pathPassedSet.clear();
      this.activityStatesById.clear();
      this.orActivities.clear();
   }

   @Override
   public boolean orActivityIsPassed(OrActivity or) {
      return this.orActivities.contains(or);
   }

   @Override
   public boolean metActivityIsPassed(MetActivity met) {
      return this.metActivities.contains(met);
   }

   @Override
   public void addPassedMetActivity(MetActivity met) {
      this.metActivities.add(met);
   }

   @Override
   public void addPassedOrActivity(OrActivity or) {
      this.orActivities.add(or);
   }

   @Override
   public CriteriaActivityState getActivityState(String id) {
      CriteriaActivityState criteriaActivityState = (CriteriaActivityState)this.activityStatesById.get(id);
      if (criteriaActivityState == null) {
         criteriaActivityState = new CriteriaActivityState(id);
         this.activityStatesById.put(id, criteriaActivityState);
      }

      return criteriaActivityState;
   }

   @Override
   public AndActivityState getAndActivityState(String id) {
      AndActivityState andActivityState = (AndActivityState)this.activityStatesById.get(id);
      if (andActivityState == null) {
         andActivityState = new AndActivityState(id);
         this.activityStatesById.put(id, andActivityState);
      }

      return andActivityState;
   }

   @Override
   public Set<String> getPathPassedSet() {
      return this.pathPassedSet;
   }

   @Override
   public Integer nextToken() {
      return this.nextToken++;
   }

   @Override
   public void reset() {
      this.nextToken = 0;
   }
}
