package com.bstek.urule.console.database.model.datasource;

import com.bstek.urule.console.database.model.batch.DataSourceType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.Date;

@JsonIgnoreProperties(
   ignoreUnknown = true
)
public class DataSource {
   private Long a;
   private String b;
   private String c;
   private DataSourceType d;
   private String e;
   private String f;
   private String g;
   private String h;
   private String i;
   private String j;
   private String k;
   private int l;
   private int m;
   private int n;
   private int o;
   private String p;
   private String q;
   private String r;
   private Date s;
   private Date t;

   public String getDesc() {
      return this.p;
   }

   public void setDesc(String var1) {
      this.p = var1;
   }

   public Long getId() {
      return this.a;
   }

   public void setId(Long var1) {
      this.a = var1;
   }

   public String getCreateUser() {
      return this.q;
   }

   public void setCreateUser(String var1) {
      this.q = var1;
   }

   public String getUpdateUser() {
      return this.r;
   }

   public void setUpdateUser(String var1) {
      this.r = var1;
   }

   public Date getCreateDate() {
      return this.s;
   }

   public void setCreateDate(Date var1) {
      this.s = var1;
   }

   public Date getUpdateDate() {
      return this.t;
   }

   public void setUpdateDate(Date var1) {
      this.t = var1;
   }

   public String getName() {
      return this.b;
   }

   public void setName(String var1) {
      this.b = var1;
   }

   public String getGroupId() {
      return this.c;
   }

   public void setGroupId(String var1) {
      this.c = var1;
   }

   public DataSourceType getType() {
      return this.d;
   }

   public void setType(DataSourceType var1) {
      this.d = var1;
   }

   public String getDbJndiName() {
      return this.e;
   }

   public void setDbJndiName(String var1) {
      this.e = var1;
   }

   public String getDataSourceBean() {
      return this.f;
   }

   public void setDataSourceBean(String var1) {
      this.f = var1;
   }

   public String getDbDriver() {
      return this.g;
   }

   public void setDbDriver(String var1) {
      this.g = var1;
   }

   public String getDbUrl() {
      return this.h;
   }

   public void setDbUrl(String var1) {
      this.h = var1;
   }

   public String getDbUser() {
      return this.i;
   }

   public void setDbUser(String var1) {
      this.i = var1;
   }

   public String getDbPwd() {
      return this.j;
   }

   public void setDbPwd(String var1) {
      this.j = var1;
   }

   public String getDbValidationQuery() {
      return this.k;
   }

   public void setDbValidationQuery(String var1) {
      this.k = var1;
   }

   public int getDbInitialsize() {
      return this.l;
   }

   public void setDbInitialsize(int var1) {
      this.l = var1;
   }

   public int getDbMaxIdle() {
      return this.m;
   }

   public void setDbMaxIdle(int var1) {
      this.m = var1;
   }

   public int getDbMaxTotal() {
      return this.n;
   }

   public void setDbMaxTotal(int var1) {
      this.n = var1;
   }

   public int getDbMinIdle() {
      return this.o;
   }

   public void setDbMinIdle(int var1) {
      this.o = var1;
   }
}
