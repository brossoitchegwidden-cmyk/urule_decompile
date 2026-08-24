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
   private static final Log logger = LogFactory.getLog(DBConfigManager.class);

   public DBConfigManager(ApplicationConfig applicationConfig) {
      super(applicationConfig);
   }
   public void load() throws ConfigLoadException {
      try {
         this.initializeConnectionSource();
         this.loadDatabaseProperties();
      } catch (Exception exception) {
         DBConfigManager.logger.error(exception);
         throw new ConfigLoadException(exception);
      }
   }

   protected abstract void initConfig(SetupInfo setupInfo);
   public void init(SetupInfo setupInfo) throws SetupException {
      try {
         this.initConfig(setupInfo);
         this.initializeConnectionSource();
         if (setupInfo.isInitializationDb()) {
            this.processSetupInfo(setupInfo);
         }

      } catch (SetupException setupException) {
         throw setupException;
      } catch (Exception exception) {
         DBConfigManager.logger.error(exception);
         throw new SetupException(exception);
      }
   }

   protected abstract void initializeConnectionSource() throws Exception;

   private void processSetupInfo(SetupInfo setupInfo) throws Exception {
      Connection connection = null;

      try {
         DBConfigManager.logger.info("规则数据库初始化...");
         connection = this.getConnection();
         connection.setAutoCommit(false);
         String platform = this.getPlatform();
         if ("dm".equals(platform)) {
            platform = "oracle";
         } else if ("kingbase".equals(platform)) {
            platform = "postgresql";
         }

         DataSourceInitializer.executeSchema(connection, platform);
         DBConfigManager.logger.info("规则数据库表结构初始化成功...");
         DataSourceInitializer.executeInitData(connection, platform);
         DBConfigManager.logger.info("规则数据库配置数据初始化成功...");
         connection.commit();
         DBConfigManager.logger.info("规则数据库初始化完成.");
      } catch (Exception exception) {
         DBConfigManager.logger.error("规则数据库初始化失败:" + exception.getMessage());
         if (connection != null) {
            connection.rollback();
         }

         throw exception;
      } finally {
         closeConnection(connection);
      }

   }

   public static void closeConnection(Connection con) {
      if (con != null) {
         try {
            con.close();
         } catch (SQLException sQLException) {
            DBConfigManager.logger.debug("Could not close JDBC Connection", sQLException);
         } catch (Throwable throwable) {
            DBConfigManager.logger.debug("Unexpected exception on closing JDBC Connection", throwable);
         }
      }

   }

   protected void loadDatabaseProperties() throws Exception {
      for(Configuration configuration : (Iterable<Configuration>)(Iterable<?>)(ConfigurationUtils.getConfigurations(this.getConnection()))) {
         this.properties.put(configuration.getKey(), configuration.getValue());
      }

   }

   public String getPlatform() {
      return this.getProperty("urule.store.database.platform");
   }

   public abstract Connection getConnection() throws Exception;
}
