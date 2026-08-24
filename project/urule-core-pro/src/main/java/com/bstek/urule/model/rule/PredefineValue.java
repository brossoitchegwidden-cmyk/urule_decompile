package com.bstek.urule.model.rule;

import com.bstek.urule.LocaleHolder;
import com.bstek.urule.model.library.Datatype;

public class PredefineValue extends AbstractValue {
   private String uuid;
   private String name;
   private Datatype datatype;
   private String variableCategory;
   private String variableCategoryUuid;
   private String propertyName;
   private String propertyLabel;
   private String propertyUuid;

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

   public Datatype getDatatype() {
      return this.datatype;
   }

   public void setDatatype(Datatype datatype) {
      this.datatype = datatype;
   }

   public String getVariableCategory() {
      return this.variableCategory;
   }

   public void setVariableCategory(String variableCategory) {
      this.variableCategory = variableCategory;
   }

   public String getVariableCategoryUuid() {
      return this.variableCategoryUuid;
   }

   public void setVariableCategoryUuid(String variableCategoryUuid) {
      this.variableCategoryUuid = variableCategoryUuid;
   }

   public String getPropertyName() {
      return this.propertyName;
   }

   public void setPropertyName(String propertyName) {
      this.propertyName = propertyName;
   }

   public String getPropertyLabel() {
      return this.propertyLabel;
   }

   public void setPropertyLabel(String propertyLabel) {
      this.propertyLabel = propertyLabel;
   }

   public String getPropertyUuid() {
      return this.propertyUuid;
   }

   public void setPropertyUuid(String propertyUuid) {
      this.propertyUuid = propertyUuid;
   }

   @Override
   public ValueType getValueType() {
      return ValueType.Predefine;
   }

   @Override
   public String getId() {
      String text = LocaleHolder.isEnglish() ? "Predefine" : "预定义";
      String id = "[" + text + "]" + this.uuid + "";
      if (this.datatype == null) {
         id = id + "(" + this.variableCategoryUuid + "." + this.propertyUuid + ")";
      }

      if (this.arithmetic != null) {
         id = id + this.arithmetic.getId();
      }

      return id;
   }

   @Override
   public String getValueId() {
      String text = LocaleHolder.isEnglish() ? "Predefine" : "预定义";
      String valueId = "[" + text + "]" + this.name + "";
      if (this.datatype != null) {
         valueId = valueId + "(" + this.datatype.name() + ")";
      } else {
         if (this.variableCategory != null) {
            valueId = valueId + ">" + this.variableCategory;
         }

         if (this.propertyLabel != null) {
            valueId = valueId + "." + this.propertyLabel;
         }
      }

      return valueId;
   }
}
