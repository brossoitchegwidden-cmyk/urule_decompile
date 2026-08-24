package com.bstek.urule.console.database.model;

import java.util.Date;

public class PacketApplyDetail {
   private long id;
   private long applyId;
   private long projectId;
   private String desc;
   private String createUser;
   private Date createDate;

   public long getId() {
      return this.id;
   }

   public void setId(long id) {
      this.id = id;
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
