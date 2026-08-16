package com.bstek.urule.model.rule;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;

public class MethodValue extends AbstractValue {
   private String beanId;
   private String beanLabel;
   private String methodLabel;
   private String methodName;
   private String categoryUuid;
   private String uuid;
   private List<Parameter> parameters;
   private ValueType valueType = ValueType.Method;

   @Override
   public ValueType getValueType() {
      return this.valueType;
   }

   @JsonIgnore
   @Override
   public String getId() {
      StringBuilder var1 = new StringBuilder();
      var1.append("[BEAN][" + this.beanId + "." + this.methodName + "]");
      if (this.parameters != null) {
         var1.append("(");

         for (int var2 = 0; var2 < this.parameters.size(); var2++) {
            if (var2 > 0) {
               var1.append(",");
            }

            Parameter var3 = this.parameters.get(var2);
            var1.append(var3.getId());
         }

         var1.append(")");
      }

      if (this.arithmetic != null) {
         var1.append(this.arithmetic.getId());
      }

      return var1.toString();
   }

   @JsonIgnore
   @Override
   public String getValueId() {
      StringBuilder var1 = new StringBuilder();
      var1.append("[BEAN][" + this.beanId + "." + this.methodName + "]");
      if (this.parameters != null) {
         var1.append("(");

         for (int var2 = 0; var2 < this.parameters.size(); var2++) {
            if (var2 > 0) {
               var1.append(",");
            }

            Parameter var3 = this.parameters.get(var2);
            var1.append(var3.getId());
         }

         var1.append(")");
      }

      return var1.toString();
   }

   public String getBeanId() {
      return this.beanId;
   }

   public void setBeanId(String var1) {
      this.beanId = var1;
   }

   public String getBeanLabel() {
      return this.beanLabel;
   }

   public void setBeanLabel(String var1) {
      this.beanLabel = var1;
   }

   public String getMethodLabel() {
      return this.methodLabel;
   }

   public void setMethodLabel(String var1) {
      this.methodLabel = var1;
   }

   public String getMethodName() {
      return this.methodName;
   }

   public void setMethodName(String var1) {
      this.methodName = var1;
   }

   public List<Parameter> getParameters() {
      return this.parameters;
   }

   public void setParameters(List<Parameter> var1) {
      this.parameters = var1;
   }

   public String getCategoryUuid() {
      return this.categoryUuid;
   }

   public void setCategoryUuid(String var1) {
      this.categoryUuid = var1;
   }

   public String getUuid() {
      return this.uuid;
   }

   public void setUuid(String var1) {
      this.uuid = var1;
   }
}
