package com.bstek.urule.model.rule;

public class SimpleArithmeticValue {
   private String content;
   private SimpleArithmetic arithmetic;

   public String getContent() {
      return this.content;
   }

   public void setContent(String content) {
      this.content = content;
   }

   public SimpleArithmetic getArithmetic() {
      return this.arithmetic;
   }

   public void setArithmetic(SimpleArithmetic arithmetic) {
      this.arithmetic = arithmetic;
   }

   public String getId() {
      String id = this.content;
      if (this.arithmetic != null) {
         id = id + this.arithmetic.getId();
      }

      return id;
   }
}
