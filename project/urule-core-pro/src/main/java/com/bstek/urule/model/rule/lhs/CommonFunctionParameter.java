package com.bstek.urule.model.rule.lhs;

import com.bstek.urule.model.rule.Value;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class CommonFunctionParameter {
   @JsonIgnore
   private String id;
   private Value objectParameter;
   private String name;
   private String ename;
   private String property;
   private String propertyLabel;

   public Value getObjectParameter() {
      return this.objectParameter;
   }

   public String getId() {
      if (this.id == null) {
         this.id = this.objectParameter.getId();
         if (this.property != null) {
            this.id = this.id + "," + this.property;
         }
      }

      return this.id;
   }

   public void setObjectParameter(Value var1) {
      this.objectParameter = var1;
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

   public String getProperty() {
      return this.property;
   }

   public void setProperty(String var1) {
      this.property = var1;
   }

   public String getPropertyLabel() {
      return this.propertyLabel;
   }

   public void setPropertyLabel(String var1) {
      this.propertyLabel = var1;
   }
}
