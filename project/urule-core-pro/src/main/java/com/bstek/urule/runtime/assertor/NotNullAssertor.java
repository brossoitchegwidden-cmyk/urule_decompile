package com.bstek.urule.runtime.assertor;

import com.bstek.urule.model.library.Datatype;
import com.bstek.urule.model.rule.Op;
import org.apache.commons.lang.StringUtils;

public class NotNullAssertor implements Assertor {
   @Override
   public boolean eval(Object left, Object right, Datatype datatype) {
      return left != null && StringUtils.isNotBlank(left.toString());
   }

   @Override
   public Op supportOp() {
      return Op.NotNull;
   }
}
