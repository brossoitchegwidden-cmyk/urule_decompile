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
   private static Log a = LogFactory.getLog(AbstractBatchService.class);
   private static ItemReader b;
   private static RuleProcessor c;

   private ItemReader b() {
      if (b == null) {
         b = new DefaultItemReader();
      }

      return b;
   }

   protected RuleProcessor a() {
      if (c == null) {
         c = new DefaultRuleProcessor();
      }

      return c;
   }

   protected boolean a(BatchContext var1) {
      Batch var2 = var1.getBatch();
      BatchResult var3 = var1.getResult();

      try {
         int var4 = this.getTotleRows(var2.getDataProvider());
         var1.setReadCount(var4);
         var3.setReadCount(var4);
         if (var4 == 0) {
            var3.setStatus(BatchStatus.completed);
            var3.setMsg(BatchStatus.completed.name());
            return false;
         } else {
            int var5 = Double.valueOf(Math.ceil(Double.valueOf((double)var4) / (double)this.a(var2))).intValue();
            var1.setPageCount(var5);
            if (var2.isThreadMulti()) {
               int var6 = Double.valueOf(Math.ceil(Double.valueOf((double)var4) / (double)(var2.getThreadDataSize() * var2.getThreadSize()))).intValue();
               var1.setBatchCount(var6);
            }

            Connection var8 = this.getReadDataSource(var1.getBatch()).getConnection();
            var1.setReadConnection(var8);
            return true;
         }
      } catch (Exception var7) {
         this.closeConnection(var1.getReadConnection());
         a.error(var7);
         var3.setStatus(BatchStatus.failed);
         var3.setException(var7);
         return false;
      }
   }

   public void rollbackConnection(Connection var1) {
      try {
         var1.rollback();
      } catch (SQLException var3) {
         throw new RuleException(var3);
      }
   }

   public void closeConnection(Connection var1) {
      try {
         if (var1 != null) {
            var1.close();
         }

      } catch (SQLException var3) {
         throw new RuleException(var3);
      }
   }

   protected int a(Batch var1) {
      int var2 = 0;
      if (var1.isThreadMulti()) {
         var2 = var1.getThreadDataSize();
      } else if (var1.getDataProvider().isSupportsPaging()) {
         var2 = var1.getDataProvider().getPageSize();
      }

      if (var2 <= 0) {
         var2 = 100;
      }

      return var2;
   }

   protected Map a(BatchDataResolver var1) {
      HashMap var2 = new HashMap();

      for(BatchDataResolverItem var4 : (Iterable<BatchDataResolverItem>)(Iterable<?>)(var1.getItems())) {
         String var5 = var4.getName();
         BatchItemResult var6 = new BatchItemResult();
         var6.setTableName(var4.getTableName());
         var6.setUpdateMode(var4.getUpdateMode());
         var6.setName(var5);
         var2.put(var5, var6);
      }

      return var2;
   }

   public int getTotleRows(BatchDataProvider var1) throws Exception {
      return this.b().getTotleRows(var1);
   }

   public List loadDatas(Connection var1, BatchContext var2, int var3) throws Exception {
      Batch var4 = var2.getBatch();
      if (var3 >= 0) {
         int var5 = this.a(var4);
         return this.b().getPageDatas(var1, var2, var3, var5);
      } else {
         return this.b().getDatas(var1, var2);
      }
   }

   protected int a(BatchResult var1, Map var2) {
      Iterator var3 = var2.values().iterator();

      int var4;
      for(var4 = 0; var3.hasNext(); var4 += (Integer)var3.next()) {
      }

      return var4;
   }

   protected void a(BatchResult var1, List var2) {
      for(BatchResult var4 : (Iterable<BatchResult>)(Iterable<?>)(var2)) {
         var1.setFilterCount(var1.getFilterCount() + var4.getFilterCount());
      }

   }

   protected void a(Batch var1, BatchResult var2, Map var3) {
      for(BatchDataResolverItem var5 : (Iterable<BatchDataResolverItem>)(Iterable<?>)(var1.getDataResolver().getItems())) {
         String var6 = var5.getName();
         BatchItemResult var7 = null;
         if (var2.getItemResults().containsKey(var6)) {
            var7 = (BatchItemResult)var2.getItemResults().get(var6);
         } else {
            var7 = new BatchItemResult();
            var7.setTableName(var5.getTableName());
            var7.setUpdateMode(var5.getUpdateMode());
            var7.setName(var6);
            var2.getItemResults().put(var6, var7);
         }

         for(Map var9 : (Iterable<Map>)(Iterable<?>)(var3.values())) {
            if (var9.containsKey(var6)) {
               BatchItemResult var10 = (BatchItemResult)var9.get(var6);
               var7.setReadCount(var7.getReadCount() + var10.getReadCount());
               var7.setWriteCount(var7.getWriteCount() + var10.getWriteCount());
               var7.setFilterCount(var7.getFilterCount() + var10.getFilterCount());
            }
         }
      }

   }

   protected void a(Batch var1, BatchResult var2, List var3) {
      for(BatchDataResolverItem var5 : (Iterable<BatchDataResolverItem>)(Iterable<?>)(var1.getDataResolver().getItems())) {
         String var6 = var5.getName();
         BatchItemResult var7 = null;
         if (var2.getItemResults().containsKey(var6)) {
            var7 = (BatchItemResult)var2.getItemResults().get(var6);
         } else {
            var7 = new BatchItemResult();
            var7.setTableName(var5.getTableName());
            var7.setUpdateMode(var5.getUpdateMode());
            var7.setName(var6);
            var2.getItemResults().put(var6, var7);
         }

         for(BatchResult var9 : (Iterable<BatchResult>)(Iterable<?>)(var3)) {
            if (var9.getItemResults().containsKey(var6)) {
               BatchItemResult var10 = (BatchItemResult)var9.getItemResults().get(var6);
               var7.setReadCount(var7.getReadCount() + var10.getReadCount());
               var7.setWriteCount(var7.getWriteCount() + var10.getWriteCount());
               var7.setFilterCount(var7.getFilterCount() + var10.getFilterCount());
            }
         }
      }

      Object var11 = var2.getExceptions();
      if (var11 == null) {
         var11 = new ArrayList();
      }

      for(BatchResult var13 : (Iterable<BatchResult>)(Iterable<?>)(var3)) {
         ((List)var11).addAll(var13.getExceptions());
      }

      var2.setExceptions((List)var11);
   }

   public DataSource getReadDataSource(Batch var1) throws Exception {
      return DataSourceHandlerManager.getDataSource(var1.getDataProvider().getDatasource());
   }

   public DataSource getWriteDataSource(Batch var1) throws Exception {
      return DataSourceHandlerManager.getDataSource(var1.getDataResolver().getDatasource());
   }

   public BatchStatus getBatchStatus(Long var1) {
      Batch var2 = BatchManager.ins.get(var1);
      return var2.getStatus();
   }
}
