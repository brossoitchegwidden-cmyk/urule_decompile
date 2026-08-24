package com.bstek.urule.model.function;

public class Argument {
   private String name;
   private String ename;
   private boolean needProperty;

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

   /**获取属性needProperty的值。*/
   public boolean isNeedProperty() {
      return this.needProperty;
   }

   public void setNeedProperty(boolean needProperty) {
      this.needProperty = needProperty;
   }
}
