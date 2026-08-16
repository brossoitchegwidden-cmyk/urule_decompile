package com.bstek.urule.console.batch.service;

import com.bstek.urule.console.batch.BatchContext;
import com.bstek.urule.console.database.model.batch.Batch;

public abstract class MultiThreadUtils {
   public static int getThreadSize(int var0, BatchContext var1) {
      int var2 = 0;
      Batch var3 = var1.getBatch();
      int var4 = var1.getPageCount();
      if (var3.getThreadSize() + var3.getThreadSize() * var0 < var4) {
         var2 = var3.getThreadSize();
      } else {
         var2 = var4 - var3.getThreadSize() * var0;
      }

      return var2;
   }
}
