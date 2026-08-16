package com.bstek.urule.model.rete.jsondeserializer.math;

import com.bstek.urule.model.rete.JsonUtils;
import com.bstek.urule.model.rule.math.MathSign;
import com.bstek.urule.model.rule.math.MathType;
import com.bstek.urule.model.rule.math.SigmaMath;
import com.fasterxml.jackson.databind.JsonNode;

public class SigmaMathDeserializer implements MathDeserializer {
   @Override
   public MathSign deserialize(JsonNode var1) {
      SigmaMath var2 = new SigmaMath();
      JsonNode var3 = var1.get("expr");
      var2.setExpr(JsonUtils.parseValueNode(var3));
      JsonNode var4 = var1.get("ivalue");
      var2.setIvalue(JsonUtils.parseValueNode(var4));
      JsonNode var5 = var1.get("superior");
      var2.setSuperior(JsonUtils.parseValueNode(var5));
      return var2;
   }

   @Override
   public MathType getType() {
      return MathType.sigma;
   }
}
