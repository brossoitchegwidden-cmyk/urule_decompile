package com.bstek.urule.console.database.manager.log.batch;

import com.bstek.urule.console.database.IDGenerator;
import com.bstek.urule.console.database.IDType;
import com.bstek.urule.console.database.model.batch.BatchSkipLog;
import com.bstek.urule.console.database.util.JdbcUtils;
import com.bstek.urule.exception.RuleException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;

public class BatchSkipLogManagerImpl implements BatchSkipLogManager {
   public void add(BatchSkipLog log) {
      Connection connection = JdbcUtils.getConnection();

      try {
         log.setCreateDate(new Timestamp(System.currentTimeMillis()));
         long longValue = IDGenerator.getInstance().nextId(IDType.LOG_BATCH_SKIP);
         PreparedStatement preparedStatement = connection.prepareStatement("insert into URULE_LOG_BATCH_SKIP (ID_, LOG_ID_, BATCH_ID_, TYPE_, MSG_, DETAIL_, DATA_, GROUP_ID_, PROJECT_ID_, CREATE_DATE_) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
         log.setId(longValue);
         preparedStatement.setLong(1, log.getId());
         preparedStatement.setLong(2, log.getLogId());
         preparedStatement.setLong(3, log.getBatchId());
         preparedStatement.setString(4, log.getType());
         preparedStatement.setString(5, log.getMsg());
         preparedStatement.setString(6, log.getDetail());
         preparedStatement.setString(7, log.getData());
         preparedStatement.setString(8, log.getGroupId());
         preparedStatement.setLong(9, log.getProjectId());
         preparedStatement.setTimestamp(10, new Timestamp(log.getCreateDate().getTime()));
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
         PreparedStatement preparedStatement = connection.prepareStatement("delete FROM URULE_LOG_BATCH_SKIP where GROUP_ID_=?");
         preparedStatement.setString(1, groupId);
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
         PreparedStatement preparedStatement = connection.prepareStatement("delete FROM URULE_LOG_BATCH_SKIP where PROJECT_ID_=?");
         preparedStatement.setLong(1, projectId);
         preparedStatement.executeUpdate();
         JdbcUtils.closeStatement(preparedStatement);
      } catch (Exception exception) {
         throw new RuleException(exception);
      } finally {
         JdbcUtils.closeConnection(connection);
      }

   }

   public BatchSkipLogQuery newQuery() {
      return new BatchSkipLogQueryImpl();
   }
}
