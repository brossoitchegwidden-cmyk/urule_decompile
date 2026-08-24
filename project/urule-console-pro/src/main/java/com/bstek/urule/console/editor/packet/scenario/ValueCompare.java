package com.bstek.urule.console.editor.packet.scenario;

public class ValueCompare {
   private boolean matched;
   private String op;
   private String category;
   private String fieldName;
   private Object data;
   private Object expectedData;

   public boolean isMatched() {
      return this.matched;
   }

   public void setMatched(boolean matched) {
      this.matched = matched;
   }

   public String getOp() {
      return this.op;
   }

   public void setOp(String op) {
      this.op = op;
   }

   public String getCategory() {
      return this.category;
   }

   public void setCategory(String category) {
      this.category = category;
   }

   public String getFieldName() {
      return this.fieldName;
   }

   public void setFieldName(String fieldName) {
      this.fieldName = fieldName;
   }

   public Object getData() {
      return this.data;
   }

   public void setData(Object data) {
      this.data = data;
   }

   public Object getExpectedData() {
      return this.expectedData;
   }

   public void setExpectedData(Object expectedData) {
      this.expectedData = expectedData;
   }
}
