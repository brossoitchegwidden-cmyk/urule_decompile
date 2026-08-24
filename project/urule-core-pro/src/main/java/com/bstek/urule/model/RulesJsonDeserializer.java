package com.bstek.urule.model;

import com.bstek.urule.model.rule.Rule;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class RulesJsonDeserializer extends AbstractJsonDeserializer<List<Rule>> {
   public List<Rule> deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
      ObjectCodec codec = jp.getCodec();
      JsonNode tree = (JsonNode)codec.readTree(jp);
      Iterator iterator = tree.elements();
      ArrayList deserializeResult = new ArrayList();

      while (iterator.hasNext()) {
         JsonNode jsonNode = (JsonNode)iterator.next();
         deserializeResult.add(this.parseRule(jp, jsonNode));
      }

      return deserializeResult;
   }
}
