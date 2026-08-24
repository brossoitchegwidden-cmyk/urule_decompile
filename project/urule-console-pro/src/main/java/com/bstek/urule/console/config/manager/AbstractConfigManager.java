package com.bstek.urule.console.config.manager;

import com.bstek.urule.console.config.ApplicationConfig;
import com.bstek.urule.console.config.ConfigManager;
import java.util.Properties;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;

public abstract class AbstractConfigManager implements ConfigManager {
   private static final Log logger = LogFactory.getLog(AbstractConfigManager.class);
   protected Properties properties;
   private ApplicationConfig applicationConfig;

   public AbstractConfigManager(ApplicationConfig applicationConfig) {
      this.applicationConfig = applicationConfig;
   }

   protected void initializeProperties() {
      AbstractConfigManager.logger.debug("[URULE-CONSOLE]初始化规则属性配置信息...");
      this.properties = this.getApplicationProperties();
   }
   public String getProperty(String key) {
      if (this.properties != null && this.properties.containsKey(key)) {
         return this.properties.getProperty(key);
      } else if (this.getApplicationProperties() != null && this.getApplicationProperties().containsKey(key)) {
         return this.getApplicationProperties().getProperty(key);
      } else {
         return this.getApplicationContext() != null && this.getApplicationContext().getEnvironment().containsProperty(key) ? this.getApplicationContext().getEnvironment().getProperty(key) : null;
      }
   }

   public String getURuleHome() {
      return this.applicationConfig.getApplicationHome();
   }

   protected Properties getApplicationProperties() {
      return this.applicationConfig.getProperties();
   }

   protected ApplicationContext getApplicationContext() {
      return this.applicationConfig.getApplicationContext();
   }
}
