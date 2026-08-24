package com.bstek.urule.console.database.model;

import java.io.Serializable;
import java.util.Date;

public class URuleLog implements Serializable {
   private static final long serialVersionUID = 1L;
   private Long id;
   private String userId;
   private String username;
   private Date createDate;

   public Long getId() {
      return this.id;
   }

   public void setId(Long id) {
      this.id = id;
   }

   public String getUserId() {
      return this.userId;
   }

   public void setUserId(String userId) {
      this.userId = userId;
   }

   public String getUsername() {
      return this.username;
   }

   public void setUsername(String username) {
      this.username = username;
   }

   public Date getCreateDate() {
      return this.createDate;
   }

   public void setCreateDate(Date createDate) {
      this.createDate = createDate;
   }

   public static long getSerialversionuid() {
      return 1L;
   }
}
