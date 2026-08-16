package com.bstek.urule.console.database.model.batch;

import com.bstek.urule.console.database.model.URuleLog;

public class BatchSkipLog extends URuleLog {
   private static final long a = -9032084549084743906L;
   private Long b;
   private Long c;
   private String d;
   private String e;
   private String f;
   private String g;
   private String h;
   private Long i;

   public String getType() {
      return this.d;
   }

   public void setType(String var1) {
      this.d = var1;
   }

   public String getMsg() {
      return this.e;
   }

   public void setMsg(String var1) {
      this.e = var1;
   }

   public String getDetail() {
      return this.f;
   }

   public void setDetail(String var1) {
      this.f = var1;
   }

   public String getData() {
      return this.g;
   }

   public void setData(String var1) {
      this.g = var1;
   }

   public String getGroupId() {
      return this.h;
   }

   public void setGroupId(String var1) {
      this.h = var1;
   }

   public Long getProjectId() {
      return this.i;
   }

   public void setProjectId(Long var1) {
      this.i = var1;
   }

   public Long getBatchId() {
      return this.b;
   }

   public void setBatchId(Long var1) {
      this.b = var1;
   }

   public Long getLogId() {
      return this.c;
   }

   public void setLogId(Long var1) {
      this.c = var1;
   }
}
