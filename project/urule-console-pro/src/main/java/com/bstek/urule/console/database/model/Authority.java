package com.bstek.urule.console.database.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(
   ignoreUnknown = true
)
public class Authority {
   private long id;
   private long roleId;
   private String roleType;
   private String resourceType;
   private String resourceCode;
   private int auth;

   public long getRoleId() {
      return this.roleId;
   }

   public void setRoleId(long roleId) {
      this.roleId = roleId;
   }

   public String getRoleType() {
      return this.roleType;
   }

   public void setRoleType(String roleType) {
      this.roleType = roleType;
   }

   public String getResourceType() {
      return this.resourceType;
   }

   public void setResourceType(String resourceType) {
      this.resourceType = resourceType;
   }

   public String getResourceCode() {
      return this.resourceCode;
   }

   public void setResourceCode(String resourceCode) {
      this.resourceCode = resourceCode;
   }

   public long getId() {
      return this.id;
   }

   public void setId(long id) {
      this.id = id;
   }

   public int getAuth() {
      return this.auth;
   }

   public void setAuth(int auth) {
      this.auth = auth;
   }
}
