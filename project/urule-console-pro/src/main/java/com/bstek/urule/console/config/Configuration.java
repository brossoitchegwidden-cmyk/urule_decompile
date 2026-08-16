package com.bstek.urule.console.config;

import java.sql.Timestamp;
import java.util.Date;

public class Configuration {
   private Long a;
   private String b;
   private String c;
   private String d;
   private String e;
   private Timestamp f;
   private Timestamp g;

   public Long getId() {
      return this.a;
   }

   public void setId(Long var1) {
      this.a = var1;
   }

   public String getKey() {
      return this.b;
   }

   public void setKey(String var1) {
      this.b = var1;
   }

   public String getValue() {
      return this.c;
   }

   public void setValue(String var1) {
      this.c = var1;
   }

   public String getType() {
      return this.e;
   }

   public void setType(String var1) {
      this.e = var1;
   }

   public String getLabel() {
      return this.d;
   }

   public void setLabel(String var1) {
      this.d = var1;
   }

   public Date getCreateDate() {
      return this.f;
   }

   public void setCreateDate(Timestamp var1) {
      this.f = var1;
   }

   public Date getUpdateDate() {
      return this.g;
   }

   public void setUpdateDate(Timestamp var1) {
      this.g = var1;
   }
}
