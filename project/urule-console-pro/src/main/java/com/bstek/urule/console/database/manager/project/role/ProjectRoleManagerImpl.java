package com.bstek.urule.console.database.manager.project.role;

import com.bstek.urule.console.database.IDGenerator;
import com.bstek.urule.console.database.IDType;
import com.bstek.urule.console.database.model.ProjectRole;
import com.bstek.urule.console.database.model.User;
import com.bstek.urule.console.database.model.UserRole;
import com.bstek.urule.console.database.util.JdbcUtils;
import com.bstek.urule.exception.RuleException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class ProjectRoleManagerImpl implements ProjectRoleManager {
   public List loadRoles(long projectId) {
      Connection connection = JdbcUtils.getConnection();

      ArrayList roles;
      try {
         String text = "select ID_, NAME_, TYPE_, CREATE_DATE_, UPDATE_DATE_ from URULE_PROJECT_ROLE WHERE PROJECT_ID_=?";
         PreparedStatement preparedStatement = connection.prepareStatement(text);
         preparedStatement.setLong(1, projectId);
         ArrayList items = new ArrayList();
         ResultSet resultSet = preparedStatement.executeQuery();

         while(resultSet.next()) {
            ProjectRole projectRole = new ProjectRole();
            projectRole.setId(resultSet.getLong(1));
            projectRole.setName(resultSet.getString(2));
            projectRole.setType(resultSet.getString(3));
            projectRole.setCreateDate(resultSet.getTimestamp(4));
            projectRole.setUpdateDate(resultSet.getTimestamp(5));
            items.add(projectRole);
         }

         JdbcUtils.closeResultSet(resultSet);
         JdbcUtils.closeStatement(preparedStatement);
         roles = items;
      } catch (Exception exception) {
         throw new RuleException(exception);
      } finally {
         JdbcUtils.closeConnection(connection);
      }

      return roles;
   }
   public List loadUserRoles(long projectId, String account) {
      Connection connection = JdbcUtils.getConnection();

      ArrayList userRoles;
      try {
         String text = "select URULE_PROJECT_ROLE.ID_, URULE_PROJECT_ROLE.NAME_, URULE_PROJECT_ROLE.TYPE_ from URULE_PROJECT_ROLE  LEFT JOIN URULE_PROJECT_USER_ROLE  ON URULE_PROJECT_ROLE.ID_=URULE_PROJECT_USER_ROLE.ROLE_ID_ WHERE URULE_PROJECT_ROLE.PROJECT_ID_=? AND URULE_PROJECT_USER_ROLE.USER_ID_=?";
         PreparedStatement preparedStatement = connection.prepareStatement(text);
         preparedStatement.setLong(1, projectId);
         preparedStatement.setString(2, account);
         ArrayList items = new ArrayList();
         ResultSet resultSet = preparedStatement.executeQuery();

         while(resultSet.next()) {
            ProjectRole projectRole = new ProjectRole();
            projectRole.setId(resultSet.getLong(1));
            projectRole.setName(resultSet.getString(2));
            projectRole.setType(resultSet.getString(3));
            items.add(projectRole);
         }

         JdbcUtils.closeResultSet(resultSet);
         JdbcUtils.closeStatement(preparedStatement);
         userRoles = items;
      } catch (Exception exception) {
         throw new RuleException(exception);
      } finally {
         JdbcUtils.closeConnection(connection);
      }

      return userRoles;
   }
   public List loadRoleUsers(long projectId, long roleId) {
      Connection connection = JdbcUtils.getConnection();

      ArrayList roleUsers;
      try {
         String text = "select URULE_PROJECT_USER.USER_ID_, URULE_PROJECT_USER.USER_NAME_ from URULE_PROJECT_USER LEFT JOIN URULE_PROJECT_USER_ROLE on URULE_PROJECT_USER_ROLE.USER_ID_=URULE_PROJECT_USER.USER_ID_ WHERE URULE_PROJECT_USER.PROJECT_ID_=? and URULE_PROJECT_USER_ROLE.ROLE_ID_=?";
         PreparedStatement preparedStatement = connection.prepareStatement(text);
         preparedStatement.setLong(1, projectId);
         preparedStatement.setLong(2, roleId);
         ArrayList items = new ArrayList();
         ResultSet resultSet = preparedStatement.executeQuery();

         while(resultSet.next()) {
            User user = new User();
            user.setId(resultSet.getString(1));
            user.setName(resultSet.getString(2));
            items.add(user);
         }

         JdbcUtils.closeResultSet(resultSet);
         JdbcUtils.closeStatement(preparedStatement);
         roleUsers = items;
      } catch (Exception exception) {
         throw new RuleException(exception);
      } finally {
         JdbcUtils.closeConnection(connection);
      }

      return roleUsers;
   }
   public void add(ProjectRole role) {
      Connection connection = JdbcUtils.getConnection();

      try {
         role.setCreateDate(new Timestamp(System.currentTimeMillis()));
         role.setUpdateDate(new Timestamp(System.currentTimeMillis()));
         long longValue = IDGenerator.getInstance().nextId(IDType.PROJECT_ROLE);
         role.setId(longValue);
         PreparedStatement preparedStatement = connection.prepareStatement("insert into URULE_PROJECT_ROLE (ID_, NAME_, TYPE_, PROJECT_ID_, CREATE_USER_, CREATE_DATE_,UPDATE_DATE_) values (?, ?, ?, ?, ?, ?, ?)");
         preparedStatement.setLong(1, role.getId());
         preparedStatement.setString(2, role.getName());
         preparedStatement.setString(3, role.getType());
         preparedStatement.setLong(4, role.getProjectId());
         preparedStatement.setString(5, role.getCreateUser());
         preparedStatement.setTimestamp(6, new Timestamp(role.getCreateDate().getTime()));
         preparedStatement.setTimestamp(7, new Timestamp(role.getUpdateDate().getTime()));
         preparedStatement.executeUpdate();
         JdbcUtils.closeStatement(preparedStatement);
      } catch (Exception exception) {
         throw new RuleException(exception);
      } finally {
         JdbcUtils.closeConnection(connection);
      }

   }
   public void update(ProjectRole role) {
      Connection connection = JdbcUtils.getConnection();

      try {
         role.setUpdateDate(new Timestamp(System.currentTimeMillis()));
         PreparedStatement preparedStatement = connection.prepareStatement("update URULE_PROJECT_ROLE set NAME_=?, UPDATE_USER_=?, UPDATE_DATE_=? where ID_=?");
         preparedStatement.setString(1, role.getName());
         preparedStatement.setString(2, role.getUpdateUser());
         preparedStatement.setTimestamp(3, new Timestamp(role.getUpdateDate().getTime()));
         preparedStatement.setLong(4, role.getId());
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
         PreparedStatement preparedStatement = connection.prepareStatement("delete FROM URULE_PROJECT_ROLE where ID_=?");
         preparedStatement.setLong(1, id);
         preparedStatement.executeUpdate();
         JdbcUtils.closeStatement(preparedStatement);
      } catch (Exception exception) {
         throw new RuleException(exception);
      } finally {
         JdbcUtils.closeConnection(connection);
      }

   }
   public ProjectRole get(long roleId) {
      ProjectRole projectRole = null;
      Connection connection = JdbcUtils.getConnection();

      ProjectRole getResult;
      try {
         String text = "select ID_, NAME_, TYPE_, PROJECT_ID_, CREATE_DATE_, UPDATE_DATE_ from URULE_PROJECT_ROLE WHERE ID_=?";
         PreparedStatement preparedStatement = connection.prepareStatement(text);
         preparedStatement.setLong(1, roleId);
         ResultSet resultSet = preparedStatement.executeQuery();
         if (resultSet.next()) {
            projectRole = new ProjectRole();
            projectRole.setId(resultSet.getLong(1));
            projectRole.setName(resultSet.getString(2));
            projectRole.setType(resultSet.getString(3));
            projectRole.setProjectId(resultSet.getLong(4));
            projectRole.setCreateDate(resultSet.getTimestamp(5));
            projectRole.setUpdateDate(resultSet.getTimestamp(6));
         }

         JdbcUtils.closeResultSet(resultSet);
         JdbcUtils.closeStatement(preparedStatement);
         getResult = projectRole;
      } catch (Exception exception) {
         throw new RuleException(exception);
      } finally {
         JdbcUtils.closeConnection(connection);
      }

      return getResult;
   }
   public boolean checkExist(long projectId, String name) {
      boolean checkExistResult = false;
      Connection connection = JdbcUtils.getConnection();

      try {
         PreparedStatement preparedStatement = connection.prepareStatement("select ID_ FROM URULE_PROJECT_ROLE where PROJECT_ID_=? and NAME_=?");
         preparedStatement.setLong(1, projectId);
         preparedStatement.setString(2, name);
         ResultSet resultSet = preparedStatement.executeQuery();
         if (resultSet.next()) {
            checkExistResult = true;
         }

         JdbcUtils.closeResultSet(resultSet);
         JdbcUtils.closeStatement(preparedStatement);
      } catch (Exception exception) {
         throw new RuleException(exception);
      } finally {
         JdbcUtils.closeConnection(connection);
      }

      return checkExistResult;
   }
   public void addUserRole(long projectId, String userId, long roleId) {
      Connection connection = JdbcUtils.getConnection();

      try {
         long longValue = IDGenerator.getInstance().nextId(IDType.PROJECT_ROLE);
         PreparedStatement preparedStatement = connection.prepareStatement("insert into URULE_PROJECT_USER_ROLE (ID_, PROJECT_ID_, USER_ID_, ROLE_ID_) values (?, ?, ?, ?)");
         preparedStatement.setLong(1, longValue);
         preparedStatement.setLong(2, projectId);
         preparedStatement.setString(3, userId);
         preparedStatement.setLong(4, roleId);
         preparedStatement.executeUpdate();
         JdbcUtils.closeStatement(preparedStatement);
      } catch (Exception exception) {
         throw new RuleException(exception);
      } finally {
         JdbcUtils.closeConnection(connection);
      }

   }
   public UserRole getUserRole(String userId, long roleId) {
      UserRole userRole = null;
      Connection connection = JdbcUtils.getConnection();

      UserRole userRole2;
      try {
         String text = "select ID_, ROLE_ID_, USER_ID_ from URULE_PROJECT_USER_ROLE WHERE ROLE_ID_=? and USER_ID_=?";
         PreparedStatement preparedStatement = connection.prepareStatement(text);
         preparedStatement.setLong(1, roleId);
         preparedStatement.setString(2, userId);
         ResultSet resultSet = preparedStatement.executeQuery();
         if (resultSet.next()) {
            userRole = new UserRole();
            userRole.setId(resultSet.getLong(1));
            userRole.setRoleId(resultSet.getLong(2));
            userRole.setUserId(resultSet.getString(3));
         }

         JdbcUtils.closeResultSet(resultSet);
         JdbcUtils.closeStatement(preparedStatement);
         userRole2 = userRole;
      } catch (Exception exception) {
         throw new RuleException(exception);
      } finally {
         JdbcUtils.closeConnection(connection);
      }

      return userRole2;
   }
   public void removeUserRole(String userId, long roleId) {
      Connection connection = JdbcUtils.getConnection();

      try {
         PreparedStatement preparedStatement = connection.prepareStatement("delete from URULE_PROJECT_USER_ROLE WHERE USER_ID_=? and ROLE_ID_=?");
         preparedStatement.setString(1, userId);
         preparedStatement.setLong(2, roleId);
         preparedStatement.executeUpdate();
         JdbcUtils.closeStatement(preparedStatement);
      } catch (Exception exception) {
         throw new RuleException(exception);
      } finally {
         JdbcUtils.closeConnection(connection);
      }

   }
   public void removeRoleUsers(long roleId) {
      Connection connection = JdbcUtils.getConnection();

      try {
         PreparedStatement preparedStatement = connection.prepareStatement("delete from URULE_PROJECT_USER_ROLE WHERE ROLE_ID_=?");
         preparedStatement.setLong(1, roleId);
         preparedStatement.executeUpdate();
         JdbcUtils.closeStatement(preparedStatement);
      } catch (Exception exception) {
         throw new RuleException(exception);
      } finally {
         JdbcUtils.closeConnection(connection);
      }

   }
   public void removeUserRoles(String userId) {
      Connection connection = JdbcUtils.getConnection();

      try {
         PreparedStatement preparedStatement = connection.prepareStatement("delete from URULE_PROJECT_USER_ROLE WHERE USER_ID_=?");
         preparedStatement.setString(1, userId);
         preparedStatement.executeUpdate();
         JdbcUtils.closeStatement(preparedStatement);
      } catch (Exception exception) {
         throw new RuleException(exception);
      } finally {
         JdbcUtils.closeConnection(connection);
      }

   }
   public ProjectRole get(long projectId, String name) {
      ProjectRole projectRole = null;
      Connection connection = JdbcUtils.getConnection();

      ProjectRole getResult;
      try {
         String text = "select ID_, NAME_, TYPE_, CREATE_DATE_, UPDATE_DATE_ from URULE_PROJECT_ROLE WHERE PROJECT_ID_=? and NAME_=?";
         PreparedStatement preparedStatement = connection.prepareStatement(text);
         preparedStatement.setLong(1, projectId);
         preparedStatement.setString(2, name);
         ResultSet resultSet = preparedStatement.executeQuery();
         if (resultSet.next()) {
            projectRole = new ProjectRole();
            projectRole.setId(resultSet.getLong(1));
            projectRole.setName(resultSet.getString(2));
            projectRole.setType(resultSet.getString(3));
            projectRole.setCreateDate(resultSet.getTimestamp(4));
            projectRole.setUpdateDate(resultSet.getTimestamp(5));
         }

         JdbcUtils.closeResultSet(resultSet);
         JdbcUtils.closeStatement(preparedStatement);
         getResult = projectRole;
      } catch (Exception exception) {
         throw new RuleException(exception);
      } finally {
         JdbcUtils.closeConnection(connection);
      }

      return getResult;
   }
   public void removeByProjectId(Long id) {
      Connection connection = JdbcUtils.getConnection();

      try {
         PreparedStatement preparedStatement = connection.prepareStatement("delete FROM URULE_PROJECT_USER_ROLE where PROJECT_ID_=?");
         preparedStatement.setLong(1, id);
         preparedStatement.executeUpdate();
         JdbcUtils.closeStatement(preparedStatement);
         preparedStatement = connection.prepareStatement("delete FROM URULE_PROJECT_ROLE where PROJECT_ID_=?");
         preparedStatement.setLong(1, id);
         preparedStatement.executeUpdate();
         JdbcUtils.closeStatement(preparedStatement);
      } catch (Exception exception) {
         throw new RuleException(exception);
      } finally {
         JdbcUtils.closeConnection(connection);
      }

   }
}
