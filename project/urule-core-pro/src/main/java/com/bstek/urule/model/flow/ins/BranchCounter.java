package com.bstek.urule.model.flow.ins;

public class BranchCounter {
   private int count;

   public synchronized int rise() {
      return ++this.count;
   }

   public synchronized int getCount() {
      return this.count;
   }
}
