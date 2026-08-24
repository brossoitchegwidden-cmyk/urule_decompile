package com.bstek.urule.model.library.constant;

public class ConstantData {
   private ConstantCategory category;
   private Constant constant;

   public ConstantData(ConstantCategory category, Constant constant) {
      this.category = category;
      this.constant = constant;
   }

   public ConstantCategory getCategory() {
      return this.category;
   }

   public Constant getConstant() {
      return this.constant;
   }
}
