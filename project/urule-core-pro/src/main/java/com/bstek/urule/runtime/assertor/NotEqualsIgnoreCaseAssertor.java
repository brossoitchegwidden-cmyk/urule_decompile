package com.bstek.urule.runtime.assertor;

import com.bstek.urule.model.library.Datatype;
import com.bstek.urule.model.rule.Op;

public class NotEqualsIgnoreCaseAssertor extends EqualsIgnoreCaseAssertor {
   @Override
   public boolean eval(Object left, Object right, Datatype datatype) {
      if (left == null && right != null) {
         return true;
      } else if (left != null && right == null) {
         return true;
      } else {
         return left == null && right == null ? false : !super.eval(left, right, datatype);
      }
   }

   @Override
   public Op supportOp() {
      return Op.NotEqualsIgnoreCase;
   }
}
