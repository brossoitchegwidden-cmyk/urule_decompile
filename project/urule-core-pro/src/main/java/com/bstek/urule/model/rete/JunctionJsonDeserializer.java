package com.bstek.urule.model.rete;

import com.bstek.urule.model.AbstractJsonDeserializer;
import com.bstek.urule.model.rule.lhs.Junction;
import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import java.io.IOException;

public class JunctionJsonDeserializer extends AbstractJsonDeserializer<Junction> {
   public Junction deserialize(JsonParser jsonParser, DeserializationContext ctxt) throws IOException, JacksonException {
      ObjectCodec codec = jsonParser.getCodec();
      JsonNode tree = (JsonNode)codec.readTree(jsonParser);
      return JsonUtils.parseJunction(tree);
   }
}
