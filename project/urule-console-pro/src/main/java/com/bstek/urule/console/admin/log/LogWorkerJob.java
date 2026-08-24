package com.bstek.urule.console.admin.log;

import com.bstek.urule.console.database.manager.group.GroupManager;
import com.bstek.urule.console.database.manager.log.KnowledgeLogManager;
import com.bstek.urule.console.database.manager.log.LoginLogManager;
import com.bstek.urule.console.database.manager.log.OperationLogManager;
import com.bstek.urule.console.database.manager.packet.PacketManager;
import com.bstek.urule.console.database.manager.project.ProjectManager;
import com.bstek.urule.console.database.model.Group;
import com.bstek.urule.console.database.model.KnowledgeLog;
import com.bstek.urule.console.database.model.LoginLog;
import com.bstek.urule.console.database.model.OperationLog;
import com.bstek.urule.console.database.model.Packet;
import com.bstek.urule.console.database.model.Project;
import com.bstek.urule.console.database.model.URuleLog;
import com.bstek.urule.console.database.util.JdbcUtils;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/** Drains audit records in small batches and persists them on a daemon thread. */
public class LogWorkerJob implements Runnable {
   private static final Log LOGGER = LogFactory.getLog(LogWorkerJob.class);
   private static final int MAX_BATCH_SIZE = 200;

   private static final String INSERT_KNOWLEDGE_LOG =
         "insert into URULE_LOG_KNOWLEDGE (ID_, USER_, KNOWLEDGE_ID_, KNOWLEDGE_NAME_, VERSION_, "
               + "IN_PARAMS_, OUT_PARAMS_, LOGS_, TIME_, IP_, USER_AGENT_, START_TIME_, END_TIME_, "
               + "GROUP_ID_, GROUP_NAME_, PROJECT_ID_, PROJECT_NAME_, CREATE_DATE_) "
               + "values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
   private static final String INSERT_LOGIN_LOG =
         "insert into URULE_LOG_USERLOGIN (ID_, USER_ID_, USER_NAME_, IP_, USER_AGENT_, CREATE_DATE_) "
               + "values (?, ?, ?, ?, ?, ?)";
   private static final String INSERT_OPERATION_LOG =
         "insert into URULE_LOG_OPERATION (ID_, USER_ID_, USER_NAME_, GROUP_ID_, GROUP_NAME_, "
               + "PROJECT_ID_, PROJECT_NAME_, CATEGORY_, ACTION_, ITEM_ID_, CONTENT_, CREATE_DATE_) "
               + "values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

   @Override
   public void run() {
      while (!Thread.currentThread().isInterrupted()) {
         try {
            Thread.sleep(10L);
            persistNextBatch();
         } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            LOGGER.info("Audit log worker stopped after interruption");
         }
      }
   }

   private void persistNextBatch() {
      List<URuleLog> logs = new ArrayList<>();
      while (!LogQueue.isQueueEmpty() && logs.size() < MAX_BATCH_SIZE) {
         URuleLog log = LogQueue.pollLog();
         if (log != null) {
            logs.add(log);
         }
      }
      if (logs.isEmpty()) {
         return;
      }

      Connection connection = JdbcUtils.getConnection();
      try {
         persistLogs(connection, logs);
      } catch (Exception exception) {
         LOGGER.error("Could not persist a batch of " + logs.size() + " audit logs", exception);
      } finally {
         JdbcUtils.closeConnection(connection);
      }
   }

   private void persistLogs(Connection connection, List<URuleLog> logs) throws SQLException {
      PreparedStatement knowledgeStatement = null;
      PreparedStatement loginStatement = null;
      PreparedStatement operationStatement = null;
      try {
         knowledgeStatement = connection.prepareStatement(INSERT_KNOWLEDGE_LOG);
         loginStatement = connection.prepareStatement(INSERT_LOGIN_LOG);
         operationStatement = connection.prepareStatement(INSERT_OPERATION_LOG);

         boolean hasKnowledgeLogs = false;
         boolean hasLoginLogs = false;
         boolean hasOperationLogs = false;

         for (URuleLog log : logs) {
            if (log instanceof LoginLog) {
               hasLoginLogs = true;
               LoginLogManager.ins.addBatch(loginStatement, (LoginLog)log);
            } else if (log instanceof OperationLog) {
               enrichOperationLog((OperationLog)log);
               hasOperationLogs = true;
               OperationLogManager.ins.addBatch(operationStatement, (OperationLog)log);
            } else if (log instanceof KnowledgeLog
                  && enrichKnowledgeLog((KnowledgeLog)log)) {
               hasKnowledgeLogs = true;
               KnowledgeLogManager.ins.addBatch(knowledgeStatement, (KnowledgeLog)log);
            }
         }

         if (hasLoginLogs) {
            loginStatement.executeBatch();
         }
         if (hasOperationLogs) {
            operationStatement.executeBatch();
         }
         if (hasKnowledgeLogs) {
            knowledgeStatement.executeBatch();
         }
      } finally {
         JdbcUtils.closeStatement(knowledgeStatement);
         JdbcUtils.closeStatement(loginStatement);
         JdbcUtils.closeStatement(operationStatement);
      }
   }

   private void enrichOperationLog(OperationLog log) {
      Group group = GroupManager.ins.get(log.getGroupId());
      if (group != null) {
         log.setGroupName(group.getName());
      }
      if (log.getProjectId() != null) {
         Project project = ProjectManager.ins.get(log.getProjectId());
         if (project != null) {
            log.setProjectName(project.getName());
         }
      }
   }

   private boolean enrichKnowledgeLog(KnowledgeLog log) {
      Packet packet = PacketManager.ins.load(log.getKnowledgeId());
      if (packet == null) {
         return false;
      }

      log.setKnowledgeName(packet.getName());
      log.setProjectId(packet.getProjectId());
      Project project = ProjectManager.ins.get(packet.getProjectId());
      if (project != null) {
         log.setGroupId(project.getGroupId());
         log.setProjectName(project.getName());
         Group group = GroupManager.ins.get(project.getGroupId());
         if (group != null) {
            log.setGroupName(group.getName());
         }
      }
      return true;
   }
}
