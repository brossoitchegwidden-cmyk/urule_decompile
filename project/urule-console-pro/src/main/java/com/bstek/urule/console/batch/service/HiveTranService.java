package com.bstek.urule.console.batch.service;

import com.bstek.urule.console.batch.BatchContext;
import com.bstek.urule.console.batch.BatchItemResult;
import com.bstek.urule.console.batch.BatchResult;
import com.bstek.urule.console.batch.BatchStatus;
import com.bstek.urule.console.batch.HiveStatement;
import com.bstek.urule.console.batch.writer.HiveWriter;
import com.bstek.urule.console.batch.writer.Writer;
import com.bstek.urule.console.batch.writer.WriterUtils;
import com.bstek.urule.console.database.model.batch.Batch;
import com.bstek.urule.console.database.model.batch.BatchDataResolver;
import com.bstek.urule.console.database.model.batch.BatchDataResolverItem;
import com.bstek.urule.console.database.util.JdbcUtils;
import com.bstek.urule.model.GeneralEntity;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class HiveTranService extends AbstractBatchService {
   private static Log logger = LogFactory.getLog(HiveTranService.class);
   private static Writer hiveWriter;

   private Writer resolveWriter() {
      if (HiveTranService.hiveWriter == null) {
         HiveTranService.hiveWriter = new HiveWriter();
      }

      return HiveTranService.hiveWriter;
   }

   protected void processRecord(BatchContext batchContext, Map valuesByKey2, GeneralEntity generalEntity, Map valuesByKey3) throws Exception {
      Map valuesByKey = this.getRuleProcessor().fireRules(batchContext, generalEntity);
      WriterUtils.write(batchContext, this.resolveWriter(), valuesByKey2, valuesByKey, generalEntity, valuesByKey3);
   }

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
            HiveTranService.logger.error(exception);
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
      ArrayList items2 = new ArrayList();
      Map valuesByKey = this.prepareItemResult(dataResolver);
      Connection connection = null;
      Statement statement = null;

      try {
         int number = 0;
         connection = this.getWriteDataSource(batch).getConnection();
         BatchStatus status = batch.getStatus();
         Map valuesByKey2 = this.prepareStmts(dataResolver);
         statement = connection.createStatement();

         for(int index = 0; index < batchContext.getPageCount(); ++index) {
            if (this.getBatchStatus(batch.getId()) == BatchStatus.stop) {
               status = BatchStatus.stop;
               break;
            }

            HiveTranService.logger.debug("execute batch 【" + batch.getName() + "】pageIndex:" + index);
            BatchResult batchResult2 = new BatchResult();
            items2.add(batchResult2);
            HiveTranService.logger.debug("execute batch 【" + batch.getName() + "】loadData pageIndex:" + index + "...");
            List datas = this.loadDatas(readConnection, batchContext, index);
            number += datas.size();
            HashMap valuesByKey3 = new HashMap();

            for(int index2 = 0; index2 < datas.size(); ++index2) {
               GeneralEntity generalEntity = (GeneralEntity)datas.get(index2);
               Object objectValue = null;
               Map valuesByKey4 = this.prepareItemResult(dataResolver);
               valuesByKey3.put(index2, valuesByKey4);

               try {
                  this.processRecord(batchContext, valuesByKey2, generalEntity, valuesByKey4);
               } catch (Exception exception) {
                  HiveTranService.logger.error(exception);
                  batchResult.setException(exception);
                  items.add(exception);
                  if (batch.getSkipLimit() <= 0) {
                     throw exception;
                  }

                  if (batch.getSkipLimit() < items.size()) {
                     throw exception;
                  }
               }
            }

            this.mergeItemResults(batch, batchResult2, valuesByKey3);

            try {
               this.executePendingStatements(statement, valuesByKey2, dataResolver, valuesByKey, false);
            } catch (Exception exception2) {
               HiveTranService.logger.error(exception2);
               batchResult.setException(exception2);
               items.add(exception2);
               if (batch.getSkipLimit() <= 0) {
                  throw exception2;
               }

               if (batch.getSkipLimit() < items.size()) {
                  throw exception2;
               }
            }
         }

         try {
            this.executePendingStatements(statement, valuesByKey2, dataResolver, valuesByKey, true);
         } catch (Exception exception3) {
            HiveTranService.logger.error(exception3);
            batchResult.setException(exception3);
            items.add(exception3);
            if (batch.getSkipLimit() <= 0) {
               throw exception3;
            }

            if (batch.getSkipLimit() < items.size()) {
               throw exception3;
            }
         }

         if (status != BatchStatus.stop) {
            batchResult.setFilterCount(batchResult.getReadCount() - number);
            this.mergeBatchResults(batch, batchResult, items2);
            this.applyWriteCounts(batch, batchResult, valuesByKey);
            boolean flag = false;

            for(BatchItemResult batchItemResult : (Iterable<BatchItemResult>)(Iterable<?>)(batchResult.getItemResults().values())) {
               if (batchItemResult.getWriteCount() > 0) {
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
         HiveTranService.logger.error(exception4);
         batchResult.setStatus(BatchStatus.failed);
         batchResult.setException(exception4);
      } finally {
         JdbcUtils.closeStatement(statement);
         batchResult.setExceptions(items);
         this.closeConnection(connection);
      }

   }

   protected Map prepareStmts(BatchDataResolver resolver) throws SQLException {
      HashMap prepareStmtsResult = new HashMap();

      for(BatchDataResolverItem batchDataResolverItem : (Iterable<BatchDataResolverItem>)(Iterable<?>)(resolver.getItems())) {
         HiveStatement hiveStatement = new HiveStatement();
         prepareStmtsResult.put(batchDataResolverItem.getName(), hiveStatement);
      }

      return prepareStmtsResult;
   }

   protected void executePendingStatements(Statement statement, Map valuesByKey, BatchDataResolver batchDataResolver, Map valuesByKey2, boolean flag) throws SQLException {
      try {
         for(String text : (Iterable<String>)(Iterable<?>)(valuesByKey.keySet())) {
            BatchItemResult batchItemResult = (BatchItemResult)valuesByKey2.get(text);
            HiveStatement hiveStatement = (HiveStatement)valuesByKey.get(text);
            List batchSqls = hiveStatement.getBatchSqls();

            for(BatchDataResolverItem batchDataResolverItem : (Iterable<BatchDataResolverItem>)(Iterable<?>)(batchDataResolver.getItems())) {
               if (batchDataResolverItem.getName().equals(text)) {
                  if (flag && batchSqls.size() > 0) {
                     if (batchSqls.size() >= batchDataResolverItem.getCommitLimit()) {
                        this.flushBatchSql(statement, hiveStatement, batchDataResolverItem, batchItemResult);
                     } else {
                        String updateSql = batchDataResolverItem.getUpdateSql();
                        updateSql = updateSql + " values ";
                        String text2 = "";

                        for(int index = 0; index < batchSqls.size(); ++index) {
                           String text3 = (String)batchSqls.get(index);
                           if (index > 0) {
                              text2 = text2 + ",";
                           }

                           text2 = text2 + "(" + text3 + ")";
                        }

                        HiveTranService.logger.debug(updateSql + text2);
                        statement.executeUpdate(updateSql + text2);
                        batchItemResult.setWriteCount(batchItemResult.getWriteCount() + batchSqls.size());
                        hiveStatement.setBatchSqls(new ArrayList());
                     }
                  } else if (!flag && batchSqls.size() >= batchDataResolverItem.getCommitLimit()) {
                     this.flushBatchSql(statement, hiveStatement, batchDataResolverItem, batchItemResult);
                  }
               }
            }
         }

      } catch (Exception exception) {
         java.util.logging.Logger.getLogger(HiveTranService.class.getName()).log(java.util.logging.Level.SEVERE, exception.getMessage(), exception);
         throw new SqlBatchException(exception.getMessage(), exception);
      }
   }

   private void flushBatchSql(Statement statement, HiveStatement hiveStatement, BatchDataResolverItem batchDataResolverItem, BatchItemResult batchItemResult) throws Exception {
      List batchSqls = hiveStatement.getBatchSqls();
      String updateSql = batchDataResolverItem.getUpdateSql();
      updateSql = updateSql + " values ";
      String text = "";

      for(int index = 0; index < batchDataResolverItem.getCommitLimit(); ++index) {
         String text2 = (String)batchSqls.get(index);
         if (index > 0) {
            text = text + ",";
         }

         text = text + "(" + text2 + ")";
      }

      HiveTranService.logger.debug(updateSql + text);
      statement.executeUpdate(updateSql + text);
      batchItemResult.setWriteCount(batchItemResult.getWriteCount() + batchDataResolverItem.getCommitLimit());
      batchSqls = batchSqls.subList(batchDataResolverItem.getCommitLimit(), batchSqls.size());
      hiveStatement.setBatchSqls(batchSqls);
      if (batchSqls.size() > batchDataResolverItem.getCommitLimit()) {
         this.flushBatchSql(statement, hiveStatement, batchDataResolverItem, batchItemResult);
      }

   }

   public void executeTotal(BatchContext batchContext) {
      BatchResult batchResult = batchContext.getResult();
      Batch batch = batchContext.getBatch();
      BatchDataResolver dataResolver = batch.getDataResolver();
      Object objectValue = null;
      List datas = null;

      try {
         HiveTranService.logger.debug("execute batch 【" + batch.getName() + "】loadData ...");
         datas = this.loadDatas(batchContext.getReadConnection(), batchContext, -1);
         batchResult.setFilterCount(batchResult.getReadCount() - datas.size());
         HiveTranService.logger.debug("execute batch 【" + batch.getName() + "】data size:" + datas.size());
      } catch (Exception exception) {
         batchResult.setStatus(BatchStatus.failed);
         batchResult.setException(exception);
         return;
      }

      ArrayList items = new ArrayList();
      HashMap valuesByKey = new HashMap();
      Connection connection = null;
      Statement statement = null;
      Map valuesByKey2 = this.prepareItemResult(dataResolver);

      try {
         connection = this.getWriteDataSource(batch).getConnection();
         statement = connection.createStatement();
         Map valuesByKey3 = this.prepareStmts(dataResolver);
         BatchStatus status = batch.getStatus();

         for(int index = 0; index < datas.size(); ++index) {
            GeneralEntity generalEntity = (GeneralEntity)datas.get(index);
            Map valuesByKey4 = this.prepareItemResult(dataResolver);
            valuesByKey.put(index, valuesByKey4);
            if (index % 10000 == 0) {
               if (HiveTranService.logger.isDebugEnabled()) {
                  HiveTranService.logger.debug("execute batch 【" + batch.getName() + "】record index: " + index + "...");
               }

               if (this.getBatchStatus(batch.getId()) == BatchStatus.stop) {
                  status = BatchStatus.stop;
                  break;
               }
            }

            try {
               this.processRecord(batchContext, valuesByKey3, generalEntity, valuesByKey4);
               this.executePendingStatements(statement, valuesByKey3, dataResolver, valuesByKey2, false);
            } catch (Exception exception2) {
               HiveTranService.logger.error(exception2);
               items.add(exception2);
               if (batch.getSkipLimit() <= 0) {
                  throw exception2;
               }

               if (batch.getSkipLimit() < items.size()) {
                  throw exception2;
               }
            }
         }

         try {
            this.executePendingStatements(statement, valuesByKey3, dataResolver, valuesByKey2, true);
         } catch (Exception exception3) {
            HiveTranService.logger.error(exception3);
            batchResult.setException(exception3);
            items.add(exception3);
            if (batch.getSkipLimit() <= 0) {
               throw exception3;
            }

            if (batch.getSkipLimit() < items.size()) {
               throw exception3;
            }
         }

         if (status != BatchStatus.stop) {
            this.mergeItemResults(batch, batchResult, valuesByKey);
            this.applyWriteCounts(batch, batchResult, valuesByKey2);
            boolean flag = false;

            for(BatchItemResult batchItemResult : (Iterable<BatchItemResult>)(Iterable<?>)(batchResult.getItemResults().values())) {
               if (batchItemResult.getWriteCount() > 0) {
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
      } catch (Exception exception4) {
         batchResult.setStatus(BatchStatus.failed);
         batchResult.setException(exception4);
      } finally {
         JdbcUtils.closeStatement(statement);
         batchResult.setExceptions(items);
         this.closeConnection(connection);
      }

   }

   protected void applyWriteCounts(Batch batch, BatchResult batchResult, Map valuesByKey) {
      for(BatchDataResolverItem batchDataResolverItem : (Iterable<BatchDataResolverItem>)(Iterable<?>)(batch.getDataResolver().getItems())) {
         String name = batchDataResolverItem.getName();
         BatchItemResult batchItemResult = (BatchItemResult)batchResult.getItemResults().get(name);

         for(BatchItemResult batchItemResult2 : (Iterable<BatchItemResult>)(Iterable<?>)(valuesByKey.values())) {
            if (batchItemResult2.getName().equals(name)) {
               batchItemResult.setWriteCount(batchItemResult2.getWriteCount());
               break;
            }
         }
      }

   }
}
