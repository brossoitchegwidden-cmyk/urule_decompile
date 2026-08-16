package com.bstek.urule.runtime.rete;

import com.bstek.urule.model.rule.Rule;
import com.bstek.urule.runtime.agenda.ActivationImpl;
import java.util.ArrayList;
import java.util.Collection;

public class TerminalActivity extends AbstractActivity {
   private Rule b;

   public TerminalActivity(Rule var1) {
      this.b = var1;
   }

   @Override
   public Collection<FactTracker> enter(EvaluationContext var1, Object var2, FactTracker var3) {
      ArrayList var4 = new ArrayList();
      ActivationImpl var5 = new ActivationImpl(this.b);
      var3.setActivation(var5);
      var4.add(var3);
      if (this.b.getDebug() != null && this.b.getDebug() && !"__*__".equals(var2)) {
         var1.getLogger().logMatchRule(this.b, var3.getCriterias());
      }

      return var4;
   }
}
