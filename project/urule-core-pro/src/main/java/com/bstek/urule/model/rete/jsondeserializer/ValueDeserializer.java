package com.bstek.urule.model.rete.jsondeserializer;

import com.bstek.urule.model.rule.Value;
import com.bstek.urule.model.rule.ValueType;
import com.fasterxml.jackson.databind.JsonNode;

public interface ValueDeserializer {
   Value deserialize(JsonNode var1);

   boolean support(ValueType var1);
}
