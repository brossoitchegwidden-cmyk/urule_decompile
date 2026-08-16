package com.bstek.urule.console.database.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(
   ignoreUnknown = true
)
public class Authority {
   private long a;
   private long b;
   private String c;
   private String d;
   private String e;
   private int f;

   public long getRoleId() {
      return this.b;
   }

   public void setRoleId(long var1) {
      this.b = var1;
   }

   public String getRoleType() {
      return this.c;
   }

   public void setRoleType(String var1) {
      this.c = var1;
   }

   public String getResourceType() {
      return this.d;
   }

   public void setResourceType(String var1) {
      this.d = var1;
   }

   public String getResourceCode() {
      return this.e;
   }

   public void setResourceCode(String var1) {
      this.e = var1;
   }

   public long getId() {
      return this.a;
   }

   public void setId(long var1) {
      this.a = var1;
   }

   public int getAuth() {
      return this.f;
   }

   public void setAuth(int var1) {
      this.f = var1;
   }
}
