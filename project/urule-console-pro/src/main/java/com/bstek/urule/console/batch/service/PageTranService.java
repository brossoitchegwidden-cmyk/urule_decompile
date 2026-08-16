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
import javax.sql.DataSource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class PageTranService extends AbstractBatchTranService {
   private static Log a = LogFactory.getLog(PageTranService.class);

   public void execute(BatchContext var1) {
      Batch var2 = var1.getBatch();
      BatchResult var3 = var1.getResult();
      boolean var4 = this.a(var1);
      if (var4) {
         try {
            ArrayList var5 = new ArrayList();
            int var6 = 0;
            BatchStatus var7 = var2.getStatus();

            for(int var8 = 0; var8 < var1.getBatchCount(); ++var8) {
               if (this.getBatchStatus(var2.getId()) == BatchStatus.stop) {
                  var7 = BatchStatus.stop;
                  break;
               }

               a.debug("execute batch 【" + var2.getName() + "】btachIndex:" + var8);
               BatchResult var9 = new BatchResult();
               var9.setStatus(BatchStatus.started);
               this.executeSubBatch(this.getWriteDataSource(var2), var1, var8, var9);
               var5.add(var9);
               var6 += var9.getExceptions().size();
               if (var2.getSkipLimit() >= 0 && var2.getSkipLimit() < var6) {
                  break;
               }
            }

            if (var7 != BatchStatus.stop) {
               this.a(var2, var3, var5);
               this.a(var3, var5);
               boolean var16 = false;

               for(BatchItemResult var10 : (Iterable<BatchItemResult>)(Iterable<?>)(var3.getItemResults().values())) {
                  if (var10.getReadCount() > 0 && (var10.getWriteCount() > 0 || var10.getFilterCount() == var10.getReadCount())) {
                     var16 = true;
                     break;
                  }
               }

               if (var16) {
                  var3.setStatus(BatchStatus.completed);
                  var3.setMsg(BatchStatus.completed.name());
               } else {
                  var3.setStatus(BatchStatus.failed);
                  var3.setMsg(BatchStatus.failed.name());
               }

               a.debug("execute batch 【" + var2.getName() + "】" + var3.getStatus() + ":" + var3.getMsg());
            } else {
               var3.setStatus(BatchStatus.stop);
            }
         } catch (Exception var14) {
            var14.printStackTrace();
            var3.setStatus(BatchStatus.failed);
            var3.setException(var14);
         } finally {
            this.closeConnection(var1.getReadConnection());
         }

      }
   }

   public void executeSubBatch(final DataSource var1, final BatchContext var2, int var3, BatchResult var4) {
      final Batch var5 = var2.getBatch();
      int var6 = MultiThreadUtils.getThreadSize(var3, var2);
      Thread[] var7 = new Thread[var6];
      final CountDownLatch var8 = new CountDownLatch(var6);
      final ConcurrentHashMap var9 = new ConcurrentHashMap();
      final ConcurrentHashMap var10 = new ConcurrentHashMap();
      final List var11 = Collections.synchronizedList(new ArrayList());
      var4.setExceptions(var11);

      for(int var12 = 0; var12 < var6; ++var12) {
         final BatchDataResolver var13 = var5.getDataResolver();
         final int var14 = var12 + var5.getThreadSize() * var3;
         var7[var12] = new Thread(new Runnable() {
            public void run() {
               Connection var1x = null;
               Map var2x = null;
               Map var3 = PageTranService.this.a(var13);
               var9.put(var14, var3);

               try {
                  var1x = var1.getConnection();
                  PageTranService.a.debug("execute batch 【" + var5.getName() + "】 pageIndex:" + var14 + "...");
                  List var4 = PageTranService.this.loadDatas(var2.getReadConnection(), var2, var14);
                  var10.put(var14, var4.size());
                  var1x.setAutoCommit(false);
                  var2x = PageTranService.this.a(var1x, var13);

                  for(GeneralEntity var14x : (Iterable<GeneralEntity>)(Iterable<?>)(var4)) {
                     PageTranService.this.a(var2, var1x, var2x, var14x, var3);
                  }

                  PageTranService.this.b(var2x);
                  var1x.commit();

                  for(BatchItemResult var15 : (Iterable<BatchItemResult>)(Iterable<?>)(var3.values())) {
                     var15.setWriteCount(var15.getReadCount() - var15.getFilterCount());
                  }
               } catch (Exception var10x) {
                  PageTranService.a.error(var10x);
                  PageTranService.this.rollbackConnection(var1x);
                  var11.add(var10x);

                  for(BatchItemResult var6 : (Iterable<BatchItemResult>)(Iterable<?>)(var3.values())) {
                     var6.setWriteCount(0);
                  }
               } finally {
                  PageTranService.this.a(var2x);
                  PageTranService.this.closeConnection(var1x);
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
      } catch (InterruptedException var15) {
         var15.printStackTrace();
         var4.setStatus(BatchStatus.failed);
         var4.setException(var15);
      }

   }
}
