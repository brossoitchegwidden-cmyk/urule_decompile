package com.bstek.urule.model.rule.lhs;

import com.bstek.urule.runtime.rete.EvaluationContext;
import java.util.List;

public class Met extends Junction {
   private int met;
   private boolean only;

   @Override
   public boolean doEval(EvaluationContext var1, boolean var2) {
      List var3 = this.getCriterions();
      int var4 = 0;

      for (Criterion var6 : (Iterable<Criterion>)(Iterable<?>)(var3)) {
         if (var6.doEval(var1, var2)) {
            var4++;
         }
      }

      if (this.only) {
         if (var4 == this.met) {
            return true;
         }
      } else if (var4 >= this.met) {
         return true;
      }

      return false;
   }

   public int getMet() {
      return this.met;
   }

   public void setMet(int var1) {
      this.met = var1;
   }

   public boolean isOnly() {
      return this.only;
   }

   public void setOnly(boolean var1) {
      this.only = var1;
   }

   @Override
   public String getJunctionType() {
      return JunctionType.met.name();
   }
}
