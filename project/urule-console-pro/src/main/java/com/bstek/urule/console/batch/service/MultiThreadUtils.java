package com.bstek.urule.console.batch.service;

import com.bstek.urule.console.batch.BatchContext;
import com.bstek.urule.console.database.model.batch.Batch;

public abstract class MultiThreadUtils {
   public static int getThreadSize(int startBatchIndex, BatchContext batchContext) {
      int threadSize = 0;
      Batch batch = batchContext.getBatch();
      int pageCount = batchContext.getPageCount();
      if (batch.getThreadSize() + batch.getThreadSize() * startBatchIndex < pageCount) {
         threadSize = batch.getThreadSize();
      } else {
         threadSize = pageCount - batch.getThreadSize() * startBatchIndex;
      }

      return threadSize;
   }
}
