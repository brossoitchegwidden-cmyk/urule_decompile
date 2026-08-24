package com.bstek.urule.parse;

import com.bstek.urule.model.rule.lhs.And;
import com.bstek.urule.model.rule.lhs.Criterion;
import com.bstek.urule.model.rule.lhs.Met;
import com.bstek.urule.model.rule.lhs.Or;
import java.util.List;
import org.dom4j.Element;

public class JunctionParser extends CriterionParser {
   public Criterion parse(Element element) {
      List criterion = this.parseCriterion(element);
      if (criterion != null && criterion.size() != 0) {
         String name = element.getName();
         if (name.equals("and")) {
            And and = new And();
            and.setCriterions(criterion);
            return and;
         } else if (name.equals("or")) {
            Or or = new Or();
            or.setCriterions(criterion);
            return or;
         } else {
            Met met = new Met();
            met.setMet(Integer.parseInt(element.attributeValue("met")));
            met.setOnly(Boolean.parseBoolean(element.attributeValue("only")));
            met.setCriterions(criterion);
            return met;
         }
      } else {
         return null;
      }
   }

   @Override
   public boolean support(String name) {
      return name.equals("and") || name.equals("or") || name.equals("met");
   }
}
