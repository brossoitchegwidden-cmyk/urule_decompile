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
   public void add(BatchSkipLog var1) {
      Connection var2 = JdbcUtils.getConnection();

      try {
         var1.setCreateDate(new Timestamp(System.currentTimeMillis()));
         long var3 = IDGenerator.getInstance().nextId(IDType.LOG_BATCH_SKIP);
         PreparedStatement var5 = var2.prepareStatement("insert into URULE_LOG_BATCH_SKIP (ID_, LOG_ID_, BATCH_ID_, TYPE_, MSG_, DETAIL_, DATA_, GROUP_ID_, PROJECT_ID_, CREATE_DATE_) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
         var1.setId(var3);
         var5.setLong(1, var1.getId());
         var5.setLong(2, var1.getLogId());
         var5.setLong(3, var1.getBatchId());
         var5.setString(4, var1.getType());
         var5.setString(5, var1.getMsg());
         var5.setString(6, var1.getDetail());
         var5.setString(7, var1.getData());
         var5.setString(8, var1.getGroupId());
         var5.setLong(9, var1.getProjectId());
         var5.setTimestamp(10, new Timestamp(var1.getCreateDate().getTime()));
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
         PreparedStatement var3 = var2.prepareStatement("delete FROM URULE_LOG_BATCH_SKIP where GROUP_ID_=?");
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
         PreparedStatement var3 = var2.prepareStatement("delete FROM URULE_LOG_BATCH_SKIP where PROJECT_ID_=?");
         var3.setLong(1, var1);
         var3.executeUpdate();
         JdbcUtils.closeStatement(var3);
      } catch (Exception var7) {
         throw new RuleException(var7);
      } finally {
         JdbcUtils.closeConnection(var2);
      }

   }

   public BatchSkipLogQuery newQuery() {
      return new BatchSkipLogQueryImpl();
   }
}
