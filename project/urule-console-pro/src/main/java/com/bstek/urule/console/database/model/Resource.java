package com.bstek.urule.console.database.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.Date;
import java.util.List;

@JsonIgnoreProperties(
   ignoreUnknown = true
)
public class Resource {
   public static final String MENU_TYPE = "MENU";
   public static final String MENU_PACKAGE = "PACKAGE";
   private Long a;
   private String b;
   private Long c;
   private int d;
   private String e;
   private String f;
   private String g;
   private Date h;
   private Date i;
   private String j;
   private List k;

   public Long getId() {
      return this.a;
   }

   public void setId(Long var1) {
      this.a = var1;
   }

   public String getName() {
      return this.b;
   }

   public void setName(String var1) {
      this.b = var1;
   }

   public Long getParentId() {
      return this.c;
   }

   public void setParentId(Long var1) {
      this.c = var1;
   }

   public int getOrderNum() {
      return this.d;
   }

   public void setOrderNum(int var1) {
      this.d = var1;
   }

   public String getPath() {
      return this.g;
   }

   public void setPath(String var1) {
      this.g = var1;
   }

   public Date getCreateDate() {
      return this.h;
   }

   public void setCreateDate(Date var1) {
      this.h = var1;
   }

   public Date getUpdateDate() {
      return this.i;
   }

   public void setUpdateDate(Date var1) {
      this.i = var1;
   }

   public List getChildren() {
      return this.k;
   }

   public void setChildren(List var1) {
      this.k = var1;
   }

   public String getIcon() {
      return this.e;
   }

   public void setIcon(String var1) {
      this.e = var1;
   }

   public String getDesc() {
      return this.f;
   }

   public void setDesc(String var1) {
      this.f = var1;
   }

   public String getType() {
      return this.j;
   }

   public void setType(String var1) {
      this.j = var1;
   }

   public String toString() {
      return "path:" + this.g + ",type:" + this.j;
   }
}
