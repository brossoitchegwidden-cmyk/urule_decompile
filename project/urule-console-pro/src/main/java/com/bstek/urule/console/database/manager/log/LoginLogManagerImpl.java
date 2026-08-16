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
   public void add(LoginLog var1) {
      Connection var2 = JdbcUtils.getConnection();

      try {
         var1.setCreateDate(new Timestamp(System.currentTimeMillis()));
         long var3 = IDGenerator.getInstance().nextId(IDType.LOG_USERLOGIN);
         PreparedStatement var5 = var2.prepareStatement("insert into URULE_LOG_USERLOGIN (ID_, USER_ID_, USER_NAME_, IP_, USER_AGENT_, CREATE_DATE_) values (?, ?, ?, ?, ?, ?)");
         var1.setId(var3);
         var5.setLong(1, var1.getId());
         var5.setString(2, var1.getUserId());
         var5.setString(3, var1.getUsername());
         var5.setString(4, var1.getIp());
         var5.setString(5, var1.getUserAgent());
         var5.setTimestamp(6, new Timestamp(var1.getCreateDate().getTime()));
         var5.executeUpdate();
         JdbcUtils.closeStatement(var5);
      } catch (Exception var9) {
         throw new RuleException(var9);
      } finally {
         JdbcUtils.closeConnection(var2);
      }

   }

   public void removeByUser(String var1) {
      Connection var2 = JdbcUtils.getConnection();

      try {
         PreparedStatement var3 = var2.prepareStatement("delete FROM URULE_LOG_USERLOGIN where USER_ID_=?");
         var3.setString(1, var1);
         var3.executeUpdate();
         JdbcUtils.closeStatement(var3);
      } catch (Exception var7) {
         throw new RuleException(var7);
      } finally {
         JdbcUtils.closeConnection(var2);
      }

   }

   public LoginLogQuery newQuery() {
      return new LoginLogQueryImpl();
   }

   public void addBatch(PreparedStatement var1, LoginLog var2) throws SQLException {
      var2.setCreateDate(new Timestamp(System.currentTimeMillis()));
      long var3 = IDGenerator.getInstance().nextId(IDType.LOG_USERLOGIN);
      var2.setId(var3);
      var1.setLong(1, var2.getId());
      var1.setString(2, var2.getUserId());
      var1.setString(3, var2.getUsername());
      var1.setString(4, var2.getIp());
      var1.setString(5, var2.getUserAgent());
      var1.setTimestamp(6, new Timestamp(var2.getCreateDate().getTime()));
      var1.addBatch();
   }
}
