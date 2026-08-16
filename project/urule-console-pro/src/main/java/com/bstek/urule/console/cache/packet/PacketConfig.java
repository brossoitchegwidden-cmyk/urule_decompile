package com.bstek.urule.console.cache.packet;

import java.util.List;

public class PacketConfig {
   private long a;
   private long b;
   private boolean c;
   private boolean d;
   private boolean e;
   private String f;
   private String g;
   private String h;
   private List i;
   private List j;
   private List k;
   private List l;
   private boolean m;

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

   public boolean isEnable() {
      return this.c;
   }

   public void setEnable(boolean var1) {
      this.c = var1;
   }

   public boolean isRestEnable() {
      return this.d;
   }

   public void setRestEnable(boolean var1) {
      this.d = var1;
   }

   public String getCode() {
      return this.f;
   }

   public void setCode(String var1) {
      this.f = var1;
   }

   public boolean isRestSecurityEnable() {
      return this.e;
   }

   public void setRestSecurityEnable(boolean var1) {
      this.e = var1;
   }

   public String getRestSecurityUser() {
      return this.g;
   }

   public void setRestSecurityUser(String var1) {
      this.g = var1;
   }

   public String getRestSecurityPassword() {
      return this.h;
   }

   public void setRestSecurityPassword(String var1) {
      this.h = var1;
   }

   public List getRestInput() {
      return this.i;
   }

   public void setRestInput(List var1) {
      this.i = var1;
   }

   public List getRestOutput() {
      return this.j;
   }

   public void setRestOutput(List var1) {
      this.j = var1;
   }

   public List getAuditInput() {
      return this.k;
   }

   public void setAuditInput(List var1) {
      this.k = var1;
   }

   public List getAuditOutput() {
      return this.l;
   }

   public void setAuditOutput(List var1) {
      this.l = var1;
   }

   public boolean isAuditEnable() {
      return this.m;
   }

   public void setAuditEnable(boolean var1) {
      this.m = var1;
   }
}
