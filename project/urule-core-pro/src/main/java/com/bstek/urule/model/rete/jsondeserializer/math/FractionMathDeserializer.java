package com.bstek.urule.model.rete.jsondeserializer.math;

import com.bstek.urule.model.rete.JsonUtils;
import com.bstek.urule.model.rule.math.FractionMath;
import com.bstek.urule.model.rule.math.MathSign;
import com.bstek.urule.model.rule.math.MathType;
import com.fasterxml.jackson.databind.JsonNode;

public class FractionMathDeserializer implements MathDeserializer {
   @Override
   public MathSign deserialize(JsonNode jsonNode) {
      FractionMath fractionMath = new FractionMath();
      JsonNode numerator = jsonNode.get("numerator");
      fractionMath.setNumerator(JsonUtils.parseValueNode(numerator));
      JsonNode denominator = jsonNode.get("denominator");
      fractionMath.setDenominator(JsonUtils.parseValueNode(denominator));
      return fractionMath;
   }

   @Override
   public MathType getType() {
      return MathType.fraction;
   }
}
