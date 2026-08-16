package com.bstek.urule.model.rete;

import com.bstek.urule.model.AbstractJsonDeserializer;
import com.bstek.urule.model.rule.Value;
import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import java.io.IOException;

public class ValueJsonDeserializer extends AbstractJsonDeserializer<Value> {
   public Value deserialize(JsonParser var1, DeserializationContext var2) throws IOException, JacksonException {
      ObjectCodec var3 = var1.getCodec();
      JsonNode var4 = (JsonNode)var3.readTree(var1);
      return JsonUtils.parseValueNode(var4);
   }
}
