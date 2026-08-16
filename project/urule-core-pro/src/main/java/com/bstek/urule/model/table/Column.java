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

   public void setNum(int var1) {
      this.num = var1;
   }

   public int getWidth() {
      return this.width;
   }

   public void setWidth(int var1) {
      this.width = var1;
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

   public ColumnType getType() {
      return this.type;
   }

   public void setType(ColumnType var1) {
      this.type = var1;
   }

   public Datatype getDatatype() {
      return this.datatype;
   }

   public void setDatatype(Datatype var1) {
      this.datatype = var1;
   }

   public String getPredefineName() {
      return this.predefineName;
   }

   public void setPredefineName(String var1) {
      this.predefineName = var1;
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

   public String getPredefinePropertyUuid() {
      return this.predefinePropertyUuid;
   }

   public void setPredefinePropertyUuid(String var1) {
      this.predefinePropertyUuid = var1;
   }

   public String getPredefinePropertyName() {
      return this.predefinePropertyName;
   }

   public void setPredefinePropertyName(String var1) {
      this.predefinePropertyName = var1;
   }

   public Datatype getPredefinePropertyDatatype() {
      return this.predefinePropertyDatatype;
   }

   public void setPredefinePropertyDatatype(Datatype var1) {
      this.predefinePropertyDatatype = var1;
   }

   public String getPredefinePropertyLabel() {
      return this.predefinePropertyLabel;
   }

   public void setPredefinePropertyLabel(String var1) {
      this.predefinePropertyLabel = var1;
   }

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

   public boolean isPredefine() {
      return this.predefine;
   }

   public void setPredefine(boolean var1) {
      this.predefine = var1;
   }

   public String getPropertyUuid() {
      return this.predefinePropertyUuid;
   }

   public void setPropertyUuid(String var1) {
      this.predefinePropertyUuid = var1;
   }

   public int compareTo(Column var1) {
      return var1.getNum() - this.num;
   }
}
