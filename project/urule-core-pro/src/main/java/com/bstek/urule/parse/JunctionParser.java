package com.bstek.urule.parse;

import com.bstek.urule.model.rule.lhs.And;
import com.bstek.urule.model.rule.lhs.Criterion;
import com.bstek.urule.model.rule.lhs.Met;
import com.bstek.urule.model.rule.lhs.Or;
import java.util.List;
import org.dom4j.Element;

public class JunctionParser extends CriterionParser {
   public Criterion parse(Element var1) {
      List var2 = this.parseCriterion(var1);
      if (var2 != null && var2.size() != 0) {
         String var3 = var1.getName();
         if (var3.equals("and")) {
            And var6 = new And();
            var6.setCriterions(var2);
            return var6;
         } else if (var3.equals("or")) {
            Or var5 = new Or();
            var5.setCriterions(var2);
            return var5;
         } else {
            Met var4 = new Met();
            var4.setMet(Integer.parseInt(var1.attributeValue("met")));
            var4.setOnly(Boolean.parseBoolean(var1.attributeValue("only")));
            var4.setCriterions(var2);
            return var4;
         }
      } else {
         return null;
      }
   }

   @Override
   public boolean support(String var1) {
      return var1.equals("and") || var1.equals("or") || var1.equals("met");
   }
}
