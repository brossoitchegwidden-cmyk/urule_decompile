package com.bstek.urule.console.config.setup;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(
   ignoreUnknown = true
)
public class SetupInfo {
   private String configType;
   private boolean initializationDb = true;
   private DataSourceInfo dataSourceInfo = new DataSourceInfo();
   private PropertiesInfo propertiesInfo = new PropertiesInfo();

   public String getConfigType() {
      return this.configType;
   }

   public void setConfigType(String configType) {
      this.configType = configType;
   }

   public DataSourceInfo getDataSourceInfo() {
      return this.dataSourceInfo;
   }

   public void setDataSourceInfo(DataSourceInfo dataSourceInfo) {
      this.dataSourceInfo = dataSourceInfo;
   }

   public PropertiesInfo getPropertiesInfo() {
      return this.propertiesInfo;
   }

   public void setPropertiesInfo(PropertiesInfo propertiesInfo) {
      this.propertiesInfo = propertiesInfo;
   }

   public boolean isInitializationDb() {
      return this.initializationDb;
   }

   public void setInitializationDb(boolean initializationDb) {
      this.initializationDb = initializationDb;
   }
}
