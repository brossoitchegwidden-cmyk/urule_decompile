package com.bstek.urule.console.database.model;

import java.util.Date;

public class Invite {
   private long a;
   private String b;
   private String c;
   private String d;
   private Date e;
   private String f;
   private Date g;

   public long getId() {
      return this.a;
   }

   public void setId(long var1) {
      this.a = var1;
   }

   public String getGroupId() {
      return this.b;
   }

   public void setGroupId(String var1) {
      this.b = var1;
   }

   public String getType() {
      return this.c;
   }

   public void setType(String var1) {
      this.c = var1;
   }

   public String getSecretKey() {
      return this.d;
   }

   public void setSecretKey(String var1) {
      this.d = var1;
   }

   public Date getExpirDate() {
      return this.e;
   }

   public void setExpirDate(Date var1) {
      this.e = var1;
   }

   public String getCreateUser() {
      return this.f;
   }

   public void setCreateUser(String var1) {
      this.f = var1;
   }

   public Date getCreateDate() {
      return this.g;
   }

   public void setCreateDate(Date var1) {
      this.g = var1;
   }

   public String toString() {
      return "groupId:" + this.b + ",secretKey:" + this.d;
   }
}
