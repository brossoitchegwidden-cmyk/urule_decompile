package com.bstek.urule.builder.resource;

import com.bstek.urule.model.library.variable.CategoryType;
import com.bstek.urule.model.library.variable.VariableCategory;
import com.bstek.urule.parse.deserializer.ParameterLibraryDeserializer;
import java.util.HashMap;
import org.dom4j.Element;

public class ParameterLibraryResourceBuilder implements ResourceBuilder<VariableCategory> {
   private ParameterLibraryDeserializer a;

   public VariableCategory build(Element var1, String var2) {
      VariableCategory var3 = new VariableCategory();
      var3.setUuid("参数");
      var3.setName("参数");
      var3.setClazz(HashMap.class.getName());
      var3.setType(CategoryType.Clazz);
      var3.setVariables(this.a.deserialize(var1));
      var3.setFile(var2);
      return var3;
   }

   @Override
   public ResourceType getType() {
      return ResourceType.ParameterLibrary;
   }

   @Override
   public boolean support(Element var1) {
      return this.a.support(var1);
   }

   public void setParameterLibraryDeserializer(ParameterLibraryDeserializer var1) {
      this.a = var1;
   }
}
