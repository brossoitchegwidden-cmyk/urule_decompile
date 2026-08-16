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
   public Object calculate(Context var1, Map<String, Object> var2) {
      Object var3 = var1.getValueCompute().complexValueCompute(this.value, var1, var2);
      BigDecimal var4 = Utils.toBigDecimal(var3);
      return new BigDecimal(Math.sqrt(var4.doubleValue())).stripTrailingZeros();
   }

   @Override
   public MathType getType() {
      return MathType.radical;
   }

   public Value getValue() {
      return this.value;
   }

   public void setValue(Value var1) {
      this.value = var1;
   }

   @Override
   public String getId() {
      String var1 = LocaleHolder.isEnglish() ? "Radical" : "平方根";
      return "[" + var1 + "]√" + this.value.getId();
   }
}
