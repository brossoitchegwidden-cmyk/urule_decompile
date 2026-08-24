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

   public void setId(String id) {
      this.id = id;
   }

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

   public String getKeyName() {
      return this.keyName;
   }

   public void setKeyName(String keyName) {
      this.keyName = keyName;
   }

   public String getKeyUuid() {
      return this.keyUuid;
   }

   public void setKeyUuid(String keyUuid) {
      this.keyUuid = keyUuid;
   }

   public String getKeyCategoryUuid() {
      return this.keyCategoryUuid;
   }

   public void setKeyCategoryUuid(String keyCategoryUuid) {
      this.keyCategoryUuid = keyCategoryUuid;
   }

   public String getKeyLabel() {
      return this.keyLabel;
   }

   public void setKeyLabel(String keyLabel) {
      this.keyLabel = keyLabel;
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
   public String getId() {
      if (this.id == null) {
         String text = LocaleHolder.isEnglish() ? "Variable" : "变量";
         String variableCategory = this.getVariableCategory();
         if ("参数".equals(variableCategory) && LocaleHolder.isEnglish()) {
            variableCategory = "Parameter";
         }

         this.id = "[" + text + "]" + variableCategory;
         if (this.keyLabel != null) {
            this.id = this.id + "." + this.keyLabel + "." + this.getVariableLabel();
         } else {
            this.id = this.id + "." + this.getVariableLabel();
         }
      }

      return this.id;
   }
}
