package com.bstek.urule.builder.rete;

import com.bstek.urule.model.rule.Rule;
import java.util.Comparator;

class ReteBuilder$1 implements Comparator<Rule> {
   final ReteBuilder a;

   ReteBuilder$1(ReteBuilder var1) {
      this.a = var1;
   }

   public int compare(Rule var1, Rule var2) {
      Integer var3 = var1.getSalience();
      Integer var4 = var2.getSalience();
      if (var3 != null && var4 != null) {
         return var4 - var3;
      } else if (var4 != null) {
         return -1;
      } else {
         return var3 != null ? 1 : 0;
      }
   }
}
