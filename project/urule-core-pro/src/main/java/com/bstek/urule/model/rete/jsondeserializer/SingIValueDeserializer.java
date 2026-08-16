package com.bstek.urule.model.rete.jsondeserializer;

import com.bstek.urule.model.rule.SignIValue;
import com.bstek.urule.model.rule.Value;
import com.bstek.urule.model.rule.ValueType;
import com.fasterxml.jackson.databind.JsonNode;

public class SingIValueDeserializer implements ValueDeserializer {
   @Override
   public Value deserialize(JsonNode var1) {
      return new SignIValue();
   }

   @Override
   public boolean support(ValueType var1) {
      return var1.equals(ValueType.SignI);
   }
}
