package com.bstek.urule.console.editor.scorecard.simple;

public class TableHeader {
   private String a;
   private boolean b;
   private boolean c;

   public boolean isScore() {
      return this.a.equals("分值");
   }

   public boolean isCondition() {
      return this.a.equals("条件");
   }

   public String getName() {
      return this.a;
   }

   public void setName(String var1) {
      this.a = var1;
   }

   public boolean isWeightWupport() {
      return this.b;
   }

   public void setWeightWupport(boolean var1) {
      this.b = var1;
   }

   public boolean isCustom() {
      return this.c;
   }

   public void setCustom(boolean var1) {
      this.c = var1;
   }
}
