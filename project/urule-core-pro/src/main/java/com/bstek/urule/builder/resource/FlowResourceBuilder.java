package com.bstek.urule.builder.resource;

import com.bstek.urule.model.flow.FlowDefinition;
import com.bstek.urule.parse.deserializer.FlowDeserializer;
import org.dom4j.Element;

public class FlowResourceBuilder implements ResourceBuilder<FlowDefinition> {
   private FlowDeserializer flowDeserializer;

   public FlowDefinition build(Element root, String file) {
      return this.flowDeserializer.deserialize(root);
   }

   @Override
   public ResourceType getType() {
      return ResourceType.Flow;
   }

   @Override
   public boolean support(Element root) {
      return this.flowDeserializer.support(root);
   }

   public void setFlowDeserializer(FlowDeserializer flowDeserializer) {
      this.flowDeserializer = flowDeserializer;
   }
}
