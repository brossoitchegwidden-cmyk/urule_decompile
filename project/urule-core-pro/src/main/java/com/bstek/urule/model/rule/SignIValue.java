package com.bstek.urule.model.rule;

public class SignIValue extends AbstractValue {
   @Override
   public ValueType getValueType() {
      return ValueType.SignI;
   }

   @Override
   public String getId() {
      String var1 = "[i]";
      if (this.arithmetic != null) {
         var1 = var1 + this.arithmetic.getId();
      }

      return var1;
   }

   @Override
   public String getValueId() {
      return "[i]";
   }
}
