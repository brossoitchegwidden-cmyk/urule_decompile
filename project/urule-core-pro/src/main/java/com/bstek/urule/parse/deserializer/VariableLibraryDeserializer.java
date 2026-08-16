package com.bstek.urule.parse.deserializer;

import com.bstek.urule.model.library.variable.VariableCategory;
import com.bstek.urule.parse.VariableLibraryParser;
import java.util.List;
import org.dom4j.Element;

public class VariableLibraryDeserializer implements Deserializer<List<VariableCategory>> {
   public static final String BEAN_ID = "urule.variableLibraryDeserializer";
   private VariableLibraryParser a;

   public List<VariableCategory> deserialize(Element var1) {
      return this.a.parse(var1);
   }

   @Override
   public boolean support(Element var1) {
      return this.a.support(var1.getName());
   }

   public void setVariableLibraryParser(VariableLibraryParser var1) {
      this.a = var1;
   }
}
