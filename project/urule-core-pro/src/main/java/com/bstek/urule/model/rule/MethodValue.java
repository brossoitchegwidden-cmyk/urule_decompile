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
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("[BEAN][" + this.beanId + "." + this.methodName + "]");
      if (this.parameters != null) {
         stringBuilder.append("(");

         for (int index = 0; index < this.parameters.size(); index++) {
            if (index > 0) {
               stringBuilder.append(",");
            }

            Parameter parameter = this.parameters.get(index);
            stringBuilder.append(parameter.getId());
         }

         stringBuilder.append(")");
      }

      if (this.arithmetic != null) {
         stringBuilder.append(this.arithmetic.getId());
      }

      return stringBuilder.toString();
   }

   @JsonIgnore
   @Override
   public String getValueId() {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("[BEAN][" + this.beanId + "." + this.methodName + "]");
      if (this.parameters != null) {
         stringBuilder.append("(");

         for (int index = 0; index < this.parameters.size(); index++) {
            if (index > 0) {
               stringBuilder.append(",");
            }

            Parameter parameter = this.parameters.get(index);
            stringBuilder.append(parameter.getId());
         }

         stringBuilder.append(")");
      }

      return stringBuilder.toString();
   }

   public String getBeanId() {
      return this.beanId;
   }

   public void setBeanId(String beanId) {
      this.beanId = beanId;
   }

   public String getBeanLabel() {
      return this.beanLabel;
   }

   public void setBeanLabel(String beanLabel) {
      this.beanLabel = beanLabel;
   }

   public String getMethodLabel() {
      return this.methodLabel;
   }

   public void setMethodLabel(String methodLabel) {
      this.methodLabel = methodLabel;
   }

   public String getMethodName() {
      return this.methodName;
   }

   public void setMethodName(String methodName) {
      this.methodName = methodName;
   }

   public List<Parameter> getParameters() {
      return this.parameters;
   }

   public void setParameters(List<Parameter> parameters) {
      this.parameters = parameters;
   }

   public String getCategoryUuid() {
      return this.categoryUuid;
   }

   public void setCategoryUuid(String categoryUuid) {
      this.categoryUuid = categoryUuid;
   }

   public String getUuid() {
      return this.uuid;
   }

   public void setUuid(String uuid) {
      this.uuid = uuid;
   }
}
