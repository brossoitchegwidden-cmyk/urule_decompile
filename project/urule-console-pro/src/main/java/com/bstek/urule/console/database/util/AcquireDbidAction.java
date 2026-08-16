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
   private static final String a = "dbid";
   private static String b = null;
   private static final Log c = LogFactory.getLog(AcquireDbidAction.class);

   private static String a() {
      if (StringUtils.isEmpty(b)) {
         DBConfigManager var0 = (DBConfigManager)BootstrapManager.get().getConfigManager();
         b = var0.getPlatform();
      }

      return b;
   }

   private static Connection b() {
      DBConfigManager var0 = (DBConfigManager)BootstrapManager.get().getConfigManager();

      try {
         return var0.getConnection();
      } catch (Exception var2) {
         throw new InfoException(var2);
      }
   }

   private static void a(Connection var0) {
      if (var0 != null) {
         try {
            var0.close();
         } catch (SQLException var2) {
            c.debug("Could not close JDBC Connection", var2);
         } catch (Throwable var3) {
            c.debug("Unexpected exception on closing JDBC Connection", var3);
         }
      }

   }

   public static Long execute(String var0, int var1) {
      Connection var2 = b();
      long var3 = 0L;
      boolean var5 = true;

      try {
         boolean var6 = true;
         var5 = var2.getAutoCommit();
         var2.setAutoCommit(false);
         String var7 = StringUtils.isEmpty(var0) ? "dbid" : var0 + "." + "dbid";
         String var8 = "select VALUE_ from URULE_PROPERTY ";
         String var9 = a();
         String var10 = "where KEY_=?";
         if ("sqlserver".equals(var9)) {
            var8 = var8 + " WITH (TABLOCKX) ";
            var8 = var8 + var10;
         } else if ("sybase".equals(var9)) {
            var8 = var8 + " lock datarows ";
            var8 = var8 + var10;
         } else if (!"sqlite".equals(var9)) {
            var8 = var8 + var10;
            var8 = var8 + " for update ";
         } else {
            var8 = var8 + var10;
         }

         PreparedStatement var11 = var2.prepareStatement(var8);
         var11.setString(1, var7);
         ResultSet var12 = var11.executeQuery();
         if (var12.next()) {
            String var13 = var12.getString(1);
            var3 = Long.parseLong(var13);
         } else {
            var6 = false;
         }

         JdbcUtils.closeResultSet(var12);
         JdbcUtils.closeStatement(var11);
         if (!var6) {
            throw new RuleException("The category:" + var0 + " not exist, id generator error!");
         }

         var11 = var2.prepareStatement("update URULE_PROPERTY set VALUE_=? where KEY_=?");
         var11.setString(1, Long.toString(var3 + (long)var1));
         var11.setString(2, var7);
         var11.executeUpdate();
         JdbcUtils.closeStatement(var11);
         var2.commit();
      } catch (SQLException var21) {
         var21.printStackTrace();
         JdbcUtils.rollback(var2);
         throw new RuleException(var21);
      } finally {
         try {
            var2.setAutoCommit(var5);
            a(var2);
         } catch (SQLException var20) {
            a(var2);
            throw new InfoException(String.format("主键[%s%]生成错误!", var0));
         }
      }

      return var3 + 1L;
   }
}
