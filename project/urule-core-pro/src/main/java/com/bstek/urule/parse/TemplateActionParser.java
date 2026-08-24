package com.bstek.urule.parse;

import com.bstek.urule.action.Action;
import com.bstek.urule.action.TemplateAction;
import org.dom4j.Element;

public class TemplateActionParser extends ActionParser {
   @Override
   public boolean support(String name) {
      return name.equals("action-template");
   }

   public Action parse(Element element) {
      TemplateAction templateAction = new TemplateAction();
      templateAction.setId(element.attributeValue("id"));
      templateAction.setName(element.attributeValue("name"));
      return templateAction;
   }
}
