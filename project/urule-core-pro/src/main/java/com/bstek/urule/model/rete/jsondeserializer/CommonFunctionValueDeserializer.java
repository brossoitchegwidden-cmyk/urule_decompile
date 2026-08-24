package com.bstek.urule.model.rete.jsondeserializer;

import com.bstek.urule.model.rete.JsonUtils;
import com.bstek.urule.model.rule.CommonFunctionValue;
import com.bstek.urule.model.rule.Value;
import com.bstek.urule.model.rule.ValueType;
import com.fasterxml.jackson.databind.JsonNode;

public class CommonFunctionValueDeserializer implements ValueDeserializer {
   @Override
   public Value deserialize(JsonNode jsonNode) {
      CommonFunctionValue commonFunctionValue = new CommonFunctionValue();
      commonFunctionValue.setArithmetic(JsonUtils.parseComplexArithmetic(jsonNode));
      commonFunctionValue.setLabel(JsonUtils.getJsonValue(jsonNode, "label"));
      commonFunctionValue.setName(JsonUtils.getJsonValue(jsonNode, "name"));
      commonFunctionValue.setParameter(JsonUtils.parseCommonFunctionParameter(jsonNode));
      commonFunctionValue.setValueType(ValueType.CommonFunction);
      return commonFunctionValue;
   }

   @Override
   public boolean support(ValueType type) {
      return type.equals(ValueType.CommonFunction);
   }
}
