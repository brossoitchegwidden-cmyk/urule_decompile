package com.bstek.urule.model.rete.jsondeserializer;

import com.bstek.urule.model.library.Datatype;
import com.bstek.urule.model.rete.JsonUtils;
import com.bstek.urule.model.rule.PredefineValue;
import com.bstek.urule.model.rule.Value;
import com.bstek.urule.model.rule.ValueType;
import com.fasterxml.jackson.databind.JsonNode;

public class PredefineValueDeserializer implements ValueDeserializer {
   @Override
   public Value deserialize(JsonNode jsonNode) {
      PredefineValue predefineValue = new PredefineValue();
      predefineValue.setUuid(JsonUtils.getJsonValue(jsonNode, "uuid"));
      predefineValue.setName(JsonUtils.getJsonValue(jsonNode, "name"));
      predefineValue.setPropertyName(JsonUtils.getJsonValue(jsonNode, "propertyName"));
      predefineValue.setPropertyLabel(JsonUtils.getJsonValue(jsonNode, "propertyLabel"));
      predefineValue.setPropertyUuid(JsonUtils.getJsonValue(jsonNode, "propertyUuid"));
      predefineValue.setVariableCategory(JsonUtils.getJsonValue(jsonNode, "variableCategory"));
      predefineValue.setVariableCategoryUuid(JsonUtils.getJsonValue(jsonNode, "variableCategoryUuid"));
      String jsonValue = JsonUtils.getJsonValue(jsonNode, "datatype");
      if (jsonValue != null) {
         predefineValue.setDatatype(Datatype.valueOf(jsonValue));
      }

      predefineValue.setArithmetic(JsonUtils.parseComplexArithmetic(jsonNode));
      return predefineValue;
   }

   @Override
   public boolean support(ValueType type) {
      return type.equals(ValueType.Predefine);
   }
}
