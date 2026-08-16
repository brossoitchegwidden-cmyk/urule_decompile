package com.bstek.urule.model.rule;

public interface Value {
   ComplexArithmetic getArithmetic();

   ValueType getValueType();

   String getId();

   String getValueId();
}
