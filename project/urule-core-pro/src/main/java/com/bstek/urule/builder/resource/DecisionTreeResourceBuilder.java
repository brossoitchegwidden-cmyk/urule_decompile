package com.bstek.urule.builder.resource;

import com.bstek.urule.model.decisiontree.DecisionTree;
import com.bstek.urule.parse.deserializer.DecisionTreeDeserializer;
import org.dom4j.Element;

public class DecisionTreeResourceBuilder implements ResourceBuilder<DecisionTree> {
   private DecisionTreeDeserializer decisionTreeDeserializer;

   public DecisionTree build(Element root, String file) {
      return this.decisionTreeDeserializer.deserialize(root);
   }

   @Override
   public ResourceType getType() {
      return ResourceType.DecisionTree;
   }

   @Override
   public boolean support(Element root) {
      return this.decisionTreeDeserializer.support(root);
   }

   public void setDecisionTreeDeserializer(DecisionTreeDeserializer decisionTreeDeserializer) {
      this.decisionTreeDeserializer = decisionTreeDeserializer;
   }
}
