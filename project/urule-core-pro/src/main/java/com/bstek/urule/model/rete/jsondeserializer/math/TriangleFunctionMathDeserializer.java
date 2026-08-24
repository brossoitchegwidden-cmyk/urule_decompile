package com.bstek.urule.model.rete.jsondeserializer.math;

import com.bstek.urule.model.rete.JsonUtils;
import com.bstek.urule.model.rule.math.MathSign;
import com.bstek.urule.model.rule.math.MathType;
import com.bstek.urule.model.rule.math.TriangleFunctionMath;
import com.fasterxml.jackson.databind.JsonNode;

public class TriangleFunctionMathDeserializer implements MathDeserializer {
   @Override
   public MathSign deserialize(JsonNode jsonNode) {
      TriangleFunctionMath triangleFunctionMath = new TriangleFunctionMath();
      triangleFunctionMath.setName(JsonUtils.getJsonValue(jsonNode, "name"));
      triangleFunctionMath.setValue(JsonUtils.parseValueNode(jsonNode.get("value")));
      return triangleFunctionMath;
   }

   @Override
   public MathType getType() {
      return MathType.triangle;
   }
}
