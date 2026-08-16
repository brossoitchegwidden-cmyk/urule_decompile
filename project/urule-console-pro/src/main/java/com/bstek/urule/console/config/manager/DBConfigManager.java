package com.bstek.urule.console.config.manager;

import com.bstek.urule.console.config.ApplicationConfig;
import com.bstek.urule.console.config.Configuration;
import com.bstek.urule.console.config.exception.ConfigLoadException;
import com.bstek.urule.console.config.exception.SetupException;
import com.bstek.urule.console.config.script.DataSourceInitializer;
import com.bstek.urule.console.config.setup.SetupInfo;
import com.bstek.urule.console.database.util.ConfigurationUtils;
import java.sql.Connection;
import java.sql.SQLException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class DBConfigManager extends AbstractConfigManager {
   private static final Log b = LogFactory.getLog(DBConfigManager.class);

   public DBConfigManager(ApplicationConfig var1) {
      super(var1);
   }

   public void load() throws ConfigLoadException {
      try {
         this.d();
         this.e();
      } catch (Exception var2) {
         b.error(var2);
         throw new ConfigLoadException(var2);
      }
   }

   protected abstract void initConfig(SetupInfo var1);

   public void init(SetupInfo var1) throws SetupException {
      try {
         this.initConfig(var1);
         this.d();
         if (var1.isInitializationDb()) {
            this.a(var1);
         }

      } catch (SetupException var3) {
         throw var3;
      } catch (Exception var4) {
         b.error(var4);
         throw new SetupException(var4);
      }
   }

   protected abstract void d() throws Exception;

   private void a(SetupInfo var1) throws Exception {
      Connection var2 = null;

      try {
         b.info("规则数据库初始化...");
         var2 = this.getConnection();
         var2.setAutoCommit(false);
         String var3 = this.getPlatform();
         if ("dm".equals(var3)) {
            var3 = "oracle";
         } else if ("kingbase".equals(var3)) {
            var3 = "postgresql";
         }

         DataSourceInitializer.executeSchema(var2, var3);
         b.info("规则数据库表结构初始化成功...");
         DataSourceInitializer.executeInitData(var2, var3);
         b.info("规则数据库配置数据初始化成功...");
         var2.commit();
         b.info("规则数据库初始化完成.");
      } catch (Exception var7) {
         b.error("规则数据库初始化失败:" + var7.getMessage());
         if (var2 != null) {
            var2.rollback();
         }

         throw var7;
      } finally {
         closeConnection(var2);
      }

   }

   public static void closeConnection(Connection var0) {
      if (var0 != null) {
         try {
            var0.close();
         } catch (SQLException var2) {
            b.debug("Could not close JDBC Connection", var2);
         } catch (Throwable var3) {
            b.debug("Unexpected exception on closing JDBC Connection", var3);
         }
      }

   }

   protected void e() throws Exception {
      for(Configuration var3 : (Iterable<Configuration>)(Iterable<?>)(ConfigurationUtils.getConfigurations(this.getConnection()))) {
         this.a.put(var3.getKey(), var3.getValue());
      }

   }

   public String getPlatform() {
      return this.getProperty("urule.store.database.platform");
   }

   public abstract Connection getConnection() throws Exception;
}
