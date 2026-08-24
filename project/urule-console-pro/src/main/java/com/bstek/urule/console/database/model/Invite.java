package com.bstek.urule.console.database.model;

import java.util.Date;

public class Invite {
   private long id;
   private String groupId;
   private String type;
   private String secretKey;
   private Date expirDate;
   private String createUser;
   private Date createDate;

   public long getId() {
      return this.id;
   }

   public void setId(long id) {
      this.id = id;
   }

   public String getGroupId() {
      return this.groupId;
   }

   public void setGroupId(String groupId) {
      this.groupId = groupId;
   }

   public String getType() {
      return this.type;
   }

   public void setType(String type) {
      this.type = type;
   }

   public String getSecretKey() {
      return this.secretKey;
   }

   public void setSecretKey(String secretKey) {
      this.secretKey = secretKey;
   }

   public Date getExpirDate() {
      return this.expirDate;
   }

   public void setExpirDate(Date expirDate) {
      this.expirDate = expirDate;
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

   public String toString() {
      return "groupId:" + this.groupId + ",secretKey:" + this.secretKey;
   }
}
