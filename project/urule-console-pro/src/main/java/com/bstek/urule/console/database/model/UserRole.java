package com.bstek.urule.console.database.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(
   ignoreUnknown = true
)
public class UserRole {
   private long a;
   private String b;
   private long c;

   public long getId() {
      return this.a;
   }

   public void setId(long var1) {
      this.a = var1;
   }

   public String getUserId() {
      return this.b;
   }

   public void setUserId(String var1) {
      this.b = var1;
   }

   public long getRoleId() {
      return this.c;
   }

   public void setRoleId(long var1) {
      this.c = var1;
   }
}
