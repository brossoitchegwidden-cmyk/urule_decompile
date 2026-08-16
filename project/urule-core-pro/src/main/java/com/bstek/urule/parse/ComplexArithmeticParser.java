package com.bstek.urule.parse;

import com.bstek.urule.model.rule.ArithmeticType;
import com.bstek.urule.model.rule.ComplexArithmetic;
import org.dom4j.Element;

public class ComplexArithmeticParser implements Parser<ComplexArithmetic> {
   public static final String BEAN_ID = "urule.complexArithmeticParser";
   private ValueParser a;
   private ParenParser b;

   public ComplexArithmetic parse(Element var1) {
      ComplexArithmetic var2 = new ComplexArithmetic();
      ArithmeticType var3 = ArithmeticType.valueOf(var1.attributeValue("type"));
      var2.setType(var3);

      for (Object var5 : var1.elements()) {
         if (var5 != null && var5 instanceof Element) {
            Element var6 = (Element)var5;
            if (this.a.support(var6.getName())) {
               var2.setValue(this.a.parse(var6));
            } else if (this.b.support(var6.getName())) {
               var2.setValue(this.b.parse(var6));
            }
         }
      }

      return var2;
   }

   public void setValueParser(ValueParser var1) {
      this.a = var1;
   }

   public void setParenParser(ParenParser var1) {
      this.b = var1;
   }

   @Override
   public boolean support(String var1) {
      return var1.equals("complex-arith");
   }
}
