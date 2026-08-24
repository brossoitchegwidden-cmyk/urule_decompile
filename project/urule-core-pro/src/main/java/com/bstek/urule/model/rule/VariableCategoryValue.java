package com.bstek.urule.model.rule;

import com.bstek.urule.LocaleHolder;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class VariableCategoryValue extends AbstractValue {
   private String uuid;
   private String variableCategory;
   private ValueType valueType = ValueType.VariableCategory;

   public VariableCategoryValue() {
   }

   public String getUuid() {
      return this.uuid;
   }

   public void setUuid(String uuid) {
      this.uuid = uuid;
   }

   public VariableCategoryValue(String variableCategory) {
      this.variableCategory = variableCategory;
   }

   @Override
   public ValueType getValueType() {
      return this.valueType;
   }

   public void setVariableCategory(String variableCategory) {
      this.variableCategory = variableCategory;
   }

   @JsonIgnore
   @Override
   public String getId() {
      String text = LocaleHolder.isEnglish() ? "Variable Category" : "变量对象";
      String id = "[" + text + "]" + this.variableCategory;
      if (this.arithmetic != null) {
         id = id + this.arithmetic.getId();
      }

      return id;
   }

   @JsonIgnore
   @Override
   public String getValueId() {
      String text = LocaleHolder.isEnglish() ? "Variable Category" : "变量对象";
      return "[" + text + "]" + this.variableCategory;
   }

   public String getVariableCategory() {
      return this.variableCategory;
   }
}
