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
   private static Log logger = LogFactory.getLog(PageTranService.class);

   public void execute(BatchContext batchContext) {
      Batch batch = batchContext.getBatch();
      BatchResult batchResult = batchContext.getResult();
      boolean flag = this.beforeExecute(batchContext);
      if (flag) {
         try {
            ArrayList items = new ArrayList();
            int number = 0;
            BatchStatus status = batch.getStatus();

            for(int index = 0; index < batchContext.getBatchCount(); ++index) {
               if (this.getBatchStatus(batch.getId()) == BatchStatus.stop) {
                  status = BatchStatus.stop;
                  break;
               }

               PageTranService.logger.debug("execute batch 【" + batch.getName() + "】btachIndex:" + index);
               BatchResult batchResult2 = new BatchResult();
               batchResult2.setStatus(BatchStatus.started);
               this.executeSubBatch(this.getWriteDataSource(batch), batchContext, index, batchResult2);
               items.add(batchResult2);
               number += batchResult2.getExceptions().size();
               if (batch.getSkipLimit() >= 0 && batch.getSkipLimit() < number) {
                  break;
               }
            }

            if (status != BatchStatus.stop) {
               this.mergeBatchResults(batch, batchResult, items);
               this.mergeFilterCounts(batchResult, items);
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

               PageTranService.logger.debug("execute batch 【" + batch.getName() + "】" + batchResult.getStatus() + ":" + batchResult.getMsg());
            } else {
               batchResult.setStatus(BatchStatus.stop);
            }
         } catch (Exception exception) {
            java.util.logging.Logger.getLogger(PageTranService.class.getName()).log(java.util.logging.Level.SEVERE, exception.getMessage(), exception);
            batchResult.setStatus(BatchStatus.failed);
            batchResult.setException(exception);
         } finally {
            this.closeConnection(batchContext.getReadConnection());
         }

      }
   }

   public void executeSubBatch(final DataSource storeDataSource, final BatchContext batchContext, int startBatchIndex, BatchResult batchIndexResult) {
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
               Connection var1x = null;
               Map var2x = null;
               Map valuesByKey = PageTranService.this.prepareItemResult(dataResolver);
               valuesByKey.put(number, valuesByKey);

               try {
                  var1x = storeDataSource.getConnection();
                  PageTranService.logger.debug("execute batch 【" + batch.getName() + "】 pageIndex:" + number + "...");
                  List datas = PageTranService.this.loadDatas(batchContext.getReadConnection(), batchContext, number);
                  valuesByKey2.put(number, datas.size());
                  var1x.setAutoCommit(false);
                  var2x = PageTranService.this.prepareStmt(var1x, dataResolver);

                  for(GeneralEntity var14x : (Iterable<GeneralEntity>)(Iterable<?>)(datas)) {
                     PageTranService.this.processRecord(batchContext, var1x, var2x, var14x, valuesByKey);
                  }

                  PageTranService.this.executePreparedStatementBatches(var2x);
                  var1x.commit();

                  for(BatchItemResult batchItemResult : (Iterable<BatchItemResult>)(Iterable<?>)(valuesByKey.values())) {
                     batchItemResult.setWriteCount(batchItemResult.getReadCount() - batchItemResult.getFilterCount());
                  }
               } catch (Exception var10x) {
                  PageTranService.logger.error(var10x);
                  PageTranService.this.rollbackConnection(var1x);
                  items.add(var10x);

                  for(BatchItemResult batchItemResult2 : (Iterable<BatchItemResult>)(Iterable<?>)(valuesByKey.values())) {
                     batchItemResult2.setWriteCount(0);
                  }
               } finally {
                  PageTranService.this.closePreparedStatements(var2x);
                  PageTranService.this.closeConnection(var1x);
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
         java.util.logging.Logger.getLogger(PageTranService.class.getName()).log(java.util.logging.Level.SEVERE, interruptedException.getMessage(), interruptedException);
         batchIndexResult.setStatus(BatchStatus.failed);
         batchIndexResult.setException(interruptedException);
      }

   }
}
