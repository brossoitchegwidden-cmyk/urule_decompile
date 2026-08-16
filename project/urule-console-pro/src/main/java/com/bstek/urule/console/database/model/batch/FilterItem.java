package com.bstek.urule.console.database.model.batch;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(
   ignoreUnknown = true
)
public class FilterItem {
   private String a;
   private FilterType b;
   private String c;
   private String d;
   private Object e;

   public FilterType getType() {
      return this.b;
   }

   public void setType(FilterType var1) {
      this.b = var1;
   }

   public String getName() {
      return this.c;
   }

   public void setName(String var1) {
      this.c = var1;
   }

   public String getValue() {
      return this.d;
   }

   public void setValue(String var1) {
      this.d = var1;
   }

   public String getUuid() {
      return this.a;
   }

   public void setUuid(String var1) {
      this.a = var1;
   }

   public Object getItemObject() {
      return this.e;
   }

   public void setItemObject(Object var1) {
      this.e = var1;
   }
}
