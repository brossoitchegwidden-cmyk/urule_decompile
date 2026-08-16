package com.bstek.urule.console.database.model.batch;

import com.bstek.urule.console.batch.BatchStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.Date;
import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(
   ignoreUnknown = true
)
public class Batch {
   private Long a;
   private Long b;
   private String c;
   private boolean d;
   private int e;
   private boolean f = true;
   private String g;
   private BatchStatus h;
   private String i;
   private boolean j = true;
   private Integer k = 10;
   private Integer l = 100;
   private Long m;
   private Long n;
   private Long o;
   private String p;
   private String q;
   private boolean r;
   private boolean s;
   private String t;
   private String u;
   private String v;
   private String w;
   private String x;
   private String y;
   private Date z;
   private Date A;
   private List B;
   private List C;
   private Map D;
   private List E;
   private BatchDataProvider F;
   private BatchDataResolver G;

   public BatchStatus getStatus() {
      return this.h;
   }

   public void setStatus(BatchStatus var1) {
      this.h = var1;
   }

   public String getListener() {
      return this.i;
   }

   public void setListener(String var1) {
      this.i = var1;
   }

   public boolean isAsync() {
      return this.f;
   }

   public void setAsync(boolean var1) {
      this.f = var1;
   }

   public boolean isThreadMulti() {
      return this.j;
   }

   public void setThreadMulti(boolean var1) {
      this.j = var1;
   }

   public Integer getThreadSize() {
      return this.k;
   }

   public void setThreadSize(Integer var1) {
      this.k = var1;
   }

   public Integer getThreadDataSize() {
      return this.l;
   }

   public void setThreadDataSize(Integer var1) {
      this.l = var1;
   }

   public List getParams() {
      return this.B;
   }

   public void setParams(List var1) {
      this.B = var1;
   }

   public List getPacketParams() {
      return this.C;
   }

   public void setPacketParams(List var1) {
      this.C = var1;
   }

   public BatchDataProvider getDataProvider() {
      return this.F;
   }

   public void setDataProvider(BatchDataProvider var1) {
      this.F = var1;
   }

   public BatchDataResolver getDataResolver() {
      return this.G;
   }

   public void setDataResolver(BatchDataResolver var1) {
      this.G = var1;
   }

   public Long getId() {
      return this.a;
   }

   public void setId(Long var1) {
      this.a = var1;
   }

   public Long getProviderId() {
      return this.m;
   }

   public void setProviderId(Long var1) {
      this.m = var1;
   }

   public Long getResolverId() {
      return this.n;
   }

   public void setResolverId(Long var1) {
      this.n = var1;
   }

   public String getName() {
      return this.c;
   }

   public void setName(String var1) {
      this.c = var1;
   }

   public Long getProjectId() {
      return this.b;
   }

   public void setProjectId(Long var1) {
      this.b = var1;
   }

   public String getDesc() {
      return this.w;
   }

   public void setDesc(String var1) {
      this.w = var1;
   }

   public Long getPacketId() {
      return this.o;
   }

   public void setPacketId(Long var1) {
      this.o = var1;
   }

   public String getPacketInputData() {
      return this.q;
   }

   public void setPacketInputData(String var1) {
      this.q = var1;
   }

   public boolean isRestEnable() {
      return this.r;
   }

   public void setRestEnable(boolean var1) {
      this.r = var1;
   }

   public boolean isRestSecurityEnable() {
      return this.s;
   }

   public void setRestSecurityEnable(boolean var1) {
      this.s = var1;
   }

   public String getRestSecurityUser() {
      return this.t;
   }

   public void setRestSecurityUser(String var1) {
      this.t = var1;
   }

   public String getRestSecurityPassword() {
      return this.u;
   }

   public void setRestSecurityPassword(String var1) {
      this.u = var1;
   }

   public String getInputData() {
      return this.v;
   }

   public void setInputData(String var1) {
      this.v = var1;
   }

   public String getCreateUser() {
      return this.x;
   }

   public void setCreateUser(String var1) {
      this.x = var1;
   }

   public String getUpdateUser() {
      return this.y;
   }

   public void setUpdateUser(String var1) {
      this.y = var1;
   }

   public Date getCreateDate() {
      return this.z;
   }

   public void setCreateDate(Date var1) {
      this.z = var1;
   }

   public Date getUpdateDate() {
      return this.A;
   }

   public void setUpdateDate(Date var1) {
      this.A = var1;
   }

   public String getCallbackUrl() {
      return this.g;
   }

   public void setCallbackUrl(String var1) {
      this.g = var1;
   }

   public boolean isEnable() {
      return this.d;
   }

   public void setEnable(boolean var1) {
      this.d = var1;
   }

   public Map getComplexPacketParams() {
      return this.D;
   }

   public void setComplexPacketParams(Map var1) {
      this.D = var1;
   }

   public List getOutParameterNameList() {
      return this.E;
   }

   public void setOutParameterNameList(List var1) {
      this.E = var1;
   }

   public int getSkipLimit() {
      return this.e;
   }

   public void setSkipLimit(int var1) {
      this.e = var1;
   }

   public String getPacketName() {
      return this.p;
   }

   public void setPacketName(String var1) {
      this.p = var1;
   }
}
