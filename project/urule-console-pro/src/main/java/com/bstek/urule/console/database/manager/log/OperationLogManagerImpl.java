package com.bstek.urule.console.database.manager.log;

import com.bstek.urule.console.database.IDGenerator;
import com.bstek.urule.console.database.IDType;
import com.bstek.urule.console.database.manager.group.GroupManager;
import com.bstek.urule.console.database.manager.project.ProjectManager;
import com.bstek.urule.console.database.model.Group;
import com.bstek.urule.console.database.model.OperationLog;
import com.bstek.urule.console.database.model.Project;
import com.bstek.urule.console.database.util.JdbcUtils;
import com.bstek.urule.console.util.StringUtils;
import com.bstek.urule.exception.RuleException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

public class OperationLogManagerImpl implements OperationLogManager {
   public void add(OperationLog var1) {
      Connection var2 = JdbcUtils.getConnection();

      try {
         var1.setCreateDate(new Timestamp(System.currentTimeMillis()));
         long var3 = IDGenerator.getInstance().nextId(IDType.LOG_OPERATION);
         PreparedStatement var5 = var2.prepareStatement("insert into URULE_LOG_OPERATION (ID_, USER_ID_, USER_NAME_, GROUP_ID_, GROUP_NAME_, PROJECT_ID_, PROJECT_NAME_, CATEGORY_, ACTION_, ITEM_ID_, CONTENT_, CREATE_DATE_) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
         var1.setId(var3);
         var5.setLong(1, var1.getId());
         var5.setString(2, var1.getUserId());
         var5.setString(3, var1.getUsername());
         var5.setString(4, var1.getGroupId());
         var5.setString(5, var1.getGroupName());
         if (StringUtils.isBlank(var1.getGroupId()) && var1.getProjectId() != null) {
            Project var6 = ProjectManager.ins.get(var1.getProjectId());
            Group var7 = GroupManager.ins.get(var6.getGroupId());
            if (null != var7) {
               var5.setString(4, var7.getId());
               var5.setString(5, var7.getName());
            }
         }

         if (var1.getProjectId() == null) {
            var5.setNull(6, 4);
         } else {
            var5.setLong(6, var1.getProjectId());
         }

         var5.setString(7, var1.getProjectName());
         var5.setString(8, var1.getCategory());
         var5.setString(9, var1.getAction());
         var5.setString(10, var1.getItemId());
         var5.setString(11, var1.getContent());
         var5.setTimestamp(12, new Timestamp(var1.getCreateDate().getTime()));
         var5.executeUpdate();
         JdbcUtils.closeStatement(var5);
      } catch (Exception var11) {
         throw new RuleException(var11);
      } finally {
         JdbcUtils.closeConnection(var2);
      }

   }

   public void removeByGroupId(String var1) {
      Connection var2 = JdbcUtils.getConnection();

      try {
         PreparedStatement var3 = var2.prepareStatement("delete FROM URULE_LOG_OPERATION where GROUP_ID_=?");
         var3.setString(1, var1);
         var3.executeUpdate();
         JdbcUtils.closeStatement(var3);
      } catch (Exception var7) {
         throw new RuleException(var7);
      } finally {
         JdbcUtils.closeConnection(var2);
      }

   }

   public void removeByProjectId(Long var1) {
      Connection var2 = JdbcUtils.getConnection();

      try {
         PreparedStatement var3 = var2.prepareStatement("delete FROM URULE_LOG_OPERATION where PROJECT_ID_=?");
         var3.setLong(1, var1);
         var3.executeUpdate();
         JdbcUtils.closeStatement(var3);
      } catch (Exception var7) {
         throw new RuleException(var7);
      } finally {
         JdbcUtils.closeConnection(var2);
      }

   }

   public OperationLogQuery newQuery() {
      return new OperationLogQueryImpl();
   }

   public void addBatch(PreparedStatement var1, OperationLog var2) throws SQLException {
      var2.setCreateDate(new Timestamp(System.currentTimeMillis()));
      long var3 = IDGenerator.getInstance().nextId(IDType.LOG_OPERATION);
      var2.setId(var3);
      var1.setLong(1, var2.getId());
      var1.setString(2, var2.getUserId());
      var1.setString(3, var2.getUsername());
      var1.setString(4, var2.getGroupId());
      var1.setString(5, var2.getGroupName());
      if (StringUtils.isBlank(var2.getGroupId()) && var2.getProjectId() != null) {
         Project var5 = ProjectManager.ins.get(var2.getProjectId());
         Group var6 = GroupManager.ins.get(var5.getGroupId());
         if (null != var6) {
            var1.setString(4, var6.getId());
            var1.setString(5, var6.getName());
         }
      }

      if (var2.getProjectId() == null) {
         var1.setNull(6, 4);
      } else {
         var1.setLong(6, var2.getProjectId());
      }

      var1.setString(7, var2.getProjectName());
      var1.setString(8, var2.getCategory());
      var1.setString(9, var2.getAction());
      var1.setString(10, var2.getItemId());
      var1.setString(11, var2.getContent());
      var1.setTimestamp(12, new Timestamp(var2.getCreateDate().getTime()));
      var1.addBatch();
   }
}
