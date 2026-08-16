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
import java.util.Date;
import java.util.List;
import java.util.Stack;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;

public class JdbcUtils {
   static ThreadLocal a = new ThreadLocal();
   static ThreadLocal b = new ThreadLocal();
   private static final String c = "urule";
   private static final Log d = LogFactory.getLog(JdbcUtils.class);

   public static FieldType buildJdbcFieldType(int var0) {
      FieldType var1;
      switch (var0) {
         case -16:
            var1 = FieldType.String;
            break;
         case -9:
            var1 = FieldType.String;
            break;
         case -7:
            var1 = FieldType.Boolean;
            break;
         case -6:
            var1 = FieldType.Short;
            break;
         case -5:
            var1 = FieldType.Double;
         case 4:
            var1 = FieldType.Integer;
         case 8:
            var1 = FieldType.Double;
         case 6:
            var1 = FieldType.Double;
            break;
         case -1:
            var1 = FieldType.String;
            break;
         case 1:
            var1 = FieldType.String;
            break;
         case 2:
            var1 = FieldType.BigDecimal;
            break;
         case 3:
            var1 = FieldType.BigDecimal;
            break;
         case 5:
            var1 = FieldType.Integer;
            break;
         case 7:
            var1 = FieldType.Float;
            break;
         case 12:
            var1 = FieldType.String;
            break;
         case 16:
            var1 = FieldType.Boolean;
            break;
         case 91:
            var1 = FieldType.Date;
            break;
         case 93:
            var1 = FieldType.Date;
            break;
         default:
            var1 = FieldType.Other;
      }

      return var1;
   }

   public static void fillPreparedStatementParameters(List var0, PreparedStatement var1) throws Exception {
      for(int var2 = 0; var2 < var0.size(); ++var2) {
         Object var3 = var0.get(var2);
         if (var3 instanceof String) {
            var1.setString(var2 + 1, (String)var3);
         } else if (var3 instanceof Long) {
            var1.setLong(var2 + 1, (Long)var3);
         } else if (var3 instanceof Boolean) {
            var1.setBoolean(var2 + 1, (Boolean)var3);
         } else if (var3 instanceof Array) {
            var1.setArray(var2 + 1, (Array)var3);
         } else if (var3 instanceof Date) {
            var1.setTimestamp(var2 + 1, new Timestamp(((Date)var3).getTime()));
         } else {
            var1.setObject(var2 + 1, var3);
         }
      }

   }

   public static void rollback(Connection var0) {
      try {
         var0.rollback();
      } catch (SQLException var2) {
         throw new RuleException(var2);
      }
   }

   public static void setAutoCommit(Connection var0, boolean var1) {
      try {
         var0.setAutoCommit(var1);
      } catch (SQLException var3) {
         throw new RuleException(var3);
      }
   }

   public static void closeConnection(Connection var0) {
      Stack var1 = (Stack)b.get();
      if (var1 != null) {
         if (var1.size() > 0) {
            var1.pop();
         }

         if (var1.size() == 0 && var0 != null) {
            try {
               var0.close();
            } catch (SQLException var3) {
               d.debug("Could not close JDBC Connection", var3);
            } catch (Throwable var4) {
               d.debug("Unexpected exception on closing JDBC Connection", var4);
            }

            a.remove();
         }
      }

   }

   public static void rollbackConnection(Connection var0) {
      if (var0 != null) {
         try {
            var0.rollback();
         } catch (SQLException var2) {
            d.debug("Could not rollback JDBC Connection", var2);
         } catch (Throwable var3) {
            d.debug("Unexpected exception on rollback JDBC Connection", var3);
         }
      }

   }

   public static void closeStatement(Statement var0) {
      if (var0 != null) {
         try {
            var0.close();
         } catch (SQLException var2) {
            d.trace("Could not close JDBC Statement", var2);
         } catch (Throwable var3) {
            d.trace("Unexpected exception on closing JDBC Statement", var3);
         }
      }

   }

   public static void closeResultSet(ResultSet var0) {
      if (var0 != null) {
         try {
            var0.close();
         } catch (SQLException var2) {
            d.trace("Could not close JDBC ResultSet", var2);
         } catch (Throwable var3) {
            d.trace("Unexpected exception on closing JDBC ResultSet", var3);
         }
      }

   }

