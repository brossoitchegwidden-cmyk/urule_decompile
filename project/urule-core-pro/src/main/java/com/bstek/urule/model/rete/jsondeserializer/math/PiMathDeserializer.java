package com.bstek.urule.model.rete.jsondeserializer.math;

import com.bstek.urule.model.rule.math.MathSign;
import com.bstek.urule.model.rule.math.MathType;
import com.bstek.urule.model.rule.math.PiMath;
import com.fasterxml.jackson.databind.JsonNode;

public class PiMathDeserializer implements MathDeserializer {
   @Override
   public MathSign deserialize(JsonNode jsonNode) {
      return new PiMath();
   }

   @Override
   public MathType getType() {
      return MathType.pi;
   }
}
