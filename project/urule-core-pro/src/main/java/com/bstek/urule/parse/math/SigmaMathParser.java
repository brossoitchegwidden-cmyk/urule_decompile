package com.bstek.urule.parse.math;

import com.bstek.urule.model.rule.math.MathSign;
import com.bstek.urule.model.rule.math.SigmaMath;
import com.bstek.urule.parse.ValueParser;
import org.dom4j.Element;

public class SigmaMathParser extends MathParser {
   public SigmaMathParser(ValueParser var1) {
      super(var1);
   }

   @Override
   public boolean support(String var1) {
      return var1.equals("sigma-sign");
   }

   public MathSign parse(Element var1) {
      SigmaMath var2 = new SigmaMath();

      for (Object var4 : var1.elements()) {
         if (var4 != null && var4 instanceof Element) {
            Element var5 = (Element)var4;
            if (var5.getName().equals("expr")) {
               var2.setExpr(this.a(var5));
            } else if (var5.getName().equals("ivalue")) {
               var2.setIvalue(this.a(var5));
            } else if (var5.getName().equals("superior")) {
               var2.setSuperior(this.a(var5));
            }
         }
      }

      return var2;
   }
}
