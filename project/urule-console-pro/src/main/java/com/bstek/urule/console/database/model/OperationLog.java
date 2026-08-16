package com.bstek.urule.console.database.model;

public class OperationLog extends URuleLog {
   private static final long a = 1L;
   private String b;
   private String c;
   private Long d;
   private String e;
   private String f;
   private String g;
   private String h;
   private String i;

   public String getCategory() {
      return this.f;
   }

   public void setCategory(String var1) {
      this.f = var1;
   }

   public String getAction() {
      return this.g;
   }

   public void setAction(String var1) {
      this.g = var1;
   }

   public String getContent() {
      return this.i;
   }

   public void setContent(String var1) {
      this.i = var1;
   }

   public String getGroupId() {
      return this.b;
   }

   public void setGroupId(String var1) {
      this.b = var1;
   }

   public String getGroupName() {
      return this.c;
   }

   public void setGroupName(String var1) {
      this.c = var1;
   }

   public Long getProjectId() {
      return this.d;
   }

   public void setProjectId(Long var1) {
      this.d = var1;
   }

   public String getProjectName() {
      return this.e;
   }

   public void setProjectName(String var1) {
      this.e = var1;
   }

   public String getItemId() {
      return this.h;
   }

   public void setItemId(String var1) {
      this.h = var1;
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append(this.getClass().getSimpleName());
      var1.append(" [");
      var1.append("Hash = ").append(this.hashCode());
      var1.append(", id=").append(this.getId());
      var1.append(", userId=").append(this.getUserId());
      var1.append(", username=").append(this.getUsername());
      var1.append(", groupId=").append(this.b);
      var1.append(", groupName=").append(this.c);
      var1.append(", projectId=").append(this.d);
      var1.append(", projectName=").append(this.e);
      var1.append(", category=").append(this.f);
      var1.append(", action=").append(this.g);
      var1.append(", content=").append(this.i);
      var1.append(", createDate=").append(this.getCreateDate());
      var1.append(", serialVersionUID=").append(1L);
      var1.append("]");
      return var1.toString();
   }
}
