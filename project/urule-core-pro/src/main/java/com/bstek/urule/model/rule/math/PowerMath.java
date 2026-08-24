package com.bstek.urule.model.rule.math;

import com.bstek.urule.LocaleHolder;
import com.bstek.urule.Utils;
import com.bstek.urule.model.rule.Value;
import com.bstek.urule.runtime.rete.Context;
import java.math.BigDecimal;
import java.util.Map;

public class PowerMath implements MathSign {
   private Value power;
   private Value base;

   @Override
   public Object calculate(Context context, Map<String, Object> factMap) {
      Object objectValue = context.getValueCompute().complexValueCompute(this.power, context, factMap);
      Object objectValue2 = context.getValueCompute().complexValueCompute(this.base, context, factMap);
      BigDecimal decimalValue = Utils.toBigDecimal(objectValue);
      BigDecimal decimalValue2 = Utils.toBigDecimal(objectValue2);
      return new BigDecimal(Math.pow(decimalValue2.doubleValue(), decimalValue.doubleValue())).stripTrailingZeros();
   }

   public Value getPower() {
      return this.power;
   }

   public void setPower(Value power) {
      this.power = power;
   }

   public Value getBase() {
      return this.base;
   }

   public void setBase(Value base) {
      this.base = base;
   }

   @Override
   public MathType getType() {
      return MathType.power;
   }

   @Override
   public String getId() {
      String text = LocaleHolder.isEnglish() ? "Power" : "乘方";
      return "[" + text + "]" + this.base.getId() + "^" + this.power.getId();
   }
}
