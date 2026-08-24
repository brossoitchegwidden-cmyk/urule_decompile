package com.bstek.urule.model.crosstab;

public class HeaderCell {
   private int rowspan;
   private int colspan;
   private String text;

   public int getRowspan() {
      return this.rowspan;
   }

   public void setRowspan(int rowspan) {
      this.rowspan = rowspan;
   }

   public int getColspan() {
      return this.colspan;
   }

   public void setColspan(int colspan) {
      this.colspan = colspan;
   }

   public String getText() {
      return this.text;
   }

   public void setText(String text) {
      this.text = text;
   }
}
