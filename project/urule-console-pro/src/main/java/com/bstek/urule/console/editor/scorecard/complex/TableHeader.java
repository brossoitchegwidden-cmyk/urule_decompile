package com.bstek.urule.console.editor.scorecard.complex;

public class TableHeader {
   private String name;
   private boolean custom;

   public boolean isScore() {
      return this.name.equals("分值");
   }

   public String getName() {
      return this.name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public boolean isCustom() {
      return this.custom;
   }

   public void setCustom(boolean custom) {
      this.custom = custom;
   }
}
