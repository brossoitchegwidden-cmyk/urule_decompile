package com.bstek.urule.builder.resource;

import com.bstek.urule.model.library.action.ActionLibrary;
import com.bstek.urule.parse.deserializer.ActionLibraryDeserializer;
import org.dom4j.Element;

public class ActionLibraryResourceBuilder implements ResourceBuilder<ActionLibrary> {
   private ActionLibraryDeserializer a;

   public ActionLibrary build(Element var1, String var2) {
      return this.a.deserialize(var1);
   }

   public void setActionLibraryDeserializer(ActionLibraryDeserializer var1) {
      this.a = var1;
   }

   @Override
   public boolean support(Element var1) {
      return this.a.support(var1);
   }

   @Override
   public ResourceType getType() {
      return ResourceType.ActionLibrary;
   }
}
