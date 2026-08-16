package com.bstek.urule.runtime.assertor;

import com.bstek.urule.model.library.Datatype;
import com.bstek.urule.model.rule.Op;
import org.apache.commons.lang.StringUtils;

public class NullAssertor implements Assertor {
   @Override
   public boolean eval(Object var1, Object var2, Datatype var3) {
      return var1 == null ? true : StringUtils.isBlank(var1.toString());
   }

   @Override
   public Op supportOp() {
      return Op.Null;
   }
}
