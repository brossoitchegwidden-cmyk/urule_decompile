package com.bstek.urule.console.database.vo;

import java.util.Date;

public class RuleCommitVO {
   private Date a;
   private int b;

   public Date getCreateDate() {
      return this.a;
   }

   public void setCreateDate(Date var1) {
      this.a = var1;
   }

   public int getCount() {
      return this.b;
   }

   public void setCount(int var1) {
      this.b = var1;
   }
}
