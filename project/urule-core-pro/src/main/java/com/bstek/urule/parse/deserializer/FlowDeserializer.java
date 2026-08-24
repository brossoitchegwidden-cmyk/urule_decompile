package com.bstek.urule.parse.deserializer;

import com.bstek.urule.model.flow.FlowDefinition;
import com.bstek.urule.parse.flow.FlowDefinitionParser;
import org.dom4j.Element;

public class FlowDeserializer implements Deserializer<FlowDefinition> {
   public static final String BEAN_ID = "urule.flowDeserializer";
   private FlowDefinitionParser flowDefinitionParser;

   public FlowDefinition deserialize(Element root) {
      return this.flowDefinitionParser.parse(root);
   }

   @Override
   public boolean support(Element root) {
      return this.flowDefinitionParser.support(root.getName());
   }

   public void setFlowDefinitionParser(FlowDefinitionParser flowDefinitionParser) {
      this.flowDefinitionParser = flowDefinitionParser;
   }
}
