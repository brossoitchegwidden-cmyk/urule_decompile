package com.bstek.urule.runtime.assertor;

import com.bstek.urule.model.library.Datatype;
import com.bstek.urule.model.rule.Op;

public class NotInAssertor extends InAssertor {
   @Override
   public boolean eval(Object var1, Object var2, Datatype var3) {
      return !super.eval(var1, var2, var3);
   }

   @Override
   public Op supportOp() {
      return Op.NotIn;
   }
}
