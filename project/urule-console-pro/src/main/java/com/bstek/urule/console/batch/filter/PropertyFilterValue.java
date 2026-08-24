package com.bstek.urule.console.batch.filter;

public class PropertyFilterValue {
   private String op;
   private String value;
   private Object objectValue;

   public String getOp() {
      return this.op;
   }

   public void setOp(String op) {
      this.op = op;
   }

   public String getValue() {
      return this.value;
   }

   public void setValue(String value) {
      this.value = value;
   }

   public Object getObjectValue() {
      return this.objectValue;
   }

   public void setObjectValue(Object objectValue) {
      this.objectValue = objectValue;
   }
}
