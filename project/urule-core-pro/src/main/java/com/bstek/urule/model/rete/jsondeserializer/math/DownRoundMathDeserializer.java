package com.bstek.urule.model.rete.jsondeserializer.math;

import com.bstek.urule.model.rete.JsonUtils;
import com.bstek.urule.model.rule.math.DownRoundMath;
import com.bstek.urule.model.rule.math.MathSign;
import com.bstek.urule.model.rule.math.MathType;
import com.fasterxml.jackson.databind.JsonNode;

public class DownRoundMathDeserializer implements MathDeserializer {
   @Override
   public MathSign deserialize(JsonNode jsonNode) {
      DownRoundMath downRoundMath = new DownRoundMath();
      downRoundMath.setValue(JsonUtils.parseValueNode(jsonNode.get("value")));
      return downRoundMath;
   }

   @Override
   public MathType getType() {
      return MathType.downRound;
   }
}
