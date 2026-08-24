package com.bstek.urule.parse.math;

import com.bstek.urule.model.rule.math.LnMath;
import com.bstek.urule.model.rule.math.MathSign;
import com.bstek.urule.parse.ValueParser;
import org.dom4j.Element;

public class LnMathParser extends MathParser {
   public LnMathParser(ValueParser valueParser) {
      super(valueParser);
   }

   @Override
   public boolean support(String name) {
      return name.equals("ln-sign");
   }

   public MathSign parse(Element element) {
      LnMath lnMath = new LnMath();
      lnMath.setValue(this.parseValue(element));
      return lnMath;
   }
}
