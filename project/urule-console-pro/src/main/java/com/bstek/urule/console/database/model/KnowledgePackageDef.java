package com.bstek.urule.console.database.model;

import java.util.Date;

public class KnowledgePackageDef {
   private long a;
   private long b;
   private String c;
   private String d;
   private String e;
   private String f;
   private Date g;
   private Date h;
   private boolean i;

   public long getId() {
      return this.a;
   }

   public void setId(long var1) {
      this.a = var1;
   }

   public long getProjectId() {
      return this.b;
   }

   public void setProjectId(long var1) {
      this.b = var1;
   }

   public String getName() {
      return this.c;
   }

   public void setName(String var1) {
      this.c = var1;
   }

   public String getDesc() {
      return this.d;
   }

   public void setDesc(String var1) {
      this.d = var1;
   }

   public String getCreateUser() {
      return this.e;
   }

   public void setCreateUser(String var1) {
      this.e = var1;
   }

   public String getUpdateUser() {
      return this.f;
   }

   public void setUpdateUser(String var1) {
      this.f = var1;
   }

   public Date getCreateDate() {
      return this.g;
   }

   public void setCreateDate(Date var1) {
      this.g = var1;
   }

   public Date getUpdateDate() {
      return this.h;
   }

   public void setUpdateDate(Date var1) {
      this.h = var1;
   }

   public boolean isEnable() {
      return this.i;
   }

   public void setEnable(boolean var1) {
      this.i = var1;
   }
}
