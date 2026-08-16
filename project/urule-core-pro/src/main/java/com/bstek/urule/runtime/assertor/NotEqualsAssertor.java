package com.bstek.urule.runtime.assertor;

import com.bstek.urule.model.library.Datatype;
import com.bstek.urule.model.rule.Op;

public class NotEqualsAssertor extends EqualsAssertor {
   @Override
   public boolean eval(Object var1, Object var2, Datatype var3) {
      if (var1 == null && var2 != null) {
         return true;
      } else if (var1 != null && var2 == null) {
         return true;
      } else {
         return var1 == null && var2 == null ? false : !super.eval(var1, var2, var3);
      }
   }

   @Override
   public Op supportOp() {
      return Op.NotEquals;
   }
}
