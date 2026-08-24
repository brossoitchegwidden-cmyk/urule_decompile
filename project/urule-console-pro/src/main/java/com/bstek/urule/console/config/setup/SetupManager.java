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
   public static void setup(SetupInfo setupInfo) throws SetupException {
      BootstrapManager.get().getApplicationConfig();
      String homePath = HomeLocator.getHomePath();
      String configType = setupInfo.getConfigType();
      ApplicationConfig applicationConfig = new ApplicationConfig();
      applicationConfig.setApplicationHome(homePath);
      applicationConfig.setConfigType(configType);
      applicationConfig.setConfigurationFileName(HomeLocator.getConfigFileName());
      populateDataSourceProperties(setupInfo, applicationConfig.getProperties());
      ConfigManager configManager = ConfigureManagerFactory.initConfigManager(applicationConfig);
      configManager.init(setupInfo);
      Properties properties = applicationConfig.getProperties();
      Properties properties2 = new Properties();
      properties2.put("urule.config.type", configType);
      properties2.putAll(properties);
      PropertiesUtils.writeConfigFile(homePath + "/" + "urule.properties", properties2);
      BootstrapManager.get().load();
   }

   private static void populateDataSourceProperties(SetupInfo setupInfo, Properties properties) {
      DataSourceInfo dataSourceInfo = setupInfo.getDataSourceInfo();
      putIfNotBlank(properties, "urule.store.database.classname", dataSourceInfo.getConnectionClassName());
      putIfNotBlank(properties, "urule.store.database.platform", dataSourceInfo.getPlatform());
      putIfNotBlank(properties, "urule.store.database.driver", dataSourceInfo.getDriver());
      putIfNotBlank(properties, "urule.store.database.url", dataSourceInfo.getUrl());
      putIfNotBlank(properties, "urule.store.database.username", dataSourceInfo.getUsername());
      putIfNotBlank(properties, "urule.store.database.password", dataSourceInfo.getPassword());
      putIfNotBlank(properties, "urule.store.database.jndiname", dataSourceInfo.getJndi());
      putIfNotBlank(properties, "urule.store.database.initialsize", Integer.toString(dataSourceInfo.getInitialSize()));
      putIfNotBlank(properties, "urule.store.database.maxTotal", Integer.toString(dataSourceInfo.getMaxTotal()));
      putIfNotBlank(properties, "urule.store.database.maxIdle", Integer.toString(dataSourceInfo.getMaxIdle()));
      putIfNotBlank(properties, "urule.store.database.minIdle", Integer.toString(dataSourceInfo.getMinIdle()));
      putIfNotBlank(properties, "urule.store.database.validationQuery", dataSourceInfo.getValidationQuery());
   }

   private static void putIfNotBlank(Properties properties, String text, String text2) {
      if (StringUtils.isNotBlank(text2)) {
         properties.put(text, text2);
      }

   }
}
