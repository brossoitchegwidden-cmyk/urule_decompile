package com.bstek.urule.model.rule.math;

import com.bstek.urule.LocaleHolder;
import com.bstek.urule.Utils;
import com.bstek.urule.model.rule.Value;
import com.bstek.urule.runtime.rete.Context;
import java.math.BigDecimal;
import java.util.Map;

public class NRadicalMath implements MathSign {
   private Value power;
   private Value value;

   @Override
   public Object calculate(Context context, Map<String, Object> factMap) {
      Object objectValue = context.getValueCompute().complexValueCompute(this.power, context, factMap);
      Object objectValue2 = context.getValueCompute().complexValueCompute(this.value, context, factMap);
      BigDecimal decimalValue = Utils.toBigDecimal(objectValue);
      BigDecimal decimalValue2 = Utils.toBigDecimal(objectValue2);
      return new BigDecimal(Math.pow(decimalValue2.doubleValue(), 1.0 / decimalValue.doubleValue())).stripTrailingZeros();
   }

   public Value getPower() {
      return this.power;
   }

   public void setPower(Value power) {
      this.power = power;
   }

   public Value getValue() {
      return this.value;
   }

   public void setValue(Value value) {
      this.value = value;
   }

   @Override
   public MathType getType() {
      return MathType.nradical;
   }

   @Override
   public String getId() {
      String text = LocaleHolder.isEnglish() ? "NRadical" : "开N次方根";
      return "[" + text + "]" + this.power.getId() + "√" + this.value.getId();
   }
}
