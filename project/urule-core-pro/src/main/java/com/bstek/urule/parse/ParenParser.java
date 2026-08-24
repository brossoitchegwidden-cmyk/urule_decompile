package com.bstek.urule.parse;

import com.bstek.urule.Utils;
import com.bstek.urule.model.rule.ParenValue;
import org.dom4j.Element;

public class ParenParser implements Parser<ParenValue> {
   private ValueParser valueParser;
   private ComplexArithmeticParser complexArithmeticParser;

   public ParenValue parse(Element element) {
      if (this.complexArithmeticParser == null) {
         this.complexArithmeticParser = (ComplexArithmeticParser)Utils.getApplicationContext().getBean("urule.complexArithmeticParser");
      }

      ParenValue parenValue = new ParenValue();

      for (Object objectValue : element.elements()) {
         if (objectValue != null && objectValue instanceof Element) {
            Element element2 = (Element)objectValue;
            if (this.valueParser.support(element2.getName())) {
               parenValue.setValue(this.valueParser.parse(element2));
            } else if (this.complexArithmeticParser.support(element2.getName())) {
               parenValue.setArithmetic(this.complexArithmeticParser.parse(element2));
            }
         }
      }

      return parenValue;
   }

   @Override
   public boolean support(String name) {
      return name.equals("paren");
   }

   public void setValueParser(ValueParser valueParser) {
      this.valueParser = valueParser;
   }
}
