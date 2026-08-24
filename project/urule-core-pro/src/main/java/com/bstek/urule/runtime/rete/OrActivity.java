package com.bstek.urule.runtime.rete;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class OrActivity extends JoinActivity {
   public List<FactTracker> enter(EvaluationContext context, Object obj, FactTracker tracker) {
      CriteriaActivityState activityState = context.getActivityState(this.activityId);
      Set tokensSet = activityState.getTokensSet();
      tokensSet.addAll(tracker.getTokens());
      if (context.orActivityIsPassed(this)) {
         return new ArrayList<>();
      }

      context.addPassedOrActivity(this);
      return this.visitPahs(context, obj, tracker);
   }

   @Override
   public boolean orNodeTokensExist(EvaluationContext context, Set<Integer> set) {
      CriteriaActivityState activityState = context.getActivityState(this.activityId);
      Set tokensSet = activityState.getTokensSet();

      for (Integer number : set) {
         if (!tokensSet.contains(number)) {
            return false;
         }
      }

      return true;
   }
}
