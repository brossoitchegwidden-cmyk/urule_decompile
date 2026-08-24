package com.bstek.urule.model.rule.lhs;

import com.bstek.urule.LocaleHolder;
import com.bstek.urule.model.library.Datatype;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class PredefineLeftPart implements LeftPart {
   @JsonIgnore
   private String id;
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
   public String getId() {
      if (this.id == null) {
         String text = LocaleHolder.isEnglish() ? "Predefine" : "预定义";
         this.id = "[" + text + "]" + this.name;
         if (this.variableCategory != null) {
            this.id = this.id + "." + this.variableCategory;
         }

         if (this.propertyLabel != null) {
            this.id = this.id + "." + this.propertyLabel;
         }
      }

      return this.id;
   }
}
