package com.bstek.urule.model.crosstab;

public class TopRow extends BundleData implements CrossRow {
   private int rowNumber;

   @Override
   public int getRowNumber() {
      return this.rowNumber;
   }

   public void setRowNumber(int var1) {
      this.rowNumber = var1;
   }

   @Override
   public String getType() {
      return "top";
   }
}
