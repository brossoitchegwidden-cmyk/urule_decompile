package com.bstek.urule.parse.deserializer;

import com.bstek.urule.model.library.variable.VariableCategory;
import com.bstek.urule.parse.VariableLibraryParser;
import java.util.List;
import org.dom4j.Element;

public class VariableLibraryDeserializer implements Deserializer<List<VariableCategory>> {
   public static final String BEAN_ID = "urule.variableLibraryDeserializer";
   private VariableLibraryParser variableLibraryParser;

   public List<VariableCategory> deserialize(Element root) {
      return this.variableLibraryParser.parse(root);
   }

   @Override
   public boolean support(Element root) {
      return this.variableLibraryParser.support(root.getName());
   }

   public void setVariableLibraryParser(VariableLibraryParser variableLibraryParser) {
      this.variableLibraryParser = variableLibraryParser;
   }
}
