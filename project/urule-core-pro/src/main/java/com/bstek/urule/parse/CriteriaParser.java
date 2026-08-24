package com.bstek.urule.parse;

import com.bstek.urule.model.rule.Op;
import com.bstek.urule.model.rule.lhs.Criteria;
import com.bstek.urule.model.rule.lhs.Criterion;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

public class CriteriaParser extends CriterionParser {
   private ValueParser valueParser;
   private LeftParser leftParser;

   public Criterion parse(Element element) {
      Criteria criteria = new Criteria();
      String text = element.attributeValue("op");
      if (StringUtils.isNotBlank(text)) {
         Op op = Op.valueOf(text);
         criteria.setOp(op);
      }

      for (Object objectValue : element.elements()) {
         if (objectValue != null && objectValue instanceof Element) {
            Element element2 = (Element)objectValue;
            String name = element2.getName();
            if (name.equals("value")) {
               criteria.setValue(this.valueParser.parse(element2));
            } else if (name.equals("left")) {
               criteria.setLeft(this.leftParser.parse(element2));
            }
         }
      }

      return criteria;
   }

   @Override
   public boolean support(String name) {
      return name.equals("atom");
   }

   public void setValueParser(ValueParser valueParser) {
      this.valueParser = valueParser;
   }

   public void setLeftParser(LeftParser leftParser) {
      this.leftParser = leftParser;
   }
}
