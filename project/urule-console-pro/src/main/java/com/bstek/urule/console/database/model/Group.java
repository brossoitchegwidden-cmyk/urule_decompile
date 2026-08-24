package com.bstek.urule.console.database.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.Date;

@JsonIgnoreProperties(
   ignoreUnknown = true
)
public class Group implements Serializable {
   private static final long serialVersionUID = 311678914470393316L;
   private String id;
   private String name;
   private String desc;
   private String createUser;
   private String updateUser;
   private Date createDate;
   private Date updateDate;

   public String getId() {
      return this.id;
   }

   public void setId(String id) {
      this.id = id;
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

   public String getUpdateUser() {
      return this.updateUser;
   }

   public void setUpdateUser(String updateUser) {
      this.updateUser = updateUser;
   }

   public String toString() {
      return "id:" + this.id + ",name:" + this.name;
   }
}
