package com.bstek.urule.builder.table;

import com.bstek.urule.model.crosstab.CrossCell;
import com.bstek.urule.model.crosstab.ValueCrossCell;
import com.bstek.urule.model.library.Datatype;
import java.util.ArrayList;
import java.util.List;

class CellRange {
   private int start;
   private int end;
   private boolean valueCell;
   private String keyName;
   private String keyLabel;
   private String variableCategory;
   private String variableName;
   private String variableLabel;
   private String categoryUuid;
   private String uuid;
   private Datatype datatype;
   private String predefineUuid;
   private String predefineName;
   private Datatype predefineDatatype;
   private String predefineVariableCategory;
   private String predefineVariableCategoryUuid;
   private String predefinePropertyName;
   private String predefinePropertyLabel;
   private String predefinePropertyUuid;
   private CellRange parentRange;
   private CrossCell cell;
   private List<CellRange> children = new ArrayList<>();

   public String getKeyName() {
      return this.keyName;
   }

   public void setKeyName(String text) {
      this.keyName = text;
   }

   public String getKeyLabel() {
      return this.keyLabel;
   }

   public void setKeyLabel(String text) {
      this.keyLabel = text;
   }

   public int getStart() {
      return this.start;
   }

   public void setStart(int number) {
      this.start = number;
   }

   public int getEnd() {
      return this.end;
   }

   public void setEnd(int number) {
      this.end = number;
   }

   public String getVariableCategory() {
      return this.variableCategory;
   }

   public void setVariableCategory(String text) {
      this.variableCategory = text;
   }

   public String getVariableName() {
      return this.variableName;
   }

   public void setVariableName(String text) {
      this.variableName = text;
   }

   public String getVariableLabel() {
      return this.variableLabel;
   }

   public void setVariableLabel(String text) {
      this.variableLabel = text;
   }

   public String getCategoryUuid() {
      return this.categoryUuid;
   }

   public void setCategoryUuid(String text) {
      this.categoryUuid = text;
   }

   public String getUuid() {
      return this.uuid;
   }

   public void setUuid(String text) {
      this.uuid = text;
   }

   public Datatype getDatatype() {
      return this.datatype;
   }

   public void setDatatype(Datatype datatype2) {
      this.datatype = datatype2;
   }

   public String getPredefineUuid() {
      return this.predefineUuid;
   }

   public void setPredefineUuid(String text) {
      this.predefineUuid = text;
   }

   public String getPredefineName() {
      return this.predefineName;
   }

   public void setPredefineName(String text) {
      this.predefineName = text;
   }

   public Datatype getPredefineDatatype() {
      return this.predefineDatatype;
   }

   public void setPredefineDatatype(Datatype datatype2) {
      this.predefineDatatype = datatype2;
   }

   public String getPredefineVariableCategory() {
      return this.predefineVariableCategory;
   }

   public void setPredefineVariableCategory(String text) {
      this.predefineVariableCategory = text;
   }

   public String getPredefineVariableCategoryUuid() {
      return this.predefineVariableCategoryUuid;
   }

   public void setPredefineVariableCategoryUuid(String text) {
      this.predefineVariableCategoryUuid = text;
   }

   public String getPredefinePropertyName() {
      return this.predefinePropertyName;
   }

   public void setPredefinePropertyName(String text) {
      this.predefinePropertyName = text;
   }

   public String getPredefinePropertyLabel() {
      return this.predefinePropertyLabel;
   }

   public void setPredefinePropertyLabel(String text) {
      this.predefinePropertyLabel = text;
   }

   public String getPredefinePropertyUuid() {
      return this.predefinePropertyUuid;
   }

   public void setPredefinePropertyUuid(String text) {
      this.predefinePropertyUuid = text;
   }

   public boolean isValueCell() {
      return this.valueCell;
   }

   public void setValueCell(boolean flag) {
      this.valueCell = flag;
   }

   public CellRange getParentRange() {
      return this.parentRange;
   }

   public void setParentRange(CellRange cellRange) {
      this.parentRange = cellRange;
   }

   public CrossCell getCell() {
      return this.cell;
   }

   public void setCell(CrossCell crossCell) {
      if (crossCell instanceof ValueCrossCell) {
         this.setValueCell(true);
      }

      this.cell = crossCell;
   }

   public List<CellRange> getChildren() {
      return this.children;
   }

   public void addChildRange(CellRange cellRange) {
      cellRange.setParentRange(this);
      this.children.add(cellRange);
   }
}
