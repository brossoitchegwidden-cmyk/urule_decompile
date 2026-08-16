package com.bstek.urule.model.rule;

import com.bstek.urule.LocaleHolder;
import com.bstek.urule.model.library.Datatype;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class VariableValue extends AbstractValue {
   private String uuid;
   private String categoryUuid;
   private String variableName;
   private String variableLabel;
   private String variableCategory;
   private Datatype datatype;
   private ValueType valueType = ValueType.Variable;

   public String getUuid() {
      return this.uuid;
   }

   public void setUuid(String var1) {
      this.uuid = var1;
   }

   public String getCategoryUuid() {
      return this.categoryUuid;
   }

   public void setCategoryUuid(String var1) {
      this.categoryUuid = var1;
   }

   public String getVariableName() {
      return this.variableName;
   }

   public void setVariableName(String var1) {
      this.variableName = var1;
   }

   public String getVariableLabel() {
      return this.variableLabel;
   }

   public void setVariableLabel(String var1) {
      this.variableLabel = var1;
   }

   public String getVariableCategory() {
      return this.variableCategory;
   }

   public void setVariableCategory(String var1) {
      this.variableCategory = var1;
   }

   public Datatype getDatatype() {
      return this.datatype;
   }

   public void setDatatype(Datatype var1) {
      this.datatype = var1;
   }

   @Override
   public ValueType getValueType() {
      return this.valueType;
   }

   @JsonIgnore
   @Override
   public String getId() {
      String var1 = LocaleHolder.isEnglish() ? "Variable" : "变量";
      String var2 = "[" + var1 + "]" + this.variableCategory + "." + this.variableLabel;
      if (this.arithmetic != null) {
         var2 = var2 + this.arithmetic.getId();
      }

      return var2;
   }

   @JsonIgnore
   @Override
   public String getValueId() {
      String var1 = LocaleHolder.isEnglish() ? "Variable" : "变量";
      return "[" + var1 + "]" + this.variableCategory + "." + this.variableLabel;
   }
}
