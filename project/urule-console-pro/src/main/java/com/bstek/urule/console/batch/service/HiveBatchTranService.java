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
   private static Log logger = LogFactory.getLog(HiveBatchTranService.class);

   public void execute(BatchContext batchContext) {
      Batch batch = batchContext.getBatch();
      BatchResult batchResult = batchContext.getResult();
      boolean flag = this.beforeExecute(batchContext);
      if (flag) {
         Connection connection = null;
         Statement statement = null;

         try {
            ArrayList items = new ArrayList();
            int number = 0;
            BatchStatus status = batch.getStatus();
            BatchDataResolver dataResolver = batch.getDataResolver();
            Map valuesByKey = this.prepareItemResult(dataResolver);
            connection = this.getWriteDataSource(batch).getConnection();
            statement = connection.createStatement();
            Map valuesByKey2 = this.prepareStmts(dataResolver);

            for(int index = 0; index < batchContext.getBatchCount(); ++index) {
               if (this.getBatchStatus(batch.getId()) == BatchStatus.stop) {
                  status = BatchStatus.stop;
                  break;
               }

               HiveBatchTranService.logger.debug("execute batch 【" + batch.getName() + "】batchIndex:" + index);
               BatchResult batchResult2 = new BatchResult();
               batchResult2.setStatus(BatchStatus.started);
               this.executeSubBatch(batchContext, index, batchResult2, valuesByKey2);
               items.add(batchResult2);

               try {
                  this.executePendingStatements(statement, valuesByKey2, dataResolver, valuesByKey, false);
               } catch (Exception exception) {
                  HiveBatchTranService.logger.error(exception);
                  java.util.logging.Logger.getLogger(HiveBatchTranService.class.getName()).log(java.util.logging.Level.SEVERE, exception.getMessage(), exception);
                  batchResult.setException(exception);
                  batchResult2.getExceptions().add(exception);
                  if (batch.getSkipLimit() > 0) {
                     if (batch.getSkipLimit() < batchResult2.getExceptions().size()) {
                        throw exception;
                     }
                     continue;
                  }

                  throw exception;
               }

               number += batchResult2.getExceptions().size();
               if (batch.getSkipLimit() >= 0 && batch.getSkipLimit() < number) {
                  break;
               }
            }

            try {
               this.executePendingStatements(statement, valuesByKey2, dataResolver, valuesByKey, true);
            } catch (Exception exception2) {
               HiveBatchTranService.logger.error(exception2);
               java.util.logging.Logger.getLogger(HiveBatchTranService.class.getName()).log(java.util.logging.Level.SEVERE, exception2.getMessage(), exception2);
               batchResult.setException(exception2);
               ++number;
               if (batch.getSkipLimit() <= 0) {
                  throw exception2;
               }

               if (batch.getSkipLimit() < number) {
                  throw exception2;
               }
            }

            if (status != BatchStatus.stop) {
               this.mergeBatchResults(batch, batchResult, items);
               this.mergeFilterCounts(batchResult, items);
               this.applyWriteCounts(batch, batchResult, valuesByKey);
               boolean flag2 = false;

               for(BatchItemResult batchItemResult : (Iterable<BatchItemResult>)(Iterable<?>)(batchResult.getItemResults().values())) {
                  if (batchItemResult.getReadCount() > 0 && (batchItemResult.getWriteCount() > 0 || batchItemResult.getFilterCount() == batchItemResult.getReadCount())) {
                     flag2 = true;
                     break;
                  }
               }

               if (flag2) {
                  batchResult.setStatus(BatchStatus.completed);
                  batchResult.setMsg(BatchStatus.completed.name());
               } else {
                  batchResult.setStatus(BatchStatus.failed);
                  batchResult.setMsg(BatchStatus.failed.name());
               }

               HiveBatchTranService.logger.debug("execute batch 【" + batch.getName() + "】" + batchResult.getStatus() + ":" + batchResult.getMsg());
            } else {
               batchResult.setStatus(BatchStatus.stop);
            }
         } catch (Exception exception3) {
            HiveBatchTranService.logger.error(exception3);
            java.util.logging.Logger.getLogger(HiveBatchTranService.class.getName()).log(java.util.logging.Level.SEVERE, exception3.getMessage(), exception3);
            batchResult.setStatus(BatchStatus.failed);
            batchResult.setException(exception3);
         } finally {
            JdbcUtils.closeStatement(statement);
            this.closeConnection(connection);
            this.closeConnection(batchContext.getReadConnection());
         }

      }
   }

   public void executeSubBatch(final BatchContext batchContext, int startBatchIndex, BatchResult batchIndexResult, Map batchStmtMap) {
      final Batch batch = batchContext.getBatch();
      int threadSize = MultiThreadUtils.getThreadSize(startBatchIndex, batchContext);
      Thread[] thread = new Thread[threadSize];
      final CountDownLatch countDownLatch = new CountDownLatch(threadSize);
      final List items = Collections.synchronizedList(new ArrayList());
      final ConcurrentHashMap valuesByKey = new ConcurrentHashMap();
      final List items2 = Collections.synchronizedList(new ArrayList());
      final List items3 = Collections.synchronizedList(new ArrayList());
      batchIndexResult.setExceptions(items2);
      final BatchDataResolver dataResolver = batch.getDataResolver();

      for(int threadIndex = 0; threadIndex < threadSize; ++threadIndex) {
         final int number = threadIndex + batch.getThreadSize() * startBatchIndex;
         thread[threadIndex] = new Thread(new Runnable() {
            public void run() {
               BatchResult var1x = new BatchResult();
               items.add(var1x);
               HashMap valuesByKey = new HashMap();

               try {
                  HiveBatchTranService.logger.debug("execute batch 【" + batch.getName() + "】 pageIndex:" + number + "...");
                  List datas = HiveBatchTranService.this.loadDatas(batchContext.getReadConnection(), batchContext, number);
                  valuesByKey.put(number, datas.size());
                  Map valuesByKey2 = HiveBatchTranService.this.prepareStmts(dataResolver);
                  items3.add(valuesByKey2);

                  for(int var5x = 0; var5x < datas.size(); ++var5x) {
                     GeneralEntity generalEntity = (GeneralEntity)datas.get(var5x);
                     Map valuesByKey3 = HiveBatchTranService.this.prepareItemResult(dataResolver);
                     valuesByKey.put(var5x, valuesByKey3);

                     try {
                        HiveBatchTranService.this.processRecord(batchContext, valuesByKey2, generalEntity, valuesByKey3);
                     } catch (Exception var13x) {
                        HiveBatchTranService.logger.error(var13x);
                        items2.add(var13x);
                        if (batch.getSkipLimit() <= 0) {
                           throw var13x;
                        }

                        if (batch.getSkipLimit() < items2.size()) {
                           throw var13x;
                        }
                     }
                  }
               } catch (Exception exception) {
                  java.util.logging.Logger.getLogger(HiveBatchTranService.class.getName()).log(java.util.logging.Level.SEVERE, exception.getMessage(), exception);
               } finally {
                  HiveBatchTranService.this.mergeItemResults(batch, var1x, valuesByKey);
               }

               countDownLatch.countDown();
            }
         });
         thread[threadIndex].start();
      }

      try {
         countDownLatch.await();
         this.mergeBatchResults(batch, batchIndexResult, items);
         this.sumResultCounts(batchIndexResult, valuesByKey);

         for(Map valuesByKey2 : (Iterable<Map>)(Iterable<?>)(items3)) {
            for(String text : (Iterable<String>)(Iterable<?>)(batchStmtMap.keySet())) {
               HiveStatement hiveStatement = (HiveStatement)valuesByKey2.get(text);
               HiveStatement hiveStatement2 = (HiveStatement)batchStmtMap.get(text);
               List batchSqls = hiveStatement2.getBatchSqls();
               batchSqls.addAll(hiveStatement.getBatchSqls());
               hiveStatement2.setBatchSqls(batchSqls);
            }
         }
      } catch (InterruptedException interruptedException) {
         Thread.currentThread().interrupt();
         java.util.logging.Logger.getLogger(HiveBatchTranService.class.getName()).log(java.util.logging.Level.SEVERE, interruptedException.getMessage(), interruptedException);
         batchIndexResult.setStatus(BatchStatus.failed);
         batchIndexResult.setException(interruptedException);
      }

   }
}
