package com.bstek.urule.console.database.model;

import java.util.Date;

public class PacketFile {
   private long id;
   private long packetId;
   private long fileId;
   private long projectId;
   private String path;
   private String version;
   private String desc;
   private String createUser;
   private String updateUser;
   private Date createDate;
   private Date updateDate;

   public long getId() {
      return this.id;
   }

   public void setId(long id) {
      this.id = id;
   }

   public long getPacketId() {
      return this.packetId;
   }

   public void setPacketId(long packetId) {
      this.packetId = packetId;
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

   public String getPath() {
      return this.path;
   }

   public void setPath(String path) {
      this.path = path;
   }

   public String getVersion() {
      return this.version;
   }

   public void setVersion(String version) {
      this.version = version;
   }

   public String getDesc() {
      return this.desc;
   }

   public void setDesc(String desc) {
      this.desc = desc;
   }

   public String getCreateUser() {
      return this.createUser;
   }

   public void setCreateUser(String createUser) {
      this.createUser = createUser;
   }

   public String getUpdateUser() {
      return this.updateUser;
   }

   public void setUpdateUser(String updateUser) {
      this.updateUser = updateUser;
   }

   public Date getCreateDate() {
      return this.createDate;
   }

   public void setCreateDate(Date createDate) {
      this.createDate = createDate;
   }

   public Date getUpdateDate() {
      return this.updateDate;
   }

   public void setUpdateDate(Date updateDate) {
      this.updateDate = updateDate;
   }
}
