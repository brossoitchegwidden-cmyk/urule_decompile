package com.bstek.urule.model.rete.jsondeserializer.math;

import com.bstek.urule.model.rete.JsonUtils;
import com.bstek.urule.model.rule.math.LnMath;
import com.bstek.urule.model.rule.math.MathSign;
import com.bstek.urule.model.rule.math.MathType;
import com.fasterxml.jackson.databind.JsonNode;

public class LnMathDeserializer implements MathDeserializer {
   @Override
   public MathSign deserialize(JsonNode var1) {
      LnMath var2 = new LnMath();
      var2.setValue(JsonUtils.parseValueNode(var1.get("value")));
      return var2;
   }

   @Override
   public MathType getType() {
      return MathType.ln;
   }
}
