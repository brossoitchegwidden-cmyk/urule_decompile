package com.bstek.urule.runtime.assertor;

import com.bstek.urule.model.library.Datatype;
import com.bstek.urule.model.rule.Op;

public class NotContainAssertor extends ContainAssertor {
   @Override
   public boolean eval(Object left, Object right, Datatype datatype) {
      return left != null && right != null ? !super.eval(left, right, datatype) : false;
   }

   @Override
   public Op supportOp() {
      return Op.NotContain;
   }
}
