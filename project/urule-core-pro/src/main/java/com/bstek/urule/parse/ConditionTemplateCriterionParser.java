package com.bstek.urule.parse;

import com.bstek.urule.model.rule.lhs.ConditionTemplateCriterion;
import com.bstek.urule.model.rule.lhs.Criterion;
import org.dom4j.Element;

public class ConditionTemplateCriterionParser extends CriterionParser {
   @Override
   public boolean support(String name) {
      return name.equals("template");
   }

   public Criterion parse(Element element) {
      ConditionTemplateCriterion conditionTemplateCriterion = new ConditionTemplateCriterion();
      conditionTemplateCriterion.setId(element.attributeValue("id"));
      conditionTemplateCriterion.setName(element.attributeValue("name"));
      return conditionTemplateCriterion;
   }
}
