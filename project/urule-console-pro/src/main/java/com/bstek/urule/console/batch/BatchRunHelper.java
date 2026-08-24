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
   static final Log logger = LogFactory.getLog(StmtUtils.class);
   private static BatchService batchTranService = new BatchTranService();
   private static BatchService pageTranService = new PageTranService();
   private static BatchService recordTranService = new RecordTranService();
   private static BatchService hiveTranService = new HiveTranService();
   private static BatchService hiveBatchTranService = new HiveBatchTranService();
   private static BatchService basicBatchService = new BasicBatchService();

   protected static BatchService getBatchService(Long batchId) {
      List items = ResolverManager.ins.createQuery().batchId(batchId).list();
      if (items.size() > 0) {
         BatchDataResolver batchDataResolver = (BatchDataResolver)ResolverManager.ins.createQuery().batchId(batchId).list().get(0);
         return resolveBatchService(batchDataResolver);
      } else {
         BatchDataResolver batchDataResolver2 = new BatchDataResolver();
         batchDataResolver2.setTranScope(TranScope.page);
         return resolveBatchService(batchDataResolver2);
      }
   }

   private static BatchService resolveBatchService(BatchDataResolver batchDataResolver) {
      BatchService batchService = null;
      if (TranScope.batch == batchDataResolver.getTranScope()) {
         batchService = BatchRunHelper.batchTranService;
      } else if (TranScope.page == batchDataResolver.getTranScope()) {
         batchService = BatchRunHelper.pageTranService;
      } else if (TranScope.record == batchDataResolver.getTranScope()) {
         batchService = BatchRunHelper.recordTranService;
      } else if (TranScope.hive == batchDataResolver.getTranScope()) {
         batchService = BatchRunHelper.hiveBatchTranService;
      } else {
         batchService = BatchRunHelper.pageTranService;
      }

      return batchService;
   }

   protected static BatchService getBatchService(Batch batch) {
      BatchDataResolver dataResolver = batch.getDataResolver();
      if (batch.isThreadMulti()) {
         return resolveBatchService(dataResolver);
      } else {
         return TranScope.hive == dataResolver.getTranScope() ? BatchRunHelper.hiveTranService : BatchRunHelper.basicBatchService;
      }
   }

   protected static void run(BatchContext batchContext) {
      BatchListener batchListener = resolveBatchListener(batchContext.getBatch());
      BatchResult batchResult = batchContext.getResult();
      Batch batch = batchContext.getBatch();

      try {
         processBatchContext(batchContext);
         batch.setStatus(BatchStatus.started);
         Connection connection = JdbcUtils.getConnection();

         try {
            connection.setAutoCommit(false);
            BatchLogManager.ins.add(batchContext.getBatchLog());
            BatchManager.ins.updateStatus(batch.getId(), BatchStatus.started);
            connection.commit();
         } catch (Exception exception) {
            connection.rollback();
            throw exception;
         } finally {
            connection.setAutoCommit(true);
            JdbcUtils.closeConnection(connection);
         }

         batchListener.beforeExecute(batchContext);
         BatchService batchService = getBatchService(batchContext.getBatch());
         batchService.execute(batchContext);
         batchListener.beforeExecute(batchContext);
         if (batchResult.getStatus() != BatchStatus.stop) {
            if (batch.getSkipLimit() > 0) {
               if (batch.getSkipLimit() < batchResult.getExceptions().size()) {
                  throw new Exception("异常数量超过默认约定数量");
               }

               batchResult.setStatus(BatchStatus.completed);
               batchResult.setMsg("OK");
               batch.setStatus(BatchStatus.completed);
            } else if (batchResult.getStatus() == BatchStatus.failed) {
               batch.setStatus(BatchStatus.failed);
            } else {
               batchResult.setStatus(BatchStatus.completed);
               batchResult.setMsg("OK");
            }
         } else {
            batch.setStatus(BatchStatus.stop);
         }

         if (batch.getStatus() == BatchStatus.started) {
            batch.setStatus(BatchStatus.completed);
         }
      } catch (Exception exception2) {
         batchResult.setStatus(BatchStatus.failed);
         batch.setStatus(BatchStatus.failed);
         batchResult.setException(exception2);
      } finally {
         batchResult.setEndTime(new Date());
         if (BatchStatus.started.name().equalsIgnoreCase(batchResult.getMsg())) {
            batchResult.setMsg("OK");
         }

         BatchRunHelper.logger.debug("execute batch 【" + batchContext.getBatch().getName() + "】completed. BatchStatus:" + batchResult.getStatus());

         try {
            persistBatchResult(batchContext);
            batchListener.onExecute(batchContext);
         } catch (Exception exception3) {
            java.util.logging.Logger.getLogger(BatchRunHelper.class.getName()).log(java.util.logging.Level.SEVERE, exception3.getMessage(), exception3);
         }

      }

   }

   private static void persistBatchResult(BatchContext batchContext) {
      BatchResult batchResult = batchContext.getResult();
      BatchLog batchLog = batchContext.getBatchLog();

      try {
         if (batchResult.getEndTime() != null) {
            batchLog.setEndTime(batchResult.getEndTime());
            batchLog.setTime(batchResult.getEndTime().getTime() - batchResult.getStartTime().getTime());
         }

         batchLog.setReadCount(batchResult.getReadCount());
         batchLog.setFilterCount(batchResult.getFilterCount());
         batchLog.setMsg(batchResult.getMsg());
         batchLog.setStatus(batchResult.getStatus());
         ObjectMapper objectMapper = new ObjectMapper();
         String text = null;

         try {
            text = objectMapper.writeValueAsString(batchResult.getItemResults());
         } catch (Exception exception) {
            BatchRunHelper.logger.error(exception.getMessage());
            java.util.logging.Logger.getLogger(BatchRunHelper.class.getName()).log(java.util.logging.Level.SEVERE, exception.getMessage(), exception);
         }

         batchLog.setItemData(text);
         if (batchResult.getExceptions().size() > 0) {
            for(Exception exception4 : (Iterable<Exception>)(Iterable<?>)(batchResult.getExceptions())) {
               BatchSkipLog batchSkipLog = new BatchSkipLog();
               batchSkipLog.setBatchId(batchContext.getBatch().getId());
               batchSkipLog.setMsg(exception4.getMessage());
               batchSkipLog.setDetail(objectMapper.writeValueAsString(exception4.getCause()));

               try {
                  if (exception4 instanceof ReaderException) {
                     batchSkipLog.setType("reader");
                  } else if (exception4 instanceof ProcessorException) {
                     ProcessorException processorException = (ProcessorException)exception4;
                     batchSkipLog.setType("processor");
                     batchSkipLog.setData(objectMapper.writeValueAsString(processorException.getData()));
                  } else if (!(exception4 instanceof WriterException) && !(exception4 instanceof SqlBatchException)) {
                     batchSkipLog.setType("base");
                  } else {
                     batchSkipLog.setType("writer");
                  }

                  batchSkipLog.setGroupId(batchLog.getGroupId());
                  batchSkipLog.setProjectId(batchLog.getProjectId());
                  batchSkipLog.setLogId(batchLog.getId());
                  BatchSkipLogManager.ins.add(batchSkipLog);
               } catch (Exception exception2) {
                  java.util.logging.Logger.getLogger(BatchRunHelper.class.getName()).log(java.util.logging.Level.SEVERE, exception4.getMessage(), exception4);
               }
            }
         }

         BatchManager.ins.updateStatus(batchLog.getBatchId(), batchContext.getBatch().getStatus());
         BatchLogManager.ins.updateStatus(batchLog);
      } catch (Exception exception3) {
         BatchRunHelper.logger.error(exception3.getMessage());
         java.util.logging.Logger.getLogger(BatchRunHelper.class.getName()).log(java.util.logging.Level.SEVERE, exception3.getMessage(), exception3);
      }

   }

   private static void processBatchContext(BatchContext batchContext) {
      BatchLog batchLog = batchContext.getBatchLog();

      try {
         BatchResult batchResult = batchContext.getResult();
         batchLog.setBatchId(batchResult.getBatchId());
         batchLog.setBatchName(batchResult.getBatchName());
         batchLog.setStartTime(batchResult.getStartTime());
         if (batchResult.getEndTime() != null) {
            batchLog.setEndTime(batchResult.getEndTime());
            batchLog.setTime(batchResult.getEndTime().getTime() - batchResult.getStartTime().getTime());
         }

         Batch batch = batchContext.getBatch();
         batchLog.setProjectId(batch.getProjectId());
         Project project = ProjectManager.ins.get(batch.getProjectId());
         Group group = GroupManager.ins.get(project.getGroupId());
         batchLog.setGroupId(group.getId());
         batchLog.setGroupName(group.getName());
         batchLog.setProjectId(batch.getProjectId());
         batchLog.setProjectName(project.getName());
         batchLog.setMsg(batchResult.getMsg());
         batchLog.setStatus(batchResult.getStatus());
         ObjectMapper objectMapper = new ObjectMapper();
         String text = null;

         try {
            text = objectMapper.writeValueAsString(batchContext.getParams());
         } catch (Exception exception) {
            BatchRunHelper.logger.error(exception.getMessage());
            java.util.logging.Logger.getLogger(BatchRunHelper.class.getName()).log(java.util.logging.Level.SEVERE, exception.getMessage(), exception);
         }

         batchLog.setInParams(text);
         batchLog.setPacketId(batch.getPacketId());
         String text2 = null;

         try {
            text2 = objectMapper.writeValueAsString(batch.getComplexPacketParams());
         } catch (Exception exception2) {
            BatchRunHelper.logger.error(exception2.getMessage());
            java.util.logging.Logger.getLogger(BatchRunHelper.class.getName()).log(java.util.logging.Level.SEVERE, exception2.getMessage(), exception2);
         }

         batchLog.setPacketParams(text2);
      } catch (Exception exception3) {
         BatchRunHelper.logger.error(exception3.getMessage());
         java.util.logging.Logger.getLogger(BatchRunHelper.class.getName()).log(java.util.logging.Level.SEVERE, exception3.getMessage(), exception3);
      }

   }

   private static BatchListener resolveBatchListener(Batch batch) {
      Object defaultBatchListener = new DefaultBatchListener();

      try {
         if (StringUtils.isNotBlank(batch.getListener())) {
            defaultBatchListener = (BatchListener)Utils.getApplicationContext().getBean(batch.getListener());
         }
      } catch (Exception exception) {
         BatchRunHelper.logger.error(exception.getMessage());
         java.util.logging.Logger.getLogger(BatchRunHelper.class.getName()).log(java.util.logging.Level.SEVERE, exception.getMessage(), exception);
      }

      return (BatchListener)defaultBatchListener;
   }
}
