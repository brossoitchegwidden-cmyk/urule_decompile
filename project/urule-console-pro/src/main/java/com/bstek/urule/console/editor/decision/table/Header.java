package com.bstek.urule.console.editor.decision.table;

public class Header {
   private String a;
   private HeaderType b;
   private boolean c;

   public Header() {
      this.b = HeaderType.condition;
   }

   public String getName() {
      return this.a;
   }

   public void setName(String var1) {
      this.a = var1;
   }

   public HeaderType getType() {
      return this.b;
   }

   public void setType(HeaderType var1) {
      this.b = var1;
   }

   public boolean isPredefine() {
      return this.c;
   }

   public void setPredefine(boolean var1) {
      this.c = var1;
   }
}
