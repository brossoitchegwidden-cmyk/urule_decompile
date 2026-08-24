package com.bstek.urule.model.rete.jsondeserializer.math;

import com.bstek.urule.model.rete.JsonUtils;
import com.bstek.urule.model.rule.math.LogMath;
import com.bstek.urule.model.rule.math.MathSign;
import com.bstek.urule.model.rule.math.MathType;
import com.fasterxml.jackson.databind.JsonNode;

public class LogMathDeserializer implements MathDeserializer {
   @Override
   public MathSign deserialize(JsonNode jsonNode) {
      LogMath logMath = new LogMath();
      logMath.setBaseValue(JsonUtils.parseValueNode(jsonNode.get("baseValue")));
      logMath.setValue(JsonUtils.parseValueNode(jsonNode.get("value")));
      return logMath;
   }

   @Override
   public MathType getType() {
      return MathType.log;
   }
}
