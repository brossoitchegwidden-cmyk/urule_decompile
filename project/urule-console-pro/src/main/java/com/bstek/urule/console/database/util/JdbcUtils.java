package com.bstek.urule.console.database.util;

import com.bstek.urule.console.InfoException;
import com.bstek.urule.console.config.bootstrap.BootstrapManager;
import com.bstek.urule.console.config.dialect.Dialect;
import com.bstek.urule.console.config.dialect.DialectResolver;
import com.bstek.urule.console.config.manager.DBConfigManager;
import com.bstek.urule.console.database.model.datasource.FieldType;
import com.bstek.urule.exception.RuleException;
import java.sql.Array;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Date;
import java.util.List;
import java.util.Stack;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;

public class JdbcUtils {
   static ThreadLocal<Connection> currentConnection = new ThreadLocal<>();
   static ThreadLocal<Stack<Connection>> connectionStack = new ThreadLocal<>();
   private static final String URULE = "urule";
   private static final Log logger = LogFactory.getLog(JdbcUtils.class);

   public static FieldType buildJdbcFieldType(int jdbcType) {
      switch (jdbcType) {
         case Types.NVARCHAR:
         case Types.NCHAR:
         case Types.LONGNVARCHAR:
         case Types.LONGVARCHAR:
         case Types.CHAR:
         case Types.VARCHAR:
            return FieldType.String;
         case Types.BIT:
         case Types.BOOLEAN:
            return FieldType.Boolean;
         case Types.TINYINT:
            return FieldType.Short;
         case Types.SMALLINT:
         case Types.INTEGER:
            return FieldType.Integer;
         case Types.BIGINT:
            return FieldType.Long;
         case Types.REAL:
            return FieldType.Float;
         case Types.FLOAT:
         case Types.DOUBLE:
            return FieldType.Double;
         case Types.NUMERIC:
         case Types.DECIMAL:
            return FieldType.BigDecimal;
         case Types.DATE:
         case Types.TIME:
         case Types.TIMESTAMP:
            return FieldType.Date;
         default:
            return FieldType.Other;
      }
   }

   public static void fillPreparedStatementParameters(List parameters, PreparedStatement stmt) throws Exception {
      for(int index = 0; index < parameters.size(); ++index) {
         Object objectValue = parameters.get(index);
         if (objectValue instanceof String) {
            stmt.setString(index + 1, (String)objectValue);
         } else if (objectValue instanceof Long) {
            stmt.setLong(index + 1, (Long)objectValue);
         } else if (objectValue instanceof Boolean) {
            stmt.setBoolean(index + 1, (Boolean)objectValue);
         } else if (objectValue instanceof Array) {
            stmt.setArray(index + 1, (Array)objectValue);
         } else if (objectValue instanceof Date) {
            stmt.setTimestamp(index + 1, new Timestamp(((Date)objectValue).getTime()));
         } else {
            stmt.setObject(index + 1, objectValue);
         }
      }

   }

   public static void rollback(Connection conn) {
      try {
         conn.rollback();
      } catch (SQLException sQLException) {
         throw new RuleException(sQLException);
      }
   }

   public static void setAutoCommit(Connection conn, boolean autoCommit) {
      try {
         conn.setAutoCommit(autoCommit);
      } catch (SQLException sQLException) {
         throw new RuleException(sQLException);
      }
   }

   /**Close the given JDBC Connection and ignore any thrown exception. This is useful for typical finally blocks in manual JDBC code.*/
   public static void closeConnection(Connection con) {
      Stack stack = connectionStack.get();
      if (stack != null) {
         if (stack.size() > 0) {
            stack.pop();
         }

         if (stack.size() == 0 && con != null) {
            try {
               con.close();
            } catch (SQLException sQLException) {
               JdbcUtils.logger.debug("Could not close JDBC Connection", sQLException);
            } catch (Throwable throwable) {
               JdbcUtils.logger.debug("Unexpected exception on closing JDBC Connection", throwable);
            }

            currentConnection.remove();
         }
      }

   }

   public static void rollbackConnection(Connection con) {
      if (con != null) {
         try {
            con.rollback();
         } catch (SQLException sQLException) {
            JdbcUtils.logger.debug("Could not rollback JDBC Connection", sQLException);
         } catch (Throwable throwable) {
            JdbcUtils.logger.debug("Unexpected exception on rollback JDBC Connection", throwable);
         }
      }

   }

   /**Close the given JDBC Statement and ignore any thrown exception. This is useful for typical finally blocks in manual JDBC code.*/
   public static void closeStatement(Statement stmt) {
      if (stmt != null) {
         try {
            stmt.close();
         } catch (SQLException sQLException) {
            JdbcUtils.logger.trace("Could not close JDBC Statement", sQLException);
         } catch (Throwable throwable) {
            JdbcUtils.logger.trace("Unexpected exception on closing JDBC Statement", throwable);
         }
      }

   }

   /**Close the given JDBC ResultSet and ignore any thrown exception. This is useful for typical finally blocks in manual JDBC code.*/
   public static void closeResultSet(ResultSet rs) {
      if (rs != null) {
         try {
            rs.close();
         } catch (SQLException sQLException) {
            JdbcUtils.logger.trace("Could not close JDBC ResultSet", sQLException);
         } catch (Throwable throwable) {
            JdbcUtils.logger.trace("Unexpected exception on closing JDBC ResultSet", throwable);
         }
      }

   }

