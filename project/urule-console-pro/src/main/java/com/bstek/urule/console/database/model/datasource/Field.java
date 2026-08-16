package com.bstek.urule.console.database.model.datasource;

public class Field {
   private String a;
   private FieldType b;

   public Field(String var1, FieldType var2) {
      this.a = var1;
      this.b = var2;
   }

   public String getName() {
      return this.a;
   }

   public FieldType getType() {
      return this.b;
   }
}
