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

   public void setUuid(String var1) {
      this.uuid = var1;
   }

   public VariableCategoryValue(String var1) {
      this.variableCategory = var1;
   }

   @Override
   public ValueType getValueType() {
      return this.valueType;
   }

   public void setVariableCategory(String var1) {
      this.variableCategory = var1;
   }

   @JsonIgnore
   @Override
   public String getId() {
      String var1 = LocaleHolder.isEnglish() ? "Variable Category" : "变量对象";
      String var2 = "[" + var1 + "]" + this.variableCategory;
      if (this.arithmetic != null) {
         var2 = var2 + this.arithmetic.getId();
      }

      return var2;
   }

   @JsonIgnore
   @Override
   public String getValueId() {
      String var1 = LocaleHolder.isEnglish() ? "Variable Category" : "变量对象";
      return "[" + var1 + "]" + this.variableCategory;
   }

   public String getVariableCategory() {
      return this.variableCategory;
   }
}
