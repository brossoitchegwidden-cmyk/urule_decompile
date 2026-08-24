package com.bstek.urule.model.table;

public class ScriptCell {
   private int row;
   private int col;
   private int rowspan;
   private String script;

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

   public int getRowspan() {
      return this.rowspan;
   }

   public void setRowspan(int rowspan) {
      this.rowspan = rowspan;
   }

   public String getScript() {
      return this.script;
   }

   public void setScript(String script) {
      this.script = script;
   }
}
