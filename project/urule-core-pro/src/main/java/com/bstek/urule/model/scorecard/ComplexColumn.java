package com.bstek.urule.model.scorecard;

public class ComplexColumn {
   private int num;
   private int width;
   private String variableCategory;
   private String uuid;
   private ComplexColumnType type;
   private String customLabel;

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

   public String getUuid() {
      return this.uuid;
   }

   public void setUuid(String uuid) {
      this.uuid = uuid;
   }

   public ComplexColumnType getType() {
      return this.type;
   }

   public void setType(ComplexColumnType type) {
      this.type = type;
   }

   public String getCustomLabel() {
      return this.customLabel;
   }

   public void setCustomLabel(String customLabel) {
      this.customLabel = customLabel;
   }
}
