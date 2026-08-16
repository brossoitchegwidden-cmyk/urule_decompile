package com.bstek.urule.console.batch;

public class BatchThread extends Thread {
   private BatchContext a;

   public BatchThread(BatchContext var1) {
      this.a = var1;
   }

   public void run() {
      BatchRunHelper.a(this.a);
   }
}
