package com.bstek.urule.console.database.model.batch;

import java.util.List;

public class BatchInput {
   private String a;
   private List b;

   public String getClazz() {
      return this.a;
   }

   public void setClazz(String var1) {
      this.a = var1;
   }

   public List getFields() {
      return this.b;
   }

   public void setFields(List var1) {
      this.b = var1;
   }
}
