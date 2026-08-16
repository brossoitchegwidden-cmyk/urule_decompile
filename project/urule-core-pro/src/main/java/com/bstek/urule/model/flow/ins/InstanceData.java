package com.bstek.urule.model.flow.ins;

public class InstanceData {
   private BranchCounter branchCounter;
   private int parallelInstanceCount;

   public InstanceData(BranchCounter var1, int var2) {
      this.branchCounter = var1;
      this.parallelInstanceCount = var2;
   }

   public int getParallelInstanceCount() {
      return this.parallelInstanceCount;
   }

   public BranchCounter getBranchCounter() {
      return this.branchCounter;
   }
}
