package com.bstek.urule.console.database.manager.log;

import com.bstek.urule.console.database.IDGenerator;
import com.bstek.urule.console.database.IDType;
import com.bstek.urule.console.database.model.KnowledgeLog;
import com.bstek.urule.console.database.util.JdbcUtils;
import com.bstek.urule.exception.RuleException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

public class KnowledgeLogManagerImpl implements KnowledgeLogManager {
   public void add(KnowledgeLog log) {
      Connection connection = JdbcUtils.getConnection();

      try {
         log.setCreateDate(new Timestamp(System.currentTimeMillis()));
         long longValue = IDGenerator.getInstance().nextId(IDType.LOG_KNOWLEDGE);
         PreparedStatement preparedStatement = connection.prepareStatement("insert into URULE_LOG_KNOWLEDGE (ID_, USER_, KNOWLEDGE_ID_, KNOWLEDGE_NAME_, VERSION_, IN_PARAMS_, OUT_PARAMS_, LOGS_, TIME_, IP_, USER_AGENT_, START_TIME_, END_TIME_, GROUP_ID_, GROUP_NAME_, PROJECT_ID_, PROJECT_NAME_, CREATE_DATE_) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
         log.setId(longValue);
         preparedStatement.setLong(1, log.getId());
         preparedStatement.setString(2, log.getUsername());
         preparedStatement.setLong(3, log.getKnowledgeId());
         preparedStatement.setString(4, log.getKnowledgeName());
         preparedStatement.setString(5, log.getVersion());
         preparedStatement.setString(6, log.getInParams());
         preparedStatement.setString(7, log.getOutParams());
         preparedStatement.setString(8, log.getLogs());
         preparedStatement.setLong(9, log.getTime());
         preparedStatement.setString(10, log.getIp());
         preparedStatement.setString(11, log.getUserAgent());
         preparedStatement.setTimestamp(12, new Timestamp(log.getStartTime().getTime()));
         preparedStatement.setTimestamp(13, new Timestamp(log.getEndTime().getTime()));
         preparedStatement.setString(14, log.getGroupId());
         preparedStatement.setString(15, log.getGroupName());
         preparedStatement.setLong(16, log.getProjectId());
         preparedStatement.setString(17, log.getProjectName());
         preparedStatement.setTimestamp(18, new Timestamp(log.getCreateDate().getTime()));
         preparedStatement.executeUpdate();
         JdbcUtils.closeStatement(preparedStatement);
      } catch (Exception exception) {
         throw new RuleException(exception);
      } finally {
         JdbcUtils.closeConnection(connection);
      }

   }
   public void removeByProject(Long projectId) {
      Connection connection = JdbcUtils.getConnection();

      try {
         PreparedStatement preparedStatement = connection.prepareStatement("delete FROM URULE_LOG_KNOWLEDGE where PROJECT_ID_=?");
         preparedStatement.setLong(1, projectId);
         preparedStatement.executeUpdate();
         JdbcUtils.closeStatement(preparedStatement);
      } catch (Exception exception) {
         throw new RuleException(exception);
      } finally {
         JdbcUtils.closeConnection(connection);
      }

   }
   public void removeByGroupId(String groupId) {
      Connection connection = JdbcUtils.getConnection();

      try {
         PreparedStatement preparedStatement = connection.prepareStatement("delete FROM URULE_LOG_KNOWLEDGE where GROUP_ID_=?");
         preparedStatement.setString(1, groupId);
         preparedStatement.executeUpdate();
         JdbcUtils.closeStatement(preparedStatement);
      } catch (Exception exception) {
         throw new RuleException(exception);
      } finally {
         JdbcUtils.closeConnection(connection);
      }

   }

   public KnowledgeLogQuery newQuery() {
      return new KnowledgeLogQueryImpl();
   }

   public KnowledgeLogCountQuery newCountQuery() {
      return new KnowledgeLogCountQueryImpl();
   }
   public void addBatch(PreparedStatement stmt, KnowledgeLog log) throws SQLException {
      log.setCreateDate(new Timestamp(System.currentTimeMillis()));
      long longValue = IDGenerator.getInstance().nextId(IDType.LOG_KNOWLEDGE);
      log.setId(longValue);
      stmt.setLong(1, log.getId());
      stmt.setString(2, log.getUsername());
      stmt.setLong(3, log.getKnowledgeId());
      stmt.setString(4, log.getKnowledgeName());
      stmt.setString(5, log.getVersion());
      stmt.setString(6, log.getInParams());
      stmt.setString(7, log.getOutParams());
      stmt.setString(8, log.getLogs());
      stmt.setLong(9, log.getTime());
      stmt.setString(10, log.getIp());
      stmt.setString(11, log.getUserAgent());
      stmt.setTimestamp(12, new Timestamp(log.getStartTime().getTime()));
      stmt.setTimestamp(13, new Timestamp(log.getEndTime().getTime()));
      stmt.setString(14, log.getGroupId());
      stmt.setString(15, log.getGroupName());
      stmt.setLong(16, log.getProjectId());
      stmt.setString(17, log.getProjectName());
      stmt.setTimestamp(18, new Timestamp(log.getCreateDate().getTime()));
      stmt.addBatch();
   }
}
