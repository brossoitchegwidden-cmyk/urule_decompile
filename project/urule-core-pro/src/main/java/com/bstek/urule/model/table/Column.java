package com.bstek.urule.model.table;

import com.bstek.urule.model.library.Datatype;

public class Column implements Comparable<Column> {
   private int num;
   private int width;
   private boolean predefine;
   private String predefineName;
   private String keyName;
   private String keyLabel;
   private String keyUuid;
   private String keyCategoryUuid;
   private String categoryUuid;
   private String uuid;
   private Datatype predefineDatatype;
   private String predefineVariableCategory;
   private String predefineVariableCategoryUuid;
   private String predefinePropertyName;
   private Datatype predefinePropertyDatatype;
   private String predefinePropertyLabel;
   private String predefinePropertyUuid;
   private String variableCategory;
   private String variableLabel;
   private String variableName;
   private Datatype datatype;
   private ColumnType type;

   public int getNum() {
      return this.num;
   }

   public void setNum(int num) {
      this.num = num;
   }

   public int getWidth() {
      return this.width;
   }

   public void setWidth(int width) {
      this.width = width;
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

   public ColumnType getType() {
      return this.type;
   }

   public void setType(ColumnType type) {
      this.type = type;
   }

   public Datatype getDatatype() {
      return this.datatype;
   }

   public void setDatatype(Datatype datatype) {
      this.datatype = datatype;
   }

   public String getPredefineName() {
      return this.predefineName;
   }

   public void setPredefineName(String predefineName) {
      this.predefineName = predefineName;
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

   public String getPredefinePropertyUuid() {
      return this.predefinePropertyUuid;
   }

   public void setPredefinePropertyUuid(String predefinePropertyUuid) {
      this.predefinePropertyUuid = predefinePropertyUuid;
   }

   public String getPredefinePropertyName() {
      return this.predefinePropertyName;
   }

   public void setPredefinePropertyName(String predefinePropertyName) {
      this.predefinePropertyName = predefinePropertyName;
   }

   public Datatype getPredefinePropertyDatatype() {
      return this.predefinePropertyDatatype;
   }

   public void setPredefinePropertyDatatype(Datatype predefinePropertyDatatype) {
      this.predefinePropertyDatatype = predefinePropertyDatatype;
   }

   public String getPredefinePropertyLabel() {
      return this.predefinePropertyLabel;
   }

   public void setPredefinePropertyLabel(String predefinePropertyLabel) {
      this.predefinePropertyLabel = predefinePropertyLabel;
   }

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

   public boolean isPredefine() {
      return this.predefine;
   }

   public void setPredefine(boolean predefine) {
      this.predefine = predefine;
   }

   public String getPropertyUuid() {
      return this.predefinePropertyUuid;
   }

   public void setPropertyUuid(String propertyUuid) {
      this.predefinePropertyUuid = propertyUuid;
   }

   public int compareTo(Column column) {
      return column.getNum() - this.num;
   }
}
