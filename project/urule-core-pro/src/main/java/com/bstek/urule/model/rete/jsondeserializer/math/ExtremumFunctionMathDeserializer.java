package com.bstek.urule.model.rete.jsondeserializer.math;

import com.bstek.urule.model.rete.JsonUtils;
import com.bstek.urule.model.rule.math.ExtremumMath;
import com.bstek.urule.model.rule.math.MathSign;
import com.bstek.urule.model.rule.math.MathType;
import com.fasterxml.jackson.databind.JsonNode;

public class ExtremumFunctionMathDeserializer implements MathDeserializer {
   @Override
   public MathSign deserialize(JsonNode jsonNode) {
      ExtremumMath extremumMath = new ExtremumMath();
      extremumMath.setName(JsonUtils.getJsonValue(jsonNode, "name"));
      extremumMath.setValue1(JsonUtils.parseValueNode(jsonNode.get("value1")));
      extremumMath.setValue2(JsonUtils.parseValueNode(jsonNode.get("value2")));
      return extremumMath;
   }

   @Override
   public MathType getType() {
      return MathType.extremum;
   }
}
