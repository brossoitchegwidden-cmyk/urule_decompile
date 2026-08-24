package com.bstek.urule.model.rete.jsondeserializer;

import com.bstek.urule.model.rete.JsonUtils;
import com.bstek.urule.model.rule.SimpleValue;
import com.bstek.urule.model.rule.Value;
import com.bstek.urule.model.rule.ValueType;
import com.fasterxml.jackson.databind.JsonNode;

public class InputValueDeserializer implements ValueDeserializer {
   @Override
   public Value deserialize(JsonNode jsonNode) {
      SimpleValue simpleValue = new SimpleValue();
      simpleValue.setContent(JsonUtils.getJsonValue(jsonNode, "content"));
      simpleValue.setArithmetic(JsonUtils.parseComplexArithmetic(jsonNode));
      return simpleValue;
   }

   @Override
   public boolean support(ValueType type) {
      return type.equals(ValueType.Input);
   }
}
