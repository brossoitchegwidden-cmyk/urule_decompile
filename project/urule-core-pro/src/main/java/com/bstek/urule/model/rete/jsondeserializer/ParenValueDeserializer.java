package com.bstek.urule.model.rete.jsondeserializer;

import com.bstek.urule.model.rete.JsonUtils;
import com.bstek.urule.model.rule.ParenValue;
import com.bstek.urule.model.rule.Value;
import com.bstek.urule.model.rule.ValueType;
import com.fasterxml.jackson.databind.JsonNode;

public class ParenValueDeserializer implements ValueDeserializer {
   @Override
   public Value deserialize(JsonNode var1) {
      ParenValue var2 = new ParenValue();
      var2.setArithmetic(JsonUtils.parseComplexArithmetic(var1));
      var2.setValue(JsonUtils.parseValue(var1));
      return var2;
   }

   @Override
   public boolean support(ValueType var1) {
      return var1.equals(ValueType.Paren);
   }
}
