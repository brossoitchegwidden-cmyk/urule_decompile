package com.bstek.urule.model;

import com.bstek.urule.model.rule.Rule;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import java.io.IOException;

public class RuleJsonDeserializer extends AbstractJsonDeserializer<Rule> {
   public Rule deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
      ObjectCodec codec = jp.getCodec();
      JsonNode tree = (JsonNode)codec.readTree(jp);
      return this.parseRule(jp, tree);
   }
}
