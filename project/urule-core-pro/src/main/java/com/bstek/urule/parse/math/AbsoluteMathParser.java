package com.bstek.urule.parse.math;

import com.bstek.urule.model.rule.math.AbsoluteMath;
import com.bstek.urule.model.rule.math.MathSign;
import com.bstek.urule.parse.ValueParser;
import org.dom4j.Element;

public class AbsoluteMathParser extends MathParser {
   public AbsoluteMathParser(ValueParser valueParser) {
      super(valueParser);
   }

   public MathSign parse(Element element) {
      AbsoluteMath absoluteMath = new AbsoluteMath();
      absoluteMath.setValue(this.parseValue(element));
      return absoluteMath;
   }

   @Override
   public boolean support(String name) {
      return name.equals("absolute-sign");
   }
}
