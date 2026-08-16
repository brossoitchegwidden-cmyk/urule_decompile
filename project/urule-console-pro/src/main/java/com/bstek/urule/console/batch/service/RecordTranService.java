package com.bstek.urule.console.batch.service;

import com.bstek.urule.console.batch.BatchContext;
import com.bstek.urule.console.batch.BatchItemResult;
import com.bstek.urule.console.batch.BatchResult;
import com.bstek.urule.console.batch.BatchStatus;
import com.bstek.urule.console.database.model.batch.Batch;
import com.bstek.urule.console.database.model.batch.BatchDataResolver;
import com.bstek.urule.console.database.model.batch.BatchDataResolverItem;
import com.bstek.urule.model.GeneralEntity;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class RecordTranService extends AbstractBatchTranService {
   private static Log a = LogFactory.getLog(BatchTranService.class);

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

               a.debug("execute batch 【" + var2.getName() + "】batchIndex:" + var8);
               BatchResult var9 = new BatchResult();
               var9.setStatus(BatchStatus.started);
               this.executeSubBatch(var1, var8, var9);
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
            a.error(var14);
            var3.setStatus(BatchStatus.failed);
            var3.setException(var14);
         } finally {
            this.closeConnection(var1.getReadConnection());
         }

      }
   }

   public void executeSubBatch(final BatchContext var1, int var2, BatchResult var3) {
      final Batch var4 = var1.getBatch();
      int var5 = MultiThreadUtils.getThreadSize(var2, var1);
      Thread[] var6 = new Thread[var5];
      final CountDownLatch var7 = new CountDownLatch(var5);
      final List var8 = Collections.synchronizedList(new ArrayList());
      final ConcurrentHashMap var9 = new ConcurrentHashMap();
      final List var10 = Collections.synchronizedList(new ArrayList());
      var3.setExceptions(var10);

      for(int var11 = 0; var11 < var5; ++var11) {
         final int var12 = var11 + var4.getThreadSize() * var2;
         var6[var11] = new Thread(new Runnable() {
            public void run() {
               BatchDataResolver var1x = var4.getDataResolver();
               BatchResult var2 = new BatchResult();
               var8.add(var2);
               HashMap var3 = new HashMap();

               try {
                  RecordTranService.a.debug("execute batch 【" + var4.getName() + "】 pageIndex:" + var12 + "...");
                  List var4x = RecordTranService.this.loadDatas(var1.getReadConnection(), var1, var12);
                  var9.put(var12, var4x.size());

                  for(int var5 = 0; var5 < var4x.size(); ++var5) {
                     GeneralEntity var6 = (GeneralEntity)var4x.get(var5);
                     Map var7x = null;
                     Map var8x = RecordTranService.this.a(var1x);
                     var3.put(var5, var8x);
                     Connection var9x = null;

                     try {
                        var9x = RecordTranService.this.getWriteDataSource(var4).getConnection();
                        if (var1x.getDialect().supportTransaction()) {
                           var9x.setAutoCommit(false);
                        }

                        var7x = RecordTranService.this.a(var9x, var1x);
                        RecordTranService.this.a(var1, var9x, var7x, var6, var8x);
                        if (var1x.getDialect().supportTransaction()) {
                           RecordTranService.this.b(var7x);
                           var9x.commit();
                        }

                        for(BatchItemResult var27 : (Iterable<BatchItemResult>)(Iterable<?>)(var8x.values())) {
                           var27.setWriteCount(var27.getReadCount() - var27.getFilterCount());
                        }
                     } catch (Exception var23) {
                        RecordTranService.a.error(var23);

                        for(BatchItemResult var12x : (Iterable<BatchItemResult>)(Iterable<?>)(var8x.values())) {
                           var12x.setWriteCount(0);
                        }

                        if (var1x.getDialect().supportTransaction()) {
                           RecordTranService.this.rollbackConnection(var9x);
                        }

                        var10.add(var23);
                        if (var4.getSkipLimit() <= 0) {
                           throw var23;
                        }

                        if (var4.getSkipLimit() < var10.size()) {
                           throw var23;
                        }
                     } finally {
                        RecordTranService.this.a(var7x);
                        RecordTranService.this.closeConnection(var9x);
                     }
                  }
               } catch (Exception var25) {
                  var25.printStackTrace();
               } finally {
                  RecordTranService.this.a(var4, var2, var3);
               }

               var7.countDown();
            }
         });
         var6[var11].start();
      }

      try {
         var7.await();
         this.a(var4, var3, var8);
         this.a(var3, var9);
      } catch (InterruptedException var13) {
         var13.printStackTrace();
         var3.setStatus(BatchStatus.failed);
         var3.setException(var13);
      }

   }

   protected Map a(Connection var1, BatchDataResolver var2) throws SQLException {
      HashMap var3 = new HashMap();

      for(BatchDataResolverItem var5 : (Iterable<BatchDataResolverItem>)(Iterable<?>)(var2.getItems())) {
         PreparedStatement var6 = var1.prepareStatement(var5.getUpdateSql());
         var3.put(var5.getName(), var6);
      }

      return var3;
   }
}
