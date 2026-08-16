package com.bstek.urule.console.database.model;

import java.util.Date;
import java.util.List;

public class RuleFile extends FileInfo {
   private String a;
   private long b;
   private long c;
   private Date d;
   private String e;
   private String f;
   private String g;
   private String h;
   private boolean i;
   private boolean j;
   private boolean k;
   private List l;

   public String getUpdateUser() {
      return this.e;
   }

   public void setUpdateUser(String var1) {
      this.e = var1;
   }

   public String getLatestVersion() {
      return this.g;
   }

   public void setLatestVersion(String var1) {
      this.g = var1;
   }

   public String getLockedUser() {
      return this.f;
   }

   public void setLockedUser(String var1) {
      this.f = var1;
   }

   public long getParentId() {
      return this.b;
   }

   public void setParentId(long var1) {
      this.b = var1;
   }

   public Date getModifyDate() {
      return this.d;
   }

   public void setModifyDate(Date var1) {
      this.d = var1;
   }

   public String getType() {
      return this.a;
   }

   public void setType(String var1) {
      this.a = var1;
   }

   public long getProjectId() {
      return this.c;
   }

   public void setProjectId(long var1) {
      this.c = var1;
   }

   public boolean isDirectory() {
      return this.i;
   }

   public void setDirectory(boolean var1) {
      this.i = var1;
   }

   public boolean isDeleted() {
      return this.j;
   }

   public void setDeleted(boolean var1) {
      this.j = var1;
   }

   public List getChildren() {
      return this.l;
   }

   public void setChildren(List var1) {
      this.l = var1;
   }

   public String getDigest() {
      return this.h;
   }

   public void setDigest(String var1) {
      this.h = var1;
   }

   public boolean isVirtual() {
      return this.k;
   }

   public void setVirtual(boolean var1) {
      this.k = var1;
   }

   public String toString() {
      return "id:" + this.getId() + ",name:" + this.getName();
   }
}
