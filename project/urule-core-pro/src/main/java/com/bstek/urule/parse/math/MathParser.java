package com.bstek.urule.parse.math;

import com.bstek.urule.exception.RuleException;
import com.bstek.urule.model.rule.Value;
import com.bstek.urule.model.rule.math.MathSign;
import com.bstek.urule.parse.Parser;
import com.bstek.urule.parse.ValueParser;
import org.dom4j.Element;

public abstract class MathParser implements Parser<MathSign> {
   protected ValueParser a;

   public MathParser(ValueParser var1) {
      this.a = var1;
   }

   protected Value a(Element var1) {
      for (Object var3 : var1.elements()) {
         if (var3 != null && var3 instanceof Element) {
            Element var4 = (Element)var3;
            if (this.a.support(var4.getName())) {
               return this.a.parse(var4);
            }
         }
      }

      throw new RuleException("Unknow value element[" + var1.asXML() + "]");
   }
}
