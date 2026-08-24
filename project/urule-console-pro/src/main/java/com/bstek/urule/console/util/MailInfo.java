package com.bstek.urule.console.util;

public class MailInfo {
   private String host;
   private String formName;
   private String formPassword;
   private String replayAddress;
   private String toAddress;
   private String subject;
   private String content;
   private boolean auth;

   public String getHost() {
      return this.host;
   }

   public void setHost(String host) {
      this.host = host;
   }

   public String getFormName() {
      return this.formName;
   }

   public void setFormName(String formName) {
      this.formName = formName;
   }

   public String getFormPassword() {
      return this.formPassword;
   }

   public void setFormPassword(String formPassword) {
      this.formPassword = formPassword;
   }

   public String getReplayAddress() {
      return this.replayAddress;
   }

   public void setReplayAddress(String replayAddress) {
      this.replayAddress = replayAddress;
   }

   public String getToAddress() {
      return this.toAddress;
   }

   public void setToAddress(String toAddress) {
      this.toAddress = toAddress;
   }

   public String getSubject() {
      return this.subject;
   }

   public void setSubject(String subject) {
      this.subject = subject;
   }

   public String getContent() {
      return this.content;
   }

   public void setContent(String content) {
      this.content = content;
   }

   public boolean isAuth() {
      return this.auth;
   }

   public void setAuth(boolean auth) {
      this.auth = auth;
   }
}
