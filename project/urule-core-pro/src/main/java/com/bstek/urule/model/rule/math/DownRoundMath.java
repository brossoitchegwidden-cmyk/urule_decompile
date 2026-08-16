package com.bstek.urule.model.rule.math;

import com.bstek.urule.LocaleHolder;
import com.bstek.urule.Utils;
import com.bstek.urule.model.rule.Value;
import com.bstek.urule.runtime.rete.Context;
import java.math.BigDecimal;
import java.util.Map;

public class DownRoundMath implements MathSign {
   private Value value;

   @Override
   public Object calculate(Context var1, Map<String, Object> var2) {
      Object var3 = var1.getValueCompute().complexValueCompute(this.value, var1, var2);
      double var4 = Utils.toBigDecimal(var3).doubleValue();
      return new BigDecimal(Math.floor(var4)).stripTrailingZeros();
   }

   @Override
   public String getId() {
      String var1 = LocaleHolder.isEnglish() ? "DownRound" : "向下取整";
      return "[" + var1 + "]" + this.value + "";
   }

   public Value getValue() {
      return this.value;
   }

   public void setValue(Value var1) {
      this.value = var1;
   }

   @Override
   public MathType getType() {
      return MathType.downRound;
   }
}
