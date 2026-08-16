package com.bstek.urule.parse;

import com.bstek.urule.model.rule.lhs.Criterion;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.dom4j.Element;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public abstract class CriterionParser extends AbstractParser<Criterion> implements ApplicationContextAware {
   private Collection<CriterionParser> a;
   private ApplicationContext b;

   public List<Criterion> parseCriterion(Element var1) {
      if (this.a == null) {
         this.a();
      }

      ArrayList var2 = null;

      for (Object var4 : var1.elements()) {
         if (var4 != null && var4 instanceof Element) {
            Element var5 = (Element)var4;
            String var6 = var5.getName();

            for (CriterionParser var8 : this.a) {
               if (var8.support(var6)) {
                  if (var2 == null) {
                     var2 = new ArrayList();
                  }

                  Criterion var9 = var8.parse(var5);
                  if (var9 != null) {
                     var2.add(var9);
                  }
                  break;
               }
            }
         }
      }

      return var2;
   }

   private void a() {
      this.a = this.b.getBeansOfType(CriterionParser.class).values();
   }

   public void setApplicationContext(ApplicationContext var1) throws BeansException {
      this.b = var1;
   }
}
