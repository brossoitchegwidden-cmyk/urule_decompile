package com.bstek.urule.builder.resource;

import com.bstek.urule.model.template.ConditionTemplate;
import com.bstek.urule.parse.deserializer.ConditionTemplateDeserializer;
import org.dom4j.Element;

public class ConditionTemplateResourceBuilder implements ResourceBuilder<ConditionTemplate> {
   private ConditionTemplateDeserializer conditionTemplateDeserializer;

   public ConditionTemplate build(Element root, String file) {
      return this.conditionTemplateDeserializer.deserialize(root);
   }

   @Override
   public ResourceType getType() {
      return ResourceType.ConditionTemplate;
   }

   @Override
   public boolean support(Element root) {
      return this.conditionTemplateDeserializer.support(root);
   }

   public void setConditionTemplateDeserializer(ConditionTemplateDeserializer conditionTemplateDeserializer) {
      this.conditionTemplateDeserializer = conditionTemplateDeserializer;
   }
}
