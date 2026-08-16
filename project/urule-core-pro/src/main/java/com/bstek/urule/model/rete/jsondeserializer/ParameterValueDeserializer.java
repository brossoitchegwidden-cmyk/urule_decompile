package com.bstek.urule.model.rete.jsondeserializer;

import com.bstek.urule.model.rete.JsonUtils;
import com.bstek.urule.model.rule.ParameterValue;
import com.bstek.urule.model.rule.Value;
import com.bstek.urule.model.rule.ValueType;
import com.fasterxml.jackson.databind.JsonNode;

public class ParameterValueDeserializer implements ValueDeserializer {
   @Override
   public Value deserialize(JsonNode var1) {
      ParameterValue var2 = new ParameterValue();
      var2.setArithmetic(JsonUtils.parseComplexArithmetic(var1));
      var2.setVariableLabel(JsonUtils.getJsonValue(var1, "variableLabel"));
      var2.setVariableName(JsonUtils.getJsonValue(var1, "variableName"));
      var2.setUuid(JsonUtils.getJsonValue(var1, "uuid"));
      var2.setKeyLabel(JsonUtils.getJsonValue(var1, "keyLabel"));
      var2.setKeyName(JsonUtils.getJsonValue(var1, "keyName"));
      return var2;
   }

   @Override
   public boolean support(ValueType var1) {
      return var1.equals(ValueType.Parameter);
   }
}
