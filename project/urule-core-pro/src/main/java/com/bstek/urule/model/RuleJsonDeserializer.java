package com.bstek.urule.model;

import com.bstek.urule.model.rule.Rule;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import java.io.IOException;

public class RuleJsonDeserializer extends AbstractJsonDeserializer<Rule> {
   public Rule deserialize(JsonParser var1, DeserializationContext var2) throws IOException, JsonProcessingException {
      ObjectCodec var3 = var1.getCodec();
      JsonNode var4 = (JsonNode)var3.readTree(var1);
      return this.parseRule(var1, var4);
   }
}
