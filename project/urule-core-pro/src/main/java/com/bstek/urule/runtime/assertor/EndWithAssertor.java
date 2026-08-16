package com.bstek.urule.runtime.assertor;

import com.bstek.urule.model.library.Datatype;
import com.bstek.urule.model.rule.Op;

public class EndWithAssertor implements Assertor {
   @Override
   public boolean eval(Object var1, Object var2, Datatype var3) {
      if (var1 == null || var2 == null) {
         return false;
      } else if (var1 instanceof String && var2 instanceof String) {
         String var4 = var1.toString();
         String var5 = var2.toString();
         return var4.endsWith(var5);
      } else {
         return false;
      }
   }

   @Override
   public Op supportOp() {
      return Op.EndWith;
   }
}
