package com.bstek.urule.console.admin.log;

import com.bstek.urule.console.ContextHolder;
import com.bstek.urule.console.database.model.KnowledgeLog;
import com.bstek.urule.console.database.model.LoginLog;
import com.bstek.urule.console.database.model.OperationLog;
import com.bstek.urule.console.database.model.URuleLog;
import com.bstek.urule.console.type.RuleFileType;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/** Creates audit records and forwards them to the configured appenders. */
public final class SystemLogUtils {
   private static final Log LOGGER = LogFactory.getLog(SystemLogUtils.class);
   private static volatile LogAppenderManager appenderManager;

   private SystemLogUtils() {
   }

   public static void addGroupOperationLog(String category, String action, Long itemId, String content) {
      addGroupOperationLog(category, action, itemId.toString(), content);
   }

   public static void addGroupOperationLog(String category, String action, String itemId, String content) {
      OperationLog operationLog = URuleLogService.ins.getOperationLog();
      operationLog.setGroupId(ContextHolder.getGroupId());
      operationLog.setCategory(category);
      operationLog.setAction(action);
      operationLog.setContent(content);
      operationLog.setItemId(itemId);
      append(operationLog);
   }

   public static void addRuleFileOperationLog(String fileName, String action, Long itemId, String content) {
      RuleFileType fileType = RuleFileType.getRuleFileType(fileName);
      addProjectOperationLog(fileType.name(), action, itemId, content.replace("{type}", fileType.getLabel()));
   }

   public static void addProjectOperationLog(String category, String action, Long itemId, String content) {
      addProjectOperationLog(category, action, itemId.toString(), content);
   }

   public static void addProjectOperationLog(String category, String action, String itemId, String content) {
      OperationLog operationLog = URuleLogService.ins.getOperationLog();
      operationLog.setGroupId(ContextHolder.getGroupId());
      operationLog.setProjectId(ContextHolder.getProjectId());
      operationLog.setCategory(category);
      operationLog.setAction(action);
      operationLog.setItemId(itemId);
      operationLog.setContent(content);
      append(operationLog);
   }

   public static void addKnowledgeLog(KnowledgeLog knowledgeLog) {
      append(knowledgeLog);
   }

   public static void addLoginLog(HttpServletRequest request) {
      LoginLog loginLog = URuleLogService.ins.getLoginLog();
      append(loginLog);
   }

   private static void append(URuleLog log) {
      if (LOGGER.isDebugEnabled()) {
         LOGGER.debug(log.toString());
      }

      try {
         getAppenderManager().putLog(log);
      } catch (InterruptedException exception) {
         Thread.currentThread().interrupt();
         LOGGER.warn("Interrupted while queuing a URule audit log", exception);
      }
   }

   private static LogAppenderManager getAppenderManager() {
      LogAppenderManager result = appenderManager;
      if (result == null) {
         synchronized (SystemLogUtils.class) {
            result = appenderManager;
            if (result == null) {
               result = new LogAppenderManager();
               appenderManager = result;
            }
         }
      }
      return result;
   }

   static {
      String threadName = "LogWorkerJob";
      Thread worker = new Thread(new LogWorkerJob(), threadName);
      worker.setDaemon(true);
      worker.start();
      LOGGER.info("Thread [" + threadName + "] started");
   }
}
