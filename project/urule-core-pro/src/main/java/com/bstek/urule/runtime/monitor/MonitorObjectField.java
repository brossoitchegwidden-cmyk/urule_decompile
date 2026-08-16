package com.bstek.urule.runtime.monitor;

public class MonitorObjectField {
   private String a;
   private String b;
   private String c;
   private MonitorObject d;

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

   public String getType() {
      return this.c;
   }

   public void setType(String var1) {
      this.c = var1;
   }

   public MonitorObject get_value() {
      return this.d;
   }

   public void set_value(MonitorObject var1) {
      this.d = var1;
   }
}
