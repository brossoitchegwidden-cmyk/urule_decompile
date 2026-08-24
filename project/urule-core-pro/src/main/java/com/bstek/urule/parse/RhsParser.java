package com.bstek.urule.parse;

import com.bstek.urule.action.Action;
import com.bstek.urule.model.rule.Rhs;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.dom4j.Element;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class RhsParser implements Parser<Rhs>, ApplicationContextAware {
   private Collection<ActionParser> actionParsers;

   public Rhs parse(Element element) {
      Rhs rhs = new Rhs();
      rhs.setActions(this.parseActions(element));
      return rhs;
   }

   public List<Action> parseActions(Element element) {
      ArrayList actions = new ArrayList();

      for (Object objectValue : element.elements()) {
         if (objectValue != null && objectValue instanceof Element) {
            Element element2 = (Element)objectValue;
            String name = element2.getName();

            for (ActionParser actionParser : this.actionParsers) {
               if (actionParser.support(name)) {
                  actions.add(actionParser.parse(element2));
                  break;
               }
            }
         }
      }

      return actions;
   }

   @Override
   public boolean support(String name) {
      return name.equals("then");
   }

   public Collection<ActionParser> getActionParsers() {
      return this.actionParsers;
   }

   public void setApplicationContext(ApplicationContext context) throws BeansException {
      this.actionParsers = context.getBeansOfType(ActionParser.class).values();
   }
}
