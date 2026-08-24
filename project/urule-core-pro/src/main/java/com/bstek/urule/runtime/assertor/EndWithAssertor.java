package com.bstek.urule.runtime.assertor;

import com.bstek.urule.model.library.Datatype;
import com.bstek.urule.model.rule.Op;

public class EndWithAssertor implements Assertor {
   @Override
   public boolean eval(Object left, Object right, Datatype datatype) {
      if (left == null || right == null) {
         return false;
      } else if (left instanceof String && right instanceof String) {
         String text = left.toString();
         String text2 = right.toString();
         return text.endsWith(text2);
      } else {
         return false;
      }
   }

   @Override
   public Op supportOp() {
      return Op.EndWith;
   }
}
