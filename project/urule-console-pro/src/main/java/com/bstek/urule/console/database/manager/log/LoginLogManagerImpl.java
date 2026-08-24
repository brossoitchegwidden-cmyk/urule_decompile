package com.bstek.urule.console.database.manager.log;

import com.bstek.urule.console.database.IDGenerator;
import com.bstek.urule.console.database.IDType;
import com.bstek.urule.console.database.model.LoginLog;
import com.bstek.urule.console.database.util.JdbcUtils;
import com.bstek.urule.exception.RuleException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

public class LoginLogManagerImpl implements LoginLogManager {
   public void add(LoginLog log) {
      Connection connection = JdbcUtils.getConnection();

      try {
         log.setCreateDate(new Timestamp(System.currentTimeMillis()));
         long longValue = IDGenerator.getInstance().nextId(IDType.LOG_USERLOGIN);
         PreparedStatement preparedStatement = connection.prepareStatement("insert into URULE_LOG_USERLOGIN (ID_, USER_ID_, USER_NAME_, IP_, USER_AGENT_, CREATE_DATE_) values (?, ?, ?, ?, ?, ?)");
         log.setId(longValue);
         preparedStatement.setLong(1, log.getId());
         preparedStatement.setString(2, log.getUserId());
         preparedStatement.setString(3, log.getUsername());
         preparedStatement.setString(4, log.getIp());
         preparedStatement.setString(5, log.getUserAgent());
         preparedStatement.setTimestamp(6, new Timestamp(log.getCreateDate().getTime()));
         preparedStatement.executeUpdate();
         JdbcUtils.closeStatement(preparedStatement);
      } catch (Exception exception) {
         throw new RuleException(exception);
      } finally {
         JdbcUtils.closeConnection(connection);
      }

   }
   public void removeByUser(String groupId) {
      Connection connection = JdbcUtils.getConnection();

      try {
         PreparedStatement preparedStatement = connection.prepareStatement("delete FROM URULE_LOG_USERLOGIN where USER_ID_=?");
         preparedStatement.setString(1, groupId);
         preparedStatement.executeUpdate();
         JdbcUtils.closeStatement(preparedStatement);
      } catch (Exception exception) {
         throw new RuleException(exception);
      } finally {
         JdbcUtils.closeConnection(connection);
      }

   }

   public LoginLogQuery newQuery() {
      return new LoginLogQueryImpl();
   }
   public void addBatch(PreparedStatement stmt, LoginLog log) throws SQLException {
      log.setCreateDate(new Timestamp(System.currentTimeMillis()));
      long longValue = IDGenerator.getInstance().nextId(IDType.LOG_USERLOGIN);
      log.setId(longValue);
      stmt.setLong(1, log.getId());
      stmt.setString(2, log.getUserId());
      stmt.setString(3, log.getUsername());
      stmt.setString(4, log.getIp());
      stmt.setString(5, log.getUserAgent());
      stmt.setTimestamp(6, new Timestamp(log.getCreateDate().getTime()));
      stmt.addBatch();
   }
}
