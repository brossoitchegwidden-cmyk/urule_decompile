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
   private static Log logger = LogFactory.getLog(BatchTranService.class);

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

               RecordTranService.logger.debug("execute batch 【" + batch.getName() + "】batchIndex:" + index);
               BatchResult batchResult2 = new BatchResult();
               batchResult2.setStatus(BatchStatus.started);
               this.executeSubBatch(batchContext, index, batchResult2);
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

               RecordTranService.logger.debug("execute batch 【" + batch.getName() + "】" + batchResult.getStatus() + ":" + batchResult.getMsg());
            } else {
               batchResult.setStatus(BatchStatus.stop);
            }
         } catch (Exception exception) {
            RecordTranService.logger.error(exception);
            batchResult.setStatus(BatchStatus.failed);
            batchResult.setException(exception);
         } finally {
            this.closeConnection(batchContext.getReadConnection());
         }

      }
   }

   public void executeSubBatch(final BatchContext batchContext, int startBatchIndex, BatchResult batchIndexResult) {
      final Batch batch = batchContext.getBatch();
      int threadSize = MultiThreadUtils.getThreadSize(startBatchIndex, batchContext);
      Thread[] thread = new Thread[threadSize];
      final CountDownLatch countDownLatch = new CountDownLatch(threadSize);
      final List items = Collections.synchronizedList(new ArrayList());
      final ConcurrentHashMap valuesByKey = new ConcurrentHashMap();
      final List items2 = Collections.synchronizedList(new ArrayList());
      batchIndexResult.setExceptions(items2);

      for(int index = 0; index < threadSize; ++index) {
         final int number = index + batch.getThreadSize() * startBatchIndex;
         thread[index] = new Thread(new Runnable() {
            public void run() {
               BatchDataResolver var1x = batch.getDataResolver();
               BatchResult batchResult = new BatchResult();
               items.add(batchResult);
               HashMap valuesByKey = new HashMap();

               try {
                  RecordTranService.logger.debug("execute batch 【" + batch.getName() + "】 pageIndex:" + number + "...");
                  List var4x = RecordTranService.this.loadDatas(batchContext.getReadConnection(), batchContext, number);
                  valuesByKey.put(number, var4x.size());

                  for(int index = 0; index < var4x.size(); ++index) {
                     GeneralEntity generalEntity = (GeneralEntity)var4x.get(index);
                     Map var7x = null;
                     Map var8x = RecordTranService.this.prepareItemResult(var1x);
                     valuesByKey.put(index, var8x);
                     Connection var9x = null;

                     try {
                        var9x = RecordTranService.this.getWriteDataSource(batch).getConnection();
                        if (var1x.getDialect().supportTransaction()) {
                           var9x.setAutoCommit(false);
                        }

                        var7x = RecordTranService.this.prepareStmt(var9x, var1x);
                        RecordTranService.this.processRecord(batchContext, var9x, var7x, generalEntity, var8x);
                        if (var1x.getDialect().supportTransaction()) {
                           RecordTranService.this.executePreparedStatementBatches(var7x);
                           var9x.commit();
                        }

                        for(BatchItemResult batchItemResult : (Iterable<BatchItemResult>)(Iterable<?>)(var8x.values())) {
                           batchItemResult.setWriteCount(batchItemResult.getReadCount() - batchItemResult.getFilterCount());
                        }
                     } catch (Exception exception) {
                        RecordTranService.logger.error(exception);

                        for(BatchItemResult var12x : (Iterable<BatchItemResult>)(Iterable<?>)(var8x.values())) {
                           var12x.setWriteCount(0);
                        }

                        if (var1x.getDialect().supportTransaction()) {
                           RecordTranService.this.rollbackConnection(var9x);
                        }

                        items2.add(exception);
                        if (batch.getSkipLimit() <= 0) {
                           throw exception;
                        }

                        if (batch.getSkipLimit() < items2.size()) {
                           throw exception;
                        }
                     } finally {
                        RecordTranService.this.closePreparedStatements(var7x);
                        RecordTranService.this.closeConnection(var9x);
                     }
                  }
               } catch (Exception exception2) {
                  java.util.logging.Logger.getLogger(RecordTranService.class.getName()).log(java.util.logging.Level.SEVERE, exception2.getMessage(), exception2);
               } finally {
                  RecordTranService.this.mergeItemResults(batch, batchResult, valuesByKey);
               }

               countDownLatch.countDown();
            }
         });
         thread[index].start();
      }

      try {
         countDownLatch.await();
         this.mergeBatchResults(batch, batchIndexResult, items);
         this.sumResultCounts(batchIndexResult, valuesByKey);
      } catch (InterruptedException interruptedException) {
         Thread.currentThread().interrupt();
         java.util.logging.Logger.getLogger(RecordTranService.class.getName()).log(java.util.logging.Level.SEVERE, interruptedException.getMessage(), interruptedException);
         batchIndexResult.setStatus(BatchStatus.failed);
         batchIndexResult.setException(interruptedException);
      }

   }

   protected Map prepareStmt(Connection conn, BatchDataResolver resolver) throws SQLException {
      HashMap prepareStmtResult = new HashMap();

      for(BatchDataResolverItem batchDataResolverItem : (Iterable<BatchDataResolverItem>)(Iterable<?>)(resolver.getItems())) {
         PreparedStatement preparedStatement = conn.prepareStatement(batchDataResolverItem.getUpdateSql());
         prepareStmtResult.put(batchDataResolverItem.getName(), preparedStatement);
      }

      return prepareStmtResult;
   }

}
