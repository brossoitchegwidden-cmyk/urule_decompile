package com.bstek.urule.parse.math;

import com.bstek.urule.model.rule.math.MathSign;
import com.bstek.urule.model.rule.math.NRadicalMath;
import com.bstek.urule.parse.ValueParser;
import org.dom4j.Element;

public class NRadicalMathParser extends MathParser {
   public NRadicalMathParser(ValueParser valueParser) {
      super(valueParser);
   }

   public MathSign parse(Element element) {
      NRadicalMath nRadicalMath = new NRadicalMath();

      for (Object objectValue : element.elements()) {
         if (objectValue != null && objectValue instanceof Element) {
            Element element2 = (Element)objectValue;
            if (element2.getName().equals("power")) {
               nRadicalMath.setPower(this.parseValue(element2));
            } else if (element2.getName().equals("value")) {
               nRadicalMath.setValue(this.parseValue(element2));
            }
         }
      }

      return nRadicalMath;
   }

   @Override
   public boolean support(String name) {
      return name.equals("nradical-sign");
   }
}
