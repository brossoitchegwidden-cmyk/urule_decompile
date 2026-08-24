package com.bstek.urule.parse.math;

import com.bstek.urule.model.rule.math.MathSign;
import com.bstek.urule.model.rule.math.PowerMath;
import com.bstek.urule.parse.ValueParser;
import org.dom4j.Element;

public class PowerMathParser extends MathParser {
   public PowerMathParser(ValueParser valueParser) {
      super(valueParser);
   }

   @Override
   public boolean support(String name) {
      return name.equals("power-sign");
   }

   public MathSign parse(Element element) {
      PowerMath powerMath = new PowerMath();

      for (Object objectValue : element.elements()) {
         if (objectValue != null && objectValue instanceof Element) {
            Element element2 = (Element)objectValue;
            if (element2.getName().equals("base")) {
               powerMath.setBase(this.parseValue(element2));
            } else if (element2.getName().equals("power")) {
               powerMath.setPower(this.parseValue(element2));
            }
         }
      }

      return powerMath;
   }
}
