package com.bstek.urule.runtime.rete;

import com.bstek.urule.model.rule.lhs.Criterion;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MetActivity extends JoinActivity {
   private int met;
   private boolean only;
   private boolean debug;
   private List<Criterion> criterions;

   public MetActivity(int met, List<Criterion> criterions, boolean debug, boolean only) {
      this.met = met;
      this.criterions = criterions;
      this.debug = debug;
      this.only = only;
   }

   @Override
   public Collection<FactTracker> enter(EvaluationContext context, Object obj, FactTracker tracker) {
      if (context.metActivityIsPassed(this)) {
         return new ArrayList<>();
      }

      int number = 0;

      for (Criterion criterion : this.criterions) {
         if (criterion.doEval(context, this.debug)) {
            number++;
         }
      }

      if (this.debug) {
         context.getLogger().logMet(this.met, number, this.only);
      }

      context.addPassedMetActivity(this);
      if (this.only) {
         return number == this.met ? this.visitPahs(context, obj, tracker) : new ArrayList<>();
      } else {
         return number >= this.met ? this.visitPahs(context, obj, tracker) : new ArrayList<>();
      }
   }
}
