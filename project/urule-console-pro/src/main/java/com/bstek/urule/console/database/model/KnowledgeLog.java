package com.bstek.urule.console.database.model;

import java.util.Date;

public class KnowledgeLog extends URuleLog {
   private static final long a = 1L;
   private Long b;
   private String c;
   private String d;
   private Long e;
   private String f;
   private String g;
   private String h;
   private String i;
   private String j;
   private Long k;
   private String l;
   private String m;
   private String n;
   private Date o;
   private Date p;

   public String getUserAgent() {
      return this.m;
   }

   public void setUserAgent(String var1) {
      this.m = var1;
   }

   public String getIp() {
      return this.n;
   }

   public void setIp(String var1) {
      this.n = var1;
   }

   public String getVersion() {
      return this.d;
   }

   public void setVersion(String var1) {
      this.d = var1;
   }

   public String getKnowledgeName() {
      return this.c;
   }

   public void setKnowledgeId(Long var1) {
      this.b = var1;
   }

   public Long getKnowledgeId() {
      return this.b;
   }

   public void setKnowledgeName(String var1) {
      this.c = var1;
   }

   public Long getTime() {
      return this.e;
   }

   public void setTime(Long var1) {
      this.e = var1;
   }

   public String getInParams() {
      return this.f;
   }

   public void setInParams(String var1) {
      this.f = var1;
   }

   public String getOutParams() {
      return this.g;
   }

   public void setOutParams(String var1) {
      this.g = var1;
   }

   public String getLogs() {
      return this.h;
   }

   public void setLogs(String var1) {
      this.h = var1;
   }

   public Long getProjectId() {
      return this.k;
   }

   public void setProjectId(Long var1) {
      this.k = var1;
   }

   public String getGroupId() {
      return this.i;
   }

   public void setGroupId(String var1) {
      this.i = var1;
   }

   public Date getStartTime() {
      return this.o;
   }

   public void setStartTime(Date var1) {
      this.o = var1;
   }

   public Date getEndTime() {
      return this.p;
   }

   public void setEndTime(Date var1) {
      this.p = var1;
   }

   public String getGroupName() {
      return this.j;
   }

   public void setGroupName(String var1) {
      this.j = var1;
   }

   public String getProjectName() {
      return this.l;
   }

   public void setProjectName(String var1) {
      this.l = var1;
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append(this.getClass().getSimpleName());
      var1.append(" [");
      var1.append("Hash = ").append(this.hashCode());
      var1.append(", id=").append(this.getId());
      var1.append(", username=").append(this.getUsername());
      var1.append(", ip=").append(this.n);
      var1.append(", userAgent=").append(this.m);
      var1.append(", knowledge=").append(this.c);
      var1.append(", time=").append(this.e);
      var1.append(", createDate=").append(this.getCreateDate());
      var1.append(", projectId=").append(this.k);
      var1.append(", serialVersionUID=").append(1L);
      var1.append("]");
      return var1.toString();
   }
}
