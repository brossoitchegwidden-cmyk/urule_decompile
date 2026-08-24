package com.bstek.urule.console.editor.scorecard.simple;

public class TableHeader {
   private String name;
   private boolean weightWupport;
   private boolean custom;

   public boolean isScore() {
      return this.name.equals("分值");
   }

   public boolean isCondition() {
      return this.name.equals("条件");
   }

   public String getName() {
      return this.name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public boolean isWeightWupport() {
      return this.weightWupport;
   }

   public void setWeightWupport(boolean weightWupport) {
      this.weightWupport = weightWupport;
   }

   public boolean isCustom() {
      return this.custom;
   }

   public void setCustom(boolean custom) {
      this.custom = custom;
   }
}
