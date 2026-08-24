package com.bstek.urule.model.rule;

public class Library {
   private long id;
   private String path;
   private String version;
   private LibraryType type;

   public Library() {
   }

   public Library(long id, String path, String version, LibraryType type) {
      this.id = id;
      this.path = path;
      this.version = version;
      this.type = type;
   }

   public long getId() {
      return this.id;
   }

   public void setId(long id) {
      this.id = id;
   }

   public void setPath(String path) {
      this.path = path;
   }

   public String getPath() {
      return this.path;
   }

   public LibraryType getType() {
      return this.type;
   }

   public void setType(LibraryType type) {
      this.type = type;
   }

   public String getVersion() {
      return this.version;
   }

   public void setVersion(String version) {
      this.version = version;
   }
}
