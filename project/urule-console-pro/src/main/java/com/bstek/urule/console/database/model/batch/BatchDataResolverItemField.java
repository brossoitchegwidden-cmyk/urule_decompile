package com.bstek.urule.console.database.model.batch;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.Date;

@JsonIgnoreProperties(
   ignoreUnknown = true
)
public class BatchDataResolverItemField {
   private Long a;
   private Long b;
   private Long c;
   private Long d;
   private Long e;
   private String f;
   private String g;
   private boolean h;
   private String i;
   private String j;
   private String k;
   private Date l;
   private Date m;
   private Object n;

   public Long getId() {
      return this.a;
   }

   public void setId(Long var1) {
      this.a = var1;
   }

   public Long getBatchId() {
      return this.b;
   }

   public void setBatchId(Long var1) {
      this.b = var1;
   }

   public Long getResolverId() {
      return this.d;
   }

   public void setResolverId(Long var1) {
      this.d = var1;
   }

   public Long getProjectId() {
      return this.c;
   }

   public void setProjectId(Long var1) {
      this.c = var1;
   }

   public Long getResolverItemId() {
      return this.e;
   }

   public void setResolverItemId(Long var1) {
      this.e = var1;
   }

   public String getSrcProperty() {
      return this.f;
   }

   public void setSrcProperty(String var1) {
      this.f = var1;
   }

   public String getDataType() {
      return this.g;
   }

   public void setDataType(String var1) {
      this.g = var1;
   }

   public boolean isKey() {
      return this.h;
   }

   public void setKey(boolean var1) {
      this.h = var1;
   }

   public String getDestProperty() {
      return this.i;
   }

   public void setDestProperty(String var1) {
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

   public Object getValue() {
      return this.n;
   }

   public void setValue(Object var1) {
      this.n = var1;
   }
}
