package com.bstek.urule.model.rule;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class ParenValue extends AbstractValue {
   private Value value;

   @JsonIgnore
   @Override
   public String getId() {
      String id = "(";
      if (this.value != null) {
         id = id + this.value.getId();
      }

      id = id + ")";
      if (this.arithmetic != null) {
         id = id + this.arithmetic.getId();
      }

      return id;
   }

   @JsonIgnore
   @Override
   public String getValueId() {
      String text = "(";
      if (this.value != null) {
         text = text + this.value.getId();
      }

      return text + ")";
   }

   @Override
   public ValueType getValueType() {
      return ValueType.Paren;
   }

   public Value getValue() {
      return this.value;
   }

   public void setValue(Value value) {
      this.value = value;
   }
}
