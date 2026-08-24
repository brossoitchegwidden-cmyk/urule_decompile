package com.bstek.urule.builder.resource;

import com.bstek.urule.model.library.action.ActionLibrary;
import com.bstek.urule.parse.deserializer.ActionLibraryDeserializer;
import org.dom4j.Element;

public class ActionLibraryResourceBuilder implements ResourceBuilder<ActionLibrary> {
   private ActionLibraryDeserializer actionLibraryDeserializer;

   public ActionLibrary build(Element root, String file) {
      return this.actionLibraryDeserializer.deserialize(root);
   }

   public void setActionLibraryDeserializer(ActionLibraryDeserializer actionLibraryDeserializer) {
      this.actionLibraryDeserializer = actionLibraryDeserializer;
   }

   @Override
   public boolean support(Element root) {
      return this.actionLibraryDeserializer.support(root);
   }

   @Override
   public ResourceType getType() {
      return ResourceType.ActionLibrary;
   }
}
