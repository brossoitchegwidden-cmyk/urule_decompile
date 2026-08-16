package com.bstek.urule.console.batch;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BatchResult {
   private Long a;
   private String b;
   private Date c;
   private Date d;
   private String e;
   private int f = 0;
   private int g = 0;
   private BatchStatus h;
   private String i;
   private String j;
   @JsonIgnore
   private List k = new ArrayList();
   private Map l = new HashMap();

   public Long getBatchId() {
      return this.a;
   }

   public void setBatchId(Long var1) {
      this.a = var1;
   }

   public String getBatchName() {
      return this.b;
   }

   public void setBatchName(String var1) {
      this.b = var1;
   }

   public Date getStartTime() {
      return this.c;
   }

   public void setStartTime(Date var1) {
      this.c = var1;
   }

   public String getIp() {
      return this.e;
   }

   public void setIp(String var1) {
      this.e = var1;
   }

   public int getReadCount() {
      return this.f;
   }

   public void setReadCount(int var1) {
      this.f = var1;
   }

   public BatchStatus getStatus() {
      return this.h;
   }

   public void setStatus(BatchStatus var1) {
      this.h = var1;
   }

   public String getMsg() {
      return this.i;
   }

   public void setMsg(String var1) {
      this.i = var1;
   }

   public Date getEndTime() {
      return this.d;
   }

   public void setEndTime(Date var1) {
      this.d = var1;
   }

   public Map getItemResults() {
      return this.l;
   }

   public void setItemResults(Map var1) {
      this.l = var1;
   }

   public String getUserAgent() {
      return this.j;
   }

   public void setUserAgent(String var1) {
      this.j = var1;
   }

   public void setException(Exception var1) {
      this.i = var1.getClass() + ":" + var1.getMessage();
   }

   public int getFilterCount() {
      return this.g;
   }

   public void setFilterCount(int var1) {
      this.g = var1;
   }

   public List getExceptions() {
      return this.k;
   }

   public void setExceptions(List var1) {
      this.k = var1;
   }

   public String toString() {
      String var1 = "id:" + this.a + ",batchName:" + this.b + ",status:" + this.h + ",recordCount:" + this.f;
      if (this.d != null) {
         var1 = var1 + ",time:" + (this.d.getTime() - this.c.getTime()) / 1000L;
      }

      return var1;
   }
}
