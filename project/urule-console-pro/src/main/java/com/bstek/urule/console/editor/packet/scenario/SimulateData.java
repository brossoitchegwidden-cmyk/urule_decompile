package com.bstek.urule.console.editor.packet.scenario;

import java.util.List;

public class SimulateData {
   private String a;
   private String b;
   private List c;

   public String getUuid() {
      return this.a;
   }

   public void setUuid(String var1) {
      this.a = var1;
   }

   public String getName() {
      return this.b;
   }

   public void setName(String var1) {
      this.b = var1;
   }

   public List getFields() {
      return this.c;
   }

   public void setFields(List var1) {
      this.c = var1;
   }
}
