package com.bstek.urule.console.database.model;

import java.util.Date;
import java.util.List;

public class PacketApply {
   private long id;
   private long packetId;
   private long deployedPacketId;
   private long projectId;
   private String title;
   private String desc;
   private ApplyType type;
   private ApplyStatus status;
   private String approver;
   private String createUser;
   private Date createDate;
   private Date updateDate;
   private List details;

   public PacketApply() {
      this.status = ApplyStatus.pending;
   }

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

   public long getDeployedPacketId() {
      return this.deployedPacketId;
   }

   public void setDeployedPacketId(long deployedPacketId) {
      this.deployedPacketId = deployedPacketId;
   }

   public long getProjectId() {
      return this.projectId;
   }

   public void setProjectId(long projectId) {
      this.projectId = projectId;
   }

   public String getTitle() {
      return this.title;
   }

   public void setTitle(String title) {
      this.title = title;
   }

   public String getDesc() {
      return this.desc;
   }

   public void setDesc(String desc) {
      this.desc = desc;
   }

   public ApplyType getType() {
      return this.type;
   }

   public void setType(ApplyType type) {
      this.type = type;
   }

   public ApplyStatus getStatus() {
      return this.status;
   }

   public void setStatus(ApplyStatus status) {
      this.status = status;
   }

   public String getApprover() {
      return this.approver;
   }

   public void setApprover(String approver) {
      this.approver = approver;
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

   public Date getUpdateDate() {
      return this.updateDate;
   }

   public void setUpdateDate(Date updateDate) {
      this.updateDate = updateDate;
   }

   public List getDetails() {
      return this.details;
   }

   public void setDetails(List details) {
      this.details = details;
   }
}
