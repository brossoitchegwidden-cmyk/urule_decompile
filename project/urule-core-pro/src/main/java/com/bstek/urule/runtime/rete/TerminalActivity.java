package com.bstek.urule.runtime.rete;

import com.bstek.urule.model.rule.Rule;
import com.bstek.urule.runtime.agenda.ActivationImpl;
import java.util.ArrayList;
import java.util.Collection;

public class TerminalActivity extends AbstractActivity {
   private Rule rule;

   public TerminalActivity(Rule rule) {
      this.rule = rule;
   }

   @Override
   public Collection<FactTracker> enter(EvaluationContext context, Object obj, FactTracker tracker) {
      ArrayList enterResult = new ArrayList();
      ActivationImpl activationImpl = new ActivationImpl(this.rule);
      tracker.setActivation(activationImpl);
      enterResult.add(tracker);
      if (this.rule.getDebug() != null && this.rule.getDebug() && !"__*__".equals(obj)) {
         context.getLogger().logMatchRule(this.rule, tracker.getCriterias());
      }

      return enterResult;
   }
}
