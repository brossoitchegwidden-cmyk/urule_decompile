package com.bstek.urule.model.rete.jsondeserializer.math;

import com.bstek.urule.model.rete.JsonUtils;
import com.bstek.urule.model.rule.math.AbsoluteMath;
import com.bstek.urule.model.rule.math.MathSign;
import com.bstek.urule.model.rule.math.MathType;
import com.fasterxml.jackson.databind.JsonNode;

public class AbsoluteMathDeserializer implements MathDeserializer {
   @Override
   public MathSign deserialize(JsonNode var1) {
      AbsoluteMath var2 = new AbsoluteMath();
      JsonNode var3 = var1.get("value");
      var2.setValue(JsonUtils.parseValueNode(var3));
      return var2;
   }

   @Override
   public MathType getType() {
      return MathType.absolute;
   }
}
