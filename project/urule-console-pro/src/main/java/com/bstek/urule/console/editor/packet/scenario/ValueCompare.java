package com.bstek.urule.console.editor.packet.scenario;

public class ValueCompare {
   private boolean a;
   private String b;
   private String c;
   private String d;
   private Object e;
   private Object f;

   public boolean isMatched() {
      return this.a;
   }

   public void setMatched(boolean var1) {
      this.a = var1;
   }

   public String getOp() {
      return this.b;
   }

   public void setOp(String var1) {
      this.b = var1;
   }

   public String getCategory() {
      return this.c;
   }

   public void setCategory(String var1) {
      this.c = var1;
   }

   public String getFieldName() {
      return this.d;
   }

   public void setFieldName(String var1) {
      this.d = var1;
   }

   public Object getData() {
      return this.e;
   }

   public void setData(Object var1) {
      this.e = var1;
   }

   public Object getExpectedData() {
      return this.f;
   }

   public void setExpectedData(Object var1) {
      this.f = var1;
   }
}
