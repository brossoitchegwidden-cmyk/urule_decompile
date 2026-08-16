package com.bstek.urule.console.database.model;

import java.util.Date;

public class VersionFile extends FileInfo {
   private long a;
   private long b;
   private long c;
   private String d;
   private String e;
   private String f;
   private String g;
   private Date h;

   public long getId() {
      return this.a;
   }

   public void setId(long var1) {
      this.a = var1;
   }

   public long getFileId() {
      return this.b;
   }

   public void setFileId(long var1) {
      this.b = var1;
   }

   public long getProjectId() {
      return this.c;
   }

   public void setProjectId(long var1) {
      this.c = var1;
   }

   public String getDigest() {
      return this.d;
   }

   public void setDigest(String var1) {
      this.d = var1;
   }

   public String getVersion() {
      return this.e;
   }

   public void setVersion(String var1) {
      this.e = var1;
   }

   public String getNote() {
      return this.f;
   }

   public void setNote(String var1) {
      this.f = var1;
   }

   public String getCreateUser() {
      return this.g;
   }

   public void setCreateUser(String var1) {
      this.g = var1;
   }

   public Date getCreateDate() {
      return this.h;
   }

   public void setCreateDate(Date var1) {
      this.h = var1;
   }
}
