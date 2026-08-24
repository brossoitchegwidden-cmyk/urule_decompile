package com.bstek.urule.model.rete.jsondeserializer;

import com.bstek.urule.model.rete.JsonUtils;
import com.bstek.urule.model.rule.ParenValue;
import com.bstek.urule.model.rule.Value;
import com.bstek.urule.model.rule.ValueType;
import com.fasterxml.jackson.databind.JsonNode;

public class ParenValueDeserializer implements ValueDeserializer {
   @Override
   public Value deserialize(JsonNode jsonNode) {
      ParenValue parenValue = new ParenValue();
      parenValue.setArithmetic(JsonUtils.parseComplexArithmetic(jsonNode));
      parenValue.setValue(JsonUtils.parseValue(jsonNode));
      return parenValue;
   }

   @Override
   public boolean support(ValueType type) {
      return type.equals(ValueType.Paren);
   }
}
