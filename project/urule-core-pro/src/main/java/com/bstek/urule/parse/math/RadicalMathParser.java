package com.bstek.urule.parse.math;

import com.bstek.urule.model.rule.math.MathSign;
import com.bstek.urule.model.rule.math.RadicalMath;
import com.bstek.urule.parse.ValueParser;
import org.dom4j.Element;

public class RadicalMathParser extends MathParser {
   public RadicalMathParser(ValueParser valueParser) {
      super(valueParser);
   }

   public MathSign parse(Element element) {
      RadicalMath radicalMath = new RadicalMath();
      radicalMath.setValue(this.parseValue(element));
      return radicalMath;
   }

   @Override
   public boolean support(String name) {
      return name.equals("radical-sign");
   }
}
