package com.bstek.urule.model.library.constant;

import java.util.ArrayList;
import java.util.List;

public class ConstantCategory {
   private String uuid;
   private String name;
   private String label;
   private List<Constant> constants;

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

   public List<Constant> getConstants() {
      return this.constants;
   }

   public void setConstants(List<Constant> var1) {
      this.constants = var1;
   }

   public void addConstant(Constant var1) {
      if (this.constants == null) {
         this.constants = new ArrayList<>();
      }

      this.constants.add(var1);
   }
}
