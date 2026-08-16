package com.bstek.urule.model.rete.jsondeserializer.math;

import com.bstek.urule.model.rule.math.MathSign;
import com.bstek.urule.model.rule.math.MathType;
import com.fasterxml.jackson.databind.JsonNode;

public interface MathDeserializer {
   MathSign deserialize(JsonNode var1);

   MathType getType();
}
