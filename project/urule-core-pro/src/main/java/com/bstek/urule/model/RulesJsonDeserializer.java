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
   public List<Rule> deserialize(JsonParser var1, DeserializationContext var2) throws IOException, JsonProcessingException {
      ObjectCodec var3 = var1.getCodec();
      JsonNode var4 = (JsonNode)var3.readTree(var1);
      Iterator var5 = var4.elements();
      ArrayList var6 = new ArrayList();

      while (var5.hasNext()) {
         JsonNode var7 = (JsonNode)var5.next();
         var6.add(this.parseRule(var1, var7));
      }

      return var6;
   }
}