   public static boolean checkTableExists(Connection conn, String tableName) {
      boolean checkTableExistsResult = false;

      try {
         DatabaseMetaData metaData = conn.getMetaData();
         ResultSet tables = metaData.getTables((String)null, (String)null, tableName, (String[])null);
         if (tables.next()) {
            checkTableExistsResult = true;
         } else {
            checkTableExistsResult = false;
         }

         tables.close();
      } catch (SQLException sQLException) {
         java.util.logging.Logger.getLogger(JdbcUtils.class.getName()).log(java.util.logging.Level.SEVERE, sQLException.getMessage(), sQLException);
      }

      return checkTableExistsResult;
   }

   public static Connection getConnection() {
      Connection connection = currentConnection.get();
      if (connection == null) {
         DBConfigManager configManager = (DBConfigManager)BootstrapManager.get().getConfigManager();

         try {
            connection = configManager.getConnection();
         } catch (Exception exception) {
            throw new InfoException(exception);
         }
      }

      currentConnection.set(connection);
      Stack stack = connectionStack.get();
      if (stack == null) {
         stack = new Stack();
      }

      stack.push("urule");
      connectionStack.set(stack);
      return connection;
   }

   public static String getPlatform() {
      DBConfigManager configManager = (DBConfigManager)BootstrapManager.get().getConfigManager();
      return configManager.getPlatform();
   }

   public static String getPageSql(Connection conn, String sql, long startRow, int pageSize) {
      Dialect dialect = DialectResolver.resolveDialect(conn);
      return dialect.getLimitString(sql, Long.valueOf(startRow).intValue(), pageSize);
   }

   /**获取分页计算的sql*/
   public static String getPageSql(String sql, long startRow, int pageSize) {
      String pageSql = "";
      String platform = getPlatform();
      if (StringUtils.isEmpty(platform)) {
         throw new RuleException("未指定参数urule.store.database.platform");
      } else {
         if (platform.indexOf("oracle") == -1 && !platform.equalsIgnoreCase("dm")) {
            if (platform.indexOf("db2") != -1) {
               pageSql = buildDb2PageSql(sql, Long.valueOf(startRow).intValue(), Long.valueOf((long)pageSize).intValue());
            } else if (platform.indexOf("sqlserver") != -1) {
               pageSql = sql + " offset " + startRow + " rows fetch next " + pageSize + " rows only";
            } else if (platform.indexOf("mysql") != -1) {
               pageSql = buildMySqlPageSql(sql, Long.valueOf(startRow).intValue(), Long.valueOf((long)pageSize).intValue());
            } else if (platform.indexOf("hsql") != -1) {
               pageSql = buildHsqlPageSql(sql, Long.valueOf(startRow).intValue(), Long.valueOf((long)pageSize).intValue());
            } else if (platform.indexOf("postgresql") != -1) {
               pageSql = buildPostgreSqlPageSql(sql, Long.valueOf(startRow).intValue(), Long.valueOf((long)pageSize).intValue());
            } else {
               pageSql = sql;
            }
         } else {
            pageSql = buildOraclePageSql(sql, Long.valueOf(startRow).intValue(), Long.valueOf((long)pageSize).intValue());
         }

         return pageSql;
      }
   }

   private static String buildDb2PageSql(String text, int number, int number2) {
      return number == 0 ? text + " fetch first " + number2 + " rows only" : "select * from ( select inner2_.*, rownumber() over(order by order of inner2_) as rownumber_ from ( " + text + " fetch first " + number2 + " rows only ) as inner2_ ) as inner1_ where rownumber_ > " + number + " order by rownumber_";
   }

   private static String buildOraclePageSql(String text, int number, int number2) {
      return number == 0 ? "select * from ( " + text + ") where rownum <= " + number2 : "select * from ( select row_.*, rownum rownum_ from ( " + text + " ) row_ ) where rownum_ <= " + (number + number2) + " and rownum_ > " + number;
   }

   private static String buildMySqlPageSql(String text, int number, int number2) {
      return number == 0 ? text + " limit " + number2 : text + " limit " + number + "," + number2;
   }

   private static String buildHsqlPageSql(String text, int number, int number2) {
      return number == 0 ? text + " limit " + number2 : text + " offset " + number + " limit " + number2;
   }

   private static String buildPostgreSqlPageSql(String text, int number, int number2) {
      return number == 0 ? text + " limit " + number2 : text + " limit " + number2 + " offset " + number;
   }

   /**获取总记录数计算的sql*/
   public static String getCountSql(String sql) {
      String countSql = "select count(*) TOTAL_ROWS_ from (" + sql + ") ";
      if (getPlatform().indexOf("oracle") == -1) {
         countSql = countSql + " as ";
      }

      countSql = countSql + " countTable ";
      return countSql;
   }

   public static String getOriginSql(String sql) {
      String originSql = sql.replaceAll(":\\w+", "?");
      return originSql;
   }
}
