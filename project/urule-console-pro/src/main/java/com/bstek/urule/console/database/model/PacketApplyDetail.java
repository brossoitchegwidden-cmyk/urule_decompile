package com.bstek.urule.console.database.model;

import java.util.Date;

public class PacketApplyDetail {
   private long a;
   private long b;
   private long c;
   private String d;
   private String e;
   private Date f;

   public long getId() {
      return this.a;
   }

   public void setId(long var1) {
      this.a = var1;
   }

   public long getApplyId() {
      return this.b;
   }

   public void setApplyId(long var1) {
      this.b = var1;
   }

   public long getProjectId() {
      return this.c;
   }

   public void setProjectId(long var1) {
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

   public Date getCreateDate() {
      return this.f;
   }

   public void setCreateDate(Date var1) {
      this.f = var1;
   }
}
