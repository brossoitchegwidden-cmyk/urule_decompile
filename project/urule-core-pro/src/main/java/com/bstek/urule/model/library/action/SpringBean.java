package com.bstek.urule.model.library.action;

import java.util.ArrayList;
import java.util.List;

public class SpringBean {
   private String id;
   private String uuid;
   private String name;
   private String ename;
   private List<Method> methods;

   public String getId() {
      return this.id;
   }

   public void setId(String var1) {
      this.id = var1;
   }

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

   public String getEname() {
      return this.ename;
   }

   public void setEname(String var1) {
      this.ename = var1;
   }

   public List<Method> getMethods() {
      return this.methods;
   }

   public void addMethod(Method var1) {
      if (this.methods == null) {
         this.methods = new ArrayList<>();
      }

      this.methods.add(var1);
   }

   public void setMethods(List<Method> var1) {
      this.methods = var1;
   }
}
