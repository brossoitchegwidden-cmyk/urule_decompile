package com.bstek.urule.console.database.model;

import java.util.Date;
import java.util.List;

public class PacketApply {
   private long a;
   private long b;
   private long c;
   private long d;
   private String e;
   private String f;
   private ApplyType g;
   private ApplyStatus h;
   private String i;
   private String j;
   private Date k;
   private Date l;
   private List m;

   public PacketApply() {
      this.h = ApplyStatus.pending;
   }

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

   public long getDeployedPacketId() {
      return this.c;
   }

   public void setDeployedPacketId(long var1) {
      this.c = var1;
   }

   public long getProjectId() {
      return this.d;
   }

   public void setProjectId(long var1) {
      this.d = var1;
   }

   public String getTitle() {
      return this.e;
   }

   public void setTitle(String var1) {
      this.e = var1;
   }

   public String getDesc() {
      return this.f;
   }

   public void setDesc(String var1) {
      this.f = var1;
   }

   public ApplyType getType() {
      return this.g;
   }

   public void setType(ApplyType var1) {
      this.g = var1;
   }

   public ApplyStatus getStatus() {
      return this.h;
   }

   public void setStatus(ApplyStatus var1) {
      this.h = var1;
   }

   public String getApprover() {
      return this.i;
   }

   public void setApprover(String var1) {
      this.i = var1;
   }

   public String getCreateUser() {
      return this.j;
   }

   public void setCreateUser(String var1) {
      this.j = var1;
   }

   public Date getCreateDate() {
      return this.k;
   }

   public void setCreateDate(Date var1) {
      this.k = var1;
   }

   public Date getUpdateDate() {
      return this.l;
   }

   public void setUpdateDate(Date var1) {
      this.l = var1;
   }

   public List getDetails() {
      return this.m;
   }

   public void setDetails(List var1) {
      this.m = var1;
   }
}
