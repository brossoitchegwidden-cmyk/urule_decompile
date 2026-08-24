package com.bstek.urule.console.database.model;

/**用户登录日志*/
public class LoginLog extends URuleLog {
   private static final long serialVersionUID = 1L;
   private String userAgent;
   private String ip;

   public String getUserAgent() {
      return this.userAgent;
   }

   public void setUserAgent(String userAgent) {
      this.userAgent = userAgent;
   }

   public String getIp() {
      return this.ip;
   }

   public void setIp(String ip) {
      this.ip = ip;
   }

   public String toString() {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(this.getClass().getSimpleName());
      stringBuilder.append(" [");
      stringBuilder.append("Hash = ").append(this.hashCode());
      stringBuilder.append(", id=").append(this.getId());
      stringBuilder.append(", userId=").append(this.getUserId());
      stringBuilder.append(", username=").append(this.getUsername());
      stringBuilder.append(", ip=").append(this.ip);
      stringBuilder.append(", userAgent=").append(this.userAgent);
      stringBuilder.append(", createDate=").append(this.getCreateDate());
      stringBuilder.append(", serialVersionUID=").append(1L);
      stringBuilder.append("]");
      return stringBuilder.toString();
   }
}
