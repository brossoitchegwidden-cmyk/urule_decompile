package com.bstek.urule.builder.resource;

import com.bstek.urule.model.template.ActionTemplate;
import com.bstek.urule.parse.deserializer.ActionTemplateDeserializer;
import org.dom4j.Element;

public class ActionTemplateResourceBuilder implements ResourceBuilder<ActionTemplate> {
   private ActionTemplateDeserializer actionTemplateDeserializer;

   public ActionTemplate build(Element root, String file) {
      return this.actionTemplateDeserializer.deserialize(root);
   }

   @Override
   public ResourceType getType() {
      return ResourceType.ActionTemplate;
   }

   @Override
   public boolean support(Element root) {
      return this.actionTemplateDeserializer.support(root);
   }

   public void setActionTemplateDeserializer(ActionTemplateDeserializer actionTemplateDeserializer) {
      this.actionTemplateDeserializer = actionTemplateDeserializer;
   }
}
