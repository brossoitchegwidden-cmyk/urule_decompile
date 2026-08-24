package com.bstek.urule.console.database.manager.invite;

import com.bstek.urule.console.database.IDGenerator;
import com.bstek.urule.console.database.IDType;
import com.bstek.urule.console.database.model.Invite;
import com.bstek.urule.console.database.util.JdbcUtils;
import com.bstek.urule.exception.RuleException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Date;

public class InviteManagerImpl implements InviteManager {
   public Invite get(String secretKey) {
      Connection connection = JdbcUtils.getConnection();

      Invite invite;
      try {
         String text = "select ID_, GROUP_ID_, TYPE_, SECRET_KEY_, EXPIR_DATE_, CREATE_USER_, CREATE_DATE_ from URULE_INVITE where SECRET_KEY_=?";
         PreparedStatement preparedStatement = connection.prepareStatement(text);
         preparedStatement.setString(1, secretKey);
         ResultSet resultSet = preparedStatement.executeQuery();
         Invite invite2 = null;
         if (resultSet.next()) {
            invite2 = new Invite();
            invite2.setId(resultSet.getLong(1));
            invite2.setGroupId(resultSet.getString(2));
            invite2.setType(resultSet.getString(3));
            invite2.setSecretKey(resultSet.getString(4));
            invite2.setExpirDate(resultSet.getTimestamp(5));
            invite2.setCreateUser(resultSet.getString(6));
            invite2.setCreateDate(resultSet.getTimestamp(7));
         }

         JdbcUtils.closeResultSet(resultSet);
         JdbcUtils.closeStatement(preparedStatement);
         invite = invite2;
      } catch (Exception exception) {
         throw new RuleException(exception);
      } finally {
         JdbcUtils.closeConnection(connection);
      }

      return invite;
   }

   public void add(Invite invite) {
      Connection connection = JdbcUtils.getConnection();

      try {
         long longValue = IDGenerator.getInstance().nextId(IDType.INVITE);
         invite.setId(longValue);
         invite.setCreateDate(new Date(System.currentTimeMillis()));
         PreparedStatement preparedStatement = connection.prepareStatement("insert into URULE_INVITE (ID_, GROUP_ID_, TYPE_, SECRET_KEY_, EXPIR_DATE_, CREATE_USER_, CREATE_DATE_) values (?, ?, ?, ?, ?, ?, ?)");
         preparedStatement.setLong(1, invite.getId());
         preparedStatement.setString(2, invite.getGroupId());
         preparedStatement.setString(3, invite.getType());
         preparedStatement.setString(4, invite.getSecretKey());
         preparedStatement.setTimestamp(5, new Timestamp(invite.getExpirDate().getTime()));
         preparedStatement.setString(6, invite.getCreateUser());
         preparedStatement.setTimestamp(7, new Timestamp(invite.getCreateDate().getTime()));
         preparedStatement.executeUpdate();
         JdbcUtils.closeStatement(preparedStatement);
      } catch (Exception exception) {
         throw new RuleException(exception);
      } finally {
         JdbcUtils.closeConnection(connection);
      }

   }

   public void update(Invite invite) {
      Connection connection = JdbcUtils.getConnection();

      try {
         PreparedStatement preparedStatement = connection.prepareStatement("update URULE_INVITE set SECRET_KEY_=?, EXPIR_DATE_=? where GROUP_ID_=?");
         preparedStatement.setString(1, invite.getSecretKey());
         preparedStatement.setTimestamp(2, invite.getExpirDate() == null ? null : new Timestamp(invite.getExpirDate().getTime()));
         preparedStatement.setString(3, invite.getGroupId());
         preparedStatement.executeUpdate();
         JdbcUtils.closeStatement(preparedStatement);
      } catch (Exception exception) {
         throw new RuleException(exception);
      } finally {
         JdbcUtils.closeConnection(connection);
      }

   }

   public void remove(String secretKey) {
      Connection connection = JdbcUtils.getConnection();

      try {
         PreparedStatement preparedStatement = connection.prepareStatement("delete FROM URULE_INVITE where SECRET_KEY_=?");
         preparedStatement.setString(1, secretKey);
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
         PreparedStatement preparedStatement = connection.prepareStatement("delete FROM URULE_INVITE where GROUP_ID_=?");
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
