package com.bstek.urule.console.config;

import java.io.File;
import java.util.Properties;
import org.springframework.context.ApplicationContext;

public class ApplicationConfig {
   private Properties a = new Properties();
   private String b;
   private String c;
   private boolean d;
   private String e;
   private String f;
   private ApplicationContext g;

   public String getApplicationHome() {
      return this.e;
   }

   public void setApplicationHome(String var1) {
      this.e = var1;
   }

   public String getConfigurationFileName() {
      return this.f;
   }

   public void setConfigurationFileName(String var1) {
      this.f = var1;
   }

   public String getSetupStep() {
      return this.b;
   }

   public void setSetupStep(String var1) {
      this.b = var1;
   }

   public boolean isSetupComplete() {
      return this.d;
   }

   public void setSetupComplete(boolean var1) {
      this.d = var1;
   }

   public boolean configFileExists() {
      File var1 = new File(this.e + "/" + this.f);
      return var1.exists();
   }

   public String getConfigType() {
      return this.c;
   }

   public void setConfigType(String var1) {
      this.c = var1;
   }

   public ApplicationContext getApplicationContext() {
      return this.g;
   }

   public void setApplicationContext(ApplicationContext var1) {
      this.g = var1;
   }

   public void load() {
      this.a = PropertiesUtils.loadConfigFile(this.e + "/" + this.f);
      this.c = this.a.getProperty("urule.config.type");
   }

   public Properties getProperties() {
      return this.a;
   }
}
