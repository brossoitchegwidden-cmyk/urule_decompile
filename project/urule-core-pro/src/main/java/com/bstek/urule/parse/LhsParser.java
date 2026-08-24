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
   private Collection<CriterionParser> criterionParsers;

   public Lhs parse(Element element) {
      Lhs lhs = new Lhs();
      lhs.setCriterion(this.parseCriterion(element));
      return lhs;
   }

   public Criterion parseCriterion(Element element) {
      Criterion criterion = null;

      for (Object objectValue : element.elements()) {
         if (objectValue != null && objectValue instanceof Element) {
            Element element2 = (Element)objectValue;
            String name = element2.getName();

            for (CriterionParser criterionParser : this.criterionParsers) {
               if (criterionParser.support(name)) {
                  criterion = criterionParser.parse(element2);
                  if (criterion != null) {
                     break;
                  }
               }
            }
         }
      }

      return criterion;
   }

   @Override
   public boolean support(String name) {
      return name.equals("if");
   }

   public Collection<CriterionParser> getCriterionParsers() {
      return this.criterionParsers;
   }

   public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
      this.criterionParsers = applicationContext.getBeansOfType(CriterionParser.class).values();
   }
}
