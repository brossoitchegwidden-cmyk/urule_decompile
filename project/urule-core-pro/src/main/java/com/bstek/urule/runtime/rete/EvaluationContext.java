package com.bstek.urule.runtime.rete;

import java.util.Set;

public interface EvaluationContext extends Context {
   void reset();

   Integer nextToken();

   void resetActivitiesState();

   void addPassedOrActivity(OrActivity var1);

   void addPassedMetActivity(MetActivity var1);

   boolean orActivityIsPassed(OrActivity var1);

   boolean metActivityIsPassed(MetActivity var1);

   CriteriaActivityState getActivityState(String var1);

   AndActivityState getAndActivityState(String var1);

   Set<String> getPathPassedSet();
}
