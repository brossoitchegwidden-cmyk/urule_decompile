package com.bstek.urule.model.table;

import com.bstek.urule.action.Action;
import com.bstek.urule.model.library.Datatype;
import com.bstek.urule.model.rule.Value;

public class Cell {
   private int row;
   private int col;
   private int rowspan;
   private String keyName;
   private String keyLabel;
   private String keyUuid;
   private String keyCategoryUuid;
   private String variableLabel;
   private String variableName;
   private String uuid;
   private Datatype datatype;
   private Joint joint;
   private Value value;
   private Action action;

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

   public String getUuid() {
      return this.uuid;
   }

   public void setUuid(String uuid) {
      this.uuid = uuid;
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

   public int getRow() {
      return this.row;
   }

   public void setRow(int row) {
      this.row = row;
   }

   public int getCol() {
      return this.col;
   }

   public void setCol(int col) {
      this.col = col;
   }

   public int getRowspan() {
      return this.rowspan;
   }

   public void setRowspan(int rowspan) {
      this.rowspan = rowspan;
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

   public Joint getJoint() {
      return this.joint;
   }

   public void setJoint(Joint joint) {
      this.joint = joint;
   }

   public Action getAction() {
      return this.action;
   }

   public void setAction(Action action) {
      this.action = action;
   }

   public Value getValue() {
      return this.value;
   }

   public void setValue(Value value) {
      this.value = value;
   }
}
