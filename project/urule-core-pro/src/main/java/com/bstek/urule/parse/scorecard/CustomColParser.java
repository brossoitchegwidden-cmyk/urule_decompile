package com.bstek.urule.parse.scorecard;

import com.bstek.urule.model.scorecard.CustomCol;
import com.bstek.urule.parse.Parser;
import org.dom4j.Element;

public class CustomColParser implements Parser<CustomCol> {
   public CustomCol parse(Element element) {
      CustomCol customCol = new CustomCol();
      customCol.setColNumber(Integer.parseInt(element.attributeValue("col-number")));
      customCol.setName(element.attributeValue("name"));
      customCol.setWidth(element.attributeValue("width"));
      return customCol;
   }

   @Override
   public boolean support(String name) {
      return name.equals("custom-col");
   }
}
