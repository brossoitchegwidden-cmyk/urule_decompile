package com.bstek.urule.console.config.bootstrap;

import com.bstek.urule.console.config.ApplicationConfig;
import com.bstek.urule.console.config.ConfigManager;
import com.bstek.urule.console.config.ConfigureManagerFactory;
import com.bstek.urule.console.config.HomeLocator;
import com.bstek.urule.console.util.StringUtils;
import com.bstek.urule.exception.RuleException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.OrderComparator;
import org.springframework.core.env.Environment;
import org.springframework.core.env.PropertiesPropertySource;

public class BootstrapManager implements InitializingBean, ApplicationContextAware {
   private static final Log logger = LogFactory.getLog(BootstrapManager.class);
   private static boolean bootstrapped;
   private static ConfigManager configManager;
   private static ApplicationConfig applicationConfig;
   private static String setupDocumentationHint = "请参考:http://www.bstek.com/resources/doc/4.0/config/2.2setup.html";
   private static String missingDatabaseConfigurationMessage;
   private static String missingConfigurationFileMessage;
   private static List filterDefinitionList;
   private ApplicationContext applicationContext;
   private static BootstrapManager bootstrapManager;

   public void load() {
      try {
         String homePath = HomeLocator.getHomePath();
         BootstrapManager.applicationConfig = new ApplicationConfig();
         BootstrapManager.applicationConfig.setApplicationContext(this.applicationContext);
         System.out.println("[URULE-CONSOLE]URule Home:" + homePath);
         BootstrapManager.logger.info("[URULE-CONSOLE]URule Home:" + homePath);
         if (StringUtils.isNotBlank(homePath)) {
            BootstrapManager.applicationConfig.setApplicationHome(homePath);
            BootstrapManager.applicationConfig.setConfigurationFileName(HomeLocator.getConfigFileName());
            if (BootstrapManager.applicationConfig.configFileExists()) {
               BootstrapManager.logger.info("[URULE-CONSOLE]检测到urule.properties配置文件,开始初始化...");
               BootstrapManager.applicationConfig.load();
               BootstrapManager.configManager = ConfigureManagerFactory.initConfigManager(BootstrapManager.applicationConfig);
               BootstrapManager.configManager.load();
               BootstrapManager.applicationConfig.getSetupStep();
               BootstrapManager.applicationConfig.setSetupComplete(true);
               BootstrapManager.logger.info("[URULE-CONSOLE]初始化完成!");
               BootstrapManager.bootstrapped = true;
            } else {
               BootstrapManager.logger.error(BootstrapManager.missingConfigurationFileMessage);
            }
         } else if (StringUtils.isNotBlank(this.resolveEnvironmentProperty("urule.config.type"))) {
            BootstrapManager.logger.info("[URULE-CONSOLE]检测到Environment配置信息,开始初始化...");
            BootstrapManager.applicationConfig.setConfigType(this.resolveEnvironmentProperty("urule.config.type"));
            BootstrapManager.configManager = ConfigureManagerFactory.initConfigManager(BootstrapManager.applicationConfig);
            BootstrapManager.configManager.load();
            BootstrapManager.applicationConfig.getSetupStep();
            BootstrapManager.applicationConfig.setSetupComplete(true);
            BootstrapManager.logger.info("[URULE-CONSOLE]初始化完成!");
            BootstrapManager.bootstrapped = true;
         } else {
            BootstrapManager.logger.error(BootstrapManager.missingDatabaseConfigurationMessage);
         }

      } catch (Exception exception) {
         throw new RuleException(exception);
      }
   }

   private String resolveEnvironmentProperty(String text) {
      Environment environment = this.applicationContext.getEnvironment();
      boolean flag = environment.containsProperty(text);
      if (flag) {
         return environment.getProperty(text);
      } else {
         Object property2 = null;
         Map propertySourcesPlaceholderConfigurers = this.applicationContext.getBeansOfType(PropertySourcesPlaceholderConfigurer.class);
         Object[] values = propertySourcesPlaceholderConfigurers.values().toArray();
         ArrayList items = new ArrayList();

         for(Object objectValue : values) {
            items.add((PropertySourcesPlaceholderConfigurer)objectValue);
         }

         OrderComparator.sort(items);

         for(PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer : (Iterable<PropertySourcesPlaceholderConfigurer>)(Iterable<?>)(items)) {
            for(Object objectValue2 : propertySourcesPlaceholderConfigurer.getAppliedPropertySources()) {
               if (objectValue2 instanceof PropertiesPropertySource) {
                  PropertiesPropertySource propertiesPropertySource = (PropertiesPropertySource)objectValue2;
                  flag = propertiesPropertySource.containsProperty(text);
                  if (flag) {
                     property2 = propertiesPropertySource.getProperty(text);
                     if (property2 != null) {
                        String[] propertyNames = propertiesPropertySource.getPropertyNames();

                        for(String text2 : propertyNames) {
                           Object property = propertiesPropertySource.getProperty(text2);
                           if (property != null) {
                              BootstrapManager.applicationConfig.getProperties().setProperty(text2, property.toString());
                           }
                        }
                     }
                  }
               }
            }
         }

         return property2 != null ? property2.toString() : null;
      }
   }

   public ConfigManager getConfigManager() {
      return BootstrapManager.configManager;
   }

   public boolean isBootstrapped() {
      return BootstrapManager.bootstrapped;
   }

   public boolean needCheckBootstrapped(String uri) {
      for(String text : (Iterable<String>)(Iterable<?>)(BootstrapManager.filterDefinitionList)) {
         if (uri.endsWith(text)) {
            return false;
         }
      }

      if (uri.indexOf("/urule/setup/init") <= -1 && uri.indexOf("/urule/setup") <= -1) {
         return true;
      } else {
         return false;
      }
   }

   public ApplicationConfig getApplicationConfig() {
      return BootstrapManager.applicationConfig;
   }

   public void setBootstrapped(boolean bootstrapped) {
      BootstrapManager.bootstrapped = bootstrapped;
   }

   public void setConfigManager(ConfigManager configManager) {
      BootstrapManager.configManager = configManager;
   }

   public void setApplicationConfig(ApplicationConfig applicationConfig) {
      BootstrapManager.applicationConfig = applicationConfig;
   }

   public List getFilterDefinitionList() {
      return BootstrapManager.filterDefinitionList;
   }

   public void setFilterDefinitionList(List filterDefinitionList) {
      BootstrapManager.filterDefinitionList = filterDefinitionList;
   }

   public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
      this.applicationContext = applicationContext;
      this.load();
      BootstrapManager.filterDefinitionList.add(".js");
      BootstrapManager.filterDefinitionList.add(".css");
      BootstrapManager.filterDefinitionList.add(".png");
      BootstrapManager.filterDefinitionList.add(".jpg");
      BootstrapManager.filterDefinitionList.add(".svg");
   }

   public void afterPropertiesSet() throws Exception {
      BootstrapManager.bootstrapManager = this;
   }

   public static BootstrapManager get() {
      return BootstrapManager.bootstrapManager;
   }

   static {
      BootstrapManager.missingDatabaseConfigurationMessage = "未能完成系统设置: 未找到URULE PRO相关的数据库连接属性配置." + BootstrapManager.setupDocumentationHint;
      BootstrapManager.missingConfigurationFileMessage = "未能完成配置文件加载,需要先进行配置文件初始化工作." + BootstrapManager.setupDocumentationHint;
      BootstrapManager.filterDefinitionList = new ArrayList();
   }
}
