package com.bstek.urule.model.rete.jsondeserializer.math;

import com.bstek.urule.model.rete.JsonUtils;
import com.bstek.urule.model.rule.math.AbsoluteMath;
import com.bstek.urule.model.rule.math.MathSign;
import com.bstek.urule.model.rule.math.MathType;
import com.fasterxml.jackson.databind.JsonNode;

public class AbsoluteMathDeserializer implements MathDeserializer {
   @Override
   public MathSign deserialize(JsonNode jsonNode) {
      AbsoluteMath absoluteMath = new AbsoluteMath();
      JsonNode jsonNode2 = jsonNode.get("value");
      absoluteMath.setValue(JsonUtils.parseValueNode(jsonNode2));
      return absoluteMath;
   }

   @Override
   public MathType getType() {
      return MathType.absolute;
   }
}
