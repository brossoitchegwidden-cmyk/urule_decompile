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
public class BatchDataResolver {
   private Long a;
   private String b;
   private Long c;
   private Long d;
   private Long e;
   private TranScope f;
   private String g;
   private String h;
   private String i;
   private String j;
   private String k;
   private Date l;
   private Date m;
   @JsonIgnore
   private DataSource n;
   @JsonIgnore
   private Dialect o;
   private List p = new ArrayList();

   public DataSource getDatasource() {
      return this.n;
   }

   public void setDatasource(DataSource var1) {
      this.n = var1;
   }

   public List getItems() {
      return this.p;
   }

   public void setItems(List var1) {
      this.p = var1;
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

   public Long getDatasourceId() {
      return this.e;
   }

   public void setDatasourceId(Long var1) {
      this.e = var1;
   }

   public TranScope getTranScope() {
      return this.f;
   }

   public void setTranScope(TranScope var1) {
      this.f = var1;
   }

   public Long getProjectId() {
      return this.d;
   }

   public void setProjectId(Long var1) {
      this.d = var1;
   }

   public String getDesc() {
      return this.h;
   }

   public void setDesc(String var1) {
      this.h = var1;
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

   public Dialect getDialect() {
      return this.o;
   }

   public void setDialect(Dialect var1) {
      this.o = var1;
   }

   public String getListener() {
      return this.i;
   }

   public void setListener(String var1) {
      this.i = var1;
   }

   public String getFilterData() {
      return this.g;
   }

   public void setFilterData(String var1) {
      this.g = var1;
   }
}
