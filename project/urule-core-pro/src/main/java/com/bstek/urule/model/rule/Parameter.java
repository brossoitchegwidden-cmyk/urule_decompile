package com.bstek.urule.model.rule;

import com.bstek.urule.exception.RuleException;
import com.bstek.urule.model.library.Datatype;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class Parameter {
   @JsonIgnore
   private String id;
   private String name;
   private String ename;
   private Datatype type;
   private Value value;

   public String getName() {
      return this.name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public String getEname() {
      return this.ename;
   }

   public void setEname(String ename) {
      this.ename = ename;
   }

   public Datatype getType() {
      return this.type;
   }

   public void setType(Datatype type) {
      this.type = type;
   }

   public Value getValue() {
      return this.value;
   }

   public void setValue(Value value) {
      this.value = value;
   }

   public String getId() {
      if (this.id == null) {
         if (this.value == null) {
            throw new RuleException("Parameter [" + this.name + "] not assignment value.");
         }

         this.id = this.value.getId();
      }

      return this.id;
   }
}
