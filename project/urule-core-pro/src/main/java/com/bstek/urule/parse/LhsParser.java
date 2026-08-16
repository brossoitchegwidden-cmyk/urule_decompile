package com.bstek.urule.parse;

import com.bstek.urule.model.rule.lhs.Criterion;
import com.bstek.urule.model.rule.lhs.Lhs;
import java.util.Collection;
import org.dom4j.Element;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class LhsParser implements Parser<Lhs>, ApplicationContextAware {
   public static final String BEAN_ID = "urule.lhsParser";
   private Collection<CriterionParser> a;

   public Lhs parse(Element var1) {
      Lhs var2 = new Lhs();
      var2.setCriterion(this.parseCriterion(var1));
      return var2;
   }

   public Criterion parseCriterion(Element var1) {
      Criterion var2 = null;

      for (Object var4 : var1.elements()) {
         if (var4 != null && var4 instanceof Element) {
            Element var5 = (Element)var4;
            String var6 = var5.getName();

            for (CriterionParser var8 : this.a) {
               if (var8.support(var6)) {
                  var2 = var8.parse(var5);
                  if (var2 != null) {
                     break;
                  }
               }
            }
         }
      }

      return var2;
   }

   @Override
   public boolean support(String var1) {
      return var1.equals("if");
   }

   public Collection<CriterionParser> getCriterionParsers() {
      return this.a;
   }

   public void setApplicationContext(ApplicationContext var1) throws BeansException {
      this.a = var1.getBeansOfType(CriterionParser.class).values();
   }
}
