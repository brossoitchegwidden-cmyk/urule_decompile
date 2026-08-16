package com.bstek.urule.runtime.assertor;

import com.bstek.urule.model.library.Datatype;
import com.bstek.urule.model.rule.Op;

public class NotContainAssertor extends ContainAssertor {
   @Override
   public boolean eval(Object var1, Object var2, Datatype var3) {
      return var1 != null && var2 != null ? !super.eval(var1, var2, var3) : false;
   }

   @Override
   public Op supportOp() {
      return Op.NotContain;
   }
}
