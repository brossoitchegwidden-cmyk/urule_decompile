package com.bstek.urule.model.rule;

public class ParentFile {
   private long id;
   private String path;
   private String version;

   public ParentFile(long var1, String var3, String var4) {
      this.id = var1;
      this.path = var3;
      this.version = var4;
   }

   public long getId() {
      return this.id;
   }

   public String getPath() {
      return this.path;
   }

   public String getVersion() {
      return this.version;
   }
}
