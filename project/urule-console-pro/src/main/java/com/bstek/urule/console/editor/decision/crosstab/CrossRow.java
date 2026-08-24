package com.bstek.urule.console.editor.decision.crosstab;

public class CrossRow {
   private int rowNumber;
   private Type type;
   private String content;
   private boolean predefine;

   public int getNumber() {
      return this.rowNumber;
   }

   public void setNumber(int number) {
      this.rowNumber = number;
   }

   public Type getType() {
      return this.type;
   }

   public void setType(Type type) {
      this.type = type;
   }

   public String getContent() {
      return this.content;
   }

   public void setContent(String content) {
      this.content = content;
   }

   public boolean isPredefine() {
      return this.predefine;
   }

   public void setPredefine(boolean predefine) {
      this.predefine = predefine;
   }
}
