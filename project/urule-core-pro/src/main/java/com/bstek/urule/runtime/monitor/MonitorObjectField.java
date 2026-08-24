package com.bstek.urule.runtime.monitor;

public class MonitorObjectField {
   private String name;
   private String label;
   private String type;
   private MonitorObject _value;

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

   public String getType() {
      return this.type;
   }

   public void setType(String type) {
      this.type = type;
   }

   public MonitorObject get_value() {
      return this._value;
   }

   public void set_value(MonitorObject _value) {
      this._value = _value;
   }
}
