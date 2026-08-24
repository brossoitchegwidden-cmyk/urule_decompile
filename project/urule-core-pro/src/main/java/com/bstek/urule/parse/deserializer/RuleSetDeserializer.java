package com.bstek.urule.parse.deserializer;

import com.bstek.urule.model.rule.RuleSet;
import com.bstek.urule.parse.RuleSetParser;
import org.dom4j.Element;

public class RuleSetDeserializer implements Deserializer<RuleSet> {
   public static final String BEAN_ID = "urule.ruleSetDeserializer";
   private RuleSetParser ruleSetParser;

   public RuleSet deserialize(Element root) {
      return this.ruleSetParser.parse(root);
   }

   @Override
   public boolean support(Element root) {
      return this.ruleSetParser.support(root.getName());
   }

   public void setRuleSetParser(RuleSetParser ruleSetParser) {
      this.ruleSetParser = ruleSetParser;
   }
}
