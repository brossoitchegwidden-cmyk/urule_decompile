package com.bstek.urule.builder.resource;

import com.bstek.urule.model.rule.RuleSet;
import com.bstek.urule.parse.deserializer.RuleSetDeserializer;
import org.dom4j.Element;

public class RuleSetResourceBuilder implements ResourceBuilder<RuleSet> {
   private RuleSetDeserializer ruleSetDeserializer;

   public RuleSet build(Element root, String file) {
      return this.ruleSetDeserializer.deserialize(root);
   }

   @Override
   public boolean support(Element root) {
      return this.ruleSetDeserializer.support(root);
   }

   @Override
   public ResourceType getType() {
      return ResourceType.RuleSet;
   }

   public void setRuleSetDeserializer(RuleSetDeserializer ruleSetDeserializer) {
      this.ruleSetDeserializer = ruleSetDeserializer;
   }
}
