package com.bstek.urule.builder.resource;

import com.bstek.urule.model.table.DecisionTable;
import com.bstek.urule.parse.deserializer.DecisionTableDeserializer;
import org.dom4j.Element;

public class DecisionTableResourceBuilder implements ResourceBuilder<DecisionTable> {
   private DecisionTableDeserializer a;

   public DecisionTable build(Element var1, String var2) {
      return this.a.deserialize(var1);
   }

   @Override
   public ResourceType getType() {
      return ResourceType.DecisionTable;
   }

   @Override
   public boolean support(Element var1) {
      return this.a.support(var1);
   }

   public void setDecisionTableDeserializer(DecisionTableDeserializer var1) {
      this.a = var1;
   }
}
