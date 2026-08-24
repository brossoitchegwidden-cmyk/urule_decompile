package com.bstek.urule.model.rete.jsondeserializer;

import com.bstek.urule.model.rete.JsonUtils;
import com.bstek.urule.model.rule.MethodValue;
import com.bstek.urule.model.rule.Value;
import com.bstek.urule.model.rule.ValueType;
import com.fasterxml.jackson.databind.JsonNode;

public class MethodValueDeserializer implements ValueDeserializer {
   @Override
   public Value deserialize(JsonNode jsonNode) {
      MethodValue methodValue = new MethodValue();
      methodValue.setArithmetic(JsonUtils.parseComplexArithmetic(jsonNode));
      methodValue.setBeanId(JsonUtils.getJsonValue(jsonNode, "beanId"));
      methodValue.setUuid(JsonUtils.getJsonValue(jsonNode, "uuid"));
      methodValue.setCategoryUuid(JsonUtils.getJsonValue(jsonNode, "categoryUuid"));
      methodValue.setBeanLabel(JsonUtils.getJsonValue(jsonNode, "beanLabel"));
      methodValue.setMethodLabel(JsonUtils.getJsonValue(jsonNode, "methodLabel"));
      methodValue.setMethodName(JsonUtils.getJsonValue(jsonNode, "methodName"));
      methodValue.setParameters(JsonUtils.parseParameters(jsonNode));
      return methodValue;
   }

   @Override
   public boolean support(ValueType type) {
      return type.equals(ValueType.Method);
   }
}
