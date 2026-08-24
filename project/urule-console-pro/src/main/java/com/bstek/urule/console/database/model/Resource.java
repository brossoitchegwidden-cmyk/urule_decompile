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
   private Long id;
   private String name;
   private Long parentId;
   private int orderNum;
   private String icon;
   private String desc;
   private String path;
   private Date createDate;
   private Date updateDate;
   private String type;
   private List children;

   public Long getId() {
      return this.id;
   }

   public void setId(Long id) {
      this.id = id;
   }

   public String getName() {
      return this.name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public Long getParentId() {
      return this.parentId;
   }

   public void setParentId(Long parentId) {
      this.parentId = parentId;
   }

   public int getOrderNum() {
      return this.orderNum;
   }

   public void setOrderNum(int orderNum) {
      this.orderNum = orderNum;
   }

   public String getPath() {
      return this.path;
   }

   public void setPath(String path) {
      this.path = path;
   }

   public Date getCreateDate() {
      return this.createDate;
   }

   public void setCreateDate(Date createDate) {
      this.createDate = createDate;
   }

   public Date getUpdateDate() {
      return this.updateDate;
   }

   public void setUpdateDate(Date updateDate) {
      this.updateDate = updateDate;
   }

   public List getChildren() {
      return this.children;
   }

   public void setChildren(List subMenus) {
      this.children = subMenus;
   }

   public String getIcon() {
      return this.icon;
   }

   public void setIcon(String icon) {
      this.icon = icon;
   }

   public String getDesc() {
      return this.desc;
   }

   public void setDesc(String desc) {
      this.desc = desc;
   }

   public String getType() {
      return this.type;
   }

   public void setType(String type) {
      this.type = type;
   }

   public String toString() {
      return "path:" + this.path + ",type:" + this.type;
   }
}
