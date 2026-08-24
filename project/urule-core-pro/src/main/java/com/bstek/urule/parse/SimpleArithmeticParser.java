package com.bstek.urule.parse;

import com.bstek.urule.model.rule.ArithmeticType;
import com.bstek.urule.model.rule.SimpleArithmetic;
import com.bstek.urule.model.rule.SimpleArithmeticValue;
import org.dom4j.Element;

public class SimpleArithmeticParser implements Parser<SimpleArithmetic> {
   public SimpleArithmetic parse(Element element) {
      SimpleArithmetic simpleArithmetic = new SimpleArithmetic();
      ArithmeticType arithmeticType = ArithmeticType.valueOf(element.attributeValue("type"));
      simpleArithmetic.setType(arithmeticType);
      SimpleArithmeticValue simpleArithmeticValue = new SimpleArithmeticValue();
      simpleArithmeticValue.setContent(element.attributeValue("value"));
      simpleArithmetic.setValue(simpleArithmeticValue);

      for (Object objectValue : element.elements()) {
         if (objectValue != null && objectValue instanceof Element) {
            Element element2 = (Element)objectValue;
            if (this.support(element2.getName())) {
               simpleArithmeticValue.setArithmetic(this.parse(element2));
               break;
            }
         }
      }

      return simpleArithmetic;
   }

   @Override
   public boolean support(String name) {
      return name.equals("simple-arith");
   }
}
