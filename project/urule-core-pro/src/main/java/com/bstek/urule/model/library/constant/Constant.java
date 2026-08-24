package com.bstek.urule.model.library.constant;

import com.bstek.urule.model.library.Datatype;

public class Constant {
   private String uuid;
   private String name;
   private String label;
   private Datatype type;

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

   public String getLabel() {
      return this.label;
   }

   public void setLabel(String label) {
      this.label = label;
   }

   public Datatype getType() {
      return this.type;
   }

   public void setType(Datatype type) {
      this.type = type;
   }
}
