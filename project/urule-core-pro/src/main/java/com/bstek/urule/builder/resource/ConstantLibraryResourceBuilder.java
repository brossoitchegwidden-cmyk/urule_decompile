package com.bstek.urule.builder.resource;

import com.bstek.urule.model.library.constant.ConstantLibrary;
import com.bstek.urule.parse.deserializer.ConstantLibraryDeserializer;
import org.dom4j.Element;

public class ConstantLibraryResourceBuilder implements ResourceBuilder<ConstantLibrary> {
   private ConstantLibraryDeserializer constantLibraryDeserializer;

   public ConstantLibrary build(Element root, String file) {
      return this.constantLibraryDeserializer.deserialize(root);
   }

   @Override
   public boolean support(Element root) {
      return this.constantLibraryDeserializer.support(root);
   }

   @Override
   public ResourceType getType() {
      return ResourceType.ConstantLibrary;
   }

   public void setConstantLibraryDeserializer(ConstantLibraryDeserializer constantLibraryDeserializer) {
      this.constantLibraryDeserializer = constantLibraryDeserializer;
   }
}
