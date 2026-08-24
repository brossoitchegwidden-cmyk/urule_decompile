package com.bstek.urule.console.database.model.batch;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(
   ignoreUnknown = true
)
public class DataParam {
   private String name;
   private String label;
   private String dataType;
   private String batchParamName;
   private Long dataProviderId;
   private Object value;
   private Integer index;
   private String formatter;
   private BatchDataProvider dataProvider;

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

   public Object getValue() {
      return this.value;
   }

   public void setValue(Object value) {
      this.value = value;
   }

   public Integer getIndex() {
      return this.index;
   }

   public void setIndex(Integer index) {
      this.index = index;
   }

   public String getBatchParamName() {
      return this.batchParamName;
   }

   public void setBatchParamName(String batchParamName) {
      this.batchParamName = batchParamName;
   }

   public Long getDataProviderId() {
      return this.dataProviderId;
   }

   public void setDataProviderId(Long dataProviderId) {
      this.dataProviderId = dataProviderId;
   }

   public BatchDataProvider getDataProvider() {
      return this.dataProvider;
   }

   public void setDataProvider(BatchDataProvider dataProvider) {
      this.dataProvider = dataProvider;
   }

   public String getFormatter() {
      return this.formatter;
   }

   public void setFormatter(String formatter) {
      this.formatter = formatter;
   }
}
