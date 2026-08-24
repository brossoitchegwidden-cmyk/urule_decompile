package com.bstek.urule.console.database.manager.batch;

import com.bstek.urule.console.batch.BatchStatus;
import com.bstek.urule.console.database.model.batch.Batch;
import com.bstek.urule.console.database.util.JdbcUtils;
import com.bstek.urule.exception.RuleException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.List;

public class BatchManagerImpl implements BatchManager {
   public Batch get(Long id) {
      List items = this.createQuery().id(id).list();
      return items.size() > 0 ? (Batch)items.get(0) : null;
   }

   public void add(Batch batch) {
      Connection connection = JdbcUtils.getConnection();

      try {
         PreparedStatement preparedStatement = connection.prepareStatement("insert into URULE_BATCH (NAME_, ASYNC_, CALLBACK_URL_, STATUS_, LISTENER_, SKIP_LIMIT_, THREAD_MULTI_, THREAD_SIZE_, THREAD_DATA_SIZE_, PROVIDER_ID_, RESOLVER_ID_, PACKET_ID_, PACKET_INPUT_DATA_, REST_ENABLE_, REST_SECURITY_ENABLE_, REST_SECURITY_USER_, REST_SECURITY_PASSWORD_, INPUT_DATA_, DESC_, CREATE_USER_, CREATE_DATE_, ID_, PROJECT_ID_, ENABLE_) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
         preparedStatement.setString(1, batch.getName());
         preparedStatement.setBoolean(2, batch.isAsync());
         preparedStatement.setString(3, batch.getCallbackUrl());
         preparedStatement.setString(4, batch.getStatus().name());
         preparedStatement.setString(5, batch.getListener());
         preparedStatement.setInt(6, batch.getSkipLimit());
         preparedStatement.setBoolean(7, batch.isThreadMulti());
         preparedStatement.setInt(8, batch.getThreadSize());
         preparedStatement.setInt(9, batch.getThreadDataSize());
         preparedStatement.setLong(10, batch.getProviderId());
         preparedStatement.setLong(11, batch.getResolverId());
         preparedStatement.setLong(12, batch.getPacketId());
         preparedStatement.setString(13, batch.getPacketInputData());
         preparedStatement.setBoolean(14, batch.isRestEnable());
         preparedStatement.setBoolean(15, batch.isRestSecurityEnable());
         preparedStatement.setString(16, batch.getRestSecurityUser());
         preparedStatement.setString(17, batch.getRestSecurityPassword());
         preparedStatement.setString(18, batch.getInputData());
         preparedStatement.setString(19, batch.getDesc());
         preparedStatement.setString(20, batch.getCreateUser());
         preparedStatement.setTimestamp(21, new Timestamp(batch.getCreateDate().getTime()));
         preparedStatement.setLong(22, batch.getId());
         preparedStatement.setLong(23, batch.getProjectId());
         preparedStatement.setBoolean(24, batch.isEnable());
         preparedStatement.executeUpdate();
         JdbcUtils.closeStatement(preparedStatement);
      } catch (Exception exception) {
         throw new RuleException(exception);
      } finally {
         JdbcUtils.closeConnection(connection);
      }

   }

   public void update(Batch batch) {
      Connection connection = JdbcUtils.getConnection();

      try {
         PreparedStatement preparedStatement = connection.prepareStatement("update URULE_BATCH set NAME_=?, ASYNC_=?, CALLBACK_URL_=?, LISTENER_=?, SKIP_LIMIT_=?, THREAD_MULTI_=?, THREAD_SIZE_=?, THREAD_DATA_SIZE_=?, PROVIDER_ID_=?, RESOLVER_ID_=?, PACKET_ID_=?, PACKET_INPUT_DATA_=?, REST_ENABLE_=?, REST_SECURITY_ENABLE_=?, REST_SECURITY_USER_=?, REST_SECURITY_PASSWORD_=?, INPUT_DATA_=?, DESC_=?, UPDATE_USER_=?, UPDATE_DATE_=?, ENABLE_=? where ID_=?");
         preparedStatement.setString(1, batch.getName());
         preparedStatement.setBoolean(2, batch.isAsync());
         preparedStatement.setString(3, batch.getCallbackUrl());
         preparedStatement.setString(4, batch.getListener());
         preparedStatement.setInt(5, batch.getSkipLimit());
         preparedStatement.setBoolean(6, batch.isThreadMulti());
         preparedStatement.setInt(7, batch.getThreadSize());
         preparedStatement.setInt(8, batch.getThreadDataSize());
         preparedStatement.setLong(9, batch.getProviderId());
         preparedStatement.setLong(10, batch.getResolverId());
         preparedStatement.setLong(11, batch.getPacketId());
         preparedStatement.setString(12, batch.getPacketInputData());
         preparedStatement.setBoolean(13, batch.isRestEnable());
         preparedStatement.setBoolean(14, batch.isRestSecurityEnable());
         preparedStatement.setString(15, batch.getRestSecurityUser());
         preparedStatement.setString(16, batch.getRestSecurityPassword());
         preparedStatement.setString(17, batch.getInputData());
         preparedStatement.setString(18, batch.getDesc());
         preparedStatement.setString(19, batch.getUpdateUser());
         preparedStatement.setTimestamp(20, new Timestamp(batch.getUpdateDate().getTime()));
         preparedStatement.setBoolean(21, batch.isEnable());
         preparedStatement.setLong(22, batch.getId());
         preparedStatement.executeUpdate();
         JdbcUtils.closeStatement(preparedStatement);
      } catch (Exception exception) {
         throw new RuleException(exception);
      } finally {
         JdbcUtils.closeConnection(connection);
      }

   }

   public void remove(Long id) {
      Connection connection = JdbcUtils.getConnection();

      try {
         PreparedStatement preparedStatement = connection.prepareStatement("delete FROM URULE_BATCH where ID_=?");
         preparedStatement.setLong(1, id);
         preparedStatement.executeUpdate();
         JdbcUtils.closeStatement(preparedStatement);
      } catch (Exception exception) {
         throw new RuleException(exception);
      } finally {
         JdbcUtils.closeConnection(connection);
      }

   }

   public BatchQuery createQuery() {
      return new BatchQueryImpl();
   }

   public void updateStatus(Long batchId, BatchStatus status) {
      Connection connection = JdbcUtils.getConnection();

      try {
         PreparedStatement preparedStatement = connection.prepareStatement("update URULE_BATCH set STATUS_=? where ID_=?");
         preparedStatement.setString(1, status.name());
         preparedStatement.setLong(2, batchId);
         preparedStatement.executeUpdate();
         JdbcUtils.closeStatement(preparedStatement);
      } catch (Exception exception) {
         throw new RuleException(exception);
      } finally {
         JdbcUtils.closeConnection(connection);
      }

   }

   public void removeByProjectId(Long id) {
      Connection connection = JdbcUtils.getConnection();

      try {
         PreparedStatement preparedStatement = connection.prepareStatement("delete FROM URULE_BATCH where PROJECT_ID_=?");
         preparedStatement.setLong(1, id);
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
         PreparedStatement preparedStatement = connection.prepareStatement("delete FROM URULE_BATCH where GROUP_ID_=?");
         preparedStatement.setString(1, groupId);
         preparedStatement.executeUpdate();
         JdbcUtils.closeStatement(preparedStatement);
      } catch (Exception exception) {
         throw new RuleException(exception);
      } finally {
         JdbcUtils.closeConnection(connection);
      }

   }
}
