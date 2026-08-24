package com.bstek.urule.runtime.assertor;

import com.bstek.urule.exception.RuleException;
import com.bstek.urule.model.library.Datatype;
import com.bstek.urule.model.rule.Op;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class AssertorEvaluator implements ApplicationContextAware {
   public static final String BEAN_ID = "urule.assertorEvaluator";
   private Map<Op, Assertor> assertorsByOperator = new HashMap<>();

   public boolean evaluate(Object left, Object right, Datatype datatype, Op op) {
      Assertor assertor = this.assertorsByOperator.get(op);
      if (assertor == null) {
         throw new RuleException("Unsupport op:" + op);
      } else {
         return assertor.eval(left, right, datatype);
      }
   }

   public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
      for (Assertor assertor : applicationContext.getBeansOfType(Assertor.class).values()) {
         this.assertorsByOperator.put(assertor.supportOp(), assertor);
      }
   }
}
