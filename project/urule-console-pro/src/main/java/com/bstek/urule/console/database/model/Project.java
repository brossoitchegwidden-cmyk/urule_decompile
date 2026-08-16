package com.bstek.urule.console.database.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.Date;

@JsonIgnoreProperties(
   ignoreUnknown = true
)
public class Project {
   private Long a;
   private String b;
   private String c;
   private ProjectViewModel d;
   private String e;
   private String f;
   private String g;
   private String h;
   private String i;
   private String j;
   private String k;
   private Date l;
   private Date m;

   public Project() {
      this.d = ProjectViewModel.category;
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

   public String getType() {
      return this.c;
   }

   public void setType(String var1) {
      this.c = var1;
   }

   public ProjectViewModel getViewModel() {
      return this.d;
   }

   public void setViewModel(ProjectViewModel var1) {
      this.d = var1;
   }

   public String getDesc() {
      return this.e;
   }

   public void setDesc(String var1) {
      this.e = var1;
   }

   public String getGroupId() {
      return this.f;
   }

   public void setGroupId(String var1) {
      this.f = var1;
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

   public String getCreateUser() {
      return this.j;
   }

   public void setCreateUser(String var1) {
      this.j = var1;
   }

   public String getEnableApproveUser() {
      return this.g;
   }

   public void setEnableApproveUser(String var1) {
      this.g = var1;
   }

   public String getDisableApproveUser() {
      return this.h;
   }

   public void setDisableApproveUser(String var1) {
      this.h = var1;
   }

   public String getDeployApproveUser() {
      return this.i;
   }

   public void setDeployApproveUser(String var1) {
      this.i = var1;
   }
}
