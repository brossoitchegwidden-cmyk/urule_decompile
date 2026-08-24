package com.bstek.urule.model.crosstab;

public class TopColumn implements CrossColumn {
   private int columnNumber;

   @Override
   public int getColumnNumber() {
      return this.columnNumber;
   }

   public void setColumnNumber(int columnNumber) {
      this.columnNumber = columnNumber;
   }

   @Override
   public String getType() {
      return "top";
   }
}
