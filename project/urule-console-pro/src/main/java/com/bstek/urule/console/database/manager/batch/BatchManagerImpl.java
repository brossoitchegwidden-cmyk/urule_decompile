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
   public Batch get(Long var1) {
      List var2 = this.createQuery().id(var1).list();
      return var2.size() > 0 ? (Batch)var2.get(0) : null;
   }

   public void add(Batch var1) {
      Connection var2 = JdbcUtils.getConnection();

      try {
         PreparedStatement var3 = var2.prepareStatement("insert into URULE_BATCH (NAME_, ASYNC_, CALLBACK_URL_, STATUS_, LISTENER_, SKIP_LIMIT_, THREAD_MULTI_, THREAD_SIZE_, THREAD_DATA_SIZE_, PROVIDER_ID_, RESOLVER_ID_, PACKET_ID_, PACKET_INPUT_DATA_, REST_ENABLE_, REST_SECURITY_ENABLE_, REST_SECURITY_USER_, REST_SECURITY_PASSWORD_, INPUT_DATA_, DESC_, CREATE_USER_, CREATE_DATE_, ID_, PROJECT_ID_, ENABLE_) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
         var3.setString(1, var1.getName());
         var3.setBoolean(2, var1.isAsync());
         var3.setString(3, var1.getCallbackUrl());
         var3.setString(4, var1.getStatus().name());
         var3.setString(5, var1.getListener());
         var3.setInt(6, var1.getSkipLimit());
         var3.setBoolean(7, var1.isThreadMulti());
         var3.setInt(8, var1.getThreadSize());
         var3.setInt(9, var1.getThreadDataSize());
         var3.setLong(10, var1.getProviderId());
         var3.setLong(11, var1.getResolverId());
         var3.setLong(12, var1.getPacketId());
         var3.setString(13, var1.getPacketInputData());
         var3.setBoolean(14, var1.isRestEnable());
         var3.setBoolean(15, var1.isRestSecurityEnable());
         var3.setString(16, var1.getRestSecurityUser());
         var3.setString(17, var1.getRestSecurityPassword());
         var3.setString(18, var1.getInputData());
         var3.setString(19, var1.getDesc());
         var3.setString(20, var1.getCreateUser());
         var3.setTimestamp(21, new Timestamp(var1.getCreateDate().getTime()));
         var3.setLong(22, var1.getId());
         var3.setLong(23, var1.getProjectId());
         var3.setBoolean(24, var1.isEnable());
         var3.executeUpdate();
         JdbcUtils.closeStatement(var3);
      } catch (Exception var7) {
         throw new RuleException(var7);
      } finally {
         JdbcUtils.closeConnection(var2);
      }

   }

   public void update(Batch var1) {
      Connection var2 = JdbcUtils.getConnection();

      try {
         PreparedStatement var3 = var2.prepareStatement("update URULE_BATCH set NAME_=?, ASYNC_=?, CALLBACK_URL_=?, LISTENER_=?, SKIP_LIMIT_=?, THREAD_MULTI_=?, THREAD_SIZE_=?, THREAD_DATA_SIZE_=?, PROVIDER_ID_=?, RESOLVER_ID_=?, PACKET_ID_=?, PACKET_INPUT_DATA_=?, REST_ENABLE_=?, REST_SECURITY_ENABLE_=?, REST_SECURITY_USER_=?, REST_SECURITY_PASSWORD_=?, INPUT_DATA_=?, DESC_=?, UPDATE_USER_=?, UPDATE_DATE_=?, ENABLE_=? where ID_=?");
         var3.setString(1, var1.getName());
         var3.setBoolean(2, var1.isAsync());
         var3.setString(3, var1.getCallbackUrl());
         var3.setString(4, var1.getListener());
         var3.setInt(5, var1.getSkipLimit());
         var3.setBoolean(6, var1.isThreadMulti());
         var3.setInt(7, var1.getThreadSize());
         var3.setInt(8, var1.getThreadDataSize());
         var3.setLong(9, var1.getProviderId());
         var3.setLong(10, var1.getResolverId());
         var3.setLong(11, var1.getPacketId());
         var3.setString(12, var1.getPacketInputData());
         var3.setBoolean(13, var1.isRestEnable());
         var3.setBoolean(14, var1.isRestSecurityEnable());
         var3.setString(15, var1.getRestSecurityUser());
         var3.setString(16, var1.getRestSecurityPassword());
         var3.setString(17, var1.getInputData());
         var3.setString(18, var1.getDesc());
         var3.setString(19, var1.getUpdateUser());
         var3.setTimestamp(20, new Timestamp(var1.getUpdateDate().getTime()));
         var3.setBoolean(21, var1.isEnable());
         var3.setLong(22, var1.getId());
         var3.executeUpdate();
         JdbcUtils.closeStatement(var3);
      } catch (Exception var7) {
         throw new RuleException(var7);
      } finally {
         JdbcUtils.closeConnection(var2);
      }

   }

   public void remove(Long var1) {
      Connection var2 = JdbcUtils.getConnection();

      try {
         PreparedStatement var3 = var2.prepareStatement("delete FROM URULE_BATCH where ID_=?");
         var3.setLong(1, var1);
         var3.executeUpdate();
         JdbcUtils.closeStatement(var3);
      } catch (Exception var7) {
         throw new RuleException(var7);
      } finally {
         JdbcUtils.closeConnection(var2);
      }

   }

   public BatchQuery createQuery() {
      return new BatchQueryImpl();
   }

   public void updateStatus(Long var1, BatchStatus var2) {
      Connection var3 = JdbcUtils.getConnection();

      try {
         PreparedStatement var4 = var3.prepareStatement("update URULE_BATCH set STATUS_=? where ID_=?");
         var4.setString(1, var2.name());
         var4.setLong(2, var1);
         var4.executeUpdate();
         JdbcUtils.closeStatement(var4);
      } catch (Exception var8) {
         throw new RuleException(var8);
      } finally {
         JdbcUtils.closeConnection(var3);
      }

   }

   public void removeByProjectId(Long var1) {
      Connection var2 = JdbcUtils.getConnection();

      try {
         PreparedStatement var3 = var2.prepareStatement("delete FROM URULE_BATCH where PROJECT_ID_=?");
         var3.setLong(1, var1);
         var3.executeUpdate();
         JdbcUtils.closeStatement(var3);
      } catch (Exception var7) {
         throw new RuleException(var7);
      } finally {
         JdbcUtils.closeConnection(var2);
      }

   }

   public void removeByGroupId(String var1) {
      Connection var2 = JdbcUtils.getConnection();

      try {
         PreparedStatement var3 = var2.prepareStatement("delete FROM URULE_BATCH where GROUP_ID_=?");
         var3.setString(1, var1);
         var3.executeUpdate();
         JdbcUtils.closeStatement(var3);
      } catch (Exception var7) {
         throw new RuleException(var7);
      } finally {
         JdbcUtils.closeConnection(var2);
      }

   }
}
