package com.bstek.urule.console.database.manager.log.batch;

import com.bstek.urule.console.database.IDGenerator;
import com.bstek.urule.console.database.IDType;
import com.bstek.urule.console.database.model.batch.BatchLog;
import com.bstek.urule.console.database.util.JdbcUtils;
import com.bstek.urule.exception.RuleException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;

public class BatchLogManagerImpl implements BatchLogManager {
   public void updateStatus(BatchLog log) {
      Connection connection = JdbcUtils.getConnection();

      try {
         PreparedStatement preparedStatement = connection.prepareStatement("update URULE_LOG_BATCH set STATUS_=?, END_TIME_=?, TIME_=?, MSG_=?, ITEM_DATA_=?, READ_COUNT_=?, FILTER_COUNT_=? where ID_=?");
         preparedStatement.setString(1, log.getStatus().name());
         preparedStatement.setTimestamp(2, new Timestamp(log.getEndTime().getTime()));
         preparedStatement.setLong(3, log.getEndTime().getTime() - log.getStartTime().getTime());
         preparedStatement.setString(4, log.getMsg());
         preparedStatement.setString(5, log.getItemData());
         preparedStatement.setInt(6, log.getReadCount());
         preparedStatement.setInt(7, log.getFilterCount());
         preparedStatement.setLong(8, log.getId());
         preparedStatement.executeUpdate();
         JdbcUtils.closeStatement(preparedStatement);
      } catch (Exception exception) {
         throw new RuleException(exception);
      } finally {
         JdbcUtils.closeConnection(connection);
      }

   }
   public void add(BatchLog log) {
      Connection connection = JdbcUtils.getConnection();

      try {
         log.setCreateDate(new Timestamp(System.currentTimeMillis()));
         long longValue = IDGenerator.getInstance().nextId(IDType.LOG_BATCH);
         PreparedStatement preparedStatement = connection.prepareStatement("insert into URULE_LOG_BATCH (ID_, USER_, IP_, USER_AGENT_, BATCH_ID_, BATCH_NAME_, STATUS_, READ_COUNT_, FILTER_COUNT_, ITEM_DATA_, IN_PARAMS_, PACKET_ID_, PACKET_PARAMS_, START_TIME_, END_TIME_, TIME_, MSG_, DETAIL_, GROUP_ID_, GROUP_NAME_, PROJECT_ID_, PROJECT_NAME_, CREATE_DATE_) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
         log.setId(longValue);
         preparedStatement.setLong(1, log.getId());
         preparedStatement.setString(2, log.getUserId());
         preparedStatement.setString(3, log.getIp());
         preparedStatement.setString(4, log.getUserAgent());
         preparedStatement.setLong(5, log.getBatchId());
         preparedStatement.setString(6, log.getBatchName());
         preparedStatement.setString(7, log.getStatus().name());
         preparedStatement.setInt(8, log.getReadCount());
         preparedStatement.setInt(9, log.getFilterCount());
         preparedStatement.setString(10, log.getItemData());
         preparedStatement.setString(11, log.getInParams());
         preparedStatement.setLong(12, log.getPacketId());
         preparedStatement.setString(13, log.getPacketParams());
         preparedStatement.setTimestamp(14, new Timestamp(log.getStartTime().getTime()));
         if (null == log.getEndTime()) {
            preparedStatement.setObject(15, (Object)null);
            preparedStatement.setObject(16, (Object)null);
         } else {
            preparedStatement.setTimestamp(15, new Timestamp(log.getEndTime().getTime()));
            preparedStatement.setLong(16, log.getEndTime().getTime() - log.getStartTime().getTime());
         }

         preparedStatement.setString(17, log.getMsg());
         preparedStatement.setString(18, log.getDetail());
         preparedStatement.setString(19, log.getGroupId());
         preparedStatement.setString(20, log.getGroupName());
         preparedStatement.setLong(21, log.getProjectId());
         preparedStatement.setString(22, log.getProjectName());
         preparedStatement.setTimestamp(23, new Timestamp(log.getCreateDate().getTime()));
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
         PreparedStatement preparedStatement = connection.prepareStatement("delete FROM URULE_LOG_BATCH where GROUP_ID_=?");
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
         PreparedStatement preparedStatement = connection.prepareStatement("delete FROM URULE_LOG_BATCH where PROJECT_ID_=?");
         preparedStatement.setLong(1, projectId);
         preparedStatement.executeUpdate();
         JdbcUtils.closeStatement(preparedStatement);
      } catch (Exception exception) {
         throw new RuleException(exception);
      } finally {
         JdbcUtils.closeConnection(connection);
      }

   }

   public BatchLogQuery newQuery() {
      return new BatchLogQueryImpl();
   }
}
