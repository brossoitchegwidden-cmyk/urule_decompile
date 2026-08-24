package com.bstek.urule.builder.resource;

import com.bstek.urule.model.library.variable.VariableCategory;
import com.bstek.urule.model.library.variable.VariableLibrary;
import com.bstek.urule.parse.deserializer.VariableLibraryDeserializer;
import org.dom4j.Element;

public class VariableLibraryResourceBuilder implements ResourceBuilder<VariableLibrary> {
   private VariableLibraryDeserializer variableLibraryDeserializer;

   public VariableLibrary build(Element root, String file) {
      VariableLibrary variableLibrary = new VariableLibrary();
      variableLibrary.setVariableCategories(this.variableLibraryDeserializer.deserialize(root));

      for (VariableCategory variableCategory : variableLibrary.getVariableCategories()) {
         variableCategory.setFile(file);
      }

      return variableLibrary;
   }

   @Override
   public boolean support(Element root) {
      return this.variableLibraryDeserializer.support(root);
   }

   @Override
   public ResourceType getType() {
      return ResourceType.VariableLibrary;
   }

   public void setVariableLibraryDeserializer(VariableLibraryDeserializer variableLibraryDeserializer) {
      this.variableLibraryDeserializer = variableLibraryDeserializer;
   }
}
