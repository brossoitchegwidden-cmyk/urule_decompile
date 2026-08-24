package com.bstek.urule.console.database.model;

import java.util.Date;

public abstract class FileInfo {
   private long id;
   private String name;
   private String path;
   private boolean fileSet;
   private String content;
   private Date createDate;
   private String createUser;

   public long getId() {
      return this.id;
   }

   public void setId(long id) {
      this.id = id;
   }

   public String getName() {
      return this.name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public String getPath() {
      return this.path;
   }

   public void setPath(String path) {
      this.path = path;
   }

   public String getContent() {
      return this.content;
   }

   public void setContent(String content) {
      this.content = content;
   }

   public Date getCreateDate() {
      return this.createDate;
   }

   public void setCreateDate(Date createDate) {
      this.createDate = createDate;
   }

   public String getCreateUser() {
      return this.createUser;
   }

   public void setCreateUser(String createUser) {
      this.createUser = createUser;
   }

   public boolean isFileSet() {
      return this.fileSet;
   }

   public void setFileSet(boolean fileSet) {
      this.fileSet = fileSet;
   }
}
