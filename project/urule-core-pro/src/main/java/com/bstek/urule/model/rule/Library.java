package com.bstek.urule.model.rule;

public class Library {
   private long id;
   private String path;
   private String version;
   private LibraryType type;

   public Library() {
   }

   public Library(long var1, String var3, String var4, LibraryType var5) {
      this.id = var1;
      this.path = var3;
      this.version = var4;
      this.type = var5;
   }

   public long getId() {
      return this.id;
   }

   public void setId(long var1) {
      this.id = var1;
   }

   public void setPath(String var1) {
      this.path = var1;
   }

   public String getPath() {
      return this.path;
   }

   public LibraryType getType() {
      return this.type;
   }

   public void setType(LibraryType var1) {
      this.type = var1;
   }

   public String getVersion() {
      return this.version;
   }

   public void setVersion(String var1) {
      this.version = var1;
   }
}
