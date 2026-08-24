package com.bstek.urule.console.batch.service;

import com.bstek.urule.console.batch.BatchContext;
import com.bstek.urule.console.batch.BatchItemResult;
import com.bstek.urule.console.batch.BatchResult;
import com.bstek.urule.console.batch.BatchStatus;
import com.bstek.urule.console.database.model.batch.Batch;
import com.bstek.urule.console.database.model.batch.BatchDataResolver;
import com.bstek.urule.console.database.model.batch.TranScope;
import com.bstek.urule.model.GeneralEntity;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class BasicBatchService extends AbstractBatchTranService {
   private static Log logger = LogFactory.getLog(BasicBatchService.class);

   public void execute(BatchContext batchContext) {
      Batch batch = batchContext.getBatch();
      BatchResult batchResult = batchContext.getResult();
      boolean flag = this.beforeExecute(batchContext);
      if (flag) {
         try {
            if (batch.getDataProvider().isSupportsPaging()) {
               this.executeByPage(batchContext);
            } else {
               this.executeTotal(batchContext);
            }
         } catch (Exception exception) {
            BasicBatchService.logger.error(exception);
            batchResult.setStatus(BatchStatus.failed);
            batchResult.setException(exception);
         } finally {
            this.closeConnection(batchContext.getReadConnection());
         }

      }
   }

   public void executeByPage(BatchContext batchContext) {
      BatchResult batchResult = batchContext.getResult();
      Connection readConnection = batchContext.getReadConnection();
      Batch batch = batchContext.getBatch();
      BatchDataResolver dataResolver = batch.getDataResolver();
      ArrayList items = new ArrayList();
      if (dataResolver.getTranScope() == TranScope.batch && dataResolver.getDialect().supportTransaction()) {
         Object objectValue = null;
         Object objectValue2 = null;
         Map valuesByKey = this.prepareItemResult(dataResolver);
         batchResult.setItemResults(valuesByKey);
         Connection connection = null;

         try {
            connection = this.getWriteDataSource(batch).getConnection();
            connection.setAutoCommit(false);
            Map valuesByKey2 = this.prepareStmt(connection, dataResolver);
            int number = 0;
            BatchStatus batchStatus = BatchStatus.started;
            int number2 = 0;

            while(true) {
               if (number2 < batchContext.getPageCount()) {
                  if (this.getBatchStatus(batch.getId()) != BatchStatus.stop) {
                     BasicBatchService.logger.debug("execute batch 【" + batch.getName() + "】loadData pageIndex:" + number2 + "...");

                     try {
                        List datas = this.loadDatas(readConnection, batchContext, number2);
                        number += datas.size();

                        for(GeneralEntity generalEntity : (Iterable<GeneralEntity>)(Iterable<?>)(datas)) {
                           this.processRecord(batchContext, connection, valuesByKey2, generalEntity, valuesByKey);
                        }

                        this.executePreparedStatementBatches(valuesByKey2);
                     } catch (Exception exception) {
                        BasicBatchService.logger.error(exception);
                        items.add(exception);
                        if (batch.getSkipLimit() <= 0) {
                           throw exception;
                        }

                        if (batch.getSkipLimit() < items.size()) {
                           throw exception;
                        }
                     }

                     ++number2;
                     continue;
                  }

                  batchStatus = BatchStatus.stop;
               }

               if (batchStatus != BatchStatus.stop) {
                  batchResult.setFilterCount(batchResult.getReadCount() - number);
                  BasicBatchService.logger.debug("commit batch 【" + batch.getName() + "】.....");
                  connection.commit();
                  BasicBatchService.logger.debug("commit batch 【" + batch.getName() + "】 success");
                  this.closePreparedStatements(valuesByKey2);

                  for(BatchItemResult batchItemResult : (Iterable<BatchItemResult>)(Iterable<?>)(valuesByKey.values())) {
                     batchItemResult.setWriteCount(batchItemResult.getReadCount() - batchItemResult.getFilterCount());
                  }

                  batchResult.setStatus(BatchStatus.completed);
                  batchResult.setMsg(BatchStatus.completed.name());
               } else {
                  this.rollbackConnection(connection);
                  batchResult.setStatus(BatchStatus.stop);
               }
               break;
            }
         } catch (Exception exception2) {
            BasicBatchService.logger.error(exception2);
            this.closePreparedStatements((Map)objectValue);
            this.rollbackConnection(connection);

            for(BatchItemResult batchItemResult2 : (Iterable<BatchItemResult>)(Iterable<?>)(valuesByKey.values())) {
               batchItemResult2.setWriteCount(0);
            }

            batchResult.setStatus(BatchStatus.failed);
            batchResult.setException(exception2);
         } finally {
            batchResult.setExceptions(items);
            this.closeConnection(connection);
         }
      } else if (dataResolver.getTranScope() == TranScope.page && dataResolver.getDialect().supportTransaction()) {
         HashMap valuesByKey3 = new HashMap();
         int number3 = 0;
         Connection connection2 = null;

         try {
            connection2 = this.getWriteDataSource(batch).getConnection();
            connection2.setAutoCommit(false);
            BatchStatus status = batch.getStatus();

            for(int index = 0; index < batchContext.getPageCount(); ++index) {
               Object objectValue3 = null;
               Map valuesByKey4 = this.prepareItemResult(dataResolver);
               valuesByKey3.put(index, valuesByKey4);
               Map valuesByKey5 = this.prepareStmt(connection2, dataResolver);

               try {
                  if (this.getBatchStatus(batch.getId()) == BatchStatus.stop) {
                     status = BatchStatus.stop;
                     break;
                  }

                  BasicBatchService.logger.debug("execute batch 【" + batch.getName() + "】loadData pageIndex:" + index + "...");
                  List datas2 = this.loadDatas(readConnection, batchContext, index);
                  number3 += datas2.size();

                  for(GeneralEntity generalEntity2 : (Iterable<GeneralEntity>)(Iterable<?>)(datas2)) {
                     this.processRecord(batchContext, connection2, valuesByKey5, generalEntity2, valuesByKey4);
                  }

                  this.executePreparedStatementBatches(valuesByKey5);
                  connection2.commit();

                  for(BatchItemResult batchItemResult3 : (Iterable<BatchItemResult>)(Iterable<?>)(valuesByKey4.values())) {
                     batchItemResult3.setWriteCount(batchItemResult3.getReadCount() - batchItemResult3.getFilterCount());
                  }
               } catch (Exception exception3) {
                  BasicBatchService.logger.error(exception3);
                  items.add(exception3);
                  this.rollbackConnection(connection2);

                  for(BatchItemResult batchItemResult4 : (Iterable<BatchItemResult>)(Iterable<?>)(valuesByKey4.values())) {
                     batchItemResult4.setWriteCount(0);
                  }

                  if (batch.getSkipLimit() <= 0) {
                     throw exception3;
                  }

                  if (batch.getSkipLimit() < items.size()) {
                     throw exception3;
                  }
               } finally {
                  this.closePreparedStatements(valuesByKey5);
               }
            }

            if (status != BatchStatus.stop) {
               batchResult.setFilterCount(batchResult.getReadCount() - number3);
               this.mergeItemResults(batch, batchResult, valuesByKey3);
               boolean flag = false;

               for(BatchItemResult batchItemResult5 : (Iterable<BatchItemResult>)(Iterable<?>)(batchResult.getItemResults().values())) {
                  if (batchItemResult5.getWriteCount() > 0) {
                     flag = true;
                     break;
                  }
               }

               if (flag) {
                  batchResult.setStatus(BatchStatus.completed);
                  batchResult.setMsg(BatchStatus.completed.name());
               } else {
                  batchResult.setStatus(BatchStatus.failed);
               }
            } else {
               batchResult.setStatus(BatchStatus.stop);
            }
         } catch (Exception exception4) {
            BasicBatchService.logger.error(exception4);
            batchResult.setStatus(BatchStatus.failed);
            batchResult.setException(exception4);
         } finally {
            batchResult.setExceptions(items);
            this.closeConnection(connection2);
         }
      } else {
         ArrayList items2 = new ArrayList();
         Connection connection3 = null;

         try {
            int number4 = 0;
            connection3 = this.getWriteDataSource(batch).getConnection();
            BatchStatus status2 = batch.getStatus();

            for(int index2 = 0; index2 < batchContext.getPageCount(); ++index2) {
               if (this.getBatchStatus(batch.getId()) == BatchStatus.stop) {
                  status2 = BatchStatus.stop;
                  break;
               }

               BasicBatchService.logger.debug("execute batch 【" + batch.getName() + "】pageIndex:" + index2);
               BatchResult batchResult2 = new BatchResult();
               items2.add(batchResult2);
               BasicBatchService.logger.debug("execute batch 【" + batch.getName() + "】loadData pageIndex:" + index2 + "...");
               List datas3 = this.loadDatas(readConnection, batchContext, index2);
               number4 += datas3.size();
               HashMap valuesByKey6 = new HashMap();

               for(int index3 = 0; index3 < datas3.size(); ++index3) {
                  GeneralEntity generalEntity3 = (GeneralEntity)datas3.get(index3);
                  Map valuesByKey7 = null;
                  Object objectValue4 = null;
                  Map valuesByKey8 = this.prepareItemResult(dataResolver);
                  valuesByKey6.put(index3, valuesByKey8);

                  try {
                     if (dataResolver.getDialect().supportTransaction()) {
                        connection3.setAutoCommit(false);
                     }

                     valuesByKey7 = this.prepareStmt(connection3, dataResolver);
                     this.processRecord(batchContext, connection3, valuesByKey7, generalEntity3, valuesByKey8);
                     if (dataResolver.getDialect().supportTransaction()) {
                        this.executePreparedStatementBatches(valuesByKey7);
                        connection3.commit();
                     }

                     for(BatchItemResult batchItemResult6 : (Iterable<BatchItemResult>)(Iterable<?>)(valuesByKey8.values())) {
                        batchItemResult6.setWriteCount(batchItemResult6.getReadCount() - batchItemResult6.getFilterCount());
                     }
                  } catch (Exception exception5) {
                     BasicBatchService.logger.error(exception5);
                     if (dataResolver.getDialect().supportTransaction()) {
                        this.rollbackConnection(connection3);
                     }

                     for(BatchItemResult batchItemResult7 : (Iterable<BatchItemResult>)(Iterable<?>)(valuesByKey8.values())) {
                        batchItemResult7.setWriteCount(0);
                     }

                     batchResult.setException(exception5);
                     items.add(exception5);
                     if (batch.getSkipLimit() <= 0) {
                        throw exception5;
                     }

                     if (batch.getSkipLimit() < items.size()) {
                        throw exception5;
                     }
                  } finally {
                     this.closePreparedStatements(valuesByKey7);
                  }
               }

               this.mergeItemResults(batch, batchResult2, valuesByKey6);
            }

            if (status2 != BatchStatus.stop) {
               batchResult.setFilterCount(batchResult.getReadCount() - number4);
               this.mergeBatchResults(batch, batchResult, items2);
               boolean flag2 = false;

               for(BatchItemResult batchItemResult8 : (Iterable<BatchItemResult>)(Iterable<?>)(batchResult.getItemResults().values())) {
                  if (batchItemResult8.getWriteCount() > 0) {
                     flag2 = true;
                     break;
                  }
               }

               if (flag2) {
                  batchResult.setStatus(BatchStatus.completed);
                  batchResult.setMsg(BatchStatus.completed.name());
               } else {
                  batchResult.setStatus(BatchStatus.failed);
               }
            } else {
               batchResult.setStatus(BatchStatus.stop);
            }
         } catch (Exception exception6) {
            BasicBatchService.logger.error(exception6);
            batchResult.setStatus(BatchStatus.failed);
            batchResult.setException(exception6);
         } finally {
            batchResult.setExceptions(items);
            this.closeConnection(connection3);
         }
      }

   }

   public void executeTotal(BatchContext batchContext) {
      BatchResult batchResult = batchContext.getResult();
      Batch batch = batchContext.getBatch();
      BatchDataResolver dataResolver = batch.getDataResolver();
      Map valuesByKey = null;
      Object objectValue = null;
      List datas = null;

      try {
         BasicBatchService.logger.debug("execute batch 【" + batch.getName() + "】loadData ...");
         datas = this.loadDatas(batchContext.getReadConnection(), batchContext, -1);
         batchResult.setFilterCount(batchResult.getReadCount() - datas.size());
         BasicBatchService.logger.debug("execute batch 【" + batch.getName() + "】data size:" + datas.size());
      } catch (Exception exception) {
         batchResult.setStatus(BatchStatus.failed);
         batchResult.setException(exception);
         return;
      }

      ArrayList items = new ArrayList();
      if ((dataResolver.getTranScope() == TranScope.batch || dataResolver.getTranScope() == TranScope.page) && dataResolver.getDialect().supportTransaction()) {
         Connection connection = null;

         try {
            connection = this.getWriteDataSource(batch).getConnection();
            connection.setAutoCommit(false);
            valuesByKey = this.prepareStmt(connection, dataResolver);
            Map valuesByKey2 = this.prepareItemResult(dataResolver);
            BatchStatus status = batch.getStatus();

            for(int index = 0; index < datas.size(); ++index) {
               try {
                  GeneralEntity generalEntity = (GeneralEntity)datas.get(index);
                  if (index % 10000 == 0) {
                     if (BasicBatchService.logger.isDebugEnabled()) {
                        BasicBatchService.logger.debug("execute batch 【" + batch.getName() + "】record index: " + index + "...");
                     }

                     if (this.getBatchStatus(batch.getId()) == BatchStatus.stop) {
                        status = BatchStatus.stop;
                        break;
                     }
                  }

                  this.processRecord(batchContext, connection, valuesByKey, generalEntity, valuesByKey2);
               } catch (Exception exception2) {
                  BasicBatchService.logger.error(exception2);
                  items.add(exception2);
                  if (batch.getSkipLimit() <= 0) {
                     throw exception2;
                  }

                  if (batch.getSkipLimit() < items.size()) {
                     throw exception2;
                  }
               }
            }

            if (status != BatchStatus.stop) {
               this.executePreparedStatementBatches(valuesByKey);
               connection.commit();

               for(BatchItemResult batchItemResult : (Iterable<BatchItemResult>)(Iterable<?>)(valuesByKey2.values())) {
                  batchItemResult.setWriteCount(batchItemResult.getReadCount() - batchItemResult.getFilterCount());
               }

               batchResult.setItemResults(valuesByKey2);
            } else {
               this.rollbackConnection(connection);
               batchResult.setStatus(BatchStatus.stop);
            }
         } catch (Exception exception3) {
            batchResult.setStatus(BatchStatus.failed);
            batchResult.setException(exception3);
         } finally {
            this.closePreparedStatements(valuesByKey);
            batchResult.setExceptions(items);
            this.closeConnection(connection);
         }
      } else {
         HashMap valuesByKey3 = new HashMap();
         Connection connection2 = null;

         try {
            connection2 = this.getWriteDataSource(batch).getConnection();
            if (dataResolver.getDialect().supportTransaction()) {
               connection2.setAutoCommit(false);
            }

            BatchStatus status2 = batch.getStatus();

            for(int index2 = 0; index2 < datas.size(); ++index2) {
               GeneralEntity generalEntity2 = (GeneralEntity)datas.get(index2);
               Map valuesByKey4 = this.prepareItemResult(dataResolver);
               valuesByKey3.put(index2, valuesByKey4);
               if (index2 % 10000 == 0) {
                  if (BasicBatchService.logger.isDebugEnabled()) {
                     BasicBatchService.logger.debug("execute batch 【" + batch.getName() + "】record index: " + index2 + "...");
                  }

                  if (this.getBatchStatus(batch.getId()) == BatchStatus.stop) {
                     status2 = BatchStatus.stop;
                     break;
                  }
               }

               try {
                  valuesByKey = this.prepareStmt(connection2, dataResolver);
                  this.processRecord(batchContext, connection2, valuesByKey, generalEntity2, valuesByKey4);
                  if (dataResolver.getDialect().supportTransaction()) {
                     this.executePreparedStatementBatches(valuesByKey);
                     connection2.commit();
                  }

                  this.closePreparedStatements(valuesByKey);

                  for(BatchItemResult batchItemResult2 : (Iterable<BatchItemResult>)(Iterable<?>)(valuesByKey4.values())) {
                     batchItemResult2.setWriteCount(batchItemResult2.getReadCount() - batchItemResult2.getFilterCount());
                  }
               } catch (Exception exception4) {
                  BasicBatchService.logger.error(exception4);
                  if (dataResolver.getDialect().supportTransaction()) {
                     this.rollbackConnection(connection2);
                  }

                  for(BatchItemResult batchItemResult3 : (Iterable<BatchItemResult>)(Iterable<?>)(valuesByKey4.values())) {
                     batchItemResult3.setWriteCount(0);
                  }

                  items.add(exception4);
                  if (batch.getSkipLimit() <= 0) {
                     throw exception4;
                  }

                  if (batch.getSkipLimit() < items.size()) {
                     throw exception4;
                  }
               } finally {
                  this.closePreparedStatements(valuesByKey);
               }
            }

            this.closeConnection(connection2);
            if (status2 != BatchStatus.stop) {
               this.mergeItemResults(batch, batchResult, valuesByKey3);
               boolean flag = false;

               for(BatchItemResult batchItemResult4 : (Iterable<BatchItemResult>)(Iterable<?>)(batchResult.getItemResults().values())) {
                  if (batchItemResult4.getWriteCount() > 0) {
                     flag = true;
                     break;
                  }
               }

               if (flag) {
                  batchResult.setStatus(BatchStatus.completed);
                  batchResult.setMsg(BatchStatus.completed.name());
               } else {
                  batchResult.setStatus(BatchStatus.failed);
                  batchResult.setMsg(BatchStatus.failed.name());
               }
            } else {
               batchResult.setStatus(BatchStatus.stop);
            }
         } catch (Exception exception5) {
            batchResult.setStatus(BatchStatus.failed);
            batchResult.setException(exception5);
         } finally {
            batchResult.setExceptions(items);
            this.closeConnection(connection2);
         }
      }

   }
}
