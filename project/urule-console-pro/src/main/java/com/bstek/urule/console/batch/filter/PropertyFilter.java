package com.bstek.urule.console.batch.filter;

import java.util.List;

public class PropertyFilter {
   private String a;
   private String b;
   private List c;

   public String getAndorType() {
      return this.a;
   }

   public void setAndorType(String var1) {
      this.a = var1;
   }

   public String getProperty() {
      return this.b;
   }

   public void setProperty(String var1) {
      this.b = var1;
   }

   public List getValues() {
      return this.c;
   }

   public void setValues(List var1) {
      this.c = var1;
   }
}
