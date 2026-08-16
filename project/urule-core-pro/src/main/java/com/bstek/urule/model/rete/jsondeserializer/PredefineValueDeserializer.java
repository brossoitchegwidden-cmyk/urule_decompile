package com.bstek.urule.model.rete.jsondeserializer;

import com.bstek.urule.model.library.Datatype;
import com.bstek.urule.model.rete.JsonUtils;
import com.bstek.urule.model.rule.PredefineValue;
import com.bstek.urule.model.rule.Value;
import com.bstek.urule.model.rule.ValueType;
import com.fasterxml.jackson.databind.JsonNode;

public class PredefineValueDeserializer implements ValueDeserializer {
   @Override
   public Value deserialize(JsonNode var1) {
      PredefineValue var2 = new PredefineValue();
      var2.setUuid(JsonUtils.getJsonValue(var1, "uuid"));
      var2.setName(JsonUtils.getJsonValue(var1, "name"));
      var2.setPropertyName(JsonUtils.getJsonValue(var1, "propertyName"));
      var2.setPropertyLabel(JsonUtils.getJsonValue(var1, "propertyLabel"));
      var2.setPropertyUuid(JsonUtils.getJsonValue(var1, "propertyUuid"));
      var2.setVariableCategory(JsonUtils.getJsonValue(var1, "variableCategory"));
      var2.setVariableCategoryUuid(JsonUtils.getJsonValue(var1, "variableCategoryUuid"));
      String var3 = JsonUtils.getJsonValue(var1, "datatype");
      if (var3 != null) {
         var2.setDatatype(Datatype.valueOf(var3));
      }

      var2.setArithmetic(JsonUtils.parseComplexArithmetic(var1));
      return var2;
   }

   @Override
   public boolean support(ValueType var1) {
      return var1.equals(ValueType.Predefine);
   }
}
