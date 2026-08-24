package com.bstek.urule.parse.math;

import com.bstek.urule.model.rule.math.MathSign;
import com.bstek.urule.model.rule.math.UpRoundMath;
import com.bstek.urule.parse.ValueParser;
import org.dom4j.Element;

public class UpRoundMathParser extends MathParser {
   public UpRoundMathParser(ValueParser valueParser) {
      super(valueParser);
   }

   @Override
   public boolean support(String name) {
      return name.equals("up-round");
   }

   public MathSign parse(Element element) {
      UpRoundMath upRoundMath = new UpRoundMath();
      upRoundMath.setValue(this.parseValue(element));
      return upRoundMath;
   }
}
