package com.bstek.urule.console.database.model;

import java.util.Date;

public class PacketFile {
   private long a;
   private long b;
   private long c;
   private long d;
   private String e;
   private String f;
   private String g;
   private String h;
   private String i;
   private Date j;
   private Date k;

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

   public long getFileId() {
      return this.c;
   }

   public void setFileId(long var1) {
      this.c = var1;
   }

   public long getProjectId() {
      return this.d;
   }

   public void setProjectId(long var1) {
      this.d = var1;
   }

   public String getPath() {
      return this.e;
   }

   public void setPath(String var1) {
      this.e = var1;
   }

   public String getVersion() {
      return this.f;
   }

   public void setVersion(String var1) {
      this.f = var1;
   }

   public String getDesc() {
      return this.g;
   }

   public void setDesc(String var1) {
      this.g = var1;
   }

   public String getCreateUser() {
      return this.h;
   }

   public void setCreateUser(String var1) {
      this.h = var1;
   }

   public String getUpdateUser() {
      return this.i;
   }

   public void setUpdateUser(String var1) {
      this.i = var1;
   }

   public Date getCreateDate() {
      return this.j;
   }

   public void setCreateDate(Date var1) {
      this.j = var1;
   }

   public Date getUpdateDate() {
      return this.k;
   }

   public void setUpdateDate(Date var1) {
      this.k = var1;
   }
}
