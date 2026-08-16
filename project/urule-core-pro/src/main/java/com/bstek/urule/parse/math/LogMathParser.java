package com.bstek.urule.parse.math;

import com.bstek.urule.model.rule.math.LogMath;
import com.bstek.urule.model.rule.math.MathSign;
import com.bstek.urule.parse.ValueParser;
import org.dom4j.Element;

public class LogMathParser extends MathParser {
   public LogMathParser(ValueParser var1) {
      super(var1);
   }

   @Override
   public boolean support(String var1) {
      return var1.equals("log-sign");
   }

   public MathSign parse(Element var1) {
      LogMath var2 = new LogMath();

      for (Object var4 : var1.elements()) {
         if (var4 != null && var4 instanceof Element) {
            Element var5 = (Element)var4;
            if (var5.getName().equals("base")) {
               var2.setBaseValue(this.a(var5));
            } else if (var5.getName().equals("real")) {
               var2.setValue(this.a(var5));
            }
         }
      }

      return var2;
   }
}
