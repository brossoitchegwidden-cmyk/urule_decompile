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
   public Object calculate(Context var1, Map<String, Object> var2) {
      Object var3 = var1.getValueCompute().complexValueCompute(this.value, var1, var2);
      if (this.name.equals("sin")) {
         double var16 = Math.sin(Utils.toBigDecimal(var3).doubleValue());
         return new BigDecimal(var16).stripTrailingZeros();
      } else if (this.name.equals("cos")) {
         double var15 = Math.cos(Utils.toBigDecimal(var3).doubleValue());
         return new BigDecimal(var15).stripTrailingZeros();
      } else if (this.name.equals("tan")) {
         double var14 = Math.tan(Utils.toBigDecimal(var3).doubleValue());
         return new BigDecimal(var14).stripTrailingZeros();
      } else if (this.name.equals("cot")) {
         double var13 = 1.0 / Math.tan(Utils.toBigDecimal(var3).doubleValue());
         return new BigDecimal(var13).stripTrailingZeros();
      } else if (this.name.equals("sec")) {
         double var12 = 1.0 / Math.cos(Utils.toBigDecimal(var3).doubleValue());
         return new BigDecimal(var12).stripTrailingZeros();
      } else if (this.name.equals("csc")) {
         double var11 = 1.0 / Math.sin(Utils.toBigDecimal(var3).doubleValue());
         return new BigDecimal(var11).stripTrailingZeros();
      } else if (this.name.equals("arcsin")) {
         double var10 = Math.asin(Utils.toBigDecimal(var3).doubleValue());
         return new BigDecimal(var10).stripTrailingZeros();
      } else if (this.name.equals("arccos")) {
         double var9 = Math.acos(Utils.toBigDecimal(var3).doubleValue());
         return new BigDecimal(var9).stripTrailingZeros();
      } else if (this.name.equals("arctan")) {
         double var8 = Math.atan(Utils.toBigDecimal(var3).doubleValue());
         return new BigDecimal(var8).stripTrailingZeros();
      } else if (this.name.equals("arccot")) {
         double var7 = Math.atan(1.0 / Utils.toBigDecimal(var3).doubleValue());
         return new BigDecimal(var7).stripTrailingZeros();
      } else if (this.name.equals("arcsec")) {
         double var6 = Math.acos(1.0 / Utils.toBigDecimal(var3).doubleValue());
         return new BigDecimal(var6).stripTrailingZeros();
      } else if (this.name.equals("arccsc")) {
         double var4 = Math.asin(1.0 / Utils.toBigDecimal(var3).doubleValue());
         return new BigDecimal(var4).stripTrailingZeros();
      } else {
         throw new RuleException("Unknow triangle function name :" + this.name + "");
      }
   }

   public Value getValue() {
      return this.value;
   }

   public void setValue(Value var1) {
      this.value = var1;
   }

   public String getName() {
      return this.name;
   }

   public void setName(String var1) {
      this.name = var1;
   }

   @Override
   public String getId() {
      String var1 = LocaleHolder.isEnglish() ? "Triangle" : "三角函数";
      return "[" + var1 + "(" + this.name + ")](" + this.value.getId() + ")";
   }

   @Override
   public MathType getType() {
      return MathType.triangle;
   }
}
