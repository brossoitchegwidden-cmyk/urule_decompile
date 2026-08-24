package com.bstek.urule.console.config.manager;

import com.bstek.urule.console.config.ApplicationConfig;
import com.bstek.urule.console.config.exception.ConfigLoadException;
import com.bstek.urule.console.config.exception.SetupException;
import com.bstek.urule.console.config.setup.DataSourceInfo;
import com.bstek.urule.console.config.setup.SetupInfo;
import com.bstek.urule.console.database.datasource.ConnectionProvider;
import com.bstek.urule.console.util.StringUtils;
import java.sql.Connection;
import java.util.Properties;

public class ConnectionConfigManager extends DBConfigManager {
   private ConnectionProvider connectionProvider = null;

   public ConnectionConfigManager(ApplicationConfig applicationConfig) {
      super(applicationConfig);
   }
   public void load() throws ConfigLoadException {
      this.initializeProperties();
      super.load();
   }

   protected void initializeConnectionSource() throws Exception {
      if (this.connectionProvider == null) {
         String property = this.getProperty("urule.store.database.classname");
         if (StringUtils.isNotBlank(property)) {
            this.getClass();
            this.connectionProvider = (ConnectionProvider)Class.forName(property).newInstance();
         } else if (null != this.getApplicationContext()) {
            this.connectionProvider = (ConnectionProvider)this.getApplicationContext().getBean("urule.connectionProvider");
         }
      }

   }

   public Connection getConnection() {
      return this.connectionProvider.getConnection();
   }

   public void initConfig(SetupInfo setupInfo) throws SetupException {
      Properties properties = this.getApplicationProperties();
      DataSourceInfo dataSourceInfo = setupInfo.getDataSourceInfo();
      properties.put("urule.store.database.classname", dataSourceInfo.getConnectionClassName());
      properties.put("urule.store.database.platform", dataSourceInfo.getPlatform());

      try {
         String property = this.getProperty("urule.store.database.classname");
         this.getClass();
         Object objectValue = Class.forName(property).newInstance();
         if (!(objectValue instanceof ConnectionProvider)) {
            throw new SetupException(new Exception("未定义urule.store.database.classname属性值或该类不是ConnectionProvider的实现类."));
         }
      } catch (Exception exception) {
         throw new SetupException(exception);
      }
   }
}
