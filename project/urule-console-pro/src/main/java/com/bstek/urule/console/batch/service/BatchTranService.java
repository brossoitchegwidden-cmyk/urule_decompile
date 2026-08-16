package com.bstek.urule.console.batch.service;

import com.bstek.urule.console.batch.BatchContext;
import com.bstek.urule.console.batch.BatchItemResult;
import com.bstek.urule.console.batch.BatchResult;
import com.bstek.urule.console.batch.BatchStatus;
import com.bstek.urule.console.database.model.batch.Batch;
import com.bstek.urule.console.database.model.batch.BatchDataResolver;
import com.bstek.urule.model.GeneralEntity;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class BatchTranService extends AbstractBatchTranService {
   private static Log a = LogFactory.getLog(RecordTranService.class);

   public void execute(BatchContext var1) {
      Batch var2 = var1.getBatch();
      BatchResult var3 = var1.getResult();
      boolean var4 = this.a(var1);
      if (var4) {
         Connection var5 = null;
         ArrayList var6 = new ArrayList();

         try {
            var5 = this.getWriteDataSource(var2).getConnection();
            var5.setAutoCommit(false);
            int var7 = 0;
            BatchStatus var17 = var2.getStatus();

            for(int var18 = 0; var18 < var1.getBatchCount(); ++var18) {
               if (this.getBatchStatus(var2.getId()) == BatchStatus.stop) {
                  var17 = BatchStatus.stop;
                  break;
               }

               a.debug("execute batch 【" + var2.getName() + "】batchIndex:" + var18);
               BatchResult var10 = new BatchResult();
               var10.setStatus(BatchStatus.started);
               var6.add(var10);
               this.executeSubBatch(var5, var1, var18, var10);
               var7 += var10.getExceptions().size();
               if (var2.getSkipLimit() >= 0 && var2.getSkipLimit() < var7) {
                  break;
               }

               var10.setStatus(BatchStatus.completed);
            }

            if (var17 != BatchStatus.stop) {
               this.a(var2, var3, var6);
               this.a(var3, var6);
               boolean var19 = true;

               for(BatchItemResult var11 : (Iterable<BatchItemResult>)(Iterable<?>)(var3.getItemResults().values())) {
                  if (var11.getReadCount() != var11.getWriteCount() + var11.getFilterCount()) {
                     var19 = false;
                     break;
                  }
               }

               if (var19) {
                  a.debug("commit batch 【" + var2.getName() + "】.....");
                  var5.commit();
                  a.debug("commit batch 【" + var2.getName() + "】 success");
                  var3.setStatus(BatchStatus.completed);
                  var3.setMsg(BatchStatus.completed.name());
               } else {
                  for(BatchItemResult var22 : (Iterable<BatchItemResult>)(Iterable<?>)(var3.getItemResults().values())) {
                     var22.setWriteCount(0);
                  }

                  if (var5 != null) {
                     this.rollbackConnection(var5);
                  }

                  var3.setStatus(BatchStatus.failed);
                  var3.setMsg(BatchStatus.failed.name());
               }
            } else {
               var3.setStatus(BatchStatus.stop);
               if (var5 != null) {
                  this.rollbackConnection(var5);
               }
            }
         } catch (Exception var15) {
            a.error(var15);
            if (var5 != null) {
               this.rollbackConnection(var5);
            }

            for(BatchItemResult var9 : (Iterable<BatchItemResult>)(Iterable<?>)(var3.getItemResults().values())) {
               var9.setWriteCount(0);
            }

            var3.setStatus(BatchStatus.failed);
            var3.setException(var15);
         } finally {
            this.closeConnection(var1.getReadConnection());
            this.closeConnection(var5);
         }

      }
   }

   public void executeSubBatch(final Connection var1, final BatchContext var2, int var3, BatchResult var4) {
      final Batch var5 = var2.getBatch();
      int var6 = MultiThreadUtils.getThreadSize(var3, var2);
      Thread[] var7 = new Thread[var6];
      final CountDownLatch var8 = new CountDownLatch(var6);
      final ConcurrentHashMap var9 = new ConcurrentHashMap();
      final ConcurrentHashMap var10 = new ConcurrentHashMap();
      final List var11 = Collections.synchronizedList(new ArrayList());
      var4.setExceptions(var11);

      for(int var12 = 0; var12 < var6; ++var12) {
         final BatchDataResolver var14 = var5.getDataResolver();
         final int var15 = var12 + var5.getThreadSize() * var3;
         var7[var12] = new Thread(new Runnable() {
            public void run() {
               Map var1x = null;
               Map var2x = BatchTranService.this.a(var14);
               var9.put(var15, var2x);

               try {
                  BatchTranService.a.debug("execute batch 【" + var5.getName() + "】 pageIndex:" + var15 + "...");
                  List var3 = BatchTranService.this.loadDatas(var2.getReadConnection(), var2, var15);
                  var10.put(var15, var3.size());
                  var1x = BatchTranService.this.a(var1, var14);

                  for(GeneralEntity var13 : (Iterable<GeneralEntity>)(Iterable<?>)(var3)) {
                     BatchTranService.this.a(var2, var1, var1x, var13, var2x);
                  }

                  BatchTranService.this.b(var1x);

                  for(BatchItemResult var14x : (Iterable<BatchItemResult>)(Iterable<?>)(var2x.values())) {
                     var14x.setWriteCount(var14x.getReadCount() - var14x.getFilterCount());
                  }
               } catch (Exception var9x) {
                  BatchTranService.a.error(var9x);
                  var11.add(var9x);

                  for(BatchItemResult var5x : (Iterable<BatchItemResult>)(Iterable<?>)(var2x.values())) {
                     var5x.setWriteCount(0);
                  }
               } finally {
                  BatchTranService.this.a(var1x);
               }

               var8.countDown();
            }
         });
         var7[var12].start();
      }

      try {
         var8.await();
         this.a(var5, var4, var9);
         this.a(var4, var10);
         var4.setStatus(BatchStatus.completed);
      } catch (InterruptedException var16) {
         var16.printStackTrace();
         var4.setStatus(BatchStatus.failed);
         var4.setException(var16);
      }

   }
}
