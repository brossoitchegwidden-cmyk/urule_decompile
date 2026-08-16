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

   public void setKeyName(String var1) {
      this.keyName = var1;
   }

   public String getKeyLabel() {
      return this.keyLabel;
   }

   public void setKeyLabel(String var1) {
      this.keyLabel = var1;
   }

   public String getKeyCategoryUuid() {
      return this.keyCategoryUuid;
   }

   public void setKeyCategoryUuid(String var1) {
      this.keyCategoryUuid = var1;
   }

   public String getKeyUuid() {
      return this.keyUuid;
   }

   public void setKeyUuid(String var1) {
      this.keyUuid = var1;
   }

   public String getBundleDataType() {
      return this.bundleDataType;
   }

   public void setBundleDataType(String var1) {
      this.bundleDataType = var1;
   }

   public String getVariableCategory() {
      return this.variableCategory;
   }

   public void setVariableCategory(String var1) {
      this.variableCategory = var1;
   }

   public String getVariableLabel() {
      return this.variableLabel;
   }

   public void setVariableLabel(String var1) {
      this.variableLabel = var1;
   }

   public String getVariableName() {
      return this.variableName;
   }

   public void setVariableName(String var1) {
      this.variableName = var1;
   }

   public Datatype getDatatype() {
      return this.datatype;
   }

   public void setDatatype(Datatype var1) {
      this.datatype = var1;
   }

   public String getCategoryUuid() {
      return this.categoryUuid;
   }

   public void setCategoryUuid(String var1) {
      this.categoryUuid = var1;
   }

   public String getUuid() {
      return this.uuid;
   }

   public void setUuid(String var1) {
      this.uuid = var1;
   }

   public String getPredefineName() {
      return this.predefineName;
   }

   public void setPredefineName(String var1) {
      this.predefineName = var1;
   }

   public String getPredefineUuid() {
      return this.predefineUuid;
   }

   public void setPredefineUuid(String var1) {
      this.predefineUuid = var1;
   }

   public Datatype getPredefineDatatype() {
      return this.predefineDatatype;
   }

   public void setPredefineDatatype(Datatype var1) {
      this.predefineDatatype = var1;
   }

   public String getPredefineVariableCategory() {
      return this.predefineVariableCategory;
   }

   public void setPredefineVariableCategory(String var1) {
      this.predefineVariableCategory = var1;
   }

   public String getPredefineVariableCategoryUuid() {
      return this.predefineVariableCategoryUuid;
   }

   public void setPredefineVariableCategoryUuid(String var1) {
      this.predefineVariableCategoryUuid = var1;
   }

   public String getPredefinePropertyName() {
      return this.predefinePropertyName;
   }

   public void setPredefinePropertyName(String var1) {
      this.predefinePropertyName = var1;
   }

   public String getPredefinePropertyLabel() {
      return this.predefinePropertyLabel;
   }

   public void setPredefinePropertyLabel(String var1) {
      this.predefinePropertyLabel = var1;
   }

   public String getPredefinePropertyUuid() {
      return this.predefinePropertyUuid;
   }

   public void setPredefinePropertyUuid(String var1) {
      this.predefinePropertyUuid = var1;
   }
}
