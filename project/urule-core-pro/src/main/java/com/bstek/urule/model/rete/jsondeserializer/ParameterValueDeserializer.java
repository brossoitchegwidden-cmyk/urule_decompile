package com.bstek.urule.model.rete.jsondeserializer;

import com.bstek.urule.model.rete.JsonUtils;
import com.bstek.urule.model.rule.ParameterValue;
import com.bstek.urule.model.rule.Value;
import com.bstek.urule.model.rule.ValueType;
import com.fasterxml.jackson.databind.JsonNode;

public class ParameterValueDeserializer implements ValueDeserializer {
   @Override
   public Value deserialize(JsonNode jsonNode) {
      ParameterValue parameterValue = new ParameterValue();
      parameterValue.setArithmetic(JsonUtils.parseComplexArithmetic(jsonNode));
      parameterValue.setVariableLabel(JsonUtils.getJsonValue(jsonNode, "variableLabel"));
      parameterValue.setVariableName(JsonUtils.getJsonValue(jsonNode, "variableName"));
      parameterValue.setUuid(JsonUtils.getJsonValue(jsonNode, "uuid"));
      parameterValue.setKeyLabel(JsonUtils.getJsonValue(jsonNode, "keyLabel"));
      parameterValue.setKeyName(JsonUtils.getJsonValue(jsonNode, "keyName"));
      return parameterValue;
   }

   @Override
   public boolean support(ValueType type) {
      return type.equals(ValueType.Parameter);
   }
}
