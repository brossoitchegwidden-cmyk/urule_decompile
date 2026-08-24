package com.bstek.urule.console.admin.log;

import com.bstek.urule.console.config.Configure;
import com.bstek.urule.console.database.model.KnowledgeLog;
import com.bstek.urule.console.database.model.LoginLog;
import com.bstek.urule.console.database.model.OperationLog;
import com.bstek.urule.console.database.model.URuleLog;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/** Sends enabled audit record types to the database log queue. */
public class DBStoreAppender implements LogAppender {
   private static final Log LOGGER = LogFactory.getLog(DBStoreAppender.class);

   private final boolean storeLoginLogs;
   private final boolean storeOperationLogs;
   private final boolean storeKnowledgeLogs;

   public DBStoreAppender() {
      Configure configure = Configure.getConfigure();
      storeLoginLogs = configure.getBoolean("urule.store.log.login", true);
      // Keep the historic property spelling for deployment compatibility.
      storeOperationLogs = configure.getBoolean("urule.store.log.opertion", true);
      storeKnowledgeLogs = configure.getBoolean("urule.store.log.knowledge", true);
   }

   @Override
   public void putLog(URuleLog log) {
      try {
         if (log instanceof LoginLog && storeLoginLogs) {
            LogQueue.putLog(log);
         } else if (log instanceof OperationLog && storeOperationLogs) {
            LogQueue.putLog(log);
         } else if (log instanceof KnowledgeLog && storeKnowledgeLogs) {
            LogQueue.putLog(log);
         }
      } catch (InterruptedException exception) {
         Thread.currentThread().interrupt();
         LOGGER.warn("Interrupted while queuing a database audit log", exception);
      }
   }
}
