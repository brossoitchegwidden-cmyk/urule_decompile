package com.bstek.urule.console.editor.decision.crosstab;

public class CrossRow {
   private int a;
   private Type b;
   private String c;
   private boolean d;

   public int getNumber() {
      return this.a;
   }

   public void setNumber(int var1) {
      this.a = var1;
   }

   public Type getType() {
      return this.b;
   }

   public void setType(Type var1) {
      this.b = var1;
   }

   public String getContent() {
      return this.c;
   }

   public void setContent(String var1) {
      this.c = var1;
   }

   public boolean isPredefine() {
      return this.d;
   }

   public void setPredefine(boolean var1) {
      this.d = var1;
   }
}
