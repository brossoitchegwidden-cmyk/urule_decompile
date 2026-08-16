package com.bstek.urule.console.config.dialect;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class DialectResolver {
   private static final Log a = LogFactory.getLog(DialectResolver.class);

   public static final Dialect resolveDialect(Connection var0) {
      try {
         return a(var0.getMetaData());
      } catch (SQLException var2) {
         a.warn(var2.getMessage());
         return null;
      } catch (Throwable var3) {
         a.warn("Error executing resolver [DialectResolver] : " + var3.getMessage());
         return null;
      }
   }

   protected static Dialect a(DatabaseMetaData var0) throws SQLException {
      String var1 = var0.getDatabaseProductName();
      if ("HSQL Database Engine".equals(var1)) {
         return new HSQLDialect();
      } else if ("H2".equals(var1)) {
         return new H2Dialect();
      } else if ("MySQL".equals(var1)) {
         return new MySQLDialect();
      } else if ("DM DBMS".equals(var1)) {
         return new DmDialect();
      } else if (!"PostgreSQL".equals(var1) && !"KingbaseES".equals(var1)) {
         if (var1.startsWith("Microsoft SQL Server")) {
            return new SQLServerDialect();
         } else if ("Informix Dynamic Server".equals(var1)) {
            return new InformixDialect();
         } else if (var1.startsWith("DB2/")) {
            return new DB2Dialect();
         } else if ("Oracle".equals(var1)) {
            return new Oracle10gDialect();
         } else if ("SQLite".equals(var1)) {
            return new SQLLiteDialect();
         } else if ("Apache Hive".equals(var1)) {
            return new HiveDialect();
         } else {
            return "Presto".equals(var1) ? new PrestoDialect() : null;
         }
      } else {
         return new PostgreSQLDialect();
      }
   }
}
