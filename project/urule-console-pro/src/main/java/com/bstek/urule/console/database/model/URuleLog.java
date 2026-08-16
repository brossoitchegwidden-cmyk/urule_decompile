package com.bstek.urule.console.database.model;

import java.io.Serializable;
import java.util.Date;

public class URuleLog implements Serializable {
   private static final long a = 1L;
   private Long b;
   private String c;
   private String d;
   private Date e;

   public Long getId() {
      return this.b;
   }

   public void setId(Long var1) {
      this.b = var1;
   }

   public String getUserId() {
      return this.c;
   }

   public void setUserId(String var1) {
      this.c = var1;
   }

   public String getUsername() {
      return this.d;
   }

   public void setUsername(String var1) {
      this.d = var1;
   }

   public Date getCreateDate() {
      return this.e;
   }

   public void setCreateDate(Date var1) {
      this.e = var1;
   }

   public static long getSerialversionuid() {
      return 1L;
   }
}
