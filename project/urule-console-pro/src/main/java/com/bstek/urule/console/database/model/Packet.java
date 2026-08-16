package com.bstek.urule.console.database.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Packet {
   private long a;
   private long b;
   private String c;
   private String d;
   private PacketType e;
   private String f;
   private String g;
   private String h;
   private boolean i;
   private boolean j;
   private boolean k;
   private String l;
   private String m;
   private boolean n;
   private boolean o;
   private String p;
   private String q;
   private String r;
   private String s;
   private String t;
   private String u;
   private Date v;
   private Date w;
   private long x;
   private PacketPackage y;
   private List z;

   public Packet() {
      this.e = PacketType.file;
      this.i = true;
      this.z = new ArrayList();
   }

   public long getId() {
      return this.a;
   }

   public void setId(long var1) {
      this.a = var1;
   }

   public long getProjectId() {
      return this.b;
   }

   public void setProjectId(long var1) {
      this.b = var1;
   }

   public String getCode() {
      return this.c;
   }

   public void setCode(String var1) {
      this.c = var1;
   }

   public String getName() {
      return this.d;
   }

   public void setName(String var1) {
      this.d = var1;
   }

   public PacketType getType() {
      return this.e;
   }

   public void setType(PacketType var1) {
      this.e = var1;
   }

   public String getDesc() {
      return this.f;
   }

   public void setDesc(String var1) {
      this.f = var1;
   }

   public String getInputData() {
      return this.g;
   }

   public void setInputData(String var1) {
      this.g = var1;
   }

   public String getOutputData() {
      return this.h;
   }

   public void setOutputData(String var1) {
      this.h = var1;
   }

   public boolean isEnable() {
      return this.j;
   }

   public void setEnable(boolean var1) {
      this.j = var1;
   }

   public boolean isDeleteEnable() {
      return this.i;
   }

   public void setDeleteEnable(boolean var1) {
      this.i = var1;
   }

   public boolean isAuditEnable() {
      return this.k;
   }

   public void setAuditEnable(boolean var1) {
      this.k = var1;
   }

   public boolean isRestEnable() {
      return this.n;
   }

   public void setRestEnable(boolean var1) {
      this.n = var1;
   }

   public boolean isRestSecurityEnable() {
      return this.o;
   }

   public void setRestSecurityEnable(boolean var1) {
      this.o = var1;
   }

   public String getRestSecurityUser() {
      return this.p;
   }

   public void setRestSecurityUser(String var1) {
      this.p = var1;
   }

   public String getRestSecurityPassword() {
      return this.q;
   }

   public void setRestSecurityPassword(String var1) {
      this.q = var1;
   }

   public String getRestInput() {
      return this.r;
   }

   public void setRestInput(String var1) {
      this.r = var1;
   }

   public String getRestOutput() {
      return this.s;
   }

   public void setRestOutput(String var1) {
      this.s = var1;
   }

   public String getAuditInput() {
      return this.l;
   }

   public void setAuditInput(String var1) {
      this.l = var1;
   }

   public String getAuditOutput() {
      return this.m;
   }

   public void setAuditOutput(String var1) {
      this.m = var1;
   }

   public String getCreateUser() {
      return this.t;
   }

   public void setCreateUser(String var1) {
      this.t = var1;
   }

   public String getUpdateUser() {
      return this.u;
   }

   public void setUpdateUser(String var1) {
      this.u = var1;
   }

   public Date getCreateDate() {
      return this.v;
   }

   public void setCreateDate(Date var1) {
      this.v = var1;
   }

   public Date getUpdateDate() {
      return this.w;
   }

   public void setUpdateDate(Date var1) {
      this.w = var1;
   }

   public long getDeployedCount() {
      return this.x;
   }

   public void setDeployedCount(long var1) {
      this.x = var1;
   }

   public PacketPackage getPacketPackage() {
      return this.y;
   }

   public void setPacketPackage(PacketPackage var1) {
      this.y = var1;
   }

   public List getFiles() {
      return this.z;
   }

   public void setFiles(List var1) {
      this.z = var1;
   }
}
