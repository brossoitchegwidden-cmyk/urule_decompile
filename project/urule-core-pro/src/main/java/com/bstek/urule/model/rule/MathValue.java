package com.bstek.urule.model.rule;

import com.bstek.urule.LocaleHolder;
import com.bstek.urule.model.rule.math.MathSign;

public class MathValue extends AbstractValue {
   private MathSign mathSign;

   public MathSign getMathSign() {
      return this.mathSign;
   }

   public void setMathSign(MathSign mathSign) {
      this.mathSign = mathSign;
   }

   @Override
   public ValueType getValueType() {
      return ValueType.Math;
   }

   @Override
   public String getId() {
      String text = LocaleHolder.isEnglish() ? "Math Value" : "数学符号";
      String id = "[" + text + "](" + this.mathSign.getId() + ")";
      if (this.arithmetic != null) {
         id = id + this.arithmetic.getId();
      }

      return id;
   }

   @Override
   public String getValueId() {
      String text = LocaleHolder.isEnglish() ? "Math Value" : "数学符号";
      return "[" + text + "](" + this.mathSign.getId() + ")";
   }
}
