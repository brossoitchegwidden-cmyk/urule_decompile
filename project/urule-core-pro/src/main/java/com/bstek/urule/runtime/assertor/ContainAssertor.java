package com.bstek.urule.runtime.assertor;

import com.bstek.urule.model.library.Datatype;
import com.bstek.urule.model.rule.Op;
import java.util.Collection;

public class ContainAssertor implements Assertor {
   @Override
   public boolean eval(Object var1, Object var2, Datatype var3) {
      if (var1 == null || var2 == null) {
         return false;
      }

      if (var1 instanceof String) {
         return var1.toString().contains(var2.toString());
      }

      if (var1 instanceof Collection) {
         Collection var6 = (Collection)var1;
         if (var2 instanceof Collection) {
            Collection var7 = (Collection)var2;
            return var6.containsAll(var7);
         } else {
            return var6.contains(var2);
         }
      } else {
         String var4 = var1.toString();
         String var5 = var2.toString();
         return var4.contains(var5);
      }
   }

   @Override
   public Op supportOp() {
      return Op.Contain;
   }
}
