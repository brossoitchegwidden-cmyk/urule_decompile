package com.bstek.urule.model.flow;

public class PercentItem {
   private String name;
   private long percent;
   private long total;
   private DecisionItem item;

   public String getName() {
      return this.name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public long getPercent() {
      return this.percent;
   }

   public void setPercent(long percent) {
      this.percent = percent;
   }

   public long getTotal() {
      return this.total;
   }

   public void setTotal(long total) {
      this.total = total;
   }

   public DecisionItem getItem() {
      return this.item;
   }

   public void setItem(DecisionItem item) {
      this.item = item;
   }
}
