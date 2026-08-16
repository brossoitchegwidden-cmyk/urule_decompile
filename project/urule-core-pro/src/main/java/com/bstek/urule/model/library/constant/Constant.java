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

   public void setUuid(String var1) {
      this.uuid = var1;
   }

   public String getName() {
      return this.name;
   }

   public void setName(String var1) {
      this.name = var1;
   }

   public String getLabel() {
      return this.label;
   }

   public void setLabel(String var1) {
      this.label = var1;
   }

   public Datatype getType() {
      return this.type;
   }

   public void setType(Datatype var1) {
      this.type = var1;
   }
}
