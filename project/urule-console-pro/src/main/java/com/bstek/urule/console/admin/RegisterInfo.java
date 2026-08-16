package com.bstek.urule.console.admin;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(
   ignoreUnknown = true
)
public class RegisterInfo {
   private String a;
   private String b;
   private String c;
   private String d;
   private String e;
   private String f;
   private String g;
   private String h;

   public String getAccount() {
      return this.a;
   }

   public void setAccount(String var1) {
      this.a = var1;
   }

   public String getUsername() {
      return this.b;
   }

   public void setUsername(String var1) {
      this.b = var1;
   }

   public String getPassword() {
      return this.c;
   }

   public void setPassword(String var1) {
      this.c = var1;
   }

   public String getCaptcha() {
      return this.d;
   }

   public void setCaptcha(String var1) {
      this.d = var1;
   }

   public String getConfirm() {
      return this.e;
   }

   public void setConfirm(String var1) {
      this.e = var1;
   }

   public String getGroupId() {
      return this.f;
   }

   public void setGroupId(String var1) {
      this.f = var1;
   }

   public String getGroupName() {
      return this.g;
   }

   public void setGroupName(String var1) {
      this.g = var1;
   }

   public String getSecretKey() {
      return this.h;
   }

   public void setSecretKey(String var1) {
      this.h = var1;
   }
}
