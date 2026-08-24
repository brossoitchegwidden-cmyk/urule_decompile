package com.bstek.urule.model.rule.math;

import com.bstek.urule.runtime.rete.Context;
import java.util.Map;

public class PiMath implements MathSign {
   @Override
   public Object calculate(Context context, Map<String, Object> factMap) {
      return Math.PI;
   }

   @Override
   public String getId() {
      return "π";
   }

   @Override
   public MathType getType() {
      return MathType.pi;
   }
}
