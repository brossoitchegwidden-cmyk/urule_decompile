package com.bstek.urule.console.database.model.batch;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(
   ignoreUnknown = true
)
public class FilterItem {
   private String uuid;
   private FilterType type;
   private String name;
   private String value;
   private Object itemObject;

   public FilterType getType() {
      return this.type;
   }

   public void setType(FilterType type) {
      this.type = type;
   }

   public String getName() {
      return this.name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public String getValue() {
      return this.value;
   }

   public void setValue(String value) {
      this.value = value;
   }

   public String getUuid() {
      return this.uuid;
   }

   public void setUuid(String uuid) {
      this.uuid = uuid;
   }

   public Object getItemObject() {
      return this.itemObject;
   }

   public void setItemObject(Object itemObject) {
      this.itemObject = itemObject;
   }
}
