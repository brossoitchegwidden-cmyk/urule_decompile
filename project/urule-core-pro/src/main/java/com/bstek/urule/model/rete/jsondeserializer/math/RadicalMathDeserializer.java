package com.bstek.urule.model.rete.jsondeserializer.math;

import com.bstek.urule.model.rete.JsonUtils;
import com.bstek.urule.model.rule.math.MathSign;
import com.bstek.urule.model.rule.math.MathType;
import com.bstek.urule.model.rule.math.RadicalMath;
import com.fasterxml.jackson.databind.JsonNode;

public class RadicalMathDeserializer implements MathDeserializer {
   @Override
   public MathSign deserialize(JsonNode jsonNode) {
      RadicalMath radicalMath = new RadicalMath();
      radicalMath.setValue(JsonUtils.parseValueNode(jsonNode.get("value")));
      return radicalMath;
   }

   @Override
   public MathType getType() {
      return MathType.radical;
   }
}
