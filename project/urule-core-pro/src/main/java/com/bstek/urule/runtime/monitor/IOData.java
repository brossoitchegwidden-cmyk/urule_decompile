package com.bstek.urule.runtime.monitor;

import java.util.List;

public class IOData {
   private String name;
   private String clazz;
   private List<IODataField> fields;

   public String getName() {
      return this.name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public String getClazz() {
      return this.clazz;
   }

   public void setClazz(String clazz) {
      this.clazz = clazz;
   }

   public List<IODataField> getFields() {
      return this.fields;
   }

   public void setFields(List<IODataField> fields) {
      this.fields = fields;
   }

   @Override
   public String toString() {
      return "IOData [name=" + this.name + ", clazz=" + this.clazz + ", fields=" + this.fields + "]";
   }
}
