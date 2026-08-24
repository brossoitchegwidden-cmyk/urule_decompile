package com.bstek.urule.model.scorecard.runtime;

public class CellItem {
   private String colName;
   private Object value;

   public CellItem(String colName, Object value) {
      this.colName = colName;
      this.value = value;
   }

   public String getColName() {
      return this.colName;
   }

   public Object getValue() {
      return this.value;
   }
}
