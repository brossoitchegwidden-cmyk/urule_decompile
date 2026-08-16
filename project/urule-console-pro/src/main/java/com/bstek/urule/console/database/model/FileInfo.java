package com.bstek.urule.console.database.model;

import java.util.Date;

public abstract class FileInfo {
   private long a;
   private String b;
   private String c;
   private boolean d;
   private String e;
   private Date f;
   private String g;

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

   public String getPath() {
      return this.c;
   }

   public void setPath(String var1) {
      this.c = var1;
   }

   public String getContent() {
      return this.e;
   }

   public void setContent(String var1) {
      this.e = var1;
   }

   public Date getCreateDate() {
      return this.f;
   }

   public void setCreateDate(Date var1) {
      this.f = var1;
   }

   public String getCreateUser() {
      return this.g;
   }

   public void setCreateUser(String var1) {
      this.g = var1;
   }

   public boolean isFileSet() {
      return this.d;
   }

   public void setFileSet(boolean var1) {
      this.d = var1;
   }
}
