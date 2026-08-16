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

   public void setUuid(String var1) {
      this.uuid = var1;
   }

   public String getName() {
      return this.name;
   }

   public void setName(String var1) {
      this.name = var1;
   }

   public Datatype getDatatype() {
      return this.datatype;
   }

   public void setDatatype(Datatype var1) {
      this.datatype = var1;
   }

   public String getVariableCategory() {
      return this.variableCategory;
   }

   public void setVariableCategory(String var1) {
      this.variableCategory = var1;
   }

   public String getVariableCategoryUuid() {
      return this.variableCategoryUuid;
   }

   public void setVariableCategoryUuid(String var1) {
      this.variableCategoryUuid = var1;
   }

   public String getPropertyName() {
      return this.propertyName;
   }

   public void setPropertyName(String var1) {
      this.propertyName = var1;
   }

   public String getPropertyLabel() {
      return this.propertyLabel;
   }

   public void setPropertyLabel(String var1) {
      this.propertyLabel = var1;
   }

   public String getPropertyUuid() {
      return this.propertyUuid;
   }

   public void setPropertyUuid(String var1) {
      this.propertyUuid = var1;
   }

   @Override
   public String getId() {
      if (this.id == null) {
         String var1 = LocaleHolder.isEnglish() ? "Predefine" : "预定义";
         this.id = "[" + var1 + "]" + this.name;
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
