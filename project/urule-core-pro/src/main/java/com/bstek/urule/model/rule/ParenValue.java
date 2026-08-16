package com.bstek.urule.model.rule;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class ParenValue extends AbstractValue {
   private Value value;

   @JsonIgnore
   @Override
   public String getId() {
      String var1 = "(";
      if (this.value != null) {
         var1 = var1 + this.value.getId();
      }

      var1 = var1 + ")";
      if (this.arithmetic != null) {
         var1 = var1 + this.arithmetic.getId();
      }

      return var1;
   }

   @JsonIgnore
   @Override
   public String getValueId() {
      String var1 = "(";
      if (this.value != null) {
         var1 = var1 + this.value.getId();
      }

      return var1 + ")";
   }

   @Override
   public ValueType getValueType() {
      return ValueType.Paren;
   }

   public Value getValue() {
      return this.value;
   }

   public void setValue(Value var1) {
      this.value = var1;
   }
}
