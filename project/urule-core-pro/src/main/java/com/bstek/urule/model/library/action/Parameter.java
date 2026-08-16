package com.bstek.urule.model.library.action;

import com.bstek.urule.model.library.Datatype;

public class Parameter {
   private String uuid;
   private String name;
   private String ename;
   private Datatype type;

   public String getName() {
      return this.name;
   }

   public void setName(String var1) {
      this.name = var1;
   }

   public String getEname() {
      return this.ename;
   }

   public void setEname(String var1) {
      this.ename = var1;
   }

   public Datatype getType() {
      return this.type;
   }

   public void setType(Datatype var1) {
      this.type = var1;
   }

   public String getUuid() {
      return this.uuid;
   }

   public void setUuid(String var1) {
      this.uuid = var1;
   }
}
