package com.bstek.urule.model.rule;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class ComplexArithmetic extends Arithmetic {
   @JsonIgnore
   private String id;
   private Value value;

   public Value getValue() {
      return this.value;
   }

   public void setValue(Value var1) {
      this.value = var1;
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
