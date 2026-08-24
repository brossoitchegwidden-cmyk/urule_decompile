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
   private static Log logger = LogFactory.getLog(RecordTranService.class);

   public void execute(BatchContext batchContext) {
      Batch batch = batchContext.getBatch();
      BatchResult batchResult = batchContext.getResult();
      boolean flag = this.beforeExecute(batchContext);
      if (flag) {
         Connection connection = null;
         ArrayList items = new ArrayList();

         try {
            connection = this.getWriteDataSource(batch).getConnection();
            connection.setAutoCommit(false);
            int number = 0;
            BatchStatus status = batch.getStatus();

            for(int index = 0; index < batchContext.getBatchCount(); ++index) {
               if (this.getBatchStatus(batch.getId()) == BatchStatus.stop) {
                  status = BatchStatus.stop;
                  break;
               }

               BatchTranService.logger.debug("execute batch 【" + batch.getName() + "】batchIndex:" + index);
               BatchResult batchResult2 = new BatchResult();
               batchResult2.setStatus(BatchStatus.started);
               items.add(batchResult2);
               this.executeSubBatch(connection, batchContext, index, batchResult2);
               number += batchResult2.getExceptions().size();
               if (batch.getSkipLimit() >= 0 && batch.getSkipLimit() < number) {
                  break;
               }

               batchResult2.setStatus(BatchStatus.completed);
            }

            if (status != BatchStatus.stop) {
               this.mergeBatchResults(batch, batchResult, items);
               this.mergeFilterCounts(batchResult, items);
               boolean flag2 = true;

               for(BatchItemResult batchItemResult : (Iterable<BatchItemResult>)(Iterable<?>)(batchResult.getItemResults().values())) {
                  if (batchItemResult.getReadCount() != batchItemResult.getWriteCount() + batchItemResult.getFilterCount()) {
                     flag2 = false;
                     break;
                  }
               }

               if (flag2) {
                  BatchTranService.logger.debug("commit batch 【" + batch.getName() + "】.....");
                  connection.commit();
                  BatchTranService.logger.debug("commit batch 【" + batch.getName() + "】 success");
                  batchResult.setStatus(BatchStatus.completed);
                  batchResult.setMsg(BatchStatus.completed.name());
               } else {
                  for(BatchItemResult batchItemResult2 : (Iterable<BatchItemResult>)(Iterable<?>)(batchResult.getItemResults().values())) {
                     batchItemResult2.setWriteCount(0);
                  }

                  if (connection != null) {
                     this.rollbackConnection(connection);
                  }

                  batchResult.setStatus(BatchStatus.failed);
                  batchResult.setMsg(BatchStatus.failed.name());
               }
            } else {
               batchResult.setStatus(BatchStatus.stop);
               if (connection != null) {
                  this.rollbackConnection(connection);
               }
            }
         } catch (Exception exception) {
            BatchTranService.logger.error(exception);
            if (connection != null) {
               this.rollbackConnection(connection);
            }

            for(BatchItemResult batchItemResult3 : (Iterable<BatchItemResult>)(Iterable<?>)(batchResult.getItemResults().values())) {
               batchItemResult3.setWriteCount(0);
            }

            batchResult.setStatus(BatchStatus.failed);
            batchResult.setException(exception);
         } finally {
            this.closeConnection(batchContext.getReadConnection());
            this.closeConnection(connection);
         }

      }
   }

   public void executeSubBatch(final Connection writeConn, final BatchContext batchContext, int startBatchIndex, BatchResult batchIndexResult) {
      final Batch batch = batchContext.getBatch();
      int threadSize = MultiThreadUtils.getThreadSize(startBatchIndex, batchContext);
      Thread[] thread = new Thread[threadSize];
      final CountDownLatch countDownLatch = new CountDownLatch(threadSize);
      final ConcurrentHashMap valuesByKey = new ConcurrentHashMap();
      final ConcurrentHashMap valuesByKey2 = new ConcurrentHashMap();
      final List items = Collections.synchronizedList(new ArrayList());
      batchIndexResult.setExceptions(items);

      for(int index = 0; index < threadSize; ++index) {
         final BatchDataResolver dataResolver = batch.getDataResolver();
         final int number = index + batch.getThreadSize() * startBatchIndex;
         thread[index] = new Thread(new Runnable() {
            public void run() {
               Map var1x = null;
               Map var2x = BatchTranService.this.prepareItemResult(dataResolver);
               valuesByKey.put(number, var2x);

               try {
                  BatchTranService.logger.debug("execute batch 【" + batch.getName() + "】 pageIndex:" + number + "...");
                  List datas = BatchTranService.this.loadDatas(batchContext.getReadConnection(), batchContext, number);
                  valuesByKey2.put(number, datas.size());
                  var1x = BatchTranService.this.prepareStmt(writeConn, dataResolver);

                  for(GeneralEntity generalEntity : (Iterable<GeneralEntity>)(Iterable<?>)(datas)) {
                     BatchTranService.this.processRecord(batchContext, writeConn, var1x, generalEntity, var2x);
                  }

                  BatchTranService.this.executePreparedStatementBatches(var1x);

                  for(BatchItemResult var14x : (Iterable<BatchItemResult>)(Iterable<?>)(var2x.values())) {
                     var14x.setWriteCount(var14x.getReadCount() - var14x.getFilterCount());
                  }
               } catch (Exception var9x) {
                  BatchTranService.logger.error(var9x);
                  items.add(var9x);

                  for(BatchItemResult var5x : (Iterable<BatchItemResult>)(Iterable<?>)(var2x.values())) {
                     var5x.setWriteCount(0);
                  }
               } finally {
                  BatchTranService.this.closePreparedStatements(var1x);
               }

               countDownLatch.countDown();
            }
         });
         thread[index].start();
      }

      try {
         countDownLatch.await();
         this.mergeItemResults(batch, batchIndexResult, valuesByKey);
         this.sumResultCounts(batchIndexResult, valuesByKey2);
         batchIndexResult.setStatus(BatchStatus.completed);
      } catch (InterruptedException interruptedException) {
         Thread.currentThread().interrupt();
         java.util.logging.Logger.getLogger(BatchTranService.class.getName()).log(java.util.logging.Level.SEVERE, interruptedException.getMessage(), interruptedException);
         batchIndexResult.setStatus(BatchStatus.failed);
         batchIndexResult.setException(interruptedException);
      }

   }
}
