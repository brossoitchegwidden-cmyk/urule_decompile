package com.bstek.urule.model.rule.lhs;

import com.bstek.urule.Utils;
import com.bstek.urule.model.rule.Op;
import com.bstek.urule.model.rule.Value;
import com.bstek.urule.runtime.rete.EvaluationContext;
import java.math.BigDecimal;
import java.util.Map;

public class ConditionItem {
   private String left;
   private Op op;
   private Value value;

   public EvaluateResponse eval(Map<String, CalculateData> resultMap, EvaluationContext context, Map<String, Object> factMap) {
      EvaluateResponse evaluateResponse = new EvaluateResponse();
      if (!resultMap.containsKey(this.left)) {
         evaluateResponse.setResult(false);
         evaluateResponse.setRightResult("not null");
         return evaluateResponse;
      }

      CalculateData calculateData = (CalculateData)resultMap.get(this.left);
      Object resultValue = calculateData.getResultValue();
      if (resultValue == null) {
         evaluateResponse.setResult(false);
         evaluateResponse.setRightResult("not null");
         return evaluateResponse;
      }

      Object objectValue = context.getValueCompute().complexValueCompute(this.value, context, factMap);
      if (objectValue == null) {
         evaluateResponse.setResult(false);
         evaluateResponse.setRightResult(resultValue);
         return evaluateResponse;
      }

      BigDecimal decimalValue = Utils.toBigDecimal(resultValue);
      BigDecimal decimalValue2 = Utils.toBigDecimal(objectValue);
      boolean flag = false;
      int number = decimalValue.compareTo(decimalValue2);
      if (this.op.equals(Op.Equals)) {
         flag = number == 0;
      } else if (this.op.equals(Op.NotEquals)) {
         flag = number != 0;
      } else if (this.op.equals(Op.LessThen)) {
         flag = number == -1;
      } else if (this.op.equals(Op.LessThenEquals)) {
         flag = number == 0 || number == -1;
      } else if (this.op.equals(Op.GreaterThen)) {
         flag = number == 1;
      } else if (this.op.equals(Op.GreaterThenEquals)) {
         flag = number == 1 || number == 0;
      }

      evaluateResponse.setOp(this.op);
      evaluateResponse.setResult(flag);
      evaluateResponse.setLeftResult(decimalValue);
      evaluateResponse.setRightResult(decimalValue2);
      return evaluateResponse;
   }

   public String getLeft() {
      return this.left;
   }

   public void setLeft(String left) {
      this.left = left;
   }

   public Op getOp() {
      return this.op;
   }

   public void setOp(Op op) {
      this.op = op;
   }

   public Value getValue() {
      return this.value;
   }

   public void setValue(Value value) {
      this.value = value;
   }
}
