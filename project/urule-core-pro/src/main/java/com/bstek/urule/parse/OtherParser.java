package com.bstek.urule.parse;

import com.bstek.urule.model.rule.Other;
import java.util.Collection;
import org.dom4j.Element;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class OtherParser implements Parser<Other>, ApplicationContextAware {
   private Collection<ActionParser> actionParsers;

   public Other parse(Element element) {
      Other other = new Other();

      for (Object objectValue : element.elements()) {
         if (objectValue != null && objectValue instanceof Element) {
            Element element2 = (Element)objectValue;
            String name = element2.getName();

            for (ActionParser actionParser : this.actionParsers) {
               if (actionParser.support(name)) {
                  other.addAction(actionParser.parse(element2));
                  break;
               }
            }
         }
      }

      return other;
   }

   @Override
   public boolean support(String name) {
      return name.equals("else");
   }

   public void setApplicationContext(ApplicationContext context) throws BeansException {
      this.actionParsers = context.getBeansOfType(ActionParser.class).values();
   }
}
