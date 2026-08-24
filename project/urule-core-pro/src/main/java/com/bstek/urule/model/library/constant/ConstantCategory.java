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

   public List<Constant> getConstants() {
      return this.constants;
   }

   public void setConstants(List<Constant> constants) {
      this.constants = constants;
   }

   public void addConstant(Constant constant) {
      if (this.constants == null) {
         this.constants = new ArrayList<>();
      }

      this.constants.add(constant);
   }
}
