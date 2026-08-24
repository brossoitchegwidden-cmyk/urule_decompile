package com.bstek.urule.model.scorecard;

import com.bstek.urule.model.library.Datatype;
import com.bstek.urule.model.rule.Value;
import com.bstek.urule.model.table.Joint;

public class CardCell implements Comparable<CardCell> {
   private String variableLabel;
   private String variableName;
   private String uuid;
   private String keyLabel;
   private String keyName;
   private String keyUuid;
   private Datatype datatype;
   private CellType type;
   private String weight;
   private Joint joint;
   private Value value;
   private int row;
   private int col;

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

   public String getUuid() {
      return this.uuid;
   }

   public void setUuid(String uuid) {
      this.uuid = uuid;
   }

   public String getKeyLabel() {
      return this.keyLabel;
   }

   public void setKeyLabel(String keyLabel) {
      this.keyLabel = keyLabel;
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

   public Datatype getDatatype() {
      return this.datatype;
   }

   public void setDatatype(Datatype datatype) {
      this.datatype = datatype;
   }

   public CellType getType() {
      return this.type;
   }

   public void setType(CellType type) {
      this.type = type;
   }

   public String getWeight() {
      return this.weight;
   }

   public void setWeight(String weight) {
      this.weight = weight;
   }

   public Joint getJoint() {
      return this.joint;
   }

   public void setJoint(Joint joint) {
      this.joint = joint;
   }

   public Value getValue() {
      return this.value;
   }

   public void setValue(Value value) {
      this.value = value;
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

   public int compareTo(CardCell cardCell) {
      return this.row - cardCell.getRow();
   }
}
