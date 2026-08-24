package com.bstek.urule.model.rete.jsondeserializer;

import com.bstek.urule.model.rule.SignIValue;
import com.bstek.urule.model.rule.Value;
import com.bstek.urule.model.rule.ValueType;
import com.fasterxml.jackson.databind.JsonNode;

public class SingIValueDeserializer implements ValueDeserializer {
   @Override
   public Value deserialize(JsonNode jsonNode) {
      return new SignIValue();
   }

   @Override
   public boolean support(ValueType type) {
      return type.equals(ValueType.SignI);
   }
}
