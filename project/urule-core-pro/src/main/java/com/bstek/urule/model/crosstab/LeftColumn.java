package com.bstek.urule.model.crosstab;

public class LeftColumn extends BundleData implements CrossColumn {
   private int columnNumber;

   @Override
   public int getColumnNumber() {
      return this.columnNumber;
   }

   public void setColumnNumber(int var1) {
      this.columnNumber = var1;
   }

   @Override
   public String getType() {
      return "left";
   }
}
