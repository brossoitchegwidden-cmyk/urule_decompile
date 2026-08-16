package com.bstek.urule.model.rule;

import com.bstek.urule.LocaleHolder;
import com.bstek.urule.model.rule.math.MathSign;

public class MathValue extends AbstractValue {
   private MathSign mathSign;

   public MathSign getMathSign() {
      return this.mathSign;
   }

   public void setMathSign(MathSign var1) {
      this.mathSign = var1;
   }

   @Override
   public ValueType getValueType() {
      return ValueType.Math;
   }

   @Override
   public String getId() {
      String var1 = LocaleHolder.isEnglish() ? "Math Value" : "数学符号";
      String var2 = "[" + var1 + "](" + this.mathSign.getId() + ")";
      if (this.arithmetic != null) {
         var2 = var2 + this.arithmetic.getId();
      }

      return var2;
   }

   @Override
   public String getValueId() {
      String var1 = LocaleHolder.isEnglish() ? "Math Value" : "数学符号";
      return "[" + var1 + "](" + this.mathSign.getId() + ")";
   }
}
