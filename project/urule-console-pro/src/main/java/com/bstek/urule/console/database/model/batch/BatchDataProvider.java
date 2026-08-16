package com.bstek.urule.console.database.model.batch;

import com.bstek.urule.console.config.dialect.Dialect;
import com.bstek.urule.console.database.model.datasource.DataSource;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@JsonIgnoreProperties(
   ignoreUnknown = true
)
public class BatchDataProvider {
   private Long a;
   private String b;
   private Long c;
   private Long d;
   private String e;
   private String f;
   private Long g;
   private String h;
   private String i;
   private String j;
   private Integer k = 100;
   private Boolean l = false;
   private String m;
   private String n;
   private String o;
   private String p;
   private String q;
   private String r;
   private String s;
   private String t;
   private Date u;
   private Date v;
   private List w;
   @JsonIgnore
   private Dialect x;
   private List y = new ArrayList();
   @JsonIgnore
   private DataSource z;
   private List A = new ArrayList();

   public DataSource getDatasource() {
      return this.z;
   }

   public List getParams() {
      return this.w;
   }

   public void setParams(List var1) {
      this.w = var1;
   }

   public void setDatasource(DataSource var1) {
      this.z = var1;
   }

   public List getFields() {
      return this.A;
   }

   public void setFields(List var1) {
      this.A = var1;
   }

   public Long getId() {
      return this.a;
   }

   public void setId(Long var1) {
      this.a = var1;
   }

   public String getName() {
      return this.b;
   }

   public void setName(String var1) {
      this.b = var1;
   }

   public Long getBatchId() {
      return this.c;
   }

   public void setBatchId(Long var1) {
      this.c = var1;
   }

   public Long getProjectId() {
      return this.d;
   }

   public void setProjectId(Long var1) {
      this.d = var1;
   }

   public String getDesc() {
      return this.e;
   }

   public void setDesc(String var1) {
      this.e = var1;
   }

   public String getType() {
      return this.f;
   }

   public void setType(String var1) {
      this.f = var1;
   }

   public String getInputData() {
      return this.i;
   }

   public void setInputData(String var1) {
      this.i = var1;
   }

   public String getCountSql() {
      return this.n;
   }

   public void setCountSql(String var1) {
      this.n = var1;
   }

   public String getCreateUser() {
      return this.s;
   }

   public void setCreateUser(String var1) {
      this.s = var1;
   }

   public String getUpdateUser() {
      return this.t;
   }

   public void setUpdateUser(String var1) {
      this.t = var1;
   }

   public Date getCreateDate() {
      return this.u;
   }

   public void setCreateDate(Date var1) {
      this.u = var1;
   }

   public Date getUpdateDate() {
      return this.v;
   }

   public void setUpdateDate(Date var1) {
      this.v = var1;
   }

   public String getPageSql() {
      return this.m;
   }

   public void setPageSql(String var1) {
      this.m = var1;
   }

   public Long getDatasourceId() {
      return this.g;
   }

   public void setDatasourceId(Long var1) {
      this.g = var1;
   }

   public Integer getPageSize() {
      return this.k;
   }

   public void setPageSize(Integer var1) {
      this.k = var1;
   }

   public Boolean isSupportsPaging() {
      return this.l;
   }

   public void setSupportsPaging(Boolean var1) {
      this.l = var1;
   }

   public String getPacketVarName() {
      return this.j;
   }

   public void setPacketVarName(String var1) {
      this.j = var1;
   }

   public Dialect getDialect() {
      return this.x;
   }

   public void setDialect(Dialect var1) {
      this.x = var1;
   }

   public String getFilterData() {
      return this.r;
   }

   public void setFilterData(String var1) {
      this.r = var1;
   }

   public List getFilters() {
      return this.y;
   }

   public void setFilters(List var1) {
      this.y = var1;
   }

   public String getOrderField() {
      return this.p;
   }

   public void setOrderField(String var1) {
      this.p = var1;
   }

   public String getPageLimitType() {
      return this.o;
   }

   public void setPageLimitType(String var1) {
      this.o = var1;
   }

   public String getOrderFieldParamName() {
      return this.q;
   }

   public void setOrderFieldParamName(String var1) {
      this.q = var1;
   }

   public String getListener() {
      return this.h;
   }

   public void setListener(String var1) {
      this.h = var1;
   }
}
