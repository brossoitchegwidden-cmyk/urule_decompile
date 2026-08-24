package com.bstek.urule.parse;

import com.bstek.urule.model.library.variable.VariableCategory;
import java.util.ArrayList;
import java.util.List;
import org.dom4j.Element;

public class VariableLibraryParser implements Parser<List<VariableCategory>> {
   private VariableCategoryParser variableCategoryParser;

   public List<VariableCategory> parse(Element element) {
      ArrayList parseResult = new ArrayList();

      for (Object objectValue : element.elements()) {
         if (objectValue != null && objectValue instanceof Element) {
            Element element2 = (Element)objectValue;
            String name = element2.getName();
            if (this.variableCategoryParser.support(name)) {
               parseResult.add(this.variableCategoryParser.parse(element2));
            }
         }
      }

      return parseResult;
   }

   @Override
   public boolean support(String name) {
      return name.equals("variable-library");
   }

   public void setVariableCategoryParser(VariableCategoryParser variableCategoryParser) {
      this.variableCategoryParser = variableCategoryParser;
   }
}
