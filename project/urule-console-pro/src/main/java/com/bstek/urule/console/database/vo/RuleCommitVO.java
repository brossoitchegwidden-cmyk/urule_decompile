package com.bstek.urule.console.database.vo;

import java.util.Date;

public class RuleCommitVO {
   private Date createDate;
   private int count;

   public Date getCreateDate() {
      return this.createDate;
   }

   public void setCreateDate(Date createDate) {
      this.createDate = createDate;
   }

   public int getCount() {
      return this.count;
   }

   public void setCount(int count) {
      this.count = count;
   }
}
