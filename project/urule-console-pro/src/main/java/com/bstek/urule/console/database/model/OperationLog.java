package com.bstek.urule.console.database.model;

/**系统操作日志*/
public class OperationLog extends URuleLog {
   private static final long serialVersionUID = 1L;
   private String groupId;
   private String groupName;
   private Long projectId;
   private String projectName;
   private String category;
   private String action;
   private String itemId;
   private String content;

   public String getCategory() {
      return this.category;
   }

   public void setCategory(String category) {
      this.category = category;
   }

   public String getAction() {
      return this.action;
   }

   public void setAction(String action) {
      this.action = action;
   }

   public String getContent() {
      return this.content;
   }

   public void setContent(String content) {
      this.content = content;
   }

   public String getGroupId() {
      return this.groupId;
   }

   public void setGroupId(String groupId) {
      this.groupId = groupId;
   }

   public String getGroupName() {
      return this.groupName;
   }

   public void setGroupName(String groupName) {
      this.groupName = groupName;
   }

   public Long getProjectId() {
      return this.projectId;
   }

   public void setProjectId(Long projectId) {
      this.projectId = projectId;
   }

   public String getProjectName() {
      return this.projectName;
   }

   public void setProjectName(String projectName) {
      this.projectName = projectName;
   }

   public String getItemId() {
      return this.itemId;
   }

   public void setItemId(String itemId) {
      this.itemId = itemId;
   }

   public String toString() {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(this.getClass().getSimpleName());
      stringBuilder.append(" [");
      stringBuilder.append("Hash = ").append(this.hashCode());
      stringBuilder.append(", id=").append(this.getId());
      stringBuilder.append(", userId=").append(this.getUserId());
      stringBuilder.append(", username=").append(this.getUsername());
      stringBuilder.append(", groupId=").append(this.groupId);
      stringBuilder.append(", groupName=").append(this.groupName);
      stringBuilder.append(", projectId=").append(this.projectId);
      stringBuilder.append(", projectName=").append(this.projectName);
      stringBuilder.append(", category=").append(this.category);
      stringBuilder.append(", action=").append(this.action);
      stringBuilder.append(", content=").append(this.content);
      stringBuilder.append(", createDate=").append(this.getCreateDate());
      stringBuilder.append(", serialVersionUID=").append(1L);
      stringBuilder.append("]");
      return stringBuilder.toString();
   }
}
