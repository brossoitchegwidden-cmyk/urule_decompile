package com.bstek.urule.console.editor.packet.scenario;

import java.util.List;

public class SimulateData {
   private String uuid;
   private String name;
   private List fields;

   public String getUuid() {
      return this.uuid;
   }

   public void setUuid(String uuid) {
      this.uuid = uuid;
   }

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
}
