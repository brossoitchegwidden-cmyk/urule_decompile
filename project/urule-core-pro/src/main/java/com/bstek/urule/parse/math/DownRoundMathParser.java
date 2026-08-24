package com.bstek.urule.parse.math;

import com.bstek.urule.model.rule.math.DownRoundMath;
import com.bstek.urule.model.rule.math.MathSign;
import com.bstek.urule.parse.ValueParser;
import org.dom4j.Element;

public class DownRoundMathParser extends MathParser {
   public DownRoundMathParser(ValueParser valueParser) {
      super(valueParser);
   }

   @Override
   public boolean support(String name) {
      return name.equals("down-round");
   }

   public MathSign parse(Element element) {
      DownRoundMath downRoundMath = new DownRoundMath();
      downRoundMath.setValue(this.parseValue(element));
      return downRoundMath;
   }
}
