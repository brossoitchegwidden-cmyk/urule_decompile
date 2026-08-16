package com.bstek.urule.console.database.model;

import java.util.Date;

public class PacketPackage {
   private long a;
   private long b;
   private long c;
   private String d;
   private String e;
   private Date f;
   private Date g;
   private String h;
   private String i;

   public long getId() {
      return this.a;
   }

   public void setId(long var1) {
      this.a = var1;
   }

   public long getPacketId() {
      return this.b;
   }

   public void setPacketId(long var1) {
      this.b = var1;
   }

   public long getProjectId() {
      return this.c;
   }

   public void setProjectId(long var1) {
      this.c = var1;
   }

   public String getCreateUser() {
      return this.d;
   }

   public void setCreateUser(String var1) {
      this.d = var1;
   }

   public String getUpdateUser() {
      return this.e;
   }

   public void setUpdateUser(String var1) {
      this.e = var1;
   }

   public Date getCreateDate() {
      return this.f;
   }

   public void setCreateDate(Date var1) {
      this.f = var1;
   }

   public Date getUpdateDate() {
      return this.g;
   }

   public void setUpdateDate(Date var1) {
      this.g = var1;
   }

   public String getDesc() {
      return this.h;
   }

   public void setDesc(String var1) {
      this.h = var1;
   }

   public String getContent() {
      return this.i;
   }

   public void setContent(String var1) {
      this.i = var1;
   }
}
