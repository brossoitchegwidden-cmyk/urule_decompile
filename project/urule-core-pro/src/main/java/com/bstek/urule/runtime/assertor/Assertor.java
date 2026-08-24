package com.bstek.urule.runtime.assertor;

import com.bstek.urule.model.library.Datatype;
import com.bstek.urule.model.rule.Op;

public interface Assertor {
   boolean eval(Object left, Object right, Datatype datatype);

   Op supportOp();
}
