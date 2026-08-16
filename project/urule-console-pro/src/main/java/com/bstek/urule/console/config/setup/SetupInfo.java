package com.bstek.urule.console.config.setup;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(
   ignoreUnknown = true
)
public class SetupInfo {
   private String a;
   private boolean b = true;
   private DataSourceInfo c = new DataSourceInfo();
   private PropertiesInfo d = new PropertiesInfo();

   public String getConfigType() {
      return this.a;
   }

   public void setConfigType(String var1) {
      this.a = var1;
   }

   public DataSourceInfo getDataSourceInfo() {
      return this.c;
   }

   public void setDataSourceInfo(DataSourceInfo var1) {
      this.c = var1;
   }

   public PropertiesInfo getPropertiesInfo() {
      return this.d;
   }

   public void setPropertiesInfo(PropertiesInfo var1) {
      this.d = var1;
   }

   public boolean isInitializationDb() {
      return this.b;
   }

   public void setInitializationDb(boolean var1) {
      this.b = var1;
   }
}
