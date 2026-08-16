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
   public Value deserialize(JsonNode var1) {
      ConstantValue var2 = new ConstantValue();
      var2.setConstantCategory(JsonUtils.getJsonValue(var1, "constantCategory"));
      var2.setConstantLabel(JsonUtils.getJsonValue(var1, "constantLabel"));
      var2.setConstantName(JsonUtils.getJsonValue(var1, "constantName"));
      var2.setUuid(JsonUtils.getJsonValue(var1, "uuid"));
      var2.setCategoryUuid(JsonUtils.getJsonValue(var1, "categoryUuid"));
      String var3 = JsonUtils.getJsonValue(var1, "datatype");
      if (StringUtils.isNotBlank(var3)) {
         var2.setDatatype(Datatype.valueOf(var3));
      }

      var2.setArithmetic(JsonUtils.parseComplexArithmetic(var1));
      return var2;
   }

   @Override
   public boolean support(ValueType var1) {
      return var1.equals(ValueType.Constant);
   }
}
