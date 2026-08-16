package com.bstek.urule.parse;

import com.bstek.urule.model.rule.Rule;
import org.dom4j.Element;

public class RuleParser extends AbstractRuleParser<Rule> {
   public Rule parse(Element var1) {
      Rule var2 = new Rule();
      this.parseRule(var2, var1);
      return var2;
   }

   @Override
   public boolean support(String var1) {
      return var1.equals("rule");
   }
}
