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

   public void setName(String name) {
      this.name = name;
   }

   public String getEname() {
      return this.ename;
   }

   public void setEname(String ename) {
      this.ename = ename;
   }

   public Datatype getType() {
      return this.type;
   }

   public void setType(Datatype type) {
      this.type = type;
   }

   public String getUuid() {
      return this.uuid;
   }

   public void setUuid(String uuid) {
      this.uuid = uuid;
   }
}
