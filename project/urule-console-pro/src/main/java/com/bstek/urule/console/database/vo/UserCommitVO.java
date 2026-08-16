package com.bstek.urule.console.database.vo;

public class UserCommitVO {
   private String a;
   private String b;
   private int c;

   public String getUserId() {
      return this.a;
   }

   public void setUserId(String var1) {
      this.a = var1;
   }

   public String getUserName() {
      return this.b;
   }

   public void setUserName(String var1) {
      this.b = var1;
   }

   public int getCount() {
      return this.c;
   }

   public void setCount(int var1) {
      this.c = var1;
   }
}
