package com.bstek.urule.console.database.model;

import java.util.Date;

public class UrlConfig {
   private long a;
   private String b;
   private String c;
   private UrlType d;
   private String e;
   private String f;
   private String g;
   private Date h;
   private Date i;

   public long getId() {
      return this.a;
   }

   public void setId(long var1) {
      this.a = var1;
   }

   public String getName() {
      return this.b;
   }

   public void setName(String var1) {
      this.b = var1;
   }

   public String getUrl() {
      return this.c;
   }

   public void setUrl(String var1) {
      this.c = var1;
   }

   public UrlType getType() {
      return this.d;
   }

   public void setType(UrlType var1) {
      this.d = var1;
   }

   public String getGroupId() {
      return this.e;
   }

   public void setGroupId(String var1) {
      this.e = var1;
   }

   public String getCreateUser() {
      return this.f;
   }

   public void setCreateUser(String var1) {
      this.f = var1;
   }

   public String getUpdateUser() {
      return this.g;
   }

   public void setUpdateUser(String var1) {
      this.g = var1;
   }

   public Date getCreateDate() {
      return this.h;
   }

   public void setCreateDate(Date var1) {
      this.h = var1;
   }

   public Date getUpdateDate() {
      return this.i;
   }

   public void setUpdateDate(Date var1) {
      this.i = var1;
   }
}
