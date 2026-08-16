package com.bstek.urule.console.config;

import com.bstek.urule.console.config.manager.ConnectionConfigManager;
import com.bstek.urule.console.config.manager.JNDIConfigManager;
import com.bstek.urule.console.config.manager.JdbcConfigManager;

public class ConfigureManagerFactory {
   public static ConfigManager initConfigManager(ApplicationConfig var0) {
      Object var1 = null;
      String var2 = var0.getConfigType();
      ConfigType var3 = ConfigType.valueOf(var2);
      if (var3 != ConfigType.embed && var3 != ConfigType.jdbc) {
         if (var3 == ConfigType.connection) {
            var1 = new ConnectionConfigManager(var0);
         } else if (var3 == ConfigType.jndi) {
            var1 = new JNDIConfigManager(var0);
         }
      } else {
         var1 = new JdbcConfigManager(var0);
      }

      if (var1 == null) {
         var1 = new JdbcConfigManager(var0);
      }

      return (ConfigManager)var1;
   }
}
