package com.bstek.urule.model.rete.jsondeserializer;

import com.bstek.urule.model.library.Datatype;
import com.bstek.urule.model.rete.JsonUtils;
import com.bstek.urule.model.rule.Value;
import com.bstek.urule.model.rule.ValueType;
import com.bstek.urule.model.rule.VariableValue;
import com.fasterxml.jackson.databind.JsonNode;

public class VariableValueDeserializer implements ValueDeserializer {
   @Override
   public Value deserialize(JsonNode jsonNode) {
      VariableValue variableValue = new VariableValue();
      variableValue.setArithmetic(JsonUtils.parseComplexArithmetic(jsonNode));
      String jsonValue = JsonUtils.getJsonValue(jsonNode, "datatype");
      if (jsonValue != null) {
         variableValue.setDatatype(Datatype.valueOf(jsonValue));
      }

      variableValue.setVariableCategory(JsonUtils.getJsonValue(jsonNode, "variableCategory"));
      variableValue.setVariableLabel(JsonUtils.getJsonValue(jsonNode, "variableLabel"));
      variableValue.setVariableName(JsonUtils.getJsonValue(jsonNode, "variableName"));
      variableValue.setUuid(JsonUtils.getJsonValue(jsonNode, "uuid"));
      variableValue.setCategoryUuid(JsonUtils.getJsonValue(jsonNode, "categoryUuid"));
      return variableValue;
   }

   @Override
   public boolean support(ValueType type) {
      return type.equals(ValueType.Variable);
   }
}
