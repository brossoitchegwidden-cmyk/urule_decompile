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

   public String getUuid() {
      return this.uuid;
   }

   public void setUuid(String var1) {
      this.uuid = var1;
   }

   public ComplexColumnType getType() {
      return this.type;
   }

   public void setType(ComplexColumnType var1) {
      this.type = var1;
   }

   public String getCustomLabel() {
      return this.customLabel;
   }

   public void setCustomLabel(String var1) {
      this.customLabel = var1;
   }
}
