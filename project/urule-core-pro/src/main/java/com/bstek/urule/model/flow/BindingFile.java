package com.bstek.urule.model.flow;

public class BindingFile {
   private long id;
   private String path;
   private String version;

   public BindingFile(long id, String path, String version) {
      this.id = id;
      this.path = path;
      this.version = version;
   }

   public long getId() {
      return this.id;
   }

   public void setPath(String path) {
      this.path = path;
   }

   public String getPath() {
      return this.path;
   }

   public String getVersion() {
      return this.version;
   }
}
