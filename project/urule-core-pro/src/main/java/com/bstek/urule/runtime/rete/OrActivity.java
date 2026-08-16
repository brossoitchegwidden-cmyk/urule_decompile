package com.bstek.urule.runtime.rete;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class OrActivity extends JoinActivity {
   public List<FactTracker> enter(EvaluationContext var1, Object var2, FactTracker var3) {
      CriteriaActivityState var4 = var1.getActivityState(this.a);
      Set var5 = var4.getTokensSet();
      var5.addAll(var3.getTokens());
      if (var1.orActivityIsPassed(this)) {
         return new ArrayList<>();
      }

      var1.addPassedOrActivity(this);
      return this.a(var1, var2, var3);
   }

   @Override
   public boolean orNodeTokensExist(EvaluationContext var1, Set<Integer> var2) {
      CriteriaActivityState var3 = var1.getActivityState(this.a);
      Set var4 = var3.getTokensSet();

      for (Integer var6 : var2) {
         if (!var4.contains(var6)) {
            return false;
         }
      }

      return true;
   }
}
