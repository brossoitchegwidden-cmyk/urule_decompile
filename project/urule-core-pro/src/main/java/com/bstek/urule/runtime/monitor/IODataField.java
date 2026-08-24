package com.bstek.urule.runtime.monitor;

public class IODataField {
   private String name;
   private String label;
   private Object value;

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

   public Object getValue() {
      return this.value;
   }

   public void setValue(Object value) {
      this.value = value;
   }

   @Override
   public String toString() {
      return "IODataField [name=" + this.name + ", label=" + this.label + ", value=" + this.value + "]";
   }
}
