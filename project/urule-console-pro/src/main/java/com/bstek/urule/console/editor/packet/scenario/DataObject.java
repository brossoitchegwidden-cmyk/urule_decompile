package com.bstek.urule.console.editor.packet.scenario;

import java.util.List;

public class DataObject {
   private String a;
   private List b;

   public String getName() {
      return this.a;
   }

   public void setName(String var1) {
      this.a = var1;
   }

   public List getFields() {
      return this.b;
   }

   public void setFields(List var1) {
      this.b = var1;
   }

   public String toString() {
      return " [name=" + this.a + ", fields=" + this.b + "]";
   }
}
