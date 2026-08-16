package com.bstek.urule.console.database.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.Date;

@JsonIgnoreProperties(
   ignoreUnknown = true
)
public class Role {
   private long a;
   private String b;
   private String c;
   private String d;
   private String e;
   private Date f;
   private Date g;

   public long getId() {
      return this.a;
   }

   public void setId(long var1) {
      this.a = var1;
   }

   public String getName() {
      return this.b;
   }

   public void setName(String var1) {
      this.b = var1;
   }

   public String getType() {
      return this.c;
   }

   public void setType(String var1) {
      this.c = var1;
   }

   public String getCreateUser() {
      return this.d;
   }

   public void setCreateUser(String var1) {
      this.d = var1;
   }

   public String getUpdateUser() {
      return this.e;
   }

   public void setUpdateUser(String var1) {
      this.e = var1;
   }

   public Date getCreateDate() {
      return this.f;
   }

   public void setCreateDate(Date var1) {
      this.f = var1;
   }

   public Date getUpdateDate() {
      return this.g;
   }

   public void setUpdateDate(Date var1) {
      this.g = var1;
   }

   public String toString() {
      return this.a + "," + this.b;
   }
}
