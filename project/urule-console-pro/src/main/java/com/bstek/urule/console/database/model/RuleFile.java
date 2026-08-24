package com.bstek.urule.console.database.model;

import java.util.Date;
import java.util.List;

public class RuleFile extends FileInfo {
   private String type;
   private long parentId;
   private long projectId;
   private Date modifyDate;
   private String updateUser;
   private String lockedUser;
   private String latestVersion;
   private String digest;
   private boolean directory;
   private boolean deleted;
   private boolean virtual;
   private List children;

   public String getUpdateUser() {
      return this.updateUser;
   }

   public void setUpdateUser(String updateUser) {
      this.updateUser = updateUser;
   }

   public String getLatestVersion() {
      return this.latestVersion;
   }

   public void setLatestVersion(String latestVersion) {
      this.latestVersion = latestVersion;
   }

   public String getLockedUser() {
      return this.lockedUser;
   }

   public void setLockedUser(String lockedUser) {
      this.lockedUser = lockedUser;
   }

   public long getParentId() {
      return this.parentId;
   }

   public void setParentId(long parentId) {
      this.parentId = parentId;
   }

   public Date getModifyDate() {
      return this.modifyDate;
   }

   public void setModifyDate(Date modifyDate) {
      this.modifyDate = modifyDate;
   }

   public String getType() {
      return this.type;
   }

   public void setType(String type) {
      this.type = type;
   }

   public long getProjectId() {
      return this.projectId;
   }

   public void setProjectId(long projectId) {
      this.projectId = projectId;
   }

   public boolean isDirectory() {
      return this.directory;
   }

   public void setDirectory(boolean directory) {
      this.directory = directory;
   }

   public boolean isDeleted() {
      return this.deleted;
   }

   public void setDeleted(boolean deleted) {
      this.deleted = deleted;
   }

   public List getChildren() {
      return this.children;
   }

   public void setChildren(List children) {
      this.children = children;
   }

   public String getDigest() {
      return this.digest;
   }

   public void setDigest(String digest) {
      this.digest = digest;
   }

   public boolean isVirtual() {
      return this.virtual;
   }

   public void setVirtual(boolean virtual) {
      this.virtual = virtual;
   }

   public String toString() {
      return "id:" + this.getId() + ",name:" + this.getName();
   }
}
