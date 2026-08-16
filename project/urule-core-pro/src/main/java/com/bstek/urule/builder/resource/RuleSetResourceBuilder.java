package com.bstek.urule.builder.resource;

import com.bstek.urule.model.rule.RuleSet;
import com.bstek.urule.parse.deserializer.RuleSetDeserializer;
import org.dom4j.Element;

public class RuleSetResourceBuilder implements ResourceBuilder<RuleSet> {
   private RuleSetDeserializer a;

   public RuleSet build(Element var1, String var2) {
      return this.a.deserialize(var1);
   }

   @Override
   public boolean support(Element var1) {
      return this.a.support(var1);
   }

   @Override
   public ResourceType getType() {
      return ResourceType.RuleSet;
   }

   public void setRuleSetDeserializer(RuleSetDeserializer var1) {
      this.a = var1;
   }
}
