package com.bstek.urule.parse.math;

import com.bstek.urule.model.rule.math.FractionMath;
import com.bstek.urule.model.rule.math.MathSign;
import com.bstek.urule.parse.ValueParser;
import org.dom4j.Element;

public class FractionMathParser extends MathParser {
   public FractionMathParser(ValueParser var1) {
      super(var1);
   }

   public MathSign parse(Element var1) {
      FractionMath var2 = new FractionMath();

      for (Object var4 : var1.elements()) {
         if (var4 != null && var4 instanceof Element) {
            Element var5 = (Element)var4;
            if (var5.getName().equals("numerator")) {
               var2.setNumerator(this.a(var5));
            } else if (var5.getName().equals("denominator")) {
               var2.setDenominator(this.a(var5));
            }
         }
      }

      return var2;
   }

   @Override
   public boolean support(String var1) {
      return var1.equals("fraction-sign");
   }
}
