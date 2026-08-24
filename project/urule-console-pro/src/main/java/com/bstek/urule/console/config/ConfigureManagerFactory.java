package com.bstek.urule.console.config;

import com.bstek.urule.console.config.manager.ConnectionConfigManager;
import com.bstek.urule.console.config.manager.JNDIConfigManager;
import com.bstek.urule.console.config.manager.JdbcConfigManager;

public class ConfigureManagerFactory {
   public static ConfigManager initConfigManager(ApplicationConfig applicationConfig) {
      Object objectValue = null;
      String configType = applicationConfig.getConfigType();
      ConfigType configType2 = ConfigType.valueOf(configType);
      if (configType2 != ConfigType.embed && configType2 != ConfigType.jdbc) {
         if (configType2 == ConfigType.connection) {
            objectValue = new ConnectionConfigManager(applicationConfig);
         } else if (configType2 == ConfigType.jndi) {
            objectValue = new JNDIConfigManager(applicationConfig);
         }
      } else {
         objectValue = new JdbcConfigManager(applicationConfig);
      }

      if (objectValue == null) {
         objectValue = new JdbcConfigManager(applicationConfig);
      }

      return (ConfigManager)objectValue;
   }
}
