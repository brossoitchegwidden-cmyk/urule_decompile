package com.bstek.urule.parse;

import com.bstek.urule.model.library.variable.Variable;
import java.util.ArrayList;
import java.util.List;
import org.dom4j.Element;

public class ParameterLibraryParser implements Parser<List<Variable>> {
   private VariableParser variableParser;

   public List<Variable> parse(Element element) {
      ArrayList parseResult = new ArrayList();

      for (Object objectValue : element.elements()) {
         if (objectValue != null && objectValue instanceof Element) {
            Element element2 = (Element)objectValue;
            String name = element2.getName();
            if (name.equals("parameter")) {
               parseResult.add(this.variableParser.parse(element2));
            }
         }
      }

      return parseResult;
   }

   @Override
   public boolean support(String name) {
      return name.equals("parameter-library");
   }

   public void setVariableParser(VariableParser variableParser) {
      this.variableParser = variableParser;
   }
}
