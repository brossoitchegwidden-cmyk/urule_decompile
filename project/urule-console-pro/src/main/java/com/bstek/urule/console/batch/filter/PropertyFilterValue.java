package com.bstek.urule.console.batch.filter;

public class PropertyFilterValue {
   private String a;
   private String b;
   private Object c;

   public String getOp() {
      return this.a;
   }

   public void setOp(String var1) {
      this.a = var1;
   }

   public String getValue() {
      return this.b;
   }

   public void setValue(String var1) {
      this.b = var1;
   }

   public Object getObjectValue() {
      return this.c;
   }

   public void setObjectValue(Object var1) {
      this.c = var1;
   }
}
