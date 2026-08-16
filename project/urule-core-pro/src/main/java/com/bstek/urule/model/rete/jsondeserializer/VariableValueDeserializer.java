package com.bstek.urule.model.rete.jsondeserializer;

import com.bstek.urule.model.library.Datatype;
import com.bstek.urule.model.rete.JsonUtils;
import com.bstek.urule.model.rule.Value;
import com.bstek.urule.model.rule.ValueType;
import com.bstek.urule.model.rule.VariableValue;
import com.fasterxml.jackson.databind.JsonNode;

public class VariableValueDeserializer implements ValueDeserializer {
   @Override
   public Value deserialize(JsonNode var1) {
      VariableValue var2 = new VariableValue();
      var2.setArithmetic(JsonUtils.parseComplexArithmetic(var1));
      String var3 = JsonUtils.getJsonValue(var1, "datatype");
      if (var3 != null) {
         var2.setDatatype(Datatype.valueOf(var3));
      }

      var2.setVariableCategory(JsonUtils.getJsonValue(var1, "variableCategory"));
      var2.setVariableLabel(JsonUtils.getJsonValue(var1, "variableLabel"));
      var2.setVariableName(JsonUtils.getJsonValue(var1, "variableName"));
      var2.setUuid(JsonUtils.getJsonValue(var1, "uuid"));
      var2.setCategoryUuid(JsonUtils.getJsonValue(var1, "categoryUuid"));
      return var2;
   }

   @Override
   public boolean support(ValueType var1) {
      return var1.equals(ValueType.Variable);
   }
}
