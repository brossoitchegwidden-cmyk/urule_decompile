package com.bstek.urule.model.rule.math;

import com.bstek.urule.LocaleHolder;
import com.bstek.urule.Utils;
import com.bstek.urule.model.rule.Value;
import com.bstek.urule.runtime.rete.Context;
import java.math.BigDecimal;
import java.util.Map;

public class AbsoluteMath implements MathSign {
   private Value value;

   @Override
   public Object calculate(Context context, Map<String, Object> factMap) {
      Object objectValue = context.getValueCompute().complexValueCompute(this.value, context, factMap);
      if (objectValue instanceof Integer) {
         int objectValue2 = (Integer)objectValue;
         return Math.abs(objectValue2);
      } else if (objectValue instanceof Double) {
         double objectValue3 = (Double)objectValue;
         return Math.abs(objectValue3);
      } else if (objectValue instanceof Float) {
         float objectValue4 = (Float)objectValue;
         return Math.abs(objectValue4);
      } else if (objectValue instanceof Long) {
         long objectValue5 = (Long)objectValue;
         return Math.abs(objectValue5);
      } else {
         BigDecimal decimalValue = Utils.toBigDecimal(objectValue);
         return new BigDecimal(Math.abs(decimalValue.doubleValue())).stripTrailingZeros();
      }
   }

   @Override
   public MathType getType() {
      return MathType.absolute;
   }

   public Value getValue() {
      return this.value;
   }

   public void setValue(Value value) {
      this.value = value;
   }

   @Override
   public String getId() {
      String text = LocaleHolder.isEnglish() ? "Absolute" : "求绝对值";
      return "[" + text + "]|" + this.value.getId() + "|";
   }
}
