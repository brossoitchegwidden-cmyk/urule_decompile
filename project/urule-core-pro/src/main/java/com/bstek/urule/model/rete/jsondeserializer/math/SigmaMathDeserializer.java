package com.bstek.urule.model.rete.jsondeserializer.math;

import com.bstek.urule.model.rete.JsonUtils;
import com.bstek.urule.model.rule.math.MathSign;
import com.bstek.urule.model.rule.math.MathType;
import com.bstek.urule.model.rule.math.SigmaMath;
import com.fasterxml.jackson.databind.JsonNode;

public class SigmaMathDeserializer implements MathDeserializer {
   @Override
   public MathSign deserialize(JsonNode jsonNode) {
      SigmaMath sigmaMath = new SigmaMath();
      JsonNode expr = jsonNode.get("expr");
      sigmaMath.setExpr(JsonUtils.parseValueNode(expr));
      JsonNode ivalue = jsonNode.get("ivalue");
      sigmaMath.setIvalue(JsonUtils.parseValueNode(ivalue));
      JsonNode superior = jsonNode.get("superior");
      sigmaMath.setSuperior(JsonUtils.parseValueNode(superior));
      return sigmaMath;
   }

   @Override
   public MathType getType() {
      return MathType.sigma;
   }
}
