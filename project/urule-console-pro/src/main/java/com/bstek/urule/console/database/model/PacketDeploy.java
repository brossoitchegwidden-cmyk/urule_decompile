package com.bstek.urule.console.database.model;

import java.util.Date;
import java.util.List;

public class PacketDeploy {
   private long a;
   private long b;
   private long c;
   private long d;
   private String e;
   private String f;
   private String g;
   private String h;
   private ApplyStatus i;
   private boolean j;
   private String k;
   private Date l;
   private List m;

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

   public long getApplyId() {
      return this.c;
   }

   public void setApplyId(long var1) {
      this.c = var1;
   }

   public long getProjectId() {
      return this.d;
   }

   public void setProjectId(long var1) {
      this.d = var1;
   }

   public String getDesc() {
      return this.e;
   }

   public void setDesc(String var1) {
      this.e = var1;
   }

   public String getDigest() {
      return this.g;
   }

   public void setDigest(String var1) {
      this.g = var1;
   }

   public String getContent() {
      return this.f;
   }

   public void setContent(String var1) {
      this.f = var1;
   }

   public String getVersion() {
      return this.h;
   }

   public void setVersion(String var1) {
      this.h = var1;
   }

   public boolean isEnable() {
      return this.j;
   }

   public void setEnable(boolean var1) {
      this.j = var1;
   }

   public ApplyStatus getStatus() {
      return this.i;
   }

   public void setStatus(ApplyStatus var1) {
      this.i = var1;
   }

   public String getCreateUser() {
      return this.k;
   }

   public void setCreateUser(String var1) {
      this.k = var1;
   }

   public Date getCreateDate() {
      return this.l;
   }

   public void setCreateDate(Date var1) {
      this.l = var1;
   }

   public List getFiles() {
      return this.m;
   }

   public void setFiles(List var1) {
      this.m = var1;
   }
}
