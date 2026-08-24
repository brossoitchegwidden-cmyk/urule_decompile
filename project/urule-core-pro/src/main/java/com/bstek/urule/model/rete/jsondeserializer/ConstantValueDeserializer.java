package com.bstek.urule.model.rete.jsondeserializer;

import com.bstek.urule.model.library.Datatype;
import com.bstek.urule.model.rete.JsonUtils;
import com.bstek.urule.model.rule.ConstantValue;
import com.bstek.urule.model.rule.Value;
import com.bstek.urule.model.rule.ValueType;
import com.fasterxml.jackson.databind.JsonNode;
import org.apache.commons.lang.StringUtils;

public class ConstantValueDeserializer implements ValueDeserializer {
   @Override
   public Value deserialize(JsonNode jsonNode) {
      ConstantValue constantValue = new ConstantValue();
      constantValue.setConstantCategory(JsonUtils.getJsonValue(jsonNode, "constantCategory"));
      constantValue.setConstantLabel(JsonUtils.getJsonValue(jsonNode, "constantLabel"));
      constantValue.setConstantName(JsonUtils.getJsonValue(jsonNode, "constantName"));
      constantValue.setUuid(JsonUtils.getJsonValue(jsonNode, "uuid"));
      constantValue.setCategoryUuid(JsonUtils.getJsonValue(jsonNode, "categoryUuid"));
      String jsonValue = JsonUtils.getJsonValue(jsonNode, "datatype");
      if (StringUtils.isNotBlank(jsonValue)) {
         constantValue.setDatatype(Datatype.valueOf(jsonValue));
      }

      constantValue.setArithmetic(JsonUtils.parseComplexArithmetic(jsonNode));
      return constantValue;
   }

   @Override
   public boolean support(ValueType type) {
      return type.equals(ValueType.Constant);
   }
}
