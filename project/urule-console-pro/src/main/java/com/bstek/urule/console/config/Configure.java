package com.bstek.urule.console.config;

import com.bstek.urule.PropertyConfigurer;
import com.bstek.urule.console.config.bootstrap.BootstrapManager;
import com.bstek.urule.console.util.StringUtils;

public class Configure {
   private static final Configure a = new Configure();
   private static final String b = "urule.rest.parameter.name";

   private ConfigManager a() {
      return BootstrapManager.get().getConfigManager();
   }

   private Configure() {
   }

   public static Configure getConfigure() {
      return a;
   }

   public String getProperty(String var1) {
      String var2 = System.getProperty(var1);
      if (StringUtils.isBlank(var2)) {
         var2 = PropertyConfigurer.getProperty(var1);
      }

      if (StringUtils.isBlank(var2)) {
         var2 = this.a().getProperty(var1);
      }

      return var2;
   }

   public String getProperty(String var1, String var2) {
      String var3 = this.getProperty(var1);
      if (StringUtils.isBlank(var3)) {
         var3 = var2;
      }

      return var3;
   }

   public boolean getBoolean(String var1, boolean var2) {
      String var3 = this.getProperty(var1);
      return StringUtils.isBlank(var3) ? var2 : Boolean.valueOf(var3);
   }

   public boolean isFreeCreateGroup() {
      return this.getBoolean("urule.group.create", false);
   }

   public String getRestParameterName() {
      return this.getProperty("urule.rest.parameter.name", "参数");
   }
}
