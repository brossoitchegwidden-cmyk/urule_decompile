package com.bstek.urule.builder.resource;

import com.bstek.urule.model.library.variable.CategoryType;
import com.bstek.urule.model.library.variable.VariableCategory;
import com.bstek.urule.parse.deserializer.ParameterLibraryDeserializer;
import java.util.HashMap;
import org.dom4j.Element;

public class ParameterLibraryResourceBuilder implements ResourceBuilder<VariableCategory> {
   private ParameterLibraryDeserializer parameterLibraryDeserializer;

   public VariableCategory build(Element root, String file) {
      VariableCategory variableCategory = new VariableCategory();
      variableCategory.setUuid("参数");
      variableCategory.setName("参数");
      variableCategory.setClazz(HashMap.class.getName());
      variableCategory.setType(CategoryType.Clazz);
      variableCategory.setVariables(this.parameterLibraryDeserializer.deserialize(root));
      variableCategory.setFile(file);
      return variableCategory;
   }

   @Override
   public ResourceType getType() {
      return ResourceType.ParameterLibrary;
   }

   @Override
   public boolean support(Element root) {
      return this.parameterLibraryDeserializer.support(root);
   }

   public void setParameterLibraryDeserializer(ParameterLibraryDeserializer parameterLibraryDeserializer) {
      this.parameterLibraryDeserializer = parameterLibraryDeserializer;
   }
}
