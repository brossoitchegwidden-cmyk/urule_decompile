package com.bstek.urule.model.rule;

public abstract class AbstractValue implements Value {
   protected ComplexArithmetic arithmetic;

   @Override
   public ComplexArithmetic getArithmetic() {
      return this.arithmetic;
   }

   public void setArithmetic(ComplexArithmetic arithmetic) {
      this.arithmetic = arithmetic;
   }
}
