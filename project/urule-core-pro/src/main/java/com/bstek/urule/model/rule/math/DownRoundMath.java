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
   public Object calculate(Context context, Map<String, Object> factMap) {
      Object objectValue = context.getValueCompute().complexValueCompute(this.value, context, factMap);
      double doubleValue = Utils.toBigDecimal(objectValue).doubleValue();
      return new BigDecimal(Math.floor(doubleValue)).stripTrailingZeros();
   }

   @Override
   public String getId() {
      String text = LocaleHolder.isEnglish() ? "DownRound" : "向下取整";
      return "[" + text + "]" + this.value + "";
   }

   public Value getValue() {
      return this.value;
   }

   public void setValue(Value value) {
      this.value = value;
   }

   @Override
   public MathType getType() {
      return MathType.downRound;
   }
}
