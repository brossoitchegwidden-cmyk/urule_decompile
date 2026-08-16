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
   public void updateStatus(BatchLog var1) {
      Connection var2 = JdbcUtils.getConnection();

      try {
         PreparedStatement var3 = var2.prepareStatement("update URULE_LOG_BATCH set STATUS_=?, END_TIME_=?, TIME_=?, MSG_=?, ITEM_DATA_=?, READ_COUNT_=?, FILTER_COUNT_=? where ID_=?");
         var3.setString(1, var1.getStatus().name());
         var3.setTimestamp(2, new Timestamp(var1.getEndTime().getTime()));
         var3.setLong(3, var1.getEndTime().getTime() - var1.getStartTime().getTime());
         var3.setString(4, var1.getMsg());
         var3.setString(5, var1.getItemData());
         var3.setInt(6, var1.getReadCount());
         var3.setInt(7, var1.getFilterCount());
         var3.setLong(8, var1.getId());
         var3.executeUpdate();
         JdbcUtils.closeStatement(var3);
      } catch (Exception var7) {
         throw new RuleException(var7);
      } finally {
         JdbcUtils.closeConnection(var2);
      }

   }

   public void add(BatchLog var1) {
      Connection var2 = JdbcUtils.getConnection();

      try {
         var1.setCreateDate(new Timestamp(System.currentTimeMillis()));
         long var3 = IDGenerator.getInstance().nextId(IDType.LOG_BATCH);
         PreparedStatement var5 = var2.prepareStatement("insert into URULE_LOG_BATCH (ID_, USER_, IP_, USER_AGENT_, BATCH_ID_, BATCH_NAME_, STATUS_, READ_COUNT_, FILTER_COUNT_, ITEM_DATA_, IN_PARAMS_, PACKET_ID_, PACKET_PARAMS_, START_TIME_, END_TIME_, TIME_, MSG_, DETAIL_, GROUP_ID_, GROUP_NAME_, PROJECT_ID_, PROJECT_NAME_, CREATE_DATE_) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
         var1.setId(var3);
         var5.setLong(1, var1.getId());
         var5.setString(2, var1.getUserId());
         var5.setString(3, var1.getIp());
         var5.setString(4, var1.getUserAgent());
         var5.setLong(5, var1.getBatchId());
         var5.setString(6, var1.getBatchName());
         var5.setString(7, var1.getStatus().name());
         var5.setInt(8, var1.getReadCount());
         var5.setInt(9, var1.getFilterCount());
         var5.setString(10, var1.getItemData());
         var5.setString(11, var1.getInParams());
         var5.setLong(12, var1.getPacketId());
         var5.setString(13, var1.getPacketParams());
         var5.setTimestamp(14, new Timestamp(var1.getStartTime().getTime()));
         if (null == var1.getEndTime()) {
            var5.setObject(15, (Object)null);
            var5.setObject(16, (Object)null);
         } else {
            var5.setTimestamp(15, new Timestamp(var1.getEndTime().getTime()));
            var5.setLong(16, var1.getEndTime().getTime() - var1.getStartTime().getTime());
         }

         var5.setString(17, var1.getMsg());
         var5.setString(18, var1.getDetail());
         var5.setString(19, var1.getGroupId());
         var5.setString(20, var1.getGroupName());
         var5.setLong(21, var1.getProjectId());
         var5.setString(22, var1.getProjectName());
         var5.setTimestamp(23, new Timestamp(var1.getCreateDate().getTime()));
         var5.executeUpdate();
         JdbcUtils.closeStatement(var5);
      } catch (Exception var9) {
         throw new RuleException(var9);
      } finally {
         JdbcUtils.closeConnection(var2);
      }

   }

   public void removeByGroupId(String var1) {
      Connection var2 = JdbcUtils.getConnection();

      try {
         PreparedStatement var3 = var2.prepareStatement("delete FROM URULE_LOG_BATCH where GROUP_ID_=?");
         var3.setString(1, var1);
         var3.executeUpdate();
         JdbcUtils.closeStatement(var3);
      } catch (Exception var7) {
         throw new RuleException(var7);
      } finally {
         JdbcUtils.closeConnection(var2);
      }

   }

   public void removeByProject(Long var1) {
      Connection var2 = JdbcUtils.getConnection();

      try {
         PreparedStatement var3 = var2.prepareStatement("delete FROM URULE_LOG_BATCH where PROJECT_ID_=?");
         var3.setLong(1, var1);
         var3.executeUpdate();
         JdbcUtils.closeStatement(var3);
      } catch (Exception var7) {
         throw new RuleException(var7);
      } finally {
         JdbcUtils.closeConnection(var2);
      }

   }

   public BatchLogQuery newQuery() {
      return new BatchLogQueryImpl();
   }
}
