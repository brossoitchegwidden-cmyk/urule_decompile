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
   public void add(OperationLog log) {
      Connection connection = JdbcUtils.getConnection();

      try {
         log.setCreateDate(new Timestamp(System.currentTimeMillis()));
         long longValue = IDGenerator.getInstance().nextId(IDType.LOG_OPERATION);
         PreparedStatement preparedStatement = connection.prepareStatement("insert into URULE_LOG_OPERATION (ID_, USER_ID_, USER_NAME_, GROUP_ID_, GROUP_NAME_, PROJECT_ID_, PROJECT_NAME_, CATEGORY_, ACTION_, ITEM_ID_, CONTENT_, CREATE_DATE_) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
         log.setId(longValue);
         preparedStatement.setLong(1, log.getId());
         preparedStatement.setString(2, log.getUserId());
         preparedStatement.setString(3, log.getUsername());
         preparedStatement.setString(4, log.getGroupId());
         preparedStatement.setString(5, log.getGroupName());
         if (StringUtils.isBlank(log.getGroupId()) && log.getProjectId() != null) {
            Project project = ProjectManager.ins.get(log.getProjectId());
            Group group = GroupManager.ins.get(project.getGroupId());
            if (null != group) {
               preparedStatement.setString(4, group.getId());
               preparedStatement.setString(5, group.getName());
            }
         }

         if (log.getProjectId() == null) {
            preparedStatement.setNull(6, 4);
         } else {
            preparedStatement.setLong(6, log.getProjectId());
         }

         preparedStatement.setString(7, log.getProjectName());
         preparedStatement.setString(8, log.getCategory());
         preparedStatement.setString(9, log.getAction());
         preparedStatement.setString(10, log.getItemId());
         preparedStatement.setString(11, log.getContent());
         preparedStatement.setTimestamp(12, new Timestamp(log.getCreateDate().getTime()));
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
         PreparedStatement preparedStatement = connection.prepareStatement("delete FROM URULE_LOG_OPERATION where GROUP_ID_=?");
         preparedStatement.setString(1, groupId);
         preparedStatement.executeUpdate();
         JdbcUtils.closeStatement(preparedStatement);
      } catch (Exception exception) {
         throw new RuleException(exception);
      } finally {
         JdbcUtils.closeConnection(connection);
      }

   }
   public void removeByProjectId(Long projectId) {
      Connection connection = JdbcUtils.getConnection();

      try {
         PreparedStatement preparedStatement = connection.prepareStatement("delete FROM URULE_LOG_OPERATION where PROJECT_ID_=?");
         preparedStatement.setLong(1, projectId);
         preparedStatement.executeUpdate();
         JdbcUtils.closeStatement(preparedStatement);
      } catch (Exception exception) {
         throw new RuleException(exception);
      } finally {
         JdbcUtils.closeConnection(connection);
      }

   }

   public OperationLogQuery newQuery() {
      return new OperationLogQueryImpl();
   }
   public void addBatch(PreparedStatement stmt, OperationLog log) throws SQLException {
      log.setCreateDate(new Timestamp(System.currentTimeMillis()));
      long longValue = IDGenerator.getInstance().nextId(IDType.LOG_OPERATION);
      log.setId(longValue);
      stmt.setLong(1, log.getId());
      stmt.setString(2, log.getUserId());
      stmt.setString(3, log.getUsername());
      stmt.setString(4, log.getGroupId());
      stmt.setString(5, log.getGroupName());
      if (StringUtils.isBlank(log.getGroupId()) && log.getProjectId() != null) {
         Project project = ProjectManager.ins.get(log.getProjectId());
         Group group = GroupManager.ins.get(project.getGroupId());
         if (null != group) {
            stmt.setString(4, group.getId());
            stmt.setString(5, group.getName());
         }
      }

      if (log.getProjectId() == null) {
         stmt.setNull(6, 4);
      } else {
         stmt.setLong(6, log.getProjectId());
      }

      stmt.setString(7, log.getProjectName());
      stmt.setString(8, log.getCategory());
      stmt.setString(9, log.getAction());
      stmt.setString(10, log.getItemId());
      stmt.setString(11, log.getContent());
      stmt.setTimestamp(12, new Timestamp(log.getCreateDate().getTime()));
      stmt.addBatch();
   }
}
