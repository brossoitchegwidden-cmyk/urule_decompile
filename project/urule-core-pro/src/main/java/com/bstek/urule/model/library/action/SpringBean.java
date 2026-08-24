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

   public void setId(String id) {
      this.id = id;
   }

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

   public String getEname() {
      return this.ename;
   }

   public void setEname(String ename) {
      this.ename = ename;
   }

   public List<Method> getMethods() {
      return this.methods;
   }

   public void addMethod(Method method) {
      if (this.methods == null) {
         this.methods = new ArrayList<>();
      }

      this.methods.add(method);
   }

   public void setMethods(List<Method> methods) {
      this.methods = methods;
   }
}
