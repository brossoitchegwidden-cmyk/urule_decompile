package com.bstek.urule.parse.deserializer;

import com.bstek.urule.model.template.ConditionTemplate;
import com.bstek.urule.parse.ConditionTemplateParser;
import org.dom4j.Element;

public class ConditionTemplateDeserializer implements Deserializer<ConditionTemplate> {
   public static final String BEAN_ID = "urule.conditionTemplateDeserializer";
   private ConditionTemplateParser conditionTemplateParser;

   public ConditionTemplate deserialize(Element root) {
      return this.conditionTemplateParser.parse(root);
   }

   @Override
   public boolean support(Element root) {
      return this.conditionTemplateParser.support(root.getName());
   }

   public void setConditionTemplateParser(ConditionTemplateParser conditionTemplateParser) {
      this.conditionTemplateParser = conditionTemplateParser;
   }
}
