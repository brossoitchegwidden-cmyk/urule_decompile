package com.bstek.urule.parse;

import com.bstek.urule.model.library.variable.Act;
import com.bstek.urule.model.library.variable.CategoryType;
import com.bstek.urule.model.library.variable.VariableCategory;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

public class VariableCategoryParser implements Parser<VariableCategory> {
   private VariableParser variableParser;

   public VariableCategory parse(Element element) {
      VariableCategory variableCategory = new VariableCategory();
      variableCategory.setUuid(element.attributeValue("uuid"));
      variableCategory.setName(element.attributeValue("name"));
      variableCategory.setClazz(element.attributeValue("clazz"));
      String text = element.attributeValue("act");
      if (StringUtils.isNotBlank(text)) {
         variableCategory.setAct(Act.valueOf(text));
      }

      variableCategory.setType(CategoryType.valueOf(element.attributeValue("type")));

      for (Object objectValue : element.elements()) {
         if (objectValue != null && objectValue instanceof Element) {
            Element element2 = (Element)objectValue;
            String name = element2.getName();
            if (this.variableParser.support(name)) {
               variableCategory.addVariable(this.variableParser.parse(element2));
            }
         }
      }

      return variableCategory;
   }

   @Override
   public boolean support(String name) {
      return name.equals("category");
   }

   public void setVariableParser(VariableParser variableParser) {
      this.variableParser = variableParser;
   }
}
