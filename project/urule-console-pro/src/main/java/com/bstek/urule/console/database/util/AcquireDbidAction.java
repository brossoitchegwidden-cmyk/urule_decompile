package com.bstek.urule.console.database.util;

import com.bstek.urule.console.InfoException;
import com.bstek.urule.console.config.bootstrap.BootstrapManager;
import com.bstek.urule.console.config.manager.DBConfigManager;
import com.bstek.urule.console.util.StringUtils;
import com.bstek.urule.exception.RuleException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class AcquireDbidAction {
   private static final String DBID = "dbid";
   private static String platform = null;
   private static final Log logger = LogFactory.getLog(AcquireDbidAction.class);

   private static String getDatabasePlatform() {
      if (StringUtils.isEmpty(AcquireDbidAction.platform)) {
         DBConfigManager configManager = (DBConfigManager)BootstrapManager.get().getConfigManager();
         AcquireDbidAction.platform = configManager.getPlatform();
      }

      return AcquireDbidAction.platform;
   }

   private static Connection resolveConnection() {
      DBConfigManager configManager = (DBConfigManager)BootstrapManager.get().getConfigManager();

      try {
         return configManager.getConnection();
      } catch (Exception exception) {
         throw new InfoException(exception);
      }
   }

   private static void closeConnection(Connection connection) {
      if (connection != null) {
         try {
            connection.close();
         } catch (SQLException sQLException) {
            AcquireDbidAction.logger.debug("Could not close JDBC Connection", sQLException);
         } catch (Throwable throwable) {
            AcquireDbidAction.logger.debug("Unexpected exception on closing JDBC Connection", throwable);
         }
      }

   }

   public static Long execute(String category, int blockSize) {
      Connection connection = resolveConnection();
      long longValue = 0L;
      boolean autoCommit = true;

      try {
         boolean flag = true;
         autoCommit = connection.getAutoCommit();
         connection.setAutoCommit(false);
         String text = StringUtils.isEmpty(category) ? "dbid" : category + "." + "dbid";
         String text2 = "select VALUE_ from URULE_PROPERTY ";
         String text3 = getDatabasePlatform();
         String text4 = "where KEY_=?";
         if ("sqlserver".equals(text3)) {
            text2 = text2 + " WITH (TABLOCKX) ";
            text2 = text2 + text4;
         } else if ("sybase".equals(text3)) {
            text2 = text2 + " lock datarows ";
            text2 = text2 + text4;
         } else if (!"sqlite".equals(text3)) {
            text2 = text2 + text4;
            text2 = text2 + " for update ";
         } else {
            text2 = text2 + text4;
         }

         PreparedStatement preparedStatement = connection.prepareStatement(text2);
         preparedStatement.setString(1, text);
         ResultSet resultSet = preparedStatement.executeQuery();
         if (resultSet.next()) {
            String string = resultSet.getString(1);
            longValue = Long.parseLong(string);
         } else {
            flag = false;
         }

         JdbcUtils.closeResultSet(resultSet);
         JdbcUtils.closeStatement(preparedStatement);
         if (!flag) {
            throw new RuleException("The category:" + category + " not exist, id generator error!");
         }

         preparedStatement = connection.prepareStatement("update URULE_PROPERTY set VALUE_=? where KEY_=?");
         preparedStatement.setString(1, Long.toString(longValue + (long)blockSize));
         preparedStatement.setString(2, text);
         preparedStatement.executeUpdate();
         JdbcUtils.closeStatement(preparedStatement);
         connection.commit();
      } catch (SQLException sQLException) {
         java.util.logging.Logger.getLogger(AcquireDbidAction.class.getName()).log(java.util.logging.Level.SEVERE, sQLException.getMessage(), sQLException);
         JdbcUtils.rollback(connection);
         throw new RuleException(sQLException);
      } finally {
         try {
            connection.setAutoCommit(autoCommit);
            closeConnection(connection);
         } catch (SQLException sQLException2) {
            closeConnection(connection);
            throw new InfoException(String.format("主键[%s%]生成错误!", category));
         }
      }

      return longValue + 1L;
   }
}
