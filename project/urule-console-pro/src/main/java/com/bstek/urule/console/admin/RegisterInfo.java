package com.bstek.urule.console.admin;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(
   ignoreUnknown = true
)
public class RegisterInfo {
   private String account;
   private String username;
   private String password;
   private String captcha;
   private String confirm;
   private String groupId;
   private String groupName;
   private String secretKey;

   public String getAccount() {
      return this.account;
   }

   public void setAccount(String account) {
      this.account = account;
   }

   public String getUsername() {
      return this.username;
   }

   public void setUsername(String username) {
      this.username = username;
   }

   public String getPassword() {
      return this.password;
   }

   public void setPassword(String password) {
      this.password = password;
   }

   public String getCaptcha() {
      return this.captcha;
   }

   public void setCaptcha(String captcha) {
      this.captcha = captcha;
   }

   public String getConfirm() {
      return this.confirm;
   }

   public void setConfirm(String confirm) {
      this.confirm = confirm;
   }

   public String getGroupId() {
      return this.groupId;
   }

   public void setGroupId(String groupId) {
      this.groupId = groupId;
   }

   public String getGroupName() {
      return this.groupName;
   }

   public void setGroupName(String groupName) {
      this.groupName = groupName;
   }

   public String getSecretKey() {
      return this.secretKey;
   }

   public void setSecretKey(String secretKey) {
      this.secretKey = secretKey;
   }
}
