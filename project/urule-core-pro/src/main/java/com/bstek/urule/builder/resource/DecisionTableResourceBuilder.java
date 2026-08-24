package com.bstek.urule.builder.resource;

import com.bstek.urule.model.table.DecisionTable;
import com.bstek.urule.parse.deserializer.DecisionTableDeserializer;
import org.dom4j.Element;

public class DecisionTableResourceBuilder implements ResourceBuilder<DecisionTable> {
   private DecisionTableDeserializer decisionTableDeserializer;

   public DecisionTable build(Element root, String file) {
      return this.decisionTableDeserializer.deserialize(root);
   }

   @Override
   public ResourceType getType() {
      return ResourceType.DecisionTable;
   }

   @Override
   public boolean support(Element root) {
      return this.decisionTableDeserializer.support(root);
   }

   public void setDecisionTableDeserializer(DecisionTableDeserializer decisionTableDeserializer) {
      this.decisionTableDeserializer = decisionTableDeserializer;
   }
}
