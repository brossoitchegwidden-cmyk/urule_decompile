package com.bstek.urule.model.rule.math;

import com.bstek.urule.LocaleHolder;
import com.bstek.urule.Utils;
import com.bstek.urule.model.rule.Value;
import com.bstek.urule.runtime.rete.Context;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

public class FractionMath implements MathSign {
   private Value numerator;
   private Value denominator;

   @Override
   public Object calculate(Context context, Map<String, Object> factMap) {
      Object objectValue = context.getValueCompute().complexValueCompute(this.numerator, context, factMap);
      Object objectValue2 = context.getValueCompute().complexValueCompute(this.denominator, context, factMap);
      BigDecimal decimalValue = Utils.toBigDecimal(objectValue);
      BigDecimal decimalValue2 = Utils.toBigDecimal(objectValue2);
      BigDecimal decimalValue3 = decimalValue.divide(decimalValue2, 15, RoundingMode.HALF_UP).stripTrailingZeros();
      return decimalValue3.stripTrailingZeros();
   }

   public Value getNumerator() {
      return this.numerator;
   }

   public void setNumerator(Value numerator) {
      this.numerator = numerator;
   }

   public Value getDenominator() {
      return this.denominator;
   }

   public void setDenominator(Value denominator) {
      this.denominator = denominator;
   }

   @Override
   public MathType getType() {
      return MathType.fraction;
   }

   @Override
   public String getId() {
      String text = LocaleHolder.isEnglish() ? "Fraction" : "分数";
      return "[" + text + "]" + this.numerator.getId() + "/" + this.denominator.getId();
   }
}
