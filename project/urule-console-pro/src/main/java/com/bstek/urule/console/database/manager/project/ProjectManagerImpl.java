package com.bstek.urule.console.database.manager.project;

import com.bstek.urule.console.InfoException;
import com.bstek.urule.console.database.IDGenerator;
import com.bstek.urule.console.database.IDType;
import com.bstek.urule.console.database.manager.project.user.UserQuery;
import com.bstek.urule.console.database.manager.project.user.UserQueryImpl;
import com.bstek.urule.console.database.model.ApplyType;
import com.bstek.urule.console.database.model.Project;
import com.bstek.urule.console.database.model.ProjectViewModel;
import com.bstek.urule.console.database.util.JdbcUtils;
import com.bstek.urule.console.util.StringUtils;
import com.bstek.urule.exception.RuleException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class ProjectManagerImpl implements ProjectManager {
   public Project get(long id) {
      Connection connection = JdbcUtils.getConnection();

      Project project;
      try {
         PreparedStatement preparedStatement = connection.prepareStatement("select ID_, NAME_, TYPE_, VIEW_MODEL_, DESC_, GROUP_ID_, APPROVE_USER_ENABLE_, APPROVE_USER_DISABLE_, APPROVE_USER_DEPLOY_, CREATE_USER_, CREATE_DATE_, UPDATE_DATE_, UPDATE_USER_ from URULE_PROJECT where ID_=?");
         preparedStatement.setLong(1, id);
         ResultSet resultSet = preparedStatement.executeQuery();
         Project project2 = new Project();
         if (resultSet.next()) {
            project2.setId(resultSet.getLong("ID_"));
            project2.setName(resultSet.getString("NAME_"));
            project2.setType(resultSet.getString("TYPE_"));
            String string = resultSet.getString("VIEW_MODEL_");
            if (StringUtils.isNotBlank(string)) {
               project2.setViewModel(ProjectViewModel.valueOf(string));
            } else {
               project2.setViewModel(ProjectViewModel.category);
            }

            project2.setDesc(resultSet.getString("DESC_"));
            project2.setGroupId(resultSet.getString("GROUP_ID_"));
            project2.setEnableApproveUser(resultSet.getString("APPROVE_USER_ENABLE_"));
            project2.setDisableApproveUser(resultSet.getString("APPROVE_USER_DISABLE_"));
            project2.setDeployApproveUser(resultSet.getString("APPROVE_USER_DEPLOY_"));
            project2.setCreateUser(resultSet.getString("CREATE_USER_"));
            project2.setCreateDate(resultSet.getTimestamp("CREATE_DATE_"));
            project2.setUpdateDate(resultSet.getTimestamp("UPDATE_DATE_"));
            project2.setUpdateUser(resultSet.getString("UPDATE_USER_"));
         }

         JdbcUtils.closeResultSet(resultSet);
         JdbcUtils.closeStatement(preparedStatement);
         project = project2;
      } catch (Exception exception) {
         throw new InfoException(exception);
      } finally {
         JdbcUtils.closeConnection(connection);
      }

      return project;
   }
   public void add(Project project) {
      Connection connection = JdbcUtils.getConnection();

      try {
         project.setCreateDate(new Timestamp(System.currentTimeMillis()));
         project.setUpdateDate(new Timestamp(System.currentTimeMillis()));
         PreparedStatement preparedStatement = connection.prepareStatement("insert into URULE_PROJECT (ID_, NAME_, TYPE_, VIEW_MODEL_, DESC_, GROUP_ID_, CREATE_DATE_, UPDATE_DATE_,CREATE_USER_,UPDATE_USER_,APPROVE_USER_ENABLE_,APPROVE_USER_DISABLE_,APPROVE_USER_DEPLOY_) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
         if (project.getId() == null || project.getId() == 0L) {
            long longValue = IDGenerator.getInstance().nextId(IDType.PROJECT);
            project.setId(longValue);
         }

         preparedStatement.setLong(1, project.getId());
         preparedStatement.setString(2, project.getName());
         preparedStatement.setString(3, project.getType());
         preparedStatement.setString(4, project.getViewModel() != null ? project.getViewModel().name() : ProjectViewModel.category.name());
         preparedStatement.setString(5, project.getDesc());
         preparedStatement.setString(6, project.getGroupId());
         preparedStatement.setTimestamp(7, new Timestamp(project.getCreateDate().getTime()));
         preparedStatement.setTimestamp(8, new Timestamp(project.getUpdateDate().getTime()));
         preparedStatement.setString(9, project.getCreateUser());
         preparedStatement.setString(10, project.getCreateUser());
         preparedStatement.setString(11, project.getCreateUser());
         preparedStatement.setString(12, project.getCreateUser());
         preparedStatement.setString(13, project.getCreateUser());
         preparedStatement.executeUpdate();
         JdbcUtils.closeStatement(preparedStatement);
      } catch (Exception exception) {
         throw new RuleException(exception);
      } finally {
         JdbcUtils.closeConnection(connection);
      }

   }
   public void update(Project project) {
      Connection connection = JdbcUtils.getConnection();

      try {
         project.setUpdateDate(new Timestamp(System.currentTimeMillis()));
         PreparedStatement preparedStatement = connection.prepareStatement("update URULE_PROJECT set NAME_=?, TYPE_=?, VIEW_MODEL_=?, DESC_=?, UPDATE_USER_=?, UPDATE_DATE_=? where ID_=?");
         preparedStatement.setString(1, project.getName());
         preparedStatement.setString(2, project.getType());
         preparedStatement.setString(3, project.getViewModel() != null ? project.getViewModel().name() : ProjectViewModel.category.name());
         preparedStatement.setString(4, project.getDesc());
         preparedStatement.setString(5, project.getUpdateUser());
         preparedStatement.setTimestamp(6, new Timestamp(project.getUpdateDate().getTime()));
         preparedStatement.setLong(7, project.getId());
         preparedStatement.executeUpdate();
         JdbcUtils.closeStatement(preparedStatement);
      } catch (Exception exception) {
         throw new RuleException(exception);
      } finally {
         JdbcUtils.closeConnection(connection);
      }

   }
   public void remove(long id) {
      Connection connection = JdbcUtils.getConnection();

      try {
         PreparedStatement preparedStatement = connection.prepareStatement("delete FROM URULE_PROJECT where ID_=?");
         preparedStatement.setLong(1, id);
         preparedStatement.executeUpdate();
         JdbcUtils.closeStatement(preparedStatement);
      } catch (Exception exception) {
         throw new RuleException(exception);
      } finally {
         JdbcUtils.closeConnection(connection);
      }

   }
   public List getProjectsByGroupId(String groupId) {
      Connection connection = JdbcUtils.getConnection();

      ArrayList projectsByGroupId;
      try {
         String text = "select ID_, NAME_, TYPE_, VIEW_MODEL_, DESC_, GROUP_ID_, CREATE_DATE_, UPDATE_DATE_, UPDATE_USER_ from URULE_PROJECT WHERE GROUP_ID_=?";
         PreparedStatement preparedStatement = connection.prepareStatement(text);
         preparedStatement.setString(1, groupId);
         ArrayList items = new ArrayList();
         ResultSet resultSet = preparedStatement.executeQuery();

         while(resultSet.next()) {
            Project project = new Project();
            project.setId(resultSet.getLong("ID_"));
            project.setName(resultSet.getString("NAME_"));
            project.setType(resultSet.getString("TYPE_"));
            String string = resultSet.getString("VIEW_MODEL_");
            if (StringUtils.isNotBlank(string)) {
               project.setViewModel(ProjectViewModel.valueOf(string));
            }

            project.setDesc(resultSet.getString("DESC_"));
            project.setGroupId(resultSet.getString("GROUP_ID_"));
            project.setCreateDate(resultSet.getTimestamp("CREATE_DATE_"));
            project.setUpdateDate(resultSet.getTimestamp("UPDATE_DATE_"));
            project.setUpdateUser(resultSet.getString("UPDATE_USER_"));
            items.add(project);
         }

         JdbcUtils.closeResultSet(resultSet);
         JdbcUtils.closeStatement(preparedStatement);
         projectsByGroupId = items;
      } catch (Exception exception) {
         throw new RuleException(exception);
      } finally {
         JdbcUtils.closeConnection(connection);
      }

      return projectsByGroupId;
   }
   public void addProjectUser(long projectId, String account, String username) {
      Connection connection = JdbcUtils.getConnection();

      try {
         PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO URULE_PROJECT_USER (ID_, PROJECT_ID_, USER_ID_, USER_NAME_, CREATE_DATE_) VALUES (?, ?, ?, ?, ?)");
         preparedStatement.setLong(1, IDGenerator.getInstance().nextId(IDType.PROJECT_USER));
         preparedStatement.setLong(2, projectId);
         preparedStatement.setString(3, account);
         preparedStatement.setString(4, username);
         preparedStatement.setTimestamp(5, new Timestamp(System.currentTimeMillis()));
         preparedStatement.executeUpdate();
         JdbcUtils.closeStatement(preparedStatement);
      } catch (Exception exception) {
         throw new RuleException(exception);
      } finally {
         JdbcUtils.closeConnection(connection);
      }

   }
   public void removeProjectUser(long projectId, String account) {
      Connection connection = JdbcUtils.getConnection();

      try {
         PreparedStatement preparedStatement = connection.prepareStatement("delete FROM URULE_PROJECT_USER where PROJECT_ID_=? AND USER_ID_=?");
         preparedStatement.setLong(1, projectId);
         preparedStatement.setString(2, account);
         preparedStatement.executeUpdate();
         JdbcUtils.closeStatement(preparedStatement);
      } catch (Exception exception) {
         throw new RuleException(exception);
      } finally {
         JdbcUtils.closeConnection(connection);
      }

   }
   public void removeProjectUsers(long projectId) {
      Connection connection = JdbcUtils.getConnection();

      try {
         PreparedStatement preparedStatement = connection.prepareStatement("delete FROM URULE_PROJECT_USER where PROJECT_ID_=?");
         preparedStatement.setLong(1, projectId);
         preparedStatement.executeUpdate();
         JdbcUtils.closeStatement(preparedStatement);
      } catch (Exception exception) {
         throw new RuleException(exception);
      } finally {
         JdbcUtils.closeConnection(connection);
      }

   }
   public String getApproveUser(long id, ApplyType type) {
      String approveUser = null;
      Connection connection = JdbcUtils.getConnection();

      try {
         PreparedStatement preparedStatement = connection.prepareStatement("select APPROVE_USER_ENABLE_, APPROVE_USER_DISABLE_, APPROVE_USER_DEPLOY_, CREATE_USER_ from URULE_PROJECT where ID_=?");
         preparedStatement.setLong(1, id);
         ResultSet resultSet = preparedStatement.executeQuery();
         if (resultSet.next()) {
            if (ApplyType.deploy == type) {
               approveUser = resultSet.getString(3);
            } else if (ApplyType.enable == type) {
               approveUser = resultSet.getString(1);
            } else if (ApplyType.disable == type) {
               approveUser = resultSet.getString(2);
            }
         }

         if (StringUtils.isEmpty(approveUser)) {
            approveUser = resultSet.getString(4);
         }

         JdbcUtils.closeResultSet(resultSet);
         JdbcUtils.closeStatement(preparedStatement);
      } catch (Exception exception) {
         throw new RuleException(exception);
      } finally {
         JdbcUtils.closeConnection(connection);
      }

      return approveUser;
   }
   public void updateApproveUser(long id, ApplyType type, String account) {
      Connection connection = JdbcUtils.getConnection();

      try {
         String text = "update URULE_PROJECT SET ";
         if (ApplyType.deploy == type) {
            text = text + "APPROVE_USER_DEPLOY_=?";
         } else if (ApplyType.enable == type) {
            text = text + "APPROVE_USER_ENABLE_=?";
         } else if (ApplyType.disable == type) {
            text = text + "APPROVE_USER_DISABLE_=?";
         }

         text = text + " where ID_=?";
         PreparedStatement preparedStatement = connection.prepareStatement(text);
         preparedStatement.setString(1, account);
         preparedStatement.setLong(2, id);
         preparedStatement.executeUpdate();
         JdbcUtils.closeStatement(preparedStatement);
      } catch (Exception exception) {
         throw new RuleException(exception);
      } finally {
         JdbcUtils.closeConnection(connection);
      }

   }

   public ProjectQuery newQuery() {
      return new ProjectQueryImpl();
   }

   public UserQuery createUserQuery() {
      return new UserQueryImpl();
   }
}
