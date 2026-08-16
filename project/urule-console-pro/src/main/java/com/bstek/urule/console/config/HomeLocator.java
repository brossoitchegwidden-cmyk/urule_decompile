package com.bstek.urule.console.config;

import com.bstek.urule.console.util.StringUtils;
import java.util.Properties;

public class HomeLocator {
   private static Properties a = PropertiesUtils.loadConfigFile("urule-init.properties");
   private static String b;

   public static String getConfigFileName() {
      return "urule.properties";
   }

   public static String getHomePath() {
      return b;
   }

   static {
      if (a != null) {
         b = a.getProperty("urule.home");
      }

      if (StringUtils.isBlank(b)) {
         b = System.getProperty("urule.home");
      }

      if (StringUtils.isBlank(b)) {
         b = System.getProperty("uruleHome");
      }

      if (StringUtils.isBlank(b)) {
         b = System.getenv("URULE_HOME");
      }

      if (StringUtils.isNotBlank(b)) {
         System.setProperty("urule.home", b);
      }

   }
}
