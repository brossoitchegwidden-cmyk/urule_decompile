package com.bstek.urule.model.rule.math;

import com.bstek.urule.LocaleHolder;
import com.bstek.urule.Utils;
import com.bstek.urule.exception.RuleException;
import com.bstek.urule.model.rule.Value;
import com.bstek.urule.runtime.rete.Context;
import java.math.BigDecimal;
import java.util.Map;

public class TriangleFunctionMath implements MathSign {
   private String name;
   private Value value;

   @Override
   public Object calculate(Context context, Map<String, Object> factMap) {
      Object objectValue = context.getValueCompute().complexValueCompute(this.value, context, factMap);
      if (this.name.equals("sin")) {
         double doubleValue = Math.sin(Utils.toBigDecimal(objectValue).doubleValue());
         return new BigDecimal(doubleValue).stripTrailingZeros();
      } else if (this.name.equals("cos")) {
         double doubleValue2 = Math.cos(Utils.toBigDecimal(objectValue).doubleValue());
         return new BigDecimal(doubleValue2).stripTrailingZeros();
      } else if (this.name.equals("tan")) {
         double doubleValue3 = Math.tan(Utils.toBigDecimal(objectValue).doubleValue());
         return new BigDecimal(doubleValue3).stripTrailingZeros();
      } else if (this.name.equals("cot")) {
         double doubleValue4 = 1.0 / Math.tan(Utils.toBigDecimal(objectValue).doubleValue());
         return new BigDecimal(doubleValue4).stripTrailingZeros();
      } else if (this.name.equals("sec")) {
         double doubleValue5 = 1.0 / Math.cos(Utils.toBigDecimal(objectValue).doubleValue());
         return new BigDecimal(doubleValue5).stripTrailingZeros();
      } else if (this.name.equals("csc")) {
         double doubleValue6 = 1.0 / Math.sin(Utils.toBigDecimal(objectValue).doubleValue());
         return new BigDecimal(doubleValue6).stripTrailingZeros();
      } else if (this.name.equals("arcsin")) {
         double doubleValue7 = Math.asin(Utils.toBigDecimal(objectValue).doubleValue());
         return new BigDecimal(doubleValue7).stripTrailingZeros();
      } else if (this.name.equals("arccos")) {
         double doubleValue8 = Math.acos(Utils.toBigDecimal(objectValue).doubleValue());
         return new BigDecimal(doubleValue8).stripTrailingZeros();
      } else if (this.name.equals("arctan")) {
         double doubleValue9 = Math.atan(Utils.toBigDecimal(objectValue).doubleValue());
         return new BigDecimal(doubleValue9).stripTrailingZeros();
      } else if (this.name.equals("arccot")) {
         double doubleValue10 = Math.atan(1.0 / Utils.toBigDecimal(objectValue).doubleValue());
         return new BigDecimal(doubleValue10).stripTrailingZeros();
      } else if (this.name.equals("arcsec")) {
         double doubleValue11 = Math.acos(1.0 / Utils.toBigDecimal(objectValue).doubleValue());
         return new BigDecimal(doubleValue11).stripTrailingZeros();
      } else if (this.name.equals("arccsc")) {
         double doubleValue12 = Math.asin(1.0 / Utils.toBigDecimal(objectValue).doubleValue());
         return new BigDecimal(doubleValue12).stripTrailingZeros();
      } else {
         throw new RuleException("Unknow triangle function name :" + this.name + "");
      }
   }

   public Value getValue() {
      return this.value;
   }

   public void setValue(Value value) {
      this.value = value;
   }

   public String getName() {
      return this.name;
   }

   public void setName(String name) {
      this.name = name;
   }

   @Override
   public String getId() {
      String text = LocaleHolder.isEnglish() ? "Triangle" : "三角函数";
      return "[" + text + "(" + this.name + ")](" + this.value.getId() + ")";
   }

   @Override
   public MathType getType() {
      return MathType.triangle;
   }
}
