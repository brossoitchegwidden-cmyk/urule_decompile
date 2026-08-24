package com.bstek.urule.console.database.model.batch;

import java.util.List;

public class BatchInput {
   private String clazz;
   private List fields;

   public String getClazz() {
      return this.clazz;
   }

   public void setClazz(String clazz) {
      this.clazz = clazz;
   }

   public List getFields() {
      return this.fields;
   }

   public void setFields(List fields) {
      this.fields = fields;
   }
}
