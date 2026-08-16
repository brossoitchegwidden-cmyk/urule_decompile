package com.bstek.urule.runtime.monitor;

import java.util.List;

public class IOData {
   private String a;
   private String b;
   private List<IODataField> c;

   public String getName() {
      return this.a;
   }

   public void setName(String var1) {
      this.a = var1;
   }

   public String getClazz() {
      return this.b;
   }

   public void setClazz(String var1) {
      this.b = var1;
   }

   public List<IODataField> getFields() {
      return this.c;
   }

   public void setFields(List<IODataField> var1) {
      this.c = var1;
   }

   @Override
   public String toString() {
      return "IOData [name=" + this.a + ", clazz=" + this.b + ", fields=" + this.c + "]";
   }
}
