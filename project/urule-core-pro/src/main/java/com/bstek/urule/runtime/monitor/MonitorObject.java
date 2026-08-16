package com.bstek.urule.runtime.monitor;

import java.util.List;

public class MonitorObject {
   private String a;
   private String b;
   private List<MonitorObjectField> c;

   public String getName() {
      return this.a;
   }

   public void setName(String var1) {
      this.a = var1;
   }

   public String getClazz() {
      return this.b;
   }

   public void setClazz(String var1) {
      this.b = var1;
   }

   public List<MonitorObjectField> getFields() {
      return this.c;
   }

   public void setFields(List<MonitorObjectField> var1) {
      this.c = var1;
   }
}
