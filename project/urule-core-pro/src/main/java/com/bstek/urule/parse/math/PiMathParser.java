package com.bstek.urule.parse.math;

import com.bstek.urule.model.rule.math.MathSign;
import com.bstek.urule.model.rule.math.PiMath;
import com.bstek.urule.parse.ValueParser;
import org.dom4j.Element;

public class PiMathParser extends MathParser {
   public PiMathParser(ValueParser valueParser) {
      super(valueParser);
   }

   @Override
   public boolean support(String name) {
      return name.equals("pi-sign");
   }

   public MathSign parse(Element element) {
      return new PiMath();
   }
}
