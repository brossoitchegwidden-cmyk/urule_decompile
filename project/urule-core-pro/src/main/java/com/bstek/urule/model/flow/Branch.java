package com.bstek.urule.model.flow;

public class Branch {
   private String name;
   private int percent;
   private long total;

   public String getName() {
      return this.name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public int getPercent() {
      return this.percent;
   }

   public void setPercent(int percent) {
      this.percent = percent;
   }

   public long getTotal() {
      return this.total;
   }

   public void setTotal(long total) {
      this.total = total;
   }
}
