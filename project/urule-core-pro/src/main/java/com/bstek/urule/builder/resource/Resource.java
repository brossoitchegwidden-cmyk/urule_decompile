package com.bstek.urule.builder.resource;

public class Resource {
   private long id;
   private String path;
   private String content;
   private String version;

   public Resource(long id, String content, String path, String version) {
      this.id = id;
      this.content = content;
      this.path = path;
      this.version = version;
   }

   public long getId() {
      return this.id;
   }

   public String getPath() {
      return this.path;
   }

   public String getContent() {
      return this.content;
   }

   public String getVersion() {
      return this.version;
   }

   @Override
   public String toString() {
      return "ID:" + this.id + ", Version:" + this.version + ", Path:" + this.path;
   }
}
