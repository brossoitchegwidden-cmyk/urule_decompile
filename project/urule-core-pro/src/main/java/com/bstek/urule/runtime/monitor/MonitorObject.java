package com.bstek.urule.runtime.monitor;

import java.util.List;

public class MonitorObject {
   private String name;
   private String clazz;
   private List<MonitorObjectField> fields;

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

   public List<MonitorObjectField> getFields() {
      return this.fields;
   }

   public void setFields(List<MonitorObjectField> fields) {
      this.fields = fields;
   }
}
