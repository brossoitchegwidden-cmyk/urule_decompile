package com.bstek.urule.model.rule;

public class SignIValue extends AbstractValue {
   @Override
   public ValueType getValueType() {
      return ValueType.SignI;
   }

   @Override
   public String getId() {
      String id = "[i]";
      if (this.arithmetic != null) {
         id = id + this.arithmetic.getId();
      }

      return id;
   }

   @Override
   public String getValueId() {
      return "[i]";
   }
}
