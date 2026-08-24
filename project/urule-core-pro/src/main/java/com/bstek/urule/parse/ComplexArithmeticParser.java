package com.bstek.urule.parse;

import com.bstek.urule.model.rule.ArithmeticType;
import com.bstek.urule.model.rule.ComplexArithmetic;
import org.dom4j.Element;

public class ComplexArithmeticParser implements Parser<ComplexArithmetic> {
   public static final String BEAN_ID = "urule.complexArithmeticParser";
   private ValueParser valueParser;
   private ParenParser parenParser;

   public ComplexArithmetic parse(Element element) {
      ComplexArithmetic complexArithmetic = new ComplexArithmetic();
      ArithmeticType arithmeticType = ArithmeticType.valueOf(element.attributeValue("type"));
      complexArithmetic.setType(arithmeticType);

      for (Object objectValue : element.elements()) {
         if (objectValue != null && objectValue instanceof Element) {
            Element element2 = (Element)objectValue;
            if (this.valueParser.support(element2.getName())) {
               complexArithmetic.setValue(this.valueParser.parse(element2));
            } else if (this.parenParser.support(element2.getName())) {
               complexArithmetic.setValue(this.parenParser.parse(element2));
            }
         }
      }

      return complexArithmetic;
   }

   public void setValueParser(ValueParser valueParser) {
      this.valueParser = valueParser;
   }

   public void setParenParser(ParenParser parenParser) {
      this.parenParser = parenParser;
   }

   @Override
   public boolean support(String name) {
      return name.equals("complex-arith");
   }
}
