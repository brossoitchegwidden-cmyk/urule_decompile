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

   public void setUuid(String uuid) {
      this.uuid = uuid;
   }

   public String getCategoryUuid() {
      return this.categoryUuid;
   }

   public void setCategoryUuid(String categoryUuid) {
      this.categoryUuid = categoryUuid;
   }

   public String getVariableName() {
      return this.variableName;
   }

   public void setVariableName(String variableName) {
      this.variableName = variableName;
   }

   public String getVariableLabel() {
      return this.variableLabel;
   }

   public void setVariableLabel(String variableLabel) {
      this.variableLabel = variableLabel;
   }

   public String getVariableCategory() {
      return this.variableCategory;
   }

   public void setVariableCategory(String variableCategory) {
      this.variableCategory = variableCategory;
   }

   public Datatype getDatatype() {
      return this.datatype;
   }

   public void setDatatype(Datatype datatype) {
      this.datatype = datatype;
   }

   @Override
   public ValueType getValueType() {
      return this.valueType;
   }

   @JsonIgnore
   @Override
   public String getId() {
      String text = LocaleHolder.isEnglish() ? "Variable" : "变量";
      String id = "[" + text + "]" + this.variableCategory + "." + this.variableLabel;
      if (this.arithmetic != null) {
         id = id + this.arithmetic.getId();
      }

      return id;
   }

   @JsonIgnore
   @Override
   public String getValueId() {
      String text = LocaleHolder.isEnglish() ? "Variable" : "变量";
      return "[" + text + "]" + this.variableCategory + "." + this.variableLabel;
   }
}
