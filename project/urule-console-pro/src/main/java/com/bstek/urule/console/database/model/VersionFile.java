package com.bstek.urule.console.database.model;

import java.util.Date;

public class VersionFile extends FileInfo {
   private long id;
   private long fileId;
   private long projectId;
   private String digest;
   private String version;
   private String note;
   private String createUser;
   private Date createDate;

   public long getId() {
      return this.id;
   }

   public void setId(long id) {
      this.id = id;
   }

   public long getFileId() {
      return this.fileId;
   }

   public void setFileId(long fileId) {
      this.fileId = fileId;
   }

   public long getProjectId() {
      return this.projectId;
   }

   public void setProjectId(long projectId) {
      this.projectId = projectId;
   }

   public String getDigest() {
      return this.digest;
   }

   public void setDigest(String digest) {
      this.digest = digest;
   }

   public String getVersion() {
      return this.version;
   }

   public void setVersion(String version) {
      this.version = version;
   }

   public String getNote() {
      return this.note;
   }

   public void setNote(String note) {
      this.note = note;
   }

   public String getCreateUser() {
      return this.createUser;
   }

   public void setCreateUser(String createUser) {
      this.createUser = createUser;
   }

   public Date getCreateDate() {
      return this.createDate;
   }

   public void setCreateDate(Date createDate) {
      this.createDate = createDate;
   }
}
