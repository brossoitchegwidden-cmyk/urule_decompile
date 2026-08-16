package com.bstek.urule.model.rete.jsondeserializer.math;

import com.bstek.urule.model.rete.JsonUtils;
import com.bstek.urule.model.rule.math.MathSign;
import com.bstek.urule.model.rule.math.MathType;
import com.bstek.urule.model.rule.math.NRadicalMath;
import com.fasterxml.jackson.databind.JsonNode;

public class NRadicalMathDeserializer implements MathDeserializer {
   @Override
   public MathSign deserialize(JsonNode var1) {
      NRadicalMath var2 = new NRadicalMath();
      JsonNode var3 = var1.get("power");
      var2.setPower(JsonUtils.parseValueNode(var3));
      var2.setValue(JsonUtils.parseValueNode(var1.get("value")));
      return var2;
   }

   @Override
   public MathType getType() {
      return MathType.nradical;
   }
}
