package com.bstek.urule.model.flow;

public class BindingFile {
   private long id;
   private String path;
   private String version;

   public BindingFile(long var1, String var3, String var4) {
      this.id = var1;
      this.path = var3;
      this.version = var4;
   }

   public long getId() {
      return this.id;
   }

   public void setPath(String var1) {
      this.path = var1;
   }

   public String getPath() {
      return this.path;
   }

   public String getVersion() {
      return this.version;
   }
}
