package com.bstek.urule.console.database.model;

import java.util.Date;

public class PacketDeployFile {
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

   public long getId() {
      return this.a;
   }

   public void setId(long var1) {
      this.a = var1;
   }

   public long getPacketDeployId() {
      return this.b;
   }

   public void setPacketDeployId(long var1) {
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

   public String getDigest() {
      return this.e;
   }

   public void setDigest(String var1) {
      this.e = var1;
   }

   public String getPath() {
      return this.f;
   }

   public void setPath(String var1) {
      this.f = var1;
   }

   public String getVersion() {
      return this.g;
   }

   public void setVersion(String var1) {
      this.g = var1;
   }

   public String getContent() {
      return this.h;
   }

   public void setContent(String var1) {
      this.h = var1;
   }

   public String getCreateUser() {
      return this.i;
   }

   public void setCreateUser(String var1) {
      this.i = var1;
   }

   public Date getCreateDate() {
      return this.j;
   }

   public void setCreateDate(Date var1) {
      this.j = var1;
   }
}
