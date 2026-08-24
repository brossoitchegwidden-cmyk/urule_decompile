package com.bstek.urule.console.database.model.datasource;

public class Field {
   private String name;
   private FieldType type;

   public Field(String name, FieldType type) {
      this.name = name;
      this.type = type;
   }

   public String getName() {
      return this.name;
   }

   public FieldType getType() {
      return this.type;
   }
}
