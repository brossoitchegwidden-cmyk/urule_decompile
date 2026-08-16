package com.bstek.urule.builder.resource;

import com.bstek.urule.model.template.ActionTemplate;
import com.bstek.urule.parse.deserializer.ActionTemplateDeserializer;
import org.dom4j.Element;

public class ActionTemplateResourceBuilder implements ResourceBuilder<ActionTemplate> {
   private ActionTemplateDeserializer a;

   public ActionTemplate build(Element var1, String var2) {
      return this.a.deserialize(var1);
   }

   @Override
   public ResourceType getType() {
      return ResourceType.ActionTemplate;
   }

   @Override
   public boolean support(Element var1) {
      return this.a.support(var1);
   }

   public void setActionTemplateDeserializer(ActionTemplateDeserializer var1) {
      this.a = var1;
   }
}
