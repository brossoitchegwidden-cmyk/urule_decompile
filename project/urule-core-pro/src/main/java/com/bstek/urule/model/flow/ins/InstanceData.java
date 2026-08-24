package com.bstek.urule.model.flow.ins;

public class InstanceData {
   private BranchCounter branchCounter;
   private int parallelInstanceCount;

   public InstanceData(BranchCounter branchCounter, int parallelInstanceCount) {
      this.branchCounter = branchCounter;
      this.parallelInstanceCount = parallelInstanceCount;
   }

   public int getParallelInstanceCount() {
      return this.parallelInstanceCount;
   }

   public BranchCounter getBranchCounter() {
      return this.branchCounter;
   }
}
