package com.bstek.urule.console.batch;

public class BatchThread extends Thread {
   private BatchContext batchContext;

   public BatchThread(BatchContext batchContext) {
      this.batchContext = batchContext;
   }

   public void run() {
      BatchRunHelper.run(this.batchContext);
   }
}
