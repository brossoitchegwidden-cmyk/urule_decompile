package com.bstek.urule.console.config.dialect;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class DialectResolver {
   private static final Log logger = LogFactory.getLog(DialectResolver.class);

   public static final Dialect resolveDialect(Connection connection) {
      try {
         return resolveDialectByMetaData(connection.getMetaData());
      } catch (SQLException sQLException) {
         DialectResolver.logger.warn(sQLException.getMessage());
         return null;
      } catch (Throwable throwable) {
         DialectResolver.logger.warn("Error executing resolver [DialectResolver] : " + throwable.getMessage());
         return null;
      }
   }

   protected static Dialect resolveDialectByMetaData(DatabaseMetaData metaData) throws SQLException {
      String databaseProductName = metaData.getDatabaseProductName();
      if ("HSQL Database Engine".equals(databaseProductName)) {
         return new HSQLDialect();
      } else if ("H2".equals(databaseProductName)) {
         return new H2Dialect();
      } else if ("MySQL".equals(databaseProductName)) {
         return new MySQLDialect();
      } else if ("DM DBMS".equals(databaseProductName)) {
         return new DmDialect();
      } else if (!"PostgreSQL".equals(databaseProductName) && !"KingbaseES".equals(databaseProductName)) {
         if (databaseProductName.startsWith("Microsoft SQL Server")) {
            return new SQLServerDialect();
         } else if ("Informix Dynamic Server".equals(databaseProductName)) {
            return new InformixDialect();
         } else if (databaseProductName.startsWith("DB2/")) {
            return new DB2Dialect();
         } else if ("Oracle".equals(databaseProductName)) {
            return new Oracle10gDialect();
         } else if ("SQLite".equals(databaseProductName)) {
            return new SQLLiteDialect();
         } else if ("Apache Hive".equals(databaseProductName)) {
            return new HiveDialect();
         } else {
            return "Presto".equals(databaseProductName) ? new PrestoDialect() : null;
         }
      } else {
         return new PostgreSQLDialect();
      }
   }
}
