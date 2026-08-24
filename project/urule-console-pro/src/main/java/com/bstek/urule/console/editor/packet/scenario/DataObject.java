package com.bstek.urule.console.editor.packet.scenario;

import java.util.List;

public class DataObject {
   private String name;
   private List fields;

   public String getName() {
      return this.name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public List getFields() {
      return this.fields;
   }

   public void setFields(List fields) {
      this.fields = fields;
   }

   public String toString() {
      return " [name=" + this.name + ", fields=" + this.fields + "]";
   }
}
