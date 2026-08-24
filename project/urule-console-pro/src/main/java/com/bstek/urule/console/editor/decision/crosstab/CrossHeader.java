package com.bstek.urule.console.editor.decision.crosstab;

public class CrossHeader {
   private int rowSpan;
   private int colSpan;
   private String content;

   public int getRowSpan() {
      return this.rowSpan;
   }

   public void setRowSpan(int rowSpan) {
      if (rowSpan == 0) {
         this.rowSpan = 1;
      } else {
         this.rowSpan = rowSpan;
      }

   }

   public int getColSpan() {
      return this.colSpan;
   }

   public void setColSpan(int colSpan) {
      if (colSpan == 0) {
         this.colSpan = 1;
      } else {
         this.colSpan = colSpan;
      }

   }

   public String getContent() {
      return this.content;
   }

   public void setContent(String content) {
      this.content = content;
   }
}
