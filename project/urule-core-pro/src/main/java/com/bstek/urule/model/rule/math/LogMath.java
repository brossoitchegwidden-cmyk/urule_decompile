package com.bstek.urule.model.rule.math;

import com.bstek.urule.Utils;
import com.bstek.urule.model.rule.Value;
import com.bstek.urule.runtime.rete.Context;
import java.math.BigDecimal;
import java.util.Map;

public class LogMath implements MathSign {
   private Value baseValue;
   private Value value;

   @Override
   public Object calculate(Context context, Map<String, Object> factMap) {
      Object objectValue = context.getValueCompute().complexValueCompute(this.value, context, factMap);
      Object objectValue2 = context.getValueCompute().complexValueCompute(this.baseValue, context, factMap);
      double doubleValue = Utils.toBigDecimal(objectValue).doubleValue();
      double doubleValue2 = Utils.toBigDecimal(objectValue2).doubleValue();
      return new BigDecimal(Math.log(doubleValue) / Math.log(doubleValue2)).stripTrailingZeros();
   }

   @Override
   public String getId() {
      return "[对数]" + this.baseValue + "," + this.value + "";
   }

   public Value getBaseValue() {
      return this.baseValue;
   }

   public void setBaseValue(Value baseValue) {
      this.baseValue = baseValue;
   }

   public Value getValue() {
      return this.value;
   }

   public void setValue(Value value) {
      this.value = value;
   }

   @Override
   public MathType getType() {
      return MathType.log;
   }
}
