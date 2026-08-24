package com.bstek.urule.model.rete.jsondeserializer.math;

import com.bstek.urule.model.rete.JsonUtils;
import com.bstek.urule.model.rule.math.LnMath;
import com.bstek.urule.model.rule.math.MathSign;
import com.bstek.urule.model.rule.math.MathType;
import com.fasterxml.jackson.databind.JsonNode;

public class LnMathDeserializer implements MathDeserializer {
   @Override
   public MathSign deserialize(JsonNode jsonNode) {
      LnMath lnMath = new LnMath();
      lnMath.setValue(JsonUtils.parseValueNode(jsonNode.get("value")));
      return lnMath;
   }

   @Override
   public MathType getType() {
      return MathType.ln;
   }
}
