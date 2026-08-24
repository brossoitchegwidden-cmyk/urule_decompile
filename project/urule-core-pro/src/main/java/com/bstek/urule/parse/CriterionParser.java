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
   private Collection<CriterionParser> criterionParsers;
   private ApplicationContext applicationContext;

   public List<Criterion> parseCriterion(Element element) {
      if (this.criterionParsers == null) {
         this.initializeState();
      }

      ArrayList criterion2 = null;

      for (Object objectValue : element.elements()) {
         if (objectValue != null && objectValue instanceof Element) {
            Element element2 = (Element)objectValue;
            String name = element2.getName();

            for (CriterionParser criterionParser : this.criterionParsers) {
               if (criterionParser.support(name)) {
                  if (criterion2 == null) {
                     criterion2 = new ArrayList();
                  }

                  Criterion criterion = criterionParser.parse(element2);
                  if (criterion != null) {
                     criterion2.add(criterion);
                  }
                  break;
               }
            }
         }
      }

      return criterion2;
   }

   private void initializeState() {
      this.criterionParsers = this.applicationContext.getBeansOfType(CriterionParser.class).values();
   }

   public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
      this.applicationContext = applicationContext;
   }
}
