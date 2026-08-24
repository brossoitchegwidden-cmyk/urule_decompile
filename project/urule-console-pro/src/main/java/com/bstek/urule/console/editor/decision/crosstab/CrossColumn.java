package com.bstek.urule.console.editor.decision.crosstab;

public class CrossColumn {
   private int columnNumber;
   private Type type;
   private String content;
   private boolean predefine;

   public int getNumber() {
      return this.columnNumber;
   }

   public void setNumber(int number) {
      this.columnNumber = number;
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
