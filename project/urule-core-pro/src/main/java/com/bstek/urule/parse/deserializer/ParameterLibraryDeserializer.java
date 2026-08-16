package com.bstek.urule.parse.deserializer;

import com.bstek.urule.model.library.variable.Variable;
import com.bstek.urule.parse.ParameterLibraryParser;
import java.util.List;
import org.dom4j.Element;

public class ParameterLibraryDeserializer implements Deserializer<List<Variable>> {
   public static final String BEAN_ID = "urule.parameterLibraryDeserializer";
   private ParameterLibraryParser a;

   public List<Variable> deserialize(Element var1) {
      return this.a.parse(var1);
   }

   @Override
   public boolean support(Element var1) {
      return this.a.support(var1.getName());
   }

   public void setParameterLibraryParser(ParameterLibraryParser var1) {
      this.a = var1;
   }
}
