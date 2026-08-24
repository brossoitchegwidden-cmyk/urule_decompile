package com.bstek.urule.console.database.model;

import java.util.Date;

public class KnowledgePackageDef {
   private long id;
   private long projectId;
   private String name;
   private String desc;
   private String createUser;
   private String updateUser;
   private Date createDate;
   private Date updateDate;
   private boolean enable;

   public long getId() {
      return this.id;
   }

   public void setId(long id) {
      this.id = id;
   }

   public long getProjectId() {
      return this.projectId;
   }

   public void setProjectId(long projectId) {
      this.projectId = projectId;
   }

   public String getName() {
      return this.name;
   }

   public void setName(String name) {
      this.name = name;
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

   public boolean isEnable() {
      return this.enable;
   }

   public void setEnable(boolean enable) {
      this.enable = enable;
   }
}
