package com.bstek.urule.console.database.model.batch;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(
   ignoreUnknown = true
)
public class PacketParam {
   private String name;
   private String label;
   private String dataType;
   private String batchParamName;

   public String getName() {
      return this.name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public String getLabel() {
      return this.label;
   }

   public void setLabel(String label) {
      this.label = label;
   }

   public String getDataType() {
      return this.dataType;
   }

   public void setDataType(String dataType) {
      this.dataType = dataType;
   }

   public String getBatchParamName() {
      return this.batchParamName;
   }

   public void setBatchParamName(String batchParamName) {
      this.batchParamName = batchParamName;
   }
}
