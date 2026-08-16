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
   private ConnectionProvider b = null;

   public ConnectionConfigManager(ApplicationConfig var1) {
      super(var1);
   }

   public void load() throws ConfigLoadException {
      this.a();
      super.load();
   }

   protected void d() throws Exception {
      if (this.b == null) {
         String var1 = this.getProperty("urule.store.database.classname");
         if (StringUtils.isNotBlank(var1)) {
            this.getClass();
            this.b = (ConnectionProvider)Class.forName(var1).newInstance();
         } else if (null != this.c()) {
            this.b = (ConnectionProvider)this.c().getBean("urule.connectionProvider");
         }
      }

   }

   public Connection getConnection() {
      return this.b.getConnection();
   }

   public void initConfig(SetupInfo var1) throws SetupException {
      Properties var2 = this.b();
      DataSourceInfo var3 = var1.getDataSourceInfo();
      var2.put("urule.store.database.classname", var3.getConnectionClassName());
      var2.put("urule.store.database.platform", var3.getPlatform());

      try {
         String var4 = this.getProperty("urule.store.database.classname");
         this.getClass();
         Object var5 = Class.forName(var4).newInstance();
         if (!(var5 instanceof ConnectionProvider)) {
            throw new SetupException(new Exception("未定义urule.store.database.classname属性值或该类不是ConnectionProvider的实现类."));
         }
      } catch (Exception var6) {
         throw new SetupException(var6);
      }
   }
}
