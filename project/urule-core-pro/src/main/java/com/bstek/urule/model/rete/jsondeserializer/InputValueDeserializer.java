package com.bstek.urule.model.rete.jsondeserializer;

import com.bstek.urule.model.rete.JsonUtils;
import com.bstek.urule.model.rule.SimpleValue;
import com.bstek.urule.model.rule.Value;
import com.bstek.urule.model.rule.ValueType;
import com.fasterxml.jackson.databind.JsonNode;

public class InputValueDeserializer implements ValueDeserializer {
   @Override
   public Value deserialize(JsonNode var1) {
      SimpleValue var2 = new SimpleValue();
      var2.setContent(JsonUtils.getJsonValue(var1, "content"));
      var2.setArithmetic(JsonUtils.parseComplexArithmetic(var1));
      return var2;
   }

   @Override
   public boolean support(ValueType var1) {
      return var1.equals(ValueType.Input);
   }
}
