package com.bstek.urule.runtime.assertor;

import com.bstek.urule.model.library.Datatype;
import com.bstek.urule.model.rule.Op;
import java.util.Collection;

public class ContainAssertor implements Assertor {
   @Override
   public boolean eval(Object left, Object right, Datatype datatype) {
      if (left == null || right == null) {
         return false;
      }

      if (left instanceof String) {
         return left.toString().contains(right.toString());
      }

      if (left instanceof Collection) {
         Collection left2 = (Collection)left;
         if (right instanceof Collection) {
            Collection right2 = (Collection)right;
            return left2.containsAll(right2);
         } else {
            return left2.contains(right);
         }
      } else {
         String text = left.toString();
         String text2 = right.toString();
         return text.contains(text2);
      }
   }

   @Override
   public Op supportOp() {
      return Op.Contain;
   }
}
