package com.bstek.urule.console.config;

import com.bstek.urule.PropertyConfigurer;
import com.bstek.urule.console.config.bootstrap.BootstrapManager;
import com.bstek.urule.console.util.StringUtils;

public class Configure {
   private static final Configure configure = new Configure();
   private static final String URULE_REST_PARAMETER_NAME = "urule.rest.parameter.name";

   private ConfigManager resolveConfigManager() {
      return BootstrapManager.get().getConfigManager();
   }

   private Configure() {
   }

   public static Configure getConfigure() {
      return Configure.configure;
   }

   public String getProperty(String property) {
      String property2 = System.getProperty(property);
      if (StringUtils.isBlank(property2)) {
         property2 = PropertyConfigurer.getProperty(property);
      }

      if (StringUtils.isBlank(property2)) {
         property2 = this.resolveConfigManager().getProperty(property);
      }

      return property2;
   }

   public String getProperty(String property, String defaultValue) {
      String property2 = this.getProperty(property);
      if (StringUtils.isBlank(property2)) {
         property2 = defaultValue;
      }

      return property2;
   }

   public boolean getBoolean(String property, boolean defaultValue) {
      String property2 = this.getProperty(property);
      return StringUtils.isBlank(property2) ? defaultValue : Boolean.valueOf(property2);
   }

   public boolean isFreeCreateGroup() {
      return this.getBoolean("urule.group.create", false);
   }

   public String getRestParameterName() {
      return this.getProperty("urule.rest.parameter.name", "参数");
   }
}
