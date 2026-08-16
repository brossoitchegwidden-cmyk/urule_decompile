package com.bstek.urule.console.database.model;

import java.util.Date;

public class Scenario {
   private long a;
   private long b;
   private long c;
   private String d;
   private String e;
   private byte[] f;
   private String g;
   private String h;
   private String i;
   private String j;
   private String k;
   private Date l;
   private Date m;

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

   public String getName() {
      return this.d;
   }

   public void setName(String var1) {
      this.d = var1;
   }

   public String getDesc() {
      return this.e;
   }

   public void setDesc(String var1) {
      this.e = var1;
   }

   public byte[] getExcelFile() {
      return this.f;
   }

   public void setExcelFile(byte[] var1) {
      this.f = var1;
   }

   public String getExcelFileName() {
      return this.g;
   }

   public void setExcelFileName(String var1) {
      this.g = var1;
   }

   public String getInputData() {
      return this.h;
   }

   public void setInputData(String var1) {
      this.h = var1;
   }

   public String getOutputData() {
      return this.i;
   }

   public void setOutputData(String var1) {
      this.i = var1;
   }

   public String getCreateUser() {
      return this.j;
   }

   public void setCreateUser(String var1) {
      this.j = var1;
   }

   public String getUpdateUser() {
      return this.k;
   }

   public void setUpdateUser(String var1) {
      this.k = var1;
   }

   public Date getCreateDate() {
      return this.l;
   }

   public void setCreateDate(Date var1) {
      this.l = var1;
   }

   public Date getUpdateDate() {
      return this.m;
   }

   public void setUpdateDate(Date var1) {
      this.m = var1;
   }
}
