package com.bstek.urule.console.database.model;

public class LoginLog extends URuleLog {
   private static final long a = 1L;
   private String b;
   private String c;

   public String getUserAgent() {
      return this.b;
   }

   public void setUserAgent(String var1) {
      this.b = var1;
   }

   public String getIp() {
      return this.c;
   }

   public void setIp(String var1) {
      this.c = var1;
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append(this.getClass().getSimpleName());
      var1.append(" [");
      var1.append("Hash = ").append(this.hashCode());
      var1.append(", id=").append(this.getId());
      var1.append(", userId=").append(this.getUserId());
      var1.append(", username=").append(this.getUsername());
      var1.append(", ip=").append(this.c);
      var1.append(", userAgent=").append(this.b);
      var1.append(", createDate=").append(this.getCreateDate());
      var1.append(", serialVersionUID=").append(1L);
      var1.append("]");
      return var1.toString();
   }
}
