package com.bstek.urule.console.batch.service;

import com.bstek.urule.console.batch.BatchContext;
import com.bstek.urule.console.batch.BatchItemResult;
import com.bstek.urule.console.batch.BatchResult;
import com.bstek.urule.console.batch.BatchStatus;
import com.bstek.urule.console.batch.HiveStatement;
import com.bstek.urule.console.database.model.batch.Batch;
import com.bstek.urule.console.database.model.batch.BatchDataResolver;
import com.bstek.urule.console.database.util.JdbcUtils;
import com.bstek.urule.model.GeneralEntity;
import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class HiveBatchTranService extends HiveTranService {
   private static Log a = LogFactory.getLog(HiveBatchTranService.class);

   public void execute(BatchContext var1) {
      Batch var2 = var1.getBatch();
      BatchResult var3 = var1.getResult();
      boolean var4 = this.a(var1);
      if (var4) {
         Connection var5 = null;
         Statement var6 = null;

         try {
            ArrayList var7 = new ArrayList();
            int var8 = 0;
            BatchStatus var9 = var2.getStatus();
            BatchDataResolver var10 = var2.getDataResolver();
            Map var11 = this.a(var10);
            var5 = this.getWriteDataSource(var2).getConnection();
            var6 = var5.createStatement();
            Map var12 = this.b(var10);

            for(int var13 = 0; var13 < var1.getBatchCount(); ++var13) {
               if (this.getBatchStatus(var2.getId()) == BatchStatus.stop) {
                  var9 = BatchStatus.stop;
                  break;
               }

               a.debug("execute batch 【" + var2.getName() + "】batchIndex:" + var13);
               BatchResult var14 = new BatchResult();
               var14.setStatus(BatchStatus.started);
               this.executeSubBatch(var1, var13, var14, var12);
               var7.add(var14);

               try {
                  this.a(var6, var12, var10, var11, false);
               } catch (Exception var21) {
                  a.error(var21);
                  var21.printStackTrace();
                  var3.setException(var21);
                  var14.getExceptions().add(var21);
                  if (var2.getSkipLimit() > 0) {
                     if (var2.getSkipLimit() < var14.getExceptions().size()) {
                        throw var21;
                     }
                     continue;
                  }

                  throw var21;
               }

               var8 += var14.getExceptions().size();
               if (var2.getSkipLimit() >= 0 && var2.getSkipLimit() < var8) {
                  break;
               }
            }

            try {
               this.a(var6, var12, var10, var11, true);
            } catch (Exception var22) {
               a.error(var22);
               var22.printStackTrace();
               var3.setException(var22);
               ++var8;
               if (var2.getSkipLimit() <= 0) {
                  throw var22;
               }

               if (var2.getSkipLimit() < var8) {
                  throw var22;
               }
            }

            if (var9 != BatchStatus.stop) {
               this.a(var2, var3, var7);
               this.a(var3, var7);
               this.b(var2, var3, var11);
               boolean var26 = false;

               for(BatchItemResult var15 : (Iterable<BatchItemResult>)(Iterable<?>)(var3.getItemResults().values())) {
                  if (var15.getReadCount() > 0 && (var15.getWriteCount() > 0 || var15.getFilterCount() == var15.getReadCount())) {
                     var26 = true;
                     break;
                  }
               }

               if (var26) {
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
         } catch (Exception var23) {
            a.error(var23);
            var23.printStackTrace();
            var3.setStatus(BatchStatus.failed);
            var3.setException(var23);
         } finally {
            JdbcUtils.closeStatement(var6);
            this.closeConnection(var5);
            this.closeConnection(var1.getReadConnection());
         }

      }
   }

   public void executeSubBatch(final BatchContext var1, int var2, BatchResult var3, Map var4) {
      final Batch var5 = var1.getBatch();
      int var6 = MultiThreadUtils.getThreadSize(var2, var1);
      Thread[] var7 = new Thread[var6];
      final CountDownLatch var8 = new CountDownLatch(var6);
      final List var9 = Collections.synchronizedList(new ArrayList());
      final ConcurrentHashMap var10 = new ConcurrentHashMap();
      final List var11 = Collections.synchronizedList(new ArrayList());
      final List var12 = Collections.synchronizedList(new ArrayList());
      var3.setExceptions(var11);
      final BatchDataResolver var13 = var5.getDataResolver();

      for(int var14 = 0; var14 < var6; ++var14) {
         final int var15 = var14 + var5.getThreadSize() * var2;
         var7[var14] = new Thread(new Runnable() {
            public void run() {
               BatchResult var1x = new BatchResult();
               var9.add(var1x);
               HashMap var2 = new HashMap();

               try {
                  HiveBatchTranService.a.debug("execute batch 【" + var5.getName() + "】 pageIndex:" + var15 + "...");
                  List var3 = HiveBatchTranService.this.loadDatas(var1.getReadConnection(), var1, var15);
                  var10.put(var15, var3.size());
                  Map var4 = HiveBatchTranService.this.b(var13);
                  var12.add(var4);

                  for(int var5x = 0; var5x < var3.size(); ++var5x) {
                     GeneralEntity var6 = (GeneralEntity)var3.get(var5x);
                     Map var7 = HiveBatchTranService.this.a(var13);
                     var2.put(var5x, var7);

                     try {
                        HiveBatchTranService.this.a(var1, var4, var6, var7);
                     } catch (Exception var13x) {
                        HiveBatchTranService.a.error(var13x);
                        var11.add(var13x);
                        if (var5.getSkipLimit() <= 0) {
                           throw var13x;
                        }

                        if (var5.getSkipLimit() < var11.size()) {
                           throw var13x;
                        }
                     }
                  }
               } catch (Exception var14) {
                  var14.printStackTrace();
               } finally {
                  HiveBatchTranService.this.a(var5, var1x, var2);
               }

               var8.countDown();
            }
         });
         var7[var14].start();
      }

      try {
         var8.await();
         this.a(var5, var3, var9);
         this.a(var3, var10);

         for(Map var23 : (Iterable<Map>)(Iterable<?>)(var12)) {
            for(String var17 : (Iterable<String>)(Iterable<?>)(var4.keySet())) {
               HiveStatement var18 = (HiveStatement)var23.get(var17);
               HiveStatement var19 = (HiveStatement)var4.get(var17);
               List var20 = var19.getBatchSqls();
               var20.addAll(var18.getBatchSqls());
               var19.setBatchSqls(var20);
            }
         }
      } catch (InterruptedException var21) {
         var21.printStackTrace();
         var3.setStatus(BatchStatus.failed);
         var3.setException(var21);
      }

   }
}
