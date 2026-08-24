package com.bstek.urule.console.database.model;

import java.util.Date;

public class PacketDeployFile {
   private long id;
   private long packetDeployId;
   private long fileId;
   private long projectId;
   private String digest;
   private String path;
   private String version;
   private String content;
   private String createUser;
   private Date createDate;

   public long getId() {
      return this.id;
   }

   public void setId(long id) {
      this.id = id;
   }

   public long getPacketDeployId() {
      return this.packetDeployId;
   }

   public void setPacketDeployId(long packetDeployId) {
      this.packetDeployId = packetDeployId;
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

   public String getContent() {
      return this.content;
   }

   public void setContent(String content) {
      this.content = content;
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
