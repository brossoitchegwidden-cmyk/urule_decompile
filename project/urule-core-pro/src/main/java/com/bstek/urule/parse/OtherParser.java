package com.bstek.urule.parse;

import com.bstek.urule.model.rule.Other;
import java.util.Collection;
import org.dom4j.Element;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class OtherParser implements Parser<Other>, ApplicationContextAware {
   private Collection<ActionParser> a;

   public Other parse(Element var1) {
      Other var2 = new Other();

      for (Object var4 : var1.elements()) {
         if (var4 != null && var4 instanceof Element) {
            Element var5 = (Element)var4;
            String var6 = var5.getName();

            for (ActionParser var8 : this.a) {
               if (var8.support(var6)) {
                  var2.addAction(var8.parse(var5));
                  break;
               }
            }
         }
      }

      return var2;
   }

   @Override
   public boolean support(String var1) {
      return var1.equals("else");
   }

   public void setApplicationContext(ApplicationContext var1) throws BeansException {
      this.a = var1.getBeansOfType(ActionParser.class).values();
   }
}
