package com.bstek.urule.console.editor.decision;

public class PredefineRow {
   private String uuid;
   private String name;
   private String fromOrIn;
   private String type;
   private String fromType;
   private String fromCategory;
   private String fromValue;
   private String params;
   private String condition;

   public String getUuid() {
      return this.uuid;
   }

   public void setUuid(String uuid) {
      this.uuid = uuid;
   }

   public String getName() {
      return this.name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public String getFromOrIn() {
      return this.fromOrIn;
   }

   public void setFromOrIn(String fromOrIn) {
      this.fromOrIn = fromOrIn;
   }

   public String getType() {
      return this.type;
   }

   public void setType(String type) {
      this.type = type;
   }

   public String getFromType() {
      return this.fromType;
   }

   public void setFromType(String fromType) {
      this.fromType = fromType;
   }

   public String getFromCategory() {
      return this.fromCategory;
   }

   public void setFromCategory(String fromCategory) {
      this.fromCategory = fromCategory;
   }

   public String getFromValue() {
      return this.fromValue;
   }

   public void setFromValue(String fromValue) {
      this.fromValue = fromValue;
   }

   public String getParams() {
      return this.params;
   }

   public void setParams(String params) {
      this.params = params;
   }

   public String getCondition() {
      return this.condition;
   }

   public void setCondition(String condition) {
      this.condition = condition;
   }
}
