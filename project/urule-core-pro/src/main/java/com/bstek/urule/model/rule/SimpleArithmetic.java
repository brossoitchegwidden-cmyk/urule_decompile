package com.bstek.urule.model.rule;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class SimpleArithmetic extends Arithmetic {
   @JsonIgnore
   private String id;
   private SimpleArithmeticValue value;

   public SimpleArithmeticValue getValue() {
      return this.value;
   }

   public void setValue(SimpleArithmeticValue value) {
      this.value = value;
   }

   @Override
   public String getId() {
      if (this.id != null) {
         return this.id;
      }

      this.id = this.type.toString() + this.value.getId();
      return this.id;
   }
}
