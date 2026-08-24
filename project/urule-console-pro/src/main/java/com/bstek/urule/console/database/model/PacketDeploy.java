package com.bstek.urule.console.database.model;

import java.util.Date;
import java.util.List;

public class PacketDeploy {
   private long id;
   private long packetId;
   private long applyId;
   private long projectId;
   private String desc;
   private String content;
   private String digest;
   private String version;
   private ApplyStatus status;
   private boolean enable;
   private String createUser;
   private Date createDate;
   private List files;

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

   public long getApplyId() {
      return this.applyId;
   }

   public void setApplyId(long applyId) {
      this.applyId = applyId;
   }

   public long getProjectId() {
      return this.projectId;
   }

   public void setProjectId(long projectId) {
      this.projectId = projectId;
   }

   public String getDesc() {
      return this.desc;
   }

   public void setDesc(String desc) {
      this.desc = desc;
   }

   public String getDigest() {
      return this.digest;
   }

   public void setDigest(String digest) {
      this.digest = digest;
   }

   public String getContent() {
      return this.content;
   }

   public void setContent(String content) {
      this.content = content;
   }

   public String getVersion() {
      return this.version;
   }

   public void setVersion(String version) {
      this.version = version;
   }

   public boolean isEnable() {
      return this.enable;
   }

   public void setEnable(boolean enable) {
      this.enable = enable;
   }

   public ApplyStatus getStatus() {
      return this.status;
   }

   public void setStatus(ApplyStatus status) {
      this.status = status;
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

   public List getFiles() {
      return this.files;
   }

   public void setFiles(List files) {
      this.files = files;
   }
}
