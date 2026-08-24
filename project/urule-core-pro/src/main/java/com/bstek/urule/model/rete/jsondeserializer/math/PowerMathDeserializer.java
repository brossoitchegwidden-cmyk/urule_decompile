package com.bstek.urule.model.rete.jsondeserializer.math;

import com.bstek.urule.model.rete.JsonUtils;
import com.bstek.urule.model.rule.math.MathSign;
import com.bstek.urule.model.rule.math.MathType;
import com.bstek.urule.model.rule.math.PowerMath;
import com.fasterxml.jackson.databind.JsonNode;

public class PowerMathDeserializer implements MathDeserializer {
   @Override
   public MathSign deserialize(JsonNode jsonNode) {
      PowerMath powerMath = new PowerMath();
      powerMath.setBase(JsonUtils.parseValueNode(jsonNode.get("base")));
      powerMath.setPower(JsonUtils.parseValueNode(jsonNode.get("power")));
      return powerMath;
   }

   @Override
   public MathType getType() {
      return MathType.power;
   }
}
