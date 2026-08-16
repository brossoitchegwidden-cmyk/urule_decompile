package com.bstek.urule.runtime.rete;

import com.bstek.urule.runtime.WorkingMemory;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class EvaluationContextImpl extends ContextImpl implements EvaluationContext {
   private int a = 0;
   private Set<OrActivity> b = new HashSet<>();
   private Set<MetActivity> c = new HashSet<>();
   private Set<String> d = new HashSet<>();
   private Map<String, ActivityState> e = new HashMap<>();

   public EvaluationContextImpl(WorkingMemory var1, Map<String, String> var2) {
      super(var1, var2);
   }

   @Override
   public void resetActivitiesState() {
      this.d.clear();
      this.e.clear();
      this.b.clear();
   }

   @Override
   public boolean orActivityIsPassed(OrActivity var1) {
      return this.b.contains(var1);
   }

   @Override
   public boolean metActivityIsPassed(MetActivity var1) {
      return this.c.contains(var1);
   }

   @Override
   public void addPassedMetActivity(MetActivity var1) {
      this.c.add(var1);
   }

   @Override
   public void addPassedOrActivity(OrActivity var1) {
      this.b.add(var1);
   }

   @Override
   public CriteriaActivityState getActivityState(String var1) {
      CriteriaActivityState var2 = (CriteriaActivityState)this.e.get(var1);
      if (var2 == null) {
         var2 = new CriteriaActivityState(var1);
         this.e.put(var1, var2);
      }

      return var2;
   }

   @Override
   public AndActivityState getAndActivityState(String var1) {
      AndActivityState var2 = (AndActivityState)this.e.get(var1);
      if (var2 == null) {
         var2 = new AndActivityState(var1);
         this.e.put(var1, var2);
      }

      return var2;
   }

   @Override
   public Set<String> getPathPassedSet() {
      return this.d;
   }

   @Override
   public Integer nextToken() {
      return this.a++;
   }

   @Override
   public void reset() {
      this.a = 0;
   }
}
