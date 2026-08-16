package com.bstek.urule.model.rule.lhs;

import com.bstek.urule.LocaleHolder;
import com.bstek.urule.model.library.Datatype;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class VariableLeftPart implements LeftPart {
   @JsonIgnore
   private String id;
   private String uuid;
   private String categoryUuid;
   private String keyName;
   private String keyLabel;
   private String keyUuid;
   private String keyCategoryUuid;
   private String variableName;
   private String variableLabel;
   private String variableCategory;
   private Datatype datatype;

   public void setId(String var1) {
      this.id = var1;
   }

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

   public String getKeyName() {
      return this.keyName;
   }

   public void setKeyName(String var1) {
      this.keyName = var1;
   }

   public String getKeyUuid() {
      return this.keyUuid;
   }

   public void setKeyUuid(String var1) {
      this.keyUuid = var1;
   }

   public String getKeyCategoryUuid() {
      return this.keyCategoryUuid;
   }

   public void setKeyCategoryUuid(String var1) {
      this.keyCategoryUuid = var1;
   }

   public String getKeyLabel() {
      return this.keyLabel;
   }

   public void setKeyLabel(String var1) {
      this.keyLabel = var1;
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
   public String getId() {
      if (this.id == null) {
         String var1 = LocaleHolder.isEnglish() ? "Variable" : "变量";
         String var2 = this.getVariableCategory();
         if ("参数".equals(var2) && LocaleHolder.isEnglish()) {
            var2 = "Parameter";
         }

         this.id = "[" + var1 + "]" + var2;
         if (this.keyLabel != null) {
            this.id = this.id + "." + this.keyLabel + "." + this.getVariableLabel();
         } else {
            this.id = this.id + "." + this.getVariableLabel();
         }
      }

      return this.id;
   }
}
