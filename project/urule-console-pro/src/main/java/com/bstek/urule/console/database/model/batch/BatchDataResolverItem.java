package com.bstek.urule.console.database.model.batch;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@JsonIgnoreProperties(
   ignoreUnknown = true
)
public class BatchDataResolverItem {
   private Long a;
   private String b;
   private Long c;
   private Long d;
   private Long e;
   private BatchUpdateMode f;
   private String g;
   private String h;
   private String i;
   private String j;
   private int k;
   private String l;
   private String m;
   private String n;
   private Date o;
   private Date p;
   private List q;
   private String r;
   private List s;
   private PreparedStatement t;
   private List u;

   public BatchDataResolverItem() {
      this.f = BatchUpdateMode.update;
      this.k = 1000;
      this.q = new ArrayList();
      this.u = new ArrayList();
   }

   public List getFilters() {
      return this.q;
   }

   public void setFilters(List var1) {
      this.q = var1;
   }

   public List getFields() {
      return this.u;
   }

   public void setFields(List var1) {
      this.u = var1;
   }

   public Long getResolverId() {
      return this.e;
   }

   public void setResolverId(Long var1) {
      this.e = var1;
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
      return this.l;
   }

   public void setDesc(String var1) {
      this.l = var1;
   }

   public String getTableName() {
      return this.g;
   }

   public void setTableName(String var1) {
      this.g = var1;
   }

   public BatchUpdateMode getUpdateMode() {
      return this.f;
   }

   public void setUpdateMode(BatchUpdateMode var1) {
      this.f = var1;
   }

   public String getCreateUser() {
      return this.m;
   }

   public void setCreateUser(String var1) {
      this.m = var1;
   }

   public String getUpdateUser() {
      return this.n;
   }

   public void setUpdateUser(String var1) {
      this.n = var1;
   }

   public Date getCreateDate() {
      return this.o;
   }

   public void setCreateDate(Date var1) {
      this.o = var1;
   }

   public Date getUpdateDate() {
      return this.p;
   }

   public void setUpdateDate(Date var1) {
      this.p = var1;
   }

   public String getFilterData() {
      return this.h;
   }

   public void setFilterData(String var1) {
      this.h = var1;
   }

   public String getUpdateSql() {
      return this.r;
   }

   public void setUpdateSql(String var1) {
      this.r = var1;
   }

   public List getParams() {
      return this.s;
   }

   public void setParams(List var1) {
      this.s = var1;
   }

   public PreparedStatement getStmt() {
      return this.t;
   }

   public void setStmt(PreparedStatement var1) {
      this.t = var1;
   }

   public String getPartitionName() {
      return this.i;
   }

   public void setPartitionName(String var1) {
      this.i = var1;
   }

   public String getPartitionValue() {
      return this.j;
   }

   public void setPartitionValue(String var1) {
      this.j = var1;
   }

   public int getCommitLimit() {
      return this.k;
   }

   public void setCommitLimit(int var1) {
      this.k = var1;
   }
}
