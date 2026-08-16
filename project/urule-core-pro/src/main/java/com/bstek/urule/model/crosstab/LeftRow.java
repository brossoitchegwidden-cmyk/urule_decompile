package com.bstek.urule.model.crosstab;

public class LeftRow implements CrossRow {
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
      return "left";
   }
}
