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
   public ValueType getValueType() {
      return ValueType.Predefine;
   }

   @Override
   public String getId() {
      String var1 = LocaleHolder.isEnglish() ? "Predefine" : "预定义";
      String var2 = "[" + var1 + "]" + this.uuid + "";
      if (this.datatype == null) {
         var2 = var2 + "(" + this.variableCategoryUuid + "." + this.propertyUuid + ")";
      }

      if (this.arithmetic != null) {
         var2 = var2 + this.arithmetic.getId();
      }

      return var2;
   }

   @Override
   public String getValueId() {
      String var1 = LocaleHolder.isEnglish() ? "Predefine" : "预定义";
      String var2 = "[" + var1 + "]" + this.name + "";
      if (this.datatype != null) {
         var2 = var2 + "(" + this.datatype.name() + ")";
      } else {
         if (this.variableCategory != null) {
            var2 = var2 + ">" + this.variableCategory;
         }

         if (this.propertyLabel != null) {
            var2 = var2 + "." + this.propertyLabel;
         }
      }

      return var2;
   }
}
