package com.bstek.urule.builder.resource;

public class Resource {
   private long a;
   private String b;
   private String c;
   private String d;

   public Resource(long var1, String var3, String var4, String var5) {
      this.a = var1;
      this.c = var3;
      this.b = var4;
      this.d = var5;
   }

   public long getId() {
      return this.a;
   }

   public String getPath() {
      return this.b;
   }

   public String getContent() {
      return this.c;
   }

   public String getVersion() {
      return this.d;
   }

   @Override
   public String toString() {
      return "ID:" + this.a + ", Version:" + this.d + ", Path:" + this.b;
   }
}
