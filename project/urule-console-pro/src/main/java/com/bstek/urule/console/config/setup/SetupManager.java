package com.bstek.urule.console.config.setup;

import com.bstek.urule.console.config.ApplicationConfig;
import com.bstek.urule.console.config.ConfigManager;
import com.bstek.urule.console.config.ConfigureManagerFactory;
import com.bstek.urule.console.config.HomeLocator;
import com.bstek.urule.console.config.PropertiesUtils;
import com.bstek.urule.console.config.bootstrap.BootstrapManager;
import com.bstek.urule.console.config.exception.SetupException;
import com.bstek.urule.console.util.StringUtils;
import java.util.Properties;

public class SetupManager {
   public static void setup(SetupInfo var0) throws SetupException {
      BootstrapManager.get().getApplicationConfig();
      String var1 = HomeLocator.getHomePath();
      String var2 = var0.getConfigType();
      ApplicationConfig var3 = new ApplicationConfig();
      var3.setApplicationHome(var1);
      var3.setConfigType(var2);
      var3.setConfigurationFileName(HomeLocator.getConfigFileName());
      a(var0, var3.getProperties());
      ConfigManager var4 = ConfigureManagerFactory.initConfigManager(var3);
      var4.init(var0);
      Properties var5 = var3.getProperties();
      Properties var6 = new Properties();
      var6.put("urule.config.type", var2);
      var6.putAll(var5);
      PropertiesUtils.writeConfigFile(var1 + "/" + "urule.properties", var6);
      BootstrapManager.get().load();
   }

   private static void a(SetupInfo var0, Properties var1) {
      DataSourceInfo var2 = var0.getDataSourceInfo();
      a(var1, "urule.store.database.classname", var2.getConnectionClassName());
      a(var1, "urule.store.database.platform", var2.getPlatform());
      a(var1, "urule.store.database.driver", var2.getDriver());
      a(var1, "urule.store.database.url", var2.getUrl());
      a(var1, "urule.store.database.username", var2.getUsername());
      a(var1, "urule.store.database.password", var2.getPassword());
      a(var1, "urule.store.database.jndiname", var2.getJndi());
      a(var1, "urule.store.database.initialsize", Integer.toString(var2.getInitialSize()));
      a(var1, "urule.store.database.maxTotal", Integer.toString(var2.getMaxTotal()));
      a(var1, "urule.store.database.maxIdle", Integer.toString(var2.getMaxIdle()));
      a(var1, "urule.store.database.minIdle", Integer.toString(var2.getMinIdle()));
      a(var1, "urule.store.database.validationQuery", var2.getValidationQuery());
   }

   private static void a(Properties var0, String var1, String var2) {
      if (StringUtils.isNotBlank(var2)) {
         var0.put(var1, var2);
      }

   }
}
