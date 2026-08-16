package com.bstek.urule.model.library.constant;

public class ConstantData {
   private ConstantCategory category;
   private Constant constant;

   public ConstantData(ConstantCategory var1, Constant var2) {
      this.category = var1;
      this.constant = var2;
   }

   public ConstantCategory getCategory() {
      return this.category;
   }

   public Constant getConstant() {
      return this.constant;
   }
}
