package com.bstek.urule.console.database.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.Date;
import java.util.List;

@JsonIgnoreProperties(
   ignoreUnknown = true
)
public class User {
   private String a;
   private String b;
   private String c;
   private String d;
   private String e;
   private Date f;
   private boolean g;
   private String h;
   private Date i;
   private String j;
   private Date k;
   private String l;
   private List m;

   public String getId() {
      return this.a;
   }

   public void setId(String var1) {
      this.a = var1;
   }

   public String getName() {
      return this.b;
   }

   public void setName(String var1) {
      this.b = var1;
   }

   public String getPassword() {
      return this.c;
   }

   public void setPassword(String var1) {
      this.c = var1;
   }

   public boolean isEnable() {
      return this.g;
   }

   public void setEnable(boolean var1) {
      this.g = var1;
   }

   public String getDesc() {
      return this.h;
   }

   public void setDesc(String var1) {
      this.h = var1;
   }

   public Date getCreateDate() {
      return this.i;
   }

   public void setCreateDate(Date var1) {
      this.i = var1;
   }

   public Date getUpdateDate() {
      return this.k;
   }

   public void setUpdateDate(Date var1) {
      this.k = var1;
   }

   public String getEmail() {
      return this.d;
   }

   public void setEmail(String var1) {
      this.d = var1;
   }

   public String getSecretKey() {
      return this.e;
   }

   public void setSecretKey(String var1) {
      this.e = var1;
   }

   public Date getExpirDate() {
      return this.f;
   }

   public void setExpirDate(Date var1) {
      this.f = var1;
   }

   public String getCreateUser() {
      return this.j;
   }

   public void setCreateUser(String var1) {
      this.j = var1;
   }

   public String getUpdateUser() {
      return this.l;
   }

   public void setUpdateUser(String var1) {
      this.l = var1;
   }

   public List getGroups() {
      return this.m;
   }

   public void setGroups(List var1) {
      this.m = var1;
   }
}
