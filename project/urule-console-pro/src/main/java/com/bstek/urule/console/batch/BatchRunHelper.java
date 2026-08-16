package com.bstek.urule.console.batch;

import com.bstek.urule.Utils;
import com.bstek.urule.console.batch.inspector.BatchListener;
import com.bstek.urule.console.batch.inspector.DefaultBatchListener;
import com.bstek.urule.console.batch.processor.ProcessorException;
import com.bstek.urule.console.batch.reader.ReaderException;
import com.bstek.urule.console.batch.service.BasicBatchService;
import com.bstek.urule.console.batch.service.BatchService;
import com.bstek.urule.console.batch.service.BatchTranService;
import com.bstek.urule.console.batch.service.HiveBatchTranService;
import com.bstek.urule.console.batch.service.HiveTranService;
import com.bstek.urule.console.batch.service.PageTranService;
import com.bstek.urule.console.batch.service.RecordTranService;
import com.bstek.urule.console.batch.service.SqlBatchException;
import com.bstek.urule.console.batch.utils.StmtUtils;
import com.bstek.urule.console.batch.writer.WriterException;
import com.bstek.urule.console.database.manager.batch.BatchManager;
import com.bstek.urule.console.database.manager.batch.resolver.ResolverManager;
import com.bstek.urule.console.database.manager.group.GroupManager;
import com.bstek.urule.console.database.manager.log.batch.BatchLogManager;
import com.bstek.urule.console.database.manager.log.batch.BatchSkipLogManager;
import com.bstek.urule.console.database.manager.project.ProjectManager;
import com.bstek.urule.console.database.model.Group;
import com.bstek.urule.console.database.model.Project;
import com.bstek.urule.console.database.model.batch.Batch;
import com.bstek.urule.console.database.model.batch.BatchDataResolver;
import com.bstek.urule.console.database.model.batch.BatchLog;
import com.bstek.urule.console.database.model.batch.BatchSkipLog;
import com.bstek.urule.console.database.model.batch.TranScope;
import com.bstek.urule.console.database.util.JdbcUtils;
import com.bstek.urule.console.util.StringUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.sql.Connection;
import java.util.Date;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class BatchRunHelper {
   static final Log a = LogFactory.getLog(StmtUtils.class);
   private static BatchService b = new BatchTranService();
   private static BatchService c = new PageTranService();
   private static BatchService d = new RecordTranService();
   private static BatchService e = new HiveTranService();
   private static BatchService f = new HiveBatchTranService();
   private static BatchService g = new BasicBatchService();

   protected static BatchService a(Long var0) {
      List var1 = ResolverManager.ins.createQuery().batchId(var0).list();
      if (var1.size() > 0) {
         BatchDataResolver var3 = (BatchDataResolver)ResolverManager.ins.createQuery().batchId(var0).list().get(0);
         return a(var3);
      } else {
         BatchDataResolver var2 = new BatchDataResolver();
         var2.setTranScope(TranScope.page);
         return a(var2);
      }
   }

   private static BatchService a(BatchDataResolver var0) {
      BatchService var1 = null;
      if (TranScope.batch == var0.getTranScope()) {
         var1 = b;
      } else if (TranScope.page == var0.getTranScope()) {
         var1 = c;
      } else if (TranScope.record == var0.getTranScope()) {
         var1 = d;
      } else if (TranScope.hive == var0.getTranScope()) {
         var1 = f;
      } else {
         var1 = c;
      }

      return var1;
   }

   protected static BatchService a(Batch var0) {
      BatchDataResolver var1 = var0.getDataResolver();
      if (var0.isThreadMulti()) {
         return a(var1);
      } else {
         return TranScope.hive == var1.getTranScope() ? e : g;
      }
   }

   protected static void a(BatchContext var0) {
      BatchListener var1 = b(var0.getBatch());
      BatchResult var2 = var0.getResult();
      Batch var3 = var0.getBatch();

      try {
         c(var0);
         var3.setStatus(BatchStatus.started);
         Connection var4 = JdbcUtils.getConnection();

         try {
            var4.setAutoCommit(false);
            BatchLogManager.ins.add(var0.getBatchLog());
            BatchManager.ins.updateStatus(var3.getId(), BatchStatus.started);
            var4.commit();
         } catch (Exception var22) {
            var4.rollback();
            throw var22;
         } finally {
            var4.setAutoCommit(true);
            JdbcUtils.closeConnection(var4);
         }

         var1.beforeExecute(var0);
         BatchService var5 = a(var0.getBatch());
         var5.execute(var0);
         var1.beforeExecute(var0);
         if (var2.getStatus() != BatchStatus.stop) {
            if (var3.getSkipLimit() > 0) {
               if (var3.getSkipLimit() < var2.getExceptions().size()) {
                  throw new Exception("异常数量超过默认约定数量");
               }

               var2.setStatus(BatchStatus.completed);
               var2.setMsg("OK");
               var3.setStatus(BatchStatus.completed);
            } else if (var2.getStatus() == BatchStatus.failed) {
               var3.setStatus(BatchStatus.failed);
            } else {
               var2.setStatus(BatchStatus.completed);
               var2.setMsg("OK");
            }
         } else {
            var3.setStatus(BatchStatus.stop);
         }

         if (var3.getStatus() == BatchStatus.started) {
            var3.setStatus(BatchStatus.completed);
         }
      } catch (Exception var24) {
         var2.setStatus(BatchStatus.failed);
         var3.setStatus(BatchStatus.failed);
         var2.setException(var24);
      } finally {
         var2.setEndTime(new Date());
         if (BatchStatus.started.name().equalsIgnoreCase(var2.getMsg())) {
            var2.setMsg("OK");
         }

         a.debug("execute batch 【" + var0.getBatch().getName() + "】completed. BatchStatus:" + var2.getStatus());

         try {
            b(var0);
            var1.onExecute(var0);
         } catch (Exception var21) {
            var21.printStackTrace();
         }

      }

   }

   private static void b(BatchContext var0) {
      BatchResult var1 = var0.getResult();
      BatchLog var2 = var0.getBatchLog();

      try {
         if (var1.getEndTime() != null) {
            var2.setEndTime(var1.getEndTime());
            var2.setTime(var1.getEndTime().getTime() - var1.getStartTime().getTime());
         }

         var2.setReadCount(var1.getReadCount());
         var2.setFilterCount(var1.getFilterCount());
         var2.setMsg(var1.getMsg());
         var2.setStatus(var1.getStatus());
         ObjectMapper var3 = new ObjectMapper();
         String var4 = null;

         try {
            var4 = var3.writeValueAsString(var1.getItemResults());
         } catch (Exception var9) {
            a.error(var9.getMessage());
            var9.printStackTrace();
         }

         var2.setItemData(var4);
         if (var1.getExceptions().size() > 0) {
            for(Exception var6 : (Iterable<Exception>)(Iterable<?>)(var1.getExceptions())) {
               BatchSkipLog var7 = new BatchSkipLog();
               var7.setBatchId(var0.getBatch().getId());
               var7.setMsg(var6.getMessage());
               var7.setDetail(var3.writeValueAsString(var6.getCause()));

               try {
                  if (var6 instanceof ReaderException) {
                     var7.setType("reader");
                  } else if (var6 instanceof ProcessorException) {
                     ProcessorException var8 = (ProcessorException)var6;
                     var7.setType("processor");
                     var7.setData(var3.writeValueAsString(var8.getData()));
                  } else if (!(var6 instanceof WriterException) && !(var6 instanceof SqlBatchException)) {
                     var7.setType("base");
                  } else {
                     var7.setType("writer");
                  }

                  var7.setGroupId(var2.getGroupId());
                  var7.setProjectId(var2.getProjectId());
                  var7.setLogId(var2.getId());
                  BatchSkipLogManager.ins.add(var7);
               } catch (Exception var10) {
                  var6.printStackTrace();
               }
            }
         }

         BatchManager.ins.updateStatus(var2.getBatchId(), var0.getBatch().getStatus());
         BatchLogManager.ins.updateStatus(var2);
      } catch (Exception var11) {
         a.error(var11.getMessage());
         var11.printStackTrace();
      }

   }

   private static void c(BatchContext var0) {
      BatchLog var1 = var0.getBatchLog();

      try {
         BatchResult var2 = var0.getResult();
         var1.setBatchId(var2.getBatchId());
         var1.setBatchName(var2.getBatchName());
         var1.setStartTime(var2.getStartTime());
         if (var2.getEndTime() != null) {
            var1.setEndTime(var2.getEndTime());
            var1.setTime(var2.getEndTime().getTime() - var2.getStartTime().getTime());
         }

         Batch var3 = var0.getBatch();
         var1.setProjectId(var3.getProjectId());
         Project var4 = ProjectManager.ins.get(var3.getProjectId());
         Group var5 = GroupManager.ins.get(var4.getGroupId());
         var1.setGroupId(var5.getId());
         var1.setGroupName(var5.getName());
         var1.setProjectId(var3.getProjectId());
         var1.setProjectName(var4.getName());
         var1.setMsg(var2.getMsg());
         var1.setStatus(var2.getStatus());
         ObjectMapper var6 = new ObjectMapper();
         String var7 = null;

         try {
            var7 = var6.writeValueAsString(var0.getParams());
         } catch (Exception var11) {
            a.error(var11.getMessage());
            var11.printStackTrace();
         }

         var1.setInParams(var7);
         var1.setPacketId(var3.getPacketId());
         String var8 = null;

         try {
            var8 = var6.writeValueAsString(var3.getComplexPacketParams());
         } catch (Exception var10) {
            a.error(var10.getMessage());
            var10.printStackTrace();
         }

         var1.setPacketParams(var8);
      } catch (Exception var12) {
         a.error(var12.getMessage());
         var12.printStackTrace();
      }

   }

   private static BatchListener b(Batch var0) {
      Object var1 = new DefaultBatchListener();

      try {
         if (StringUtils.isNotBlank(var0.getListener())) {
            var1 = (BatchListener)Utils.getApplicationContext().getBean(var0.getListener());
         }
      } catch (Exception var3) {
         a.error(var3.getMessage());
         var3.printStackTrace();
      }

      return (BatchListener)var1;
   }
}
