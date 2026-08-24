package com.bstek.urule.model.rule;

public class ParentFile {
   private long id;
   private String path;
   private String version;

   public ParentFile(long id, String path, String version) {
      this.id = id;
      this.path = path;
      this.version = version;
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
