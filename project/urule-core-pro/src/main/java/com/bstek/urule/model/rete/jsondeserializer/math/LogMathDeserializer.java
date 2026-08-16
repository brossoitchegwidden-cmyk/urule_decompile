package com.bstek.urule.model.rete.jsondeserializer.math;

import com.bstek.urule.model.rete.JsonUtils;
import com.bstek.urule.model.rule.math.LogMath;
import com.bstek.urule.model.rule.math.MathSign;
import com.bstek.urule.model.rule.math.MathType;
import com.fasterxml.jackson.databind.JsonNode;

public class LogMathDeserializer implements MathDeserializer {
   @Override
   public MathSign deserialize(JsonNode var1) {
      LogMath var2 = new LogMath();
      var2.setBaseValue(JsonUtils.parseValueNode(var1.get("baseValue")));
      var2.setValue(JsonUtils.parseValueNode(var1.get("value")));
      return var2;
   }

   @Override
   public MathType getType() {
      return MathType.log;
   }
}
