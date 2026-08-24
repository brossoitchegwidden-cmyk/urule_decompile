package com.bstek.urule.parse.deserializer;

import com.bstek.urule.model.template.ActionTemplate;
import com.bstek.urule.parse.ActionTemplateParser;
import org.dom4j.Element;

public class ActionTemplateDeserializer implements Deserializer<ActionTemplate> {
   public static final String BEAN_ID = "urule.actionTemplateDeserializer";
   private ActionTemplateParser actionTemplateParser;

   public ActionTemplate deserialize(Element root) {
      return this.actionTemplateParser.parse(root);
   }

   @Override
   public boolean support(Element root) {
      return this.actionTemplateParser.support(root.getName());
   }

   public void setActionTemplateParser(ActionTemplateParser actionTemplateParser) {
      this.actionTemplateParser = actionTemplateParser;
   }
}
