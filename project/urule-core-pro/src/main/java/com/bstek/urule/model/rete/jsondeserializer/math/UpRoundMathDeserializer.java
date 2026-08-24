package com.bstek.urule.model.rete.jsondeserializer.math;

import com.bstek.urule.model.rete.JsonUtils;
import com.bstek.urule.model.rule.math.MathSign;
import com.bstek.urule.model.rule.math.MathType;
import com.bstek.urule.model.rule.math.UpRoundMath;
import com.fasterxml.jackson.databind.JsonNode;

public class UpRoundMathDeserializer implements MathDeserializer {
   @Override
   public MathSign deserialize(JsonNode jsonNode) {
      UpRoundMath upRoundMath = new UpRoundMath();
      upRoundMath.setValue(JsonUtils.parseValueNode(jsonNode.get("value")));
      return upRoundMath;
   }

   @Override
   public MathType getType() {
      return MathType.upRound;
   }
}
