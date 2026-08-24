package com.bstek.urule.console.editor.decision.table;

public class Header {
   private String name;
   private HeaderType type;
   private boolean predefine;

   public Header() {
      this.type = HeaderType.condition;
   }

   public String getName() {
      return this.name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public HeaderType getType() {
      return this.type;
   }

   public void setType(HeaderType type) {
      this.type = type;
   }

   public boolean isPredefine() {
      return this.predefine;
   }

   public void setPredefine(boolean predefine) {
      this.predefine = predefine;
   }
}
