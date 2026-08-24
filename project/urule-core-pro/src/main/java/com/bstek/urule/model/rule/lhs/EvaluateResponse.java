package com.bstek.urule.model.rule.lhs;

import com.bstek.urule.model.rule.Op;

public class EvaluateResponse {
   private Op op;
   private boolean result;
   private Object leftResult;
   private Object rightResult;

   public Op getOp() {
      return this.op;
   }

   public void setOp(Op op) {
      this.op = op;
   }

   public void setLeftResult(Object leftResult) {
      this.leftResult = leftResult;
   }

   public void setRightResult(Object rightResult) {
      this.rightResult = rightResult;
   }

   public Object getLeftResult() {
      return this.leftResult;
   }

   public Object getRightResult() {
      return this.rightResult;
   }

   public void setResult(boolean result) {
      this.result = result;
   }

   public boolean getResult() {
      return this.result;
   }
}
