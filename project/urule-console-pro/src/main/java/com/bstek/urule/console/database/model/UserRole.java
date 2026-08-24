package com.bstek.urule.console.database.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(
   ignoreUnknown = true
)
public class UserRole {
   private long id;
   private String userId;
   private long roleId;

   public long getId() {
      return this.id;
   }

   public void setId(long id) {
      this.id = id;
   }

   public String getUserId() {
      return this.userId;
   }

   public void setUserId(String userId) {
      this.userId = userId;
   }

   public long getRoleId() {
      return this.roleId;
   }

   public void setRoleId(long roleId) {
      this.roleId = roleId;
   }
}
