package com.bstek.urule.parse;

import com.bstek.urule.Utils;
import com.bstek.urule.model.rule.ParenValue;
import org.dom4j.Element;

public class ParenParser implements Parser<ParenValue> {
   private ValueParser a;
   private ComplexArithmeticParser b;

   public ParenValue parse(Element var1) {
      if (this.b == null) {
         this.b = (ComplexArithmeticParser)Utils.getApplicationContext().getBean("urule.complexArithmeticParser");
      }

      ParenValue var2 = new ParenValue();

      for (Object var4 : var1.elements()) {
         if (var4 != null && var4 instanceof Element) {
            Element var5 = (Element)var4;
            if (this.a.support(var5.getName())) {
               var2.setValue(this.a.parse(var5));
            } else if (this.b.support(var5.getName())) {
               var2.setArithmetic(this.b.parse(var5));
            }
         }
      }

      return var2;
   }

   @Override
   public boolean support(String var1) {
      return var1.equals("paren");
   }

   public void setValueParser(ValueParser var1) {
      this.a = var1;
   }
}
