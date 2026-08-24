package com.bstek.urule.model.crosstab;

import com.bstek.urule.model.library.Datatype;

public abstract class BundleData {
   private String keyName;
   private String keyLabel;
   private String keyCategoryUuid;
   private String keyUuid;
   private String bundleDataType;
   private String variableCategory;
   private String variableLabel;
   private String variableName;
   private Datatype datatype;
   private String categoryUuid;
   private String uuid;
   private String predefineUuid;
   private String predefineName;
   private Datatype predefineDatatype;
   private String predefineVariableCategory;
   private String predefineVariableCategoryUuid;
   private String predefinePropertyName;
   private String predefinePropertyLabel;
   private String predefinePropertyUuid;

   public String getKeyName() {
      return this.keyName;
   }

   public void setKeyName(String keyName) {
      this.keyName = keyName;
   }

   public String getKeyLabel() {
      return this.keyLabel;
   }

   public void setKeyLabel(String keyLabel) {
      this.keyLabel = keyLabel;
   }

   public String getKeyCategoryUuid() {
      return this.keyCategoryUuid;
   }

   public void setKeyCategoryUuid(String keyCategoryUuid) {
      this.keyCategoryUuid = keyCategoryUuid;
   }

   public String getKeyUuid() {
      return this.keyUuid;
   }

   public void setKeyUuid(String keyUuid) {
      this.keyUuid = keyUuid;
   }

   public String getBundleDataType() {
      return this.bundleDataType;
   }

   public void setBundleDataType(String bundleDataType) {
      this.bundleDataType = bundleDataType;
   }

   public String getVariableCategory() {
      return this.variableCategory;
   }

   public void setVariableCategory(String variableCategory) {
      this.variableCategory = variableCategory;
   }

   public String getVariableLabel() {
      return this.variableLabel;
   }

   public void setVariableLabel(String variableLabel) {
      this.variableLabel = variableLabel;
   }

   public String getVariableName() {
      return this.variableName;
   }

   public void setVariableName(String variableName) {
      this.variableName = variableName;
   }

   public Datatype getDatatype() {
      return this.datatype;
   }

   public void setDatatype(Datatype datatype) {
      this.datatype = datatype;
   }

   public String getCategoryUuid() {
      return this.categoryUuid;
   }

   public void setCategoryUuid(String categoryUuid) {
      this.categoryUuid = categoryUuid;
   }

   public String getUuid() {
      return this.uuid;
   }

   public void setUuid(String uuid) {
      this.uuid = uuid;
   }

   public String getPredefineName() {
      return this.predefineName;
   }

   public void setPredefineName(String predefineName) {
      this.predefineName = predefineName;
   }

   public String getPredefineUuid() {
      return this.predefineUuid;
   }

   public void setPredefineUuid(String predefineUuid) {
      this.predefineUuid = predefineUuid;
   }

   public Datatype getPredefineDatatype() {
      return this.predefineDatatype;
   }

   public void setPredefineDatatype(Datatype predefineDatatype) {
      this.predefineDatatype = predefineDatatype;
   }

   public String getPredefineVariableCategory() {
      return this.predefineVariableCategory;
   }

   public void setPredefineVariableCategory(String predefineVariableCategory) {
      this.predefineVariableCategory = predefineVariableCategory;
   }

   public String getPredefineVariableCategoryUuid() {
      return this.predefineVariableCategoryUuid;
   }

   public void setPredefineVariableCategoryUuid(String predefineVariableCategoryUuid) {
      this.predefineVariableCategoryUuid = predefineVariableCategoryUuid;
   }

   public String getPredefinePropertyName() {
      return this.predefinePropertyName;
   }

   public void setPredefinePropertyName(String predefinePropertyName) {
      this.predefinePropertyName = predefinePropertyName;
   }

   public String getPredefinePropertyLabel() {
      return this.predefinePropertyLabel;
   }

   public void setPredefinePropertyLabel(String predefinePropertyLabel) {
      this.predefinePropertyLabel = predefinePropertyLabel;
   }

   public String getPredefinePropertyUuid() {
      return this.predefinePropertyUuid;
   }

   public void setPredefinePropertyUuid(String predefinePropertyUuid) {
      this.predefinePropertyUuid = predefinePropertyUuid;
   }
}
