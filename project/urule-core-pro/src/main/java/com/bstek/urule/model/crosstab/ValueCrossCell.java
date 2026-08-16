package com.bstek.urule.model.crosstab;

import com.bstek.urule.model.rule.Value;

public class ValueCrossCell extends CrossCell {
   private Value value;

   @Override
   public String getType() {
      return "value";
   }

   public Value getValue() {
      return this.value;
   }

   public void setValue(Value var1) {
      this.value = var1;
   }
}
