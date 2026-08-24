package com.bstek.urule.console.database.manager.group;

import com.bstek.urule.console.database.IDGenerator;
import com.bstek.urule.console.database.IDType;
import com.bstek.urule.console.database.manager.group.user.UserQuery;
import com.bstek.urule.console.database.manager.group.user.UserQueryImpl;
import com.bstek.urule.console.database.model.Group;
import com.bstek.urule.console.database.model.User;
import com.bstek.urule.console.database.util.JdbcUtils;
import com.bstek.urule.exception.RuleException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class GroupManagerImpl implements GroupManager {
   public Group get(String groupId) {
      Connection connection = JdbcUtils.getConnection();

      Group group;
      try {
         String text = "SELECT ID_, NAME_, CREATE_USER_, CREATE_DATE_, DESC_ FROM URULE_GROUP  WHERE URULE_GROUP.ID_=?";
         PreparedStatement preparedStatement = connection.prepareStatement(text);
         preparedStatement.setString(1, groupId);
         Group group2 = null;
         ResultSet resultSet = preparedStatement.executeQuery();
         if (resultSet.next()) {
            group2 = new Group();
            group2.setId(resultSet.getString(1));
            group2.setName(resultSet.getString(2));
            group2.setCreateUser(resultSet.getString(3));
            group2.setCreateDate(resultSet.getTimestamp(4));
            group2.setDesc(resultSet.getString(5));
         }

         JdbcUtils.closeResultSet(resultSet);
         JdbcUtils.closeStatement(preparedStatement);
         group = group2;
      } catch (Exception exception) {
         throw new RuleException(exception);
      } finally {
         JdbcUtils.closeConnection(connection);
      }

      return group;
   }
   public void add(Group group) {
      Connection connection = JdbcUtils.getConnection();

      try {
         group.setCreateDate(new Timestamp(System.currentTimeMillis()));
         group.setUpdateDate(new Timestamp(System.currentTimeMillis()));
         PreparedStatement preparedStatement = connection.prepareStatement("insert into URULE_GROUP (ID_, NAME_, DESC_, CREATE_USER_,CREATE_DATE_) values (?, ?, ?, ?, ?)");
         preparedStatement.setString(1, group.getId());
         preparedStatement.setString(2, group.getName());
         preparedStatement.setString(3, group.getDesc());
         preparedStatement.setString(4, group.getCreateUser());
         preparedStatement.setTimestamp(5, new Timestamp(group.getCreateDate().getTime()));
         preparedStatement.executeUpdate();
         JdbcUtils.closeStatement(preparedStatement);
      } catch (Exception exception) {
         throw new RuleException(exception);
      } finally {
         JdbcUtils.closeConnection(connection);
      }

   }
   public void update(Group group) {
      Connection connection = JdbcUtils.getConnection();

      try {
         group.setUpdateDate(new Timestamp(System.currentTimeMillis()));
         PreparedStatement preparedStatement = connection.prepareStatement("update URULE_GROUP set NAME_=?, DESC_=?, UPDATE_USER_=?, UPDATE_DATE_=? where ID_=?");
         preparedStatement.setString(1, group.getName());
         preparedStatement.setString(2, group.getDesc());
         preparedStatement.setString(3, group.getUpdateUser());
         preparedStatement.setTimestamp(4, new Timestamp(group.getUpdateDate().getTime()));
         preparedStatement.setString(5, group.getId());
         preparedStatement.executeUpdate();
         JdbcUtils.closeStatement(preparedStatement);
      } catch (Exception exception) {
         throw new RuleException(exception);
      } finally {
         JdbcUtils.closeConnection(connection);
      }

   }
   public void remove(String groupId) {
      Connection connection = JdbcUtils.getConnection();

      try {
         PreparedStatement preparedStatement = connection.prepareStatement("delete FROM URULE_GROUP where ID_=?");
         preparedStatement.setString(1, groupId);
         preparedStatement.executeUpdate();
         JdbcUtils.closeStatement(preparedStatement);
      } catch (Exception exception) {
         throw new RuleException(exception);
      } finally {
         JdbcUtils.closeConnection(connection);
      }

   }
   public void addGroupUser(String groupId, String account, String username) {
      Connection connection = JdbcUtils.getConnection();

      try {
         PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO URULE_GROUP_USER (ID_, GROUP_ID_, USER_ID_, USER_NAME_) VALUES (?, ?, ?, ?)");
         preparedStatement.setLong(1, IDGenerator.getInstance().nextId(IDType.GROUP_USER));
         preparedStatement.setString(2, groupId);
         preparedStatement.setString(3, account);
         preparedStatement.setString(4, username);
         preparedStatement.executeUpdate();
         JdbcUtils.closeStatement(preparedStatement);
      } catch (Exception exception) {
         throw new RuleException(exception);
      } finally {
         JdbcUtils.closeConnection(connection);
      }

   }
   public int count() {
      int number = 0;
      Connection connection = JdbcUtils.getConnection();

      int countResult;
      try {
         String text = "SELECT count(*) GROUP_COUNT_ FROM URULE_GROUP";
         PreparedStatement preparedStatement = connection.prepareStatement(text);
         ResultSet resultSet = preparedStatement.executeQuery();
         if (resultSet.next()) {
            number = resultSet.getInt(1);
         }

         JdbcUtils.closeResultSet(resultSet);
         JdbcUtils.closeStatement(preparedStatement);
         countResult = number;
      } catch (Exception exception) {
         throw new RuleException(exception);
      } finally {
         JdbcUtils.closeConnection(connection);
      }

      return countResult;
   }
   public void removeGroupUser(String groupId, String account) {
      Connection connection = JdbcUtils.getConnection();

      try {
         PreparedStatement preparedStatement = connection.prepareStatement("delete FROM URULE_GROUP_USER where GROUP_ID_=? AND USER_ID_=?");
         preparedStatement.setString(1, groupId);
         preparedStatement.setString(2, account);
         preparedStatement.executeUpdate();
         JdbcUtils.closeStatement(preparedStatement);
      } catch (Exception exception) {
         throw new RuleException(exception);
      } finally {
         JdbcUtils.closeConnection(connection);
      }

   }
   public void removeGroupUsers(String groupId) {
      Connection connection = JdbcUtils.getConnection();

      try {
         PreparedStatement preparedStatement = connection.prepareStatement("delete FROM URULE_GROUP_USER where GROUP_ID_=?");
         preparedStatement.setString(1, groupId);
         preparedStatement.executeUpdate();
         JdbcUtils.closeStatement(preparedStatement);
      } catch (Exception exception) {
         throw new RuleException(exception);
      } finally {
         JdbcUtils.closeConnection(connection);
      }

   }
   public List getUsers(String groupId) {
      Connection connection = JdbcUtils.getConnection();

      ArrayList users;
      try {
         String text = "SELECT USER_ID_, CREATE_DATE_ FROM URULE_GROUP_USER  WHERE GROUP_ID_=?";
         PreparedStatement preparedStatement = connection.prepareStatement(text);
         preparedStatement.setString(1, groupId);
         ArrayList items = new ArrayList();
         ResultSet resultSet = preparedStatement.executeQuery();

         while(resultSet.next()) {
            User user = new User();
            user.setId(resultSet.getString(1));
            user.setCreateDate(resultSet.getTimestamp(2));
            items.add(user);
         }

         JdbcUtils.closeResultSet(resultSet);
         JdbcUtils.closeStatement(preparedStatement);
         users = items;
      } catch (Exception exception) {
         throw new RuleException(exception);
      } finally {
         JdbcUtils.closeConnection(connection);
      }

      return users;
   }

   public GroupQuery createQuery() {
      return new GroupQueryImpl();
   }

   public UserQuery createUserQuery() {
      return new UserQueryImpl();
   }
   public User getGroupUser(String groupId, String account) {
      Connection connection = JdbcUtils.getConnection();

      User user;
      try {
         String text = "SELECT USER_ID_, CREATE_DATE_ FROM URULE_GROUP_USER  WHERE GROUP_ID_=? and USER_ID_=?";
         PreparedStatement preparedStatement = connection.prepareStatement(text);
         preparedStatement.setString(1, groupId);
         preparedStatement.setString(2, account);
         User user2 = null;
         ResultSet resultSet = preparedStatement.executeQuery();
         if (resultSet.next()) {
            user2 = new User();
            user2.setId(resultSet.getString(1));
            user2.setCreateDate(resultSet.getTimestamp(2));
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
}
