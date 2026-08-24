package com.bstek.urule.runtime.rete;

import java.util.Set;

public interface EvaluationContext extends Context {
   void reset();

   Integer nextToken();

   void resetActivitiesState();

   void addPassedOrActivity(OrActivity or);

   void addPassedMetActivity(MetActivity met);

   boolean orActivityIsPassed(OrActivity or);

   boolean metActivityIsPassed(MetActivity met);

   CriteriaActivityState getActivityState(String id);

   AndActivityState getAndActivityState(String id);

   Set<String> getPathPassedSet();
}
