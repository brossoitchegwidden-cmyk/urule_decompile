package com.bstek.urule.model.rete.jsondeserializer.math;

import com.bstek.urule.model.rete.JsonUtils;
import com.bstek.urule.model.rule.math.MathSign;
import com.bstek.urule.model.rule.math.MathType;
import com.bstek.urule.model.rule.math.NRadicalMath;
import com.fasterxml.jackson.databind.JsonNode;

public class NRadicalMathDeserializer implements MathDeserializer {
   @Override
   public MathSign deserialize(JsonNode jsonNode) {
      NRadicalMath nRadicalMath = new NRadicalMath();
      JsonNode power = jsonNode.get("power");
      nRadicalMath.setPower(JsonUtils.parseValueNode(power));
      nRadicalMath.setValue(JsonUtils.parseValueNode(jsonNode.get("value")));
      return nRadicalMath;
   }

   @Override
   public MathType getType() {
      return MathType.nradical;
   }
}
