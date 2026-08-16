package com.bstek.urule.parse.math;

import com.bstek.urule.model.rule.math.MathSign;
import com.bstek.urule.model.rule.math.PowerMath;
import com.bstek.urule.parse.ValueParser;
import org.dom4j.Element;

public class PowerMathParser extends MathParser {
   public PowerMathParser(ValueParser var1) {
      super(var1);
   }

   @Override
   public boolean support(String var1) {
      return var1.equals("power-sign");
   }

   public MathSign parse(Element var1) {
      PowerMath var2 = new PowerMath();

      for (Object var4 : var1.elements()) {
         if (var4 != null && var4 instanceof Element) {
            Element var5 = (Element)var4;
            if (var5.getName().equals("base")) {
               var2.setBase(this.a(var5));
            } else if (var5.getName().equals("power")) {
               var2.setPower(this.a(var5));
            }
         }
      }

      return var2;
   }
}
