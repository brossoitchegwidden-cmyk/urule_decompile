package com.bstek.urule.runtime.assertor;

import com.bstek.urule.model.library.Datatype;
import com.bstek.urule.model.rule.Op;

public class NotInAssertor extends InAssertor {
   @Override
   public boolean eval(Object left, Object right, Datatype datatype) {
      return !super.eval(left, right, datatype);
   }

   @Override
   public Op supportOp() {
      return Op.NotIn;
   }
}
