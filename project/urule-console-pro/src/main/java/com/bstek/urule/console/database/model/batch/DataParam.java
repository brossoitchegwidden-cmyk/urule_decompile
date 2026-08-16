package com.bstek.urule.console.database.model.batch;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(
   ignoreUnknown = true
)
public class DataParam {
   private String a;
   private String b;
   private String c;
   private String d;
   private Long e;
   private Object f;
   private Integer g;
   private String h;
   private BatchDataProvider i;

   public String getName() {
      return this.a;
   }

   public void setName(String var1) {
      this.a = var1;
   }

   public String getLabel() {
      return this.b;
   }

   public void setLabel(String var1) {
      this.b = var1;
   }

   public String getDataType() {
      return this.c;
   }

   public void setDataType(String var1) {
      this.c = var1;
   }

   public Object getValue() {
      return this.f;
   }

   public void setValue(Object var1) {
      this.f = var1;
   }

   public Integer getIndex() {
      return this.g;
   }

   public void setIndex(Integer var1) {
      this.g = var1;
   }

   public String getBatchParamName() {
      return this.d;
   }

   public void setBatchParamName(String var1) {
      this.d = var1;
   }

   public Long getDataProviderId() {
      return this.e;
   }

   public void setDataProviderId(Long var1) {
      this.e = var1;
   }

   public BatchDataProvider getDataProvider() {
      return this.i;
   }

   public void setDataProvider(BatchDataProvider var1) {
      this.i = var1;
   }

   public String getFormatter() {
      return this.h;
   }

   public void setFormatter(String var1) {
      this.h = var1;
   }
}
