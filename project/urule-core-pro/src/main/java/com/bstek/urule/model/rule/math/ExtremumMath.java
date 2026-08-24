package com.bstek.urule.model.rule.math;

import com.bstek.urule.LocaleHolder;
import com.bstek.urule.Utils;
import com.bstek.urule.exception.RuleException;
import com.bstek.urule.model.rule.Value;
import com.bstek.urule.runtime.rete.Context;
import java.math.BigDecimal;
import java.util.Map;

public class ExtremumMath implements MathSign {
   private String name;
   private Value firstValue;
   private Value secondValue;

   @Override
   public Object calculate(Context context, Map<String, Object> factMap) {
      Object objectValue = context.getValueCompute().complexValueCompute(this.firstValue, context, factMap);
      Object objectValue2 = context.getValueCompute().complexValueCompute(this.secondValue, context, factMap);
      if (this.name.equals("max")) {
         return this.doMax(objectValue, objectValue2);
      } else if (this.name.equals("min")) {
         return this.doMin(objectValue, objectValue2);
      } else {
         throw new RuleException("不支持的极值函数：" + this.name);
      }
   }

   private BigDecimal doMax(Object objectValue, Object objectValue2) {
      BigDecimal decimalValue = Utils.toBigDecimal(objectValue);
      BigDecimal decimalValue2 = Utils.toBigDecimal(objectValue2);
      return decimalValue.compareTo(decimalValue2) > 0 ? decimalValue : decimalValue2;
   }

   private BigDecimal doMin(Object objectValue, Object objectValue2) {
      BigDecimal decimalValue = Utils.toBigDecimal(objectValue);
      BigDecimal decimalValue2 = Utils.toBigDecimal(objectValue2);
      return decimalValue.compareTo(decimalValue2) < 1 ? decimalValue : decimalValue2;
   }

   public String getName() {
      return this.name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public Value getValue1() {
      return this.firstValue;
   }

   public void setValue1(Value value1) {
      this.firstValue = value1;
   }

   public Value getValue2() {
      return this.secondValue;
   }

   public void setValue2(Value value2) {
      this.secondValue = value2;
   }

   @Override
   public String getId() {
      String text = LocaleHolder.isEnglish() ? "Extremum" : "极值";
      return "[" + text + "(" + this.name + ")](" + this.firstValue.getId() + "," + this.secondValue.getId() + ")";
   }

   @Override
   public MathType getType() {
      return MathType.extremum;
   }
}
