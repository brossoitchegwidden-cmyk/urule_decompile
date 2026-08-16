package com.bstek.urule.model.function;

public class Argument {
   private String name;
   private String ename;
   private boolean needProperty;

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

   public boolean isNeedProperty() {
      return this.needProperty;
   }

   public void setNeedProperty(boolean var1) {
      this.needProperty = var1;
   }
}
