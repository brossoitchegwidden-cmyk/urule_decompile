package com.bstek.urule.runtime.assertor;

import com.bstek.urule.model.library.Datatype;
import com.bstek.urule.model.rule.Op;

public class StartWithAssertor implements Assertor {
   @Override
   public boolean eval(Object left, Object right, Datatype datatype) {
      if (left == null || right == null) {
         return false;
      } else if (left instanceof String && right instanceof String) {
         String left2 = (String)left;
         String right2 = (String)right;
         return left2.startsWith(right2);
      } else {
         return false;
      }
   }

   @Override
   public Op supportOp() {
      return Op.StartWith;
   }
}