   public static boolean checkTableExists(Connection var0, String var1) {
      boolean var2 = false;

      try {
         DatabaseMetaData var3 = var0.getMetaData();
         ResultSet var4 = var3.getTables((String)null, (String)null, var1, (String[])null);
         if (var4.next()) {
            var2 = true;
         } else {
            var2 = false;
         }

         var4.close();
      } catch (SQLException var5) {
         var5.printStackTrace();
      }

      return var2;
   }

   public static Connection getConnection() {
      Connection var0 = (Connection)a.get();
      if (var0 == null) {
         DBConfigManager var1 = (DBConfigManager)BootstrapManager.get().getConfigManager();

         try {
            var0 = var1.getConnection();
         } catch (Exception var3) {
            throw new InfoException(var3);
         }
      }

      a.set(var0);
      Stack var4 = (Stack)b.get();
      if (var4 == null) {
         var4 = new Stack();
      }

      var4.push("urule");
      b.set(var4);
      return var0;
   }

   public static String getPlatform() {
      DBConfigManager var0 = (DBConfigManager)BootstrapManager.get().getConfigManager();
      return var0.getPlatform();
   }

   public static String getPageSql(Connection var0, String var1, long var2, int var4) {
      Dialect var5 = DialectResolver.resolveDialect(var0);
      return var5.getLimitString(var1, Long.valueOf(var2).intValue(), var4);
   }

   public static String getPageSql(String var0, long var1, int var3) {
      String var4 = "";
      String var5 = getPlatform();
      if (StringUtils.isEmpty(var5)) {
         throw new RuleException("未指定参数urule.store.database.platform");
      } else {
         if (var5.indexOf("oracle") == -1 && !var5.equalsIgnoreCase("dm")) {
            if (var5.indexOf("db2") != -1) {
               var4 = a(var0, Long.valueOf(var1).intValue(), Long.valueOf((long)var3).intValue());
            } else if (var5.indexOf("sqlserver") != -1) {
               var4 = var0 + " offset " + var1 + " rows fetch next " + var3 + " rows only";
            } else if (var5.indexOf("mysql") != -1) {
               var4 = c(var0, Long.valueOf(var1).intValue(), Long.valueOf((long)var3).intValue());
            } else if (var5.indexOf("hsql") != -1) {
               var4 = d(var0, Long.valueOf(var1).intValue(), Long.valueOf((long)var3).intValue());
            } else if (var5.indexOf("postgresql") != -1) {
               var4 = e(var0, Long.valueOf(var1).intValue(), Long.valueOf((long)var3).intValue());
            } else {
               var4 = var0;
            }
         } else {
            var4 = b(var0, Long.valueOf(var1).intValue(), Long.valueOf((long)var3).intValue());
         }

         return var4;
      }
   }

   private static String a(String var0, int var1, int var2) {
      return var1 == 0 ? var0 + " fetch first " + var2 + " rows only" : "select * from ( select inner2_.*, rownumber() over(order by order of inner2_) as rownumber_ from ( " + var0 + " fetch first " + var2 + " rows only ) as inner2_ ) as inner1_ where rownumber_ > " + var1 + " order by rownumber_";
   }

   private static String b(String var0, int var1, int var2) {
      return var1 == 0 ? "select * from ( " + var0 + ") where rownum <= " + var2 : "select * from ( select row_.*, rownum rownum_ from ( " + var0 + " ) row_ ) where rownum_ <= " + (var1 + var2) + " and rownum_ > " + var1;
   }

   private static String c(String var0, int var1, int var2) {
      return var1 == 0 ? var0 + " limit " + var2 : var0 + " limit " + var1 + "," + var2;
   }

   private static String d(String var0, int var1, int var2) {
      return var1 == 0 ? var0 + " limit " + var2 : var0 + " offset " + var1 + " limit " + var2;
   }

   private static String e(String var0, int var1, int var2) {
      return var1 == 0 ? var0 + " limit " + var2 : var0 + " limit " + var2 + " offset " + var1;
   }

   public static String getCountSql(String var0) {
      String var1 = "select count(*) TOTAL_ROWS_ from (" + var0 + ") ";
      if (getPlatform().indexOf("oracle") == -1) {
         var1 = var1 + " as ";
      }

      var1 = var1 + " countTable ";
      return var1;
   }

   public static String getOriginSql(String var0) {
      String var1 = var0.replaceAll(":\\w+", "?");
      return var1;
   }
}
