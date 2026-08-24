package com.bstek.urule.console.database.manager.user;

import com.bstek.urule.console.database.model.User;
import com.bstek.urule.console.database.util.JdbcUtils;
import com.bstek.urule.exception.RuleException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class UserManagerImpl implements UserManager {
   public void add(User user) {
      Connection connection = JdbcUtils.getConnection();

      try {
         user.setCreateDate(new Timestamp(System.currentTimeMillis()));
         PreparedStatement preparedStatement = connection.prepareStatement("insert into URULE_USER (ID_, NAME_, PASSWORD_, EMAIL_, DESC_, CREATE_USER_,CREATE_DATE_) values (?, ?, ?, ?, ?,?, ?)");
         preparedStatement.setString(1, user.getId());
         preparedStatement.setString(2, user.getName());
         preparedStatement.setString(3, user.getPassword());
         preparedStatement.setString(4, user.getEmail());
         preparedStatement.setString(5, user.getDesc());
         preparedStatement.setString(6, user.getCreateUser());
         preparedStatement.setTimestamp(7, new Timestamp(user.getCreateDate().getTime()));
         preparedStatement.executeUpdate();
         JdbcUtils.closeStatement(preparedStatement);
      } catch (Exception exception) {
         throw new RuleException(exception);
      } finally {
         JdbcUtils.closeConnection(connection);
      }

   }

   public void update(User user) {
      Connection connection = JdbcUtils.getConnection();

      try {
         user.setUpdateDate(new Timestamp(System.currentTimeMillis()));
         PreparedStatement preparedStatement = connection.prepareStatement("update URULE_USER set PASSWORD_=?, EMAIL_=?, SECRET_KEY_=?, DESC_=?, UPDATE_DATE_=?, EXPIR_DATE_=?, UPDATE_USER_=?, NAME_=?  where ID_=?");
         preparedStatement.setString(1, user.getPassword());
         preparedStatement.setString(2, user.getEmail());
         preparedStatement.setString(3, user.getSecretKey());
         preparedStatement.setString(4, user.getDesc());
         preparedStatement.setTimestamp(5, new Timestamp(user.getUpdateDate().getTime()));
         preparedStatement.setTimestamp(6, user.getExpirDate() == null ? null : new Timestamp(user.getExpirDate().getTime()));
         preparedStatement.setString(7, user.getId());
         preparedStatement.setString(8, user.getName());
         preparedStatement.setString(9, user.getUpdateUser());
         preparedStatement.executeUpdate();
         JdbcUtils.closeStatement(preparedStatement);
      } catch (Exception exception) {
         throw new RuleException(exception);
      } finally {
         JdbcUtils.closeConnection(connection);
      }

   }

   public void remove(String account) {
      Connection connection = JdbcUtils.getConnection();

      try {
         PreparedStatement preparedStatement = connection.prepareStatement("delete FROM URULE_USER where ID_=?");
         preparedStatement.setString(1, account);
         preparedStatement.executeUpdate();
         JdbcUtils.closeStatement(preparedStatement);
      } catch (Exception exception) {
         throw new RuleException(exception);
      } finally {
         JdbcUtils.closeConnection(connection);
      }

   }

   public User get(String account) {
      Connection connection = JdbcUtils.getConnection();

      User user;
      try {
         String text = "select ID_, NAME_, PASSWORD_, EMAIL_, SECRET_KEY_, DESC_, EXPIR_DATE_, CREATE_DATE_, UPDATE_DATE_ from URULE_USER where ID_=?";
         PreparedStatement preparedStatement = connection.prepareStatement(text);
         preparedStatement.setString(1, account);
         ResultSet resultSet = preparedStatement.executeQuery();
         User user2 = null;
         if (resultSet.next()) {
            user2 = new User();
            user2.setId(resultSet.getString(1));
            user2.setName(resultSet.getString(2));
            user2.setPassword(resultSet.getString(3));
            user2.setEmail(resultSet.getString(4));
            user2.setSecretKey(resultSet.getString(5));
            user2.setDesc(resultSet.getString(6));
            user2.setExpirDate(resultSet.getTimestamp(7));
            user2.setCreateDate(resultSet.getTimestamp(8));
            user2.setUpdateDate(resultSet.getTimestamp(9));
         }

         JdbcUtils.closeResultSet(resultSet);
         JdbcUtils.closeStatement(preparedStatement);
         user = user2;
      } catch (Exception exception) {
         throw new RuleException(exception);
      } finally {
         JdbcUtils.closeConnection(connection);
      }

      return user;
   }
   public void changePassword(String account, String password) {
      Connection connection = JdbcUtils.getConnection();

      try {
         String text = "update URULE_USER set PASSWORD_=? where ID_=?";
         PreparedStatement preparedStatement = connection.prepareStatement(text);
         preparedStatement.setString(1, account);
         preparedStatement.setString(2, password);
         preparedStatement.executeUpdate();
         JdbcUtils.closeStatement(preparedStatement);
      } catch (Exception exception) {
         throw new RuleException(exception);
      } finally {
         JdbcUtils.closeConnection(connection);
      }

   }
   public List getUsersByGroupId(String groupdId) {
      Connection connection = JdbcUtils.getConnection();

      ArrayList usersByGroupId;
      try {
         String text = "select URULE_USER.ID_, URULE_USER.NAME_, URULE_USER.PASSWORD_, URULE_USER.EMAIL_, URULE_USER.SECRET_KEY_, URULE_USER.DESC_, URULE_USER.EXPIR_DATE_, URULE_USER.CREATE_DATE_, URULE_USER.UPDATE_DATE_ from URULE_USER left join URULE_GROUP_USER on URULE_USER.ID_=URULE_GROUP_USER.USER_ID_  where URULE_GROUP_USER.GROUP_ID_ = ?";
         PreparedStatement preparedStatement = connection.prepareStatement(text);
         preparedStatement.setString(1, groupdId);
         ResultSet resultSet = preparedStatement.executeQuery();
         ArrayList items = new ArrayList();

         while(resultSet.next()) {
            User user = new User();
            user.setId(resultSet.getString(1));
            user.setName(resultSet.getString(2));
            user.setEmail(resultSet.getString(3));
            user.setSecretKey(resultSet.getString(4));
            user.setEnable(resultSet.getBoolean(5));
            user.setDesc(resultSet.getString(6));
            user.setExpirDate(resultSet.getTimestamp(7));
            user.setCreateDate(resultSet.getTimestamp(8));
            user.setUpdateDate(resultSet.getTimestamp(9));
            items.add(user);
         }

         JdbcUtils.closeResultSet(resultSet);
         JdbcUtils.closeStatement(preparedStatement);
         usersByGroupId = items;
      } catch (Exception exception) {
         throw new RuleException(exception);
      } finally {
         JdbcUtils.closeConnection(connection);
      }

      return usersByGroupId;
   }
   public List getUsersByRoleId(long roleId) {
      Connection connection = JdbcUtils.getConnection();

      ArrayList usersByRoleId;
      try {
         String text = "select URULE_USER.ID_, URULE_USER.NAME_, URULE_USER.PASSWORD_, URULE_USER.EMAIL_, URULE_USER.SECRET_KEY_, URULE_USER.DESC_, URULE_USER.EXPIR_DATE_, URULE_USER.CREATE_DATE_, URULE_USER.UPDATE_DATE_ from URULE_USER left join URULE_GROUP_USER_ROLE on URULE_USER.ID_=URULE_GROUP_USER_ROLE.USER_ID_  where URULE_GROUP_USER_ROLE.ROLE_ID_ = ?";
         PreparedStatement preparedStatement = connection.prepareStatement(text);
         preparedStatement.setLong(1, roleId);
         ResultSet resultSet = preparedStatement.executeQuery();
         ArrayList items = new ArrayList();

         while(resultSet.next()) {
            User user = new User();
            user.setId(resultSet.getString(1));
            user.setName(resultSet.getString(2));
            user.setEmail(resultSet.getString(3));
            user.setSecretKey(resultSet.getString(4));
            user.setEnable(resultSet.getBoolean(5));
            user.setDesc(resultSet.getString(6));
            user.setExpirDate(resultSet.getTimestamp(7));
            user.setCreateDate(resultSet.getTimestamp(8));
            user.setUpdateDate(resultSet.getTimestamp(9));
            items.add(user);
         }

         JdbcUtils.closeResultSet(resultSet);
         JdbcUtils.closeStatement(preparedStatement);
         usersByRoleId = items;
      } catch (Exception exception) {
         throw new RuleException(exception);
      } finally {
         JdbcUtils.closeConnection(connection);
      }

      return usersByRoleId;
   }
   public List getUsersByProjectId(long projectId) {
      Connection connection = JdbcUtils.getConnection();

      ArrayList usersByProjectId;
      try {
         String text = "select URULE_USER.ID_, NAME_, PASSWORD_, EMAIL_, SECRET_KEY_, DESC_, EXPIR_DATE_, URULE_PROJECT_USER.CREATE_DATE_, UPDATE_DATE_ from URULE_USER left join URULE_PROJECT_USER on URULE_USER.ID_=URULE_PROJECT_USER.USER_ID_  where URULE_PROJECT_USER.PROJECT_ID_=? order by URULE_PROJECT_USER.CREATE_DATE_";
         PreparedStatement preparedStatement = connection.prepareStatement(text);
         preparedStatement.setLong(1, projectId);
         ResultSet resultSet = preparedStatement.executeQuery();
         ArrayList items = new ArrayList();

         while(resultSet.next()) {
            User user = new User();
            user.setId(resultSet.getString(1));
            user.setName(resultSet.getString(2));
            user.setEmail(resultSet.getString(3));
            user.setSecretKey(resultSet.getString(4));
            user.setEnable(resultSet.getBoolean(5));
            user.setDesc(resultSet.getString(6));
            user.setExpirDate(resultSet.getTimestamp(7));
            user.setCreateDate(resultSet.getTimestamp(8));
            user.setUpdateDate(resultSet.getTimestamp(9));
            items.add(user);
         }

         JdbcUtils.closeResultSet(resultSet);
         JdbcUtils.closeStatement(preparedStatement);
         usersByProjectId = items;
      } catch (Exception exception) {
         throw new RuleException(exception);
      } finally {
         JdbcUtils.closeConnection(connection);
      }

      return usersByProjectId;
   }

   public User getByEmail(String email) {
      Connection connection = JdbcUtils.getConnection();

      User user;
      try {
         String text = "select ID_, NAME_, PASSWORD_, EMAIL_, SECRET_KEY_, DESC_, EXPIR_DATE_, CREATE_DATE_, UPDATE_DATE_ from URULE_USER where EMAIL_=?";
         PreparedStatement preparedStatement = connection.prepareStatement(text);
         preparedStatement.setString(1, email);
         ResultSet resultSet = preparedStatement.executeQuery();
         User user2 = null;
         if (resultSet.next()) {
            user2 = new User();
            user2.setId(resultSet.getString(1));
            user2.setName(resultSet.getString(2));
            user2.setPassword(resultSet.getString(3));
            user2.setEmail(resultSet.getString(4));
            user2.setSecretKey(resultSet.getString(5));
            user2.setDesc(resultSet.getString(6));
            user2.setExpirDate(resultSet.getTimestamp(7));
            user2.setCreateDate(resultSet.getTimestamp(8));
            user2.setUpdateDate(resultSet.getTimestamp(9));
         }

         JdbcUtils.closeResultSet(resultSet);
         JdbcUtils.closeStatement(preparedStatement);
         user = user2;
      } catch (Exception exception) {
         throw new RuleException(exception);
      } finally {
         JdbcUtils.closeConnection(connection);
      }

      return user;
   }

   public UserQuery newQuery() {
      return new UserQueryImpl();
   }
   public User getGroupUser(String groupId, String account) {
      Connection connection = JdbcUtils.getConnection();

      User user;
      try {
         String text = "select USER_ID_, USER_NAME_ from URULE_GROUP_USER where GROUP_ID_=? and USER_ID_=?";
         PreparedStatement preparedStatement = connection.prepareStatement(text);
         preparedStatement.setString(1, groupId);
         preparedStatement.setString(2, account);
         User user2 = null;
         ResultSet resultSet = preparedStatement.executeQuery();
         if (resultSet.next()) {
            user2 = new User();
            user2.setId(resultSet.getString(1));
            user2.setName(resultSet.getString(2));
         }

         JdbcUtils.closeResultSet(resultSet);
         JdbcUtils.closeStatement(preparedStatement);
         user = user2;
      } catch (Exception exception) {
         throw new RuleException(exception);
      } finally {
         JdbcUtils.closeConnection(connection);
      }

      return user;
   }
   public User getProjectUser(long projectId, String account) {
      Connection connection = JdbcUtils.getConnection();

      User user;
      try {
         String text = "select USER_ID_, USER_NAME_ from URULE_PROJECT_USER where PROJECT_ID_=? and USER_ID_=?";
         PreparedStatement preparedStatement = connection.prepareStatement(text);
         preparedStatement.setLong(1, projectId);
         preparedStatement.setString(2, account);
         User user2 = null;
         ResultSet resultSet = preparedStatement.executeQuery();
         if (resultSet.next()) {
            user2 = new User();
            user2.setId(resultSet.getString(1));
            user2.setName(resultSet.getString(2));
         }

         JdbcUtils.closeResultSet(resultSet);
         JdbcUtils.closeStatement(preparedStatement);
         user = user2;
      } catch (Exception exception) {
         throw new RuleException(exception);
      } finally {
         JdbcUtils.closeConnection(connection);
      }

      return user;
   }
   public void changeEmail(String account, String email) {
      Connection connection = JdbcUtils.getConnection();

      try {
         String text = "update URULE_USER set EMAIL_=? where ID_=?";
         PreparedStatement preparedStatement = connection.prepareStatement(text);
         preparedStatement.setString(1, email);
         preparedStatement.setString(2, account);
         preparedStatement.executeUpdate();
         JdbcUtils.closeStatement(preparedStatement);
      } catch (Exception exception) {
         throw new RuleException(exception);
      } finally {
         JdbcUtils.closeConnection(connection);
      }

   }
}
