package com.bstek.urule.model.table;

public class Row implements Comparable<Row> {
   private int num;
   private int height;

   public int getNum() {
      return this.num;
   }

   public void setNum(int num) {
      this.num = num;
   }

   public int getHeight() {
      return this.height;
   }

   public void setHeight(int height) {
      this.height = height;
   }

   public int compareTo(Row row) {
      return row.getNum() - this.num;
   }
}
