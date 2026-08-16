package com.bstek.urule.runtime.rete;

import com.bstek.urule.model.rule.lhs.Criterion;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MetActivity extends JoinActivity {
   private int b;
   private boolean c;
   private boolean d;
   private List<Criterion> e;

   public MetActivity(int var1, List<Criterion> var2, boolean var3, boolean var4) {
      this.b = var1;
      this.e = var2;
      this.d = var3;
      this.c = var4;
   }

   @Override
   public Collection<FactTracker> enter(EvaluationContext var1, Object var2, FactTracker var3) {
      if (var1.metActivityIsPassed(this)) {
         return new ArrayList<>();
      }

      int var4 = 0;

      for (Criterion var6 : this.e) {
         if (var6.doEval(var1, this.d)) {
            var4++;
         }
      }

      if (this.d) {
         var1.getLogger().logMet(this.b, var4, this.c);
      }

      var1.addPassedMetActivity(this);
      if (this.c) {
         return var4 == this.b ? this.a(var1, var2, var3) : new ArrayList<>();
      } else {
         return var4 >= this.b ? this.a(var1, var2, var3) : new ArrayList<>();
      }
   }
}
