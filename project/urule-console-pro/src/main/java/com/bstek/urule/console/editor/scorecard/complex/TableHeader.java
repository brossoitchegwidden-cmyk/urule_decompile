package com.bstek.urule.console.editor.scorecard.complex;

public class TableHeader {
   private String a;
   private boolean b;

   public boolean isScore() {
      return this.a.equals("分值");
   }

   public String getName() {
      return this.a;
   }

   public void setName(String var1) {
      this.a = var1;
   }

   public boolean isCustom() {
      return this.b;
   }

   public void setCustom(boolean var1) {
      this.b = var1;
   }
}
