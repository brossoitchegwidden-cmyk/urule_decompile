package com.bstek.urule.model.rule.math;

import com.bstek.urule.LocaleHolder;
import com.bstek.urule.Utils;
import com.bstek.urule.model.rule.Value;
import com.bstek.urule.runtime.rete.Context;
import java.math.BigDecimal;
import java.util.Map;

public class RadicalMath implements MathSign {
   private Value value;

   @Override
   public Object calculate(Context context, Map<String, Object> factMap) {
      Object objectValue = context.getValueCompute().complexValueCompute(this.value, context, factMap);
      BigDecimal decimalValue = Utils.toBigDecimal(objectValue);
      return new BigDecimal(Math.sqrt(decimalValue.doubleValue())).stripTrailingZeros();
   }

   @Override
   public MathType getType() {
      return MathType.radical;
   }

   public Value getValue() {
      return this.value;
   }

   public void setValue(Value value) {
      this.value = value;
   }

   @Override
   public String getId() {
      String text = LocaleHolder.isEnglish() ? "Radical" : "平方根";
      return "[" + text + "]√" + this.value.getId();
   }
}
