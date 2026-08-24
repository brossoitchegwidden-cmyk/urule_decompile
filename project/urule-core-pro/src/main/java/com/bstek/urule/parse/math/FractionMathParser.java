package com.bstek.urule.parse.math;

import com.bstek.urule.model.rule.math.FractionMath;
import com.bstek.urule.model.rule.math.MathSign;
import com.bstek.urule.parse.ValueParser;
import org.dom4j.Element;

public class FractionMathParser extends MathParser {
   public FractionMathParser(ValueParser valueParser) {
      super(valueParser);
   }

   public MathSign parse(Element element) {
      FractionMath fractionMath = new FractionMath();

      for (Object objectValue : element.elements()) {
         if (objectValue != null && objectValue instanceof Element) {
            Element element2 = (Element)objectValue;
            if (element2.getName().equals("numerator")) {
               fractionMath.setNumerator(this.parseValue(element2));
            } else if (element2.getName().equals("denominator")) {
               fractionMath.setDenominator(this.parseValue(element2));
            }
         }
      }

      return fractionMath;
   }

   @Override
   public boolean support(String name) {
      return name.equals("fraction-sign");
   }
}
