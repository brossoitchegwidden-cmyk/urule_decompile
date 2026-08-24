package com.bstek.urule.model.rete.jsondeserializer;

import com.bstek.urule.model.rete.JsonUtils;
import com.bstek.urule.model.rule.Value;
import com.bstek.urule.model.rule.ValueType;
import com.bstek.urule.model.rule.VariableCategoryValue;
import com.fasterxml.jackson.databind.JsonNode;

public class VariableCategoryValueDeserializer implements ValueDeserializer {
   @Override
   public Value deserialize(JsonNode jsonNode) {
      VariableCategoryValue variableCategoryValue = new VariableCategoryValue();
      variableCategoryValue.setArithmetic(JsonUtils.parseComplexArithmetic(jsonNode));
      variableCategoryValue.setUuid(JsonUtils.getJsonValue(jsonNode, "uuid"));
      variableCategoryValue.setVariableCategory(JsonUtils.getJsonValue(jsonNode, "variableCategory"));
      return variableCategoryValue;
   }

   @Override
   public boolean support(ValueType type) {
      return type.equals(ValueType.VariableCategory);
   }
}
