package com.bstek.urule.console.batch.service;

import com.bstek.urule.console.batch.BatchContext;
import com.bstek.urule.console.batch.BatchItemResult;
import com.bstek.urule.console.batch.BatchResult;
import com.bstek.urule.console.batch.BatchStatus;
import com.bstek.urule.console.batch.processor.DefaultRuleProcessor;
import com.bstek.urule.console.batch.processor.RuleProcessor;
import com.bstek.urule.console.batch.reader.DefaultItemReader;
import com.bstek.urule.console.batch.reader.ItemReader;
import com.bstek.urule.console.database.manager.batch.BatchManager;
import com.bstek.urule.console.database.model.batch.Batch;
import com.bstek.urule.console.database.model.batch.BatchDataProvider;
import com.bstek.urule.console.database.model.batch.BatchDataResolver;
import com.bstek.urule.console.database.model.batch.BatchDataResolverItem;
import com.bstek.urule.console.database.service.repository.DataSourceHandlerManager;
import com.bstek.urule.exception.RuleException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class AbstractBatchService implements BatchService {
   private static Log logger = LogFactory.getLog(AbstractBatchService.class);
   private static ItemReader defaultItemReader;
   private static RuleProcessor defaultRuleProcessor;

   private ItemReader resolveItemReader() {
      if (AbstractBatchService.defaultItemReader == null) {
         AbstractBatchService.defaultItemReader = new DefaultItemReader();
      }

      return AbstractBatchService.defaultItemReader;
   }

   protected RuleProcessor getRuleProcessor() {
      if (AbstractBatchService.defaultRuleProcessor == null) {
         AbstractBatchService.defaultRuleProcessor = new DefaultRuleProcessor();
      }

      return AbstractBatchService.defaultRuleProcessor;
   }

   protected boolean beforeExecute(BatchContext context) {
      Batch batch = context.getBatch();
      BatchResult batchResult = context.getResult();

      try {
         int totleRows = this.getTotleRows(batch.getDataProvider());
         context.setReadCount(totleRows);
         batchResult.setReadCount(totleRows);
         if (totleRows == 0) {
            batchResult.setStatus(BatchStatus.completed);
            batchResult.setMsg(BatchStatus.completed.name());
            return false;
         } else {
            int number = Double.valueOf(Math.ceil(Double.valueOf((double)totleRows) / (double)this.resolvePageSize(batch))).intValue();
            context.setPageCount(number);
            if (batch.isThreadMulti()) {
               int number2 = Double.valueOf(Math.ceil(Double.valueOf((double)totleRows) / (double)(batch.getThreadDataSize() * batch.getThreadSize()))).intValue();
               context.setBatchCount(number2);
            }

            Connection connection = this.getReadDataSource(context.getBatch()).getConnection();
            context.setReadConnection(connection);
            return true;
         }
      } catch (Exception exception) {
         this.closeConnection(context.getReadConnection());
         AbstractBatchService.logger.error(exception);
         batchResult.setStatus(BatchStatus.failed);
         batchResult.setException(exception);
         return false;
      }
   }

   public void rollbackConnection(Connection conn) {
      try {
         conn.rollback();
      } catch (SQLException sQLException) {
         throw new RuleException(sQLException);
      }
   }

   public void closeConnection(Connection conn) {
      try {
         if (conn != null) {
            conn.close();
         }

      } catch (SQLException sQLException) {
         throw new RuleException(sQLException);
      }
   }

   protected int resolvePageSize(Batch batch) {
      int number = 0;
      if (batch.isThreadMulti()) {
         number = batch.getThreadDataSize();
      } else if (batch.getDataProvider().isSupportsPaging()) {
         number = batch.getDataProvider().getPageSize();
      }

      if (number <= 0) {
         number = 100;
      }

      return number;
   }

   protected Map prepareItemResult(BatchDataResolver resolver) {
      HashMap prepareItemResultResult = new HashMap();

      for(BatchDataResolverItem batchDataResolverItem : (Iterable<BatchDataResolverItem>)(Iterable<?>)(resolver.getItems())) {
         String name = batchDataResolverItem.getName();
         BatchItemResult batchItemResult = new BatchItemResult();
         batchItemResult.setTableName(batchDataResolverItem.getTableName());
         batchItemResult.setUpdateMode(batchDataResolverItem.getUpdateMode());
         batchItemResult.setName(name);
         prepareItemResultResult.put(name, batchItemResult);
      }

      return prepareItemResultResult;
   }

   public int getTotleRows(BatchDataProvider dataProvider) throws Exception {
      return this.resolveItemReader().getTotleRows(dataProvider);
   }

   public List loadDatas(Connection loadConnection, BatchContext context, int pageIndex) throws Exception {
      Batch batch = context.getBatch();
      if (pageIndex >= 0) {
         int number = this.resolvePageSize(batch);
         return this.resolveItemReader().getPageDatas(loadConnection, context, pageIndex, number);
      } else {
         return this.resolveItemReader().getDatas(loadConnection, context);
      }
   }

   protected int sumResultCounts(BatchResult batchResult, Map valuesByKey) {
      Iterator iterator = valuesByKey.values().iterator();

      int number;
      for(number = 0; iterator.hasNext(); number += (Integer)iterator.next()) {
      }

      return number;
   }

   protected void mergeFilterCounts(BatchResult batchResult2, List items) {
      for(BatchResult batchResult : (Iterable<BatchResult>)(Iterable<?>)(items)) {
         batchResult2.setFilterCount(batchResult2.getFilterCount() + batchResult.getFilterCount());
      }

   }

   protected void mergeItemResults(Batch batch, BatchResult batchResult, Map valuesByKey2) {
      for(BatchDataResolverItem batchDataResolverItem : (Iterable<BatchDataResolverItem>)(Iterable<?>)(batch.getDataResolver().getItems())) {
         String name = batchDataResolverItem.getName();
         BatchItemResult batchItemResult = null;
         if (batchResult.getItemResults().containsKey(name)) {
            batchItemResult = (BatchItemResult)batchResult.getItemResults().get(name);
         } else {
            batchItemResult = new BatchItemResult();
            batchItemResult.setTableName(batchDataResolverItem.getTableName());
            batchItemResult.setUpdateMode(batchDataResolverItem.getUpdateMode());
            batchItemResult.setName(name);
            batchResult.getItemResults().put(name, batchItemResult);
         }

         for(Map valuesByKey : (Iterable<Map>)(Iterable<?>)(valuesByKey2.values())) {
            if (valuesByKey.containsKey(name)) {
               BatchItemResult batchItemResult2 = (BatchItemResult)valuesByKey.get(name);
               batchItemResult.setReadCount(batchItemResult.getReadCount() + batchItemResult2.getReadCount());
               batchItemResult.setWriteCount(batchItemResult.getWriteCount() + batchItemResult2.getWriteCount());
               batchItemResult.setFilterCount(batchItemResult.getFilterCount() + batchItemResult2.getFilterCount());
            }
         }
      }

   }

   protected void mergeBatchResults(Batch batch, BatchResult batchResult2, List items) {
      for(BatchDataResolverItem batchDataResolverItem : (Iterable<BatchDataResolverItem>)(Iterable<?>)(batch.getDataResolver().getItems())) {
         String name = batchDataResolverItem.getName();
         BatchItemResult batchItemResult = null;
         if (batchResult2.getItemResults().containsKey(name)) {
            batchItemResult = (BatchItemResult)batchResult2.getItemResults().get(name);
         } else {
            batchItemResult = new BatchItemResult();
            batchItemResult.setTableName(batchDataResolverItem.getTableName());
            batchItemResult.setUpdateMode(batchDataResolverItem.getUpdateMode());
            batchItemResult.setName(name);
            batchResult2.getItemResults().put(name, batchItemResult);
         }

         for(BatchResult batchResult : (Iterable<BatchResult>)(Iterable<?>)(items)) {
            if (batchResult.getItemResults().containsKey(name)) {
               BatchItemResult batchItemResult2 = (BatchItemResult)batchResult.getItemResults().get(name);
               batchItemResult.setReadCount(batchItemResult.getReadCount() + batchItemResult2.getReadCount());
               batchItemResult.setWriteCount(batchItemResult.getWriteCount() + batchItemResult2.getWriteCount());
               batchItemResult.setFilterCount(batchItemResult.getFilterCount() + batchItemResult2.getFilterCount());
            }
         }
      }

      Object exceptions = batchResult2.getExceptions();
      if (exceptions == null) {
         exceptions = new ArrayList();
      }

      for(BatchResult batchResult3 : (Iterable<BatchResult>)(Iterable<?>)(items)) {
         ((List)exceptions).addAll(batchResult3.getExceptions());
      }

      batchResult2.setExceptions((List)exceptions);
   }

   public DataSource getReadDataSource(Batch batch) throws Exception {
      return DataSourceHandlerManager.getDataSource(batch.getDataProvider().getDatasource());
   }

   public DataSource getWriteDataSource(Batch batch) throws Exception {
      return DataSourceHandlerManager.getDataSource(batch.getDataResolver().getDatasource());
   }

   public BatchStatus getBatchStatus(Long batchId) {
      Batch batch = BatchManager.ins.get(batchId);
      return batch.getStatus();
   }
}
