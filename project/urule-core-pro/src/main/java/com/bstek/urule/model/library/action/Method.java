package com.bstek.urule.model.library.action;

import java.util.ArrayList;
import java.util.List;

public class Method {
   private String uuid;
   private String name;
   private String methodName;
   private List<Parameter> parameters;

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

   public List<Parameter> getParameters() {
      return this.parameters;
   }

   public String getMethodName() {
      return this.methodName;
   }

   public void setMethodName(String methodName) {
      this.methodName = methodName;
   }

   public void addParameter(Parameter parameter) {
      if (this.parameters == null) {
         this.parameters = new ArrayList<>();
      }

      this.parameters.add(parameter);
   }

   public void setParameters(List<Parameter> parameters) {
      this.parameters = parameters;
   }
}
