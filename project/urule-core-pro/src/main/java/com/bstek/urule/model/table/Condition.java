package com.bstek.urule.model.table;

import com.bstek.urule.model.rule.Op;
import com.bstek.urule.model.rule.Value;

public class Condition {
   private Op op;
   private Value value;

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
