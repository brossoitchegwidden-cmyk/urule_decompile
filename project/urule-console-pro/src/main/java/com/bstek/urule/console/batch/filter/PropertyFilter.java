package com.bstek.urule.console.batch.filter;

import java.util.List;

public class PropertyFilter {
   private String andorType;
   private String property;
   private List values;

   public String getAndorType() {
      return this.andorType;
   }

   public void setAndorType(String andorType) {
      this.andorType = andorType;
   }

   public String getProperty() {
      return this.property;
   }

   public void setProperty(String property) {
      this.property = property;
   }

   public List getValues() {
      return this.values;
   }

   public void setValues(List values) {
      this.values = values;
   }
}
