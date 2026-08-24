package com.bstek.urule.console.config;

import java.io.File;
import java.util.Properties;
import org.springframework.context.ApplicationContext;

public class ApplicationConfig {
   private Properties properties = new Properties();
   private String setupStep;
   private String configType;
   private boolean setupComplete;
   private String applicationHome;
   private String configurationFileName;
   private ApplicationContext applicationContext;

   public String getApplicationHome() {
      return this.applicationHome;
   }

   public void setApplicationHome(String applicationHome) {
      this.applicationHome = applicationHome;
   }

   public String getConfigurationFileName() {
      return this.configurationFileName;
   }

   public void setConfigurationFileName(String configurationFileName) {
      this.configurationFileName = configurationFileName;
   }

   public String getSetupStep() {
      return this.setupStep;
   }

   public void setSetupStep(String setupStep) {
      this.setupStep = setupStep;
   }

   public boolean isSetupComplete() {
      return this.setupComplete;
   }

   public void setSetupComplete(boolean setupComplete) {
      this.setupComplete = setupComplete;
   }

   public boolean configFileExists() {
      File file = new File(this.applicationHome + "/" + this.configurationFileName);
      return file.exists();
   }

   public String getConfigType() {
      return this.configType;
   }

   public void setConfigType(String configType) {
      this.configType = configType;
   }

   public ApplicationContext getApplicationContext() {
      return this.applicationContext;
   }

   public void setApplicationContext(ApplicationContext applicationContext) {
      this.applicationContext = applicationContext;
   }

   public void load() {
      this.properties = PropertiesUtils.loadConfigFile(this.applicationHome + "/" + this.configurationFileName);
      this.configType = this.properties.getProperty("urule.config.type");
   }

   public Properties getProperties() {
      return this.properties;
   }
}
