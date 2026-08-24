package com.bstek.urule.model.rule;

public abstract class Arithmetic {
   protected ArithmeticType type;

   public ArithmeticType getType() {
      return this.type;
   }

   public void setType(ArithmeticType type) {
      this.type = type;
   }

   public abstract String getId();
}
