package com.bstek.urule.console.editor.scorecard.complex;

public class CellData {
   private int span;
   private int row;
   private int col;
   private String property;
   private TableHeader header;
   private String content;

   public int getSpan() {
      return this.span;
   }

   public void setSpan(int span) {
      this.span = span;
   }

   public int getRow() {
      return this.row;
   }

   public void setRow(int row) {
      this.row = row;
   }

   public int getCol() {
      return this.col;
   }

   public void setCol(int col) {
      this.col = col;
   }

   public String getProperty() {
      return this.property;
   }

   public void setProperty(String property) {
      this.property = property;
   }

   public TableHeader getHeader() {
      return this.header;
   }

   public void setHeader(TableHeader header) {
      this.header = header;
   }

   public String getContent() {
      return this.content;
   }

   public void setContent(String content) {
      this.content = content;
   }
}
