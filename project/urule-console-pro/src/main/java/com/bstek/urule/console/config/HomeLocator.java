package com.bstek.urule.console.config;

import com.bstek.urule.console.util.StringUtils;
import java.util.Properties;

public class HomeLocator {
   private static Properties configFile = PropertiesUtils.loadConfigFile("urule-init.properties");
   private static String homePath;

   public static String getConfigFileName() {
      return "urule.properties";
   }

   public static String getHomePath() {
      return HomeLocator.homePath;
   }

   static {
      if (HomeLocator.configFile != null) {
         HomeLocator.homePath = HomeLocator.configFile.getProperty("urule.home");
      }

      if (StringUtils.isBlank(HomeLocator.homePath)) {
         HomeLocator.homePath = System.getProperty("urule.home");
      }

      if (StringUtils.isBlank(HomeLocator.homePath)) {
         HomeLocator.homePath = System.getProperty("uruleHome");
      }

      if (StringUtils.isBlank(HomeLocator.homePath)) {
         HomeLocator.homePath = System.getenv("URULE_HOME");
      }

      if (StringUtils.isNotBlank(HomeLocator.homePath)) {
         System.setProperty("urule.home", HomeLocator.homePath);
      }

   }
}
