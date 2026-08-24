package com.bstek.urule.parse.deserializer;

import com.bstek.urule.model.library.variable.Variable;
import com.bstek.urule.parse.ParameterLibraryParser;
import java.util.List;
import org.dom4j.Element;

public class ParameterLibraryDeserializer implements Deserializer<List<Variable>> {
   public static final String BEAN_ID = "urule.parameterLibraryDeserializer";
   private ParameterLibraryParser parameterLibraryParser;

   public List<Variable> deserialize(Element root) {
      return this.parameterLibraryParser.parse(root);
   }

   @Override
   public boolean support(Element root) {
      return this.parameterLibraryParser.support(root.getName());
   }

   public void setParameterLibraryParser(ParameterLibraryParser parameterLibraryParser) {
      this.parameterLibraryParser = parameterLibraryParser;
   }
}
