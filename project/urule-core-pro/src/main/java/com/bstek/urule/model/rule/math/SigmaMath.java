package com.bstek.urule.model.rule.math;

import com.bstek.urule.LocaleHolder;
import com.bstek.urule.Utils;
import com.bstek.urule.model.rule.Value;
import com.bstek.urule.runtime.rete.Context;
import java.math.BigDecimal;
import java.util.Map;

public class SigmaMath implements MathSign {
   private Value ivalue;
   private Value superior;
   private Value expr;

   @Override
   public Object calculate(Context context, Map<String, Object> factMap) {
      Object objectValue = context.getValueCompute().complexValueCompute(this.ivalue, context, factMap);
      Object objectValue2 = context.getValueCompute().complexValueCompute(this.superior, context, factMap);
      int number = Utils.toBigDecimal(objectValue).intValue();
      int number2 = Utils.toBigDecimal(objectValue2).intValue();
      BigDecimal bigDecimal = new BigDecimal(0);

      for (int index = number; index <= number2; index++) {
         context.getWorkingMemory().getParameters().put("__math_sigma_step_index_", index);
         Object objectValue3 = context.getValueCompute().complexValueCompute(this.expr, context, factMap);
         BigDecimal decimalValue = Utils.toBigDecimal(objectValue3);
         bigDecimal = bigDecimal.add(decimalValue);
      }

      return bigDecimal.stripTrailingZeros();
   }

   public Value getIvalue() {
      return this.ivalue;
   }

   public void setIvalue(Value ivalue) {
      this.ivalue = ivalue;
   }

   public Value getSuperior() {
      return this.superior;
   }

   public void setSuperior(Value superior) {
      this.superior = superior;
   }

   public Value getExpr() {
      return this.expr;
   }

   public void setExpr(Value expr) {
      this.expr = expr;
   }

   @Override
   public MathType getType() {
      return MathType.sigma;
   }

   @Override
   public String getId() {
      String text = LocaleHolder.isEnglish() ? "Sigma" : "求和";
      return "[" + text + "]Σ" + this.ivalue.getId() + "|" + this.superior.getId() + "|" + this.expr.getId();
   }
}
