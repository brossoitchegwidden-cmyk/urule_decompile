package com.bstek.urule.console.database.model.batch;

import com.bstek.urule.console.batch.BatchStatus;
import com.bstek.urule.console.database.model.URuleLog;
import java.util.Date;

public class BatchLog extends URuleLog {
   private static final long a = -7678531060299019128L;
   private String b;
   private String c;
   private Long d;
   private String e;
   private BatchStatus f;
   private int g = 0;
   private int h = 0;
   private String i;
   private String j;
   private Long k;
   private String l;
   private Date m;
   private Date n;
   private long o;
   private String p;
   private String q;
   private String r;
   private String s;
   private Long t;
   private String u;

   public String getDetail() {
      return this.q;
   }

   public void setDetail(String var1) {
      this.q = var1;
   }

   public String getIp() {
      return this.b;
   }

   public void setIp(String var1) {
      this.b = var1;
   }

   public Long getBatchId() {
      return this.d;
   }

   public void setBatchId(Long var1) {
      this.d = var1;
   }

   public String getBatchName() {
      return this.e;
   }

   public void setBatchName(String var1) {
      this.e = var1;
   }

   public String getUserAgent() {
      return this.c;
   }

   public void setUserAgent(String var1) {
      this.c = var1;
   }

   public BatchStatus getStatus() {
      return this.f;
   }

   public void setStatus(BatchStatus var1) {
      this.f = var1;
   }

   public int getReadCount() {
      return this.g;
   }

   public void setReadCount(int var1) {
      this.g = var1;
   }

   public int getFilterCount() {
      return this.h;
   }

   public void setFilterCount(int var1) {
      this.h = var1;
   }

   public String getItemData() {
      return this.i;
   }

   public void setItemData(String var1) {
      this.i = var1;
   }

   public String getInParams() {
      return this.j;
   }

   public void setInParams(String var1) {
      this.j = var1;
   }

   public Date getStartTime() {
      return this.m;
   }

   public void setStartTime(Date var1) {
      this.m = var1;
   }

   public Date getEndTime() {
      return this.n;
   }

   public void setEndTime(Date var1) {
      this.n = var1;
   }

   public long getTime() {
      return this.o;
   }

   public void setTime(long var1) {
      this.o = var1;
   }

   public String getMsg() {
      return this.p;
   }

   public void setMsg(String var1) {
      this.p = var1;
   }

   public String getGroupId() {
      return this.r;
   }

   public void setGroupId(String var1) {
      this.r = var1;
   }

   public String getGroupName() {
      return this.s;
   }

   public void setGroupName(String var1) {
      this.s = var1;
   }

   public Long getProjectId() {
      return this.t;
   }

   public void setProjectId(Long var1) {
      this.t = var1;
   }

   public String getProjectName() {
      return this.u;
   }

   public void setProjectName(String var1) {
      this.u = var1;
   }

   public Long getPacketId() {
      return this.k;
   }

   public void setPacketId(Long var1) {
      this.k = var1;
   }

   public String getPacketParams() {
      return this.l;
   }

   public void setPacketParams(String var1) {
      this.l = var1;
   }
}
