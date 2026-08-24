package com.bstek.urule.console.database.manager.group.role;

import com.bstek.urule.console.database.IDGenerator;
import com.bstek.urule.console.database.IDType;
import com.bstek.urule.console.database.model.GroupRole;
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

public class GroupRoleManagerImpl implements GroupRoleManager {
   public List loadRoles(String groupId) {
      Connection connection = JdbcUtils.getConnection();

      ArrayList roles;
      try {
         String text = "select ID_, NAME_, TYPE_, CREATE_DATE_, UPDATE_DATE_ from URULE_GROUP_ROLE WHERE GROUP_ID_=?";
         PreparedStatement preparedStatement = connection.prepareStatement(text);
         preparedStatement.setString(1, groupId);
         ArrayList items = new ArrayList();
         ResultSet resultSet = preparedStatement.executeQuery();

         while(resultSet.next()) {
            GroupRole groupRole = new GroupRole();
            groupRole.setId(resultSet.getLong(1));
            groupRole.setName(resultSet.getString(2));
            groupRole.setType(resultSet.getString(3));
            groupRole.setCreateDate(resultSet.getTimestamp(4));
            groupRole.setUpdateDate(resultSet.getTimestamp(5));
            items.add(groupRole);
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
   public List loadUserRoles(String groupId, String account) {
      Connection connection = JdbcUtils.getConnection();

      ArrayList userRoles;
      try {
         String text = "select URULE_GROUP_ROLE.ID_, URULE_GROUP_ROLE.NAME_, URULE_GROUP_ROLE.TYPE_ from URULE_GROUP_ROLE  LEFT JOIN URULE_GROUP_USER_ROLE  ON URULE_GROUP_ROLE.ID_=URULE_GROUP_USER_ROLE.ROLE_ID_ WHERE URULE_GROUP_ROLE.GROUP_ID_=? AND URULE_GROUP_USER_ROLE.USER_ID_=?";
         PreparedStatement preparedStatement = connection.prepareStatement(text);
         preparedStatement.setString(1, groupId);
         preparedStatement.setString(2, account);
         ArrayList items = new ArrayList();
         ResultSet resultSet = preparedStatement.executeQuery();

         while(resultSet.next()) {
            GroupRole groupRole = new GroupRole();
            groupRole.setId(resultSet.getLong(1));
            groupRole.setName(resultSet.getString(2));
            groupRole.setType(resultSet.getString(3));
            items.add(groupRole);
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
   public List loadRoleUsers(String groupId, long roleId) {
      Connection connection = JdbcUtils.getConnection();

      ArrayList roleUsers;
      try {
         String text = "select URULE_GROUP_USER.USER_ID_, URULE_GROUP_USER.USER_NAME_ from URULE_GROUP_USER LEFT JOIN URULE_GROUP_USER_ROLE on URULE_GROUP_USER_ROLE.USER_ID_=URULE_GROUP_USER.USER_ID_ WHERE URULE_GROUP_USER.GROUP_ID_=? and URULE_GROUP_USER_ROLE.ROLE_ID_=?";
         PreparedStatement preparedStatement = connection.prepareStatement(text);
         preparedStatement.setString(1, groupId);
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
   public void add(GroupRole role) {
      Connection connection = JdbcUtils.getConnection();

      try {
         role.setCreateDate(new Timestamp(System.currentTimeMillis()));
         role.setUpdateDate(new Timestamp(System.currentTimeMillis()));
         long longValue = IDGenerator.getInstance().nextId(IDType.GROUP_ROLE);
         role.setId(longValue);
         PreparedStatement preparedStatement = connection.prepareStatement("insert into URULE_GROUP_ROLE (ID_, NAME_, TYPE_, GROUP_ID_, CREATE_USER_, CREATE_DATE_,UPDATE_DATE_) values (?, ?, ?, ?, ?, ?, ?)");
         preparedStatement.setLong(1, role.getId());
         preparedStatement.setString(2, role.getName());
         preparedStatement.setString(3, role.getType());
         preparedStatement.setString(4, role.getGroupId());
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
   public void update(GroupRole role) {
      Connection connection = JdbcUtils.getConnection();

      try {
         role.setUpdateDate(new Timestamp(System.currentTimeMillis()));
         PreparedStatement preparedStatement = connection.prepareStatement("update URULE_GROUP_ROLE set NAME_=?, UPDATE_DATE_=? where ID_=?");
         preparedStatement.setString(1, role.getName());
         preparedStatement.setTimestamp(2, new Timestamp(role.getUpdateDate().getTime()));
         preparedStatement.setLong(3, role.getId());
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
         PreparedStatement preparedStatement = connection.prepareStatement("delete FROM URULE_GROUP_ROLE where ID_=?");
         preparedStatement.setLong(1, id);
         preparedStatement.executeUpdate();
         JdbcUtils.closeStatement(preparedStatement);
      } catch (Exception exception) {
         throw new RuleException(exception);
      } finally {
         JdbcUtils.closeConnection(connection);
      }

   }
   public boolean checkExist(String groupId, String name) {
      boolean checkExistResult = false;
      Connection connection = JdbcUtils.getConnection();

      try {
         PreparedStatement preparedStatement = connection.prepareStatement("select ID_ FROM URULE_GROUP_ROLE where GROUP_ID_=? and NAME_=?");
         preparedStatement.setString(1, groupId);
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
   public void addUserRole(String groupId, String userId, long roleId) {
      Connection connection = JdbcUtils.getConnection();

      try {
         long longValue = IDGenerator.getInstance().nextId(IDType.GROUP_USER_ROLE);
         PreparedStatement preparedStatement = connection.prepareStatement("insert into URULE_GROUP_USER_ROLE (ID_, GROUP_ID_, USER_ID_, ROLE_ID_) values (?, ?, ?, ?)");
         preparedStatement.setLong(1, longValue);
         preparedStatement.setString(2, groupId);
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
   public void removeUserRole(String groupId, String userId, long roleId) {
      Connection connection = JdbcUtils.getConnection();

      try {
         PreparedStatement preparedStatement = connection.prepareStatement("delete from URULE_GROUP_USER_ROLE WHERE USER_ID_=? and ROLE_ID_=? and GROUP_ID_=?");
         preparedStatement.setString(1, userId);
         preparedStatement.setLong(2, roleId);
         preparedStatement.setString(3, groupId);
         preparedStatement.executeUpdate();
         JdbcUtils.closeStatement(preparedStatement);
      } catch (Exception exception) {
         throw new RuleException(exception);
      } finally {
         JdbcUtils.closeConnection(connection);
      }

   }
   public GroupRole get(long roleId) {
      GroupRole groupRole = null;
      Connection connection = JdbcUtils.getConnection();

      GroupRole getResult;
      try {
         String text = "select ID_, NAME_, TYPE_, GROUP_ID_, CREATE_DATE_, UPDATE_DATE_ from URULE_GROUP_ROLE WHERE ID_=?";
         PreparedStatement preparedStatement = connection.prepareStatement(text);
         preparedStatement.setLong(1, roleId);
         ResultSet resultSet = preparedStatement.executeQuery();
         if (resultSet.next()) {
            groupRole = new GroupRole();
            groupRole.setId(resultSet.getLong(1));
            groupRole.setName(resultSet.getString(2));
            groupRole.setType(resultSet.getString(3));
            groupRole.setGroupId(resultSet.getString(4));
            groupRole.setCreateDate(resultSet.getTimestamp(5));
            groupRole.setUpdateDate(resultSet.getTimestamp(6));
         }

         JdbcUtils.closeResultSet(resultSet);
         JdbcUtils.closeStatement(preparedStatement);
         getResult = groupRole;
      } catch (Exception exception) {
         throw new RuleException(exception);
      } finally {
         JdbcUtils.closeConnection(connection);
      }

      return getResult;
   }
   public void removeRoleUsers(long roleId) {
      Connection connection = JdbcUtils.getConnection();

      try {
         PreparedStatement preparedStatement = connection.prepareStatement("delete from URULE_GROUP_USER_ROLE WHERE ROLE_ID_=?");
         preparedStatement.setLong(1, roleId);
         preparedStatement.executeUpdate();
         JdbcUtils.closeStatement(preparedStatement);
      } catch (Exception exception) {
         throw new RuleException(exception);
      } finally {
         JdbcUtils.closeConnection(connection);
      }

   }
   public void removeUserRoles(String groupId, String userId) {
      Connection connection = JdbcUtils.getConnection();

      try {
         PreparedStatement preparedStatement = connection.prepareStatement("delete from URULE_GROUP_USER_ROLE WHERE USER_ID_=? and GROUP_ID_=?");
         preparedStatement.setString(1, userId);
         preparedStatement.setString(2, groupId);
         preparedStatement.executeUpdate();
         JdbcUtils.closeStatement(preparedStatement);
      } catch (Exception exception) {
         throw new RuleException(exception);
      } finally {
         JdbcUtils.closeConnection(connection);
      }

   }
   public GroupRole get(String groupId, String name) {
      GroupRole groupRole = null;
      Connection connection = JdbcUtils.getConnection();

      GroupRole getResult;
      try {
         String text = "select ID_, NAME_, TYPE_, CREATE_DATE_, UPDATE_DATE_ from URULE_GROUP_ROLE WHERE GROUP_ID_=? and NAME_=?";
         PreparedStatement preparedStatement = connection.prepareStatement(text);
         preparedStatement.setString(1, groupId);
         preparedStatement.setString(2, name);
         ResultSet resultSet = preparedStatement.executeQuery();
         if (resultSet.next()) {
            groupRole = new GroupRole();
            groupRole.setId(resultSet.getLong(1));
            groupRole.setName(resultSet.getString(2));
            groupRole.setType(resultSet.getString(3));
            groupRole.setCreateDate(resultSet.getTimestamp(4));
            groupRole.setUpdateDate(resultSet.getTimestamp(5));
         }

         JdbcUtils.closeResultSet(resultSet);
         JdbcUtils.closeStatement(preparedStatement);
         getResult = groupRole;
      } catch (Exception exception) {
         throw new RuleException(exception);
      } finally {
         JdbcUtils.closeConnection(connection);
      }

      return getResult;
   }
   public UserRole getUserRole(String userId, long roleId) {
      UserRole userRole = null;
      Connection connection = JdbcUtils.getConnection();

      UserRole userRole2;
      try {
         String text = "select ID_, ROLE_ID_, USER_ID_ from URULE_GROUP_USER_ROLE WHERE ROLE_ID_=? and USER_ID_=?";
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
   public void removeByGroupId(String groupId) {
      Connection connection = JdbcUtils.getConnection();

      try {
         PreparedStatement preparedStatement = connection.prepareStatement("delete FROM URULE_GROUP_USER_ROLE where GROUP_ID_=?");
         preparedStatement.setString(1, groupId);
         preparedStatement.executeUpdate();
         preparedStatement = connection.prepareStatement("delete FROM URULE_GROUP_ROLE where GROUP_ID_=?");
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
