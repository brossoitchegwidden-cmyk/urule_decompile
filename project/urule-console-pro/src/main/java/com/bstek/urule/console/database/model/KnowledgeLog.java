package com.bstek.urule.console.database.model;

import java.util.Date;

/**知识包执行日志*/
public class KnowledgeLog extends URuleLog {
   private static final long serialVersionUID = 1L;
   private Long knowledgeId;
   private String knowledgeName;
   private String version;
   private Long time;
   private String inParams;
   private String outParams;
   private String logs;
   private String groupId;
   private String groupName;
   private Long projectId;
   private String projectName;
   private String userAgent;
   private String ip;
   private Date startTime;
   private Date endTime;

   public String getUserAgent() {
      return this.userAgent;
   }

   public void setUserAgent(String userAgent) {
      this.userAgent = userAgent;
   }

   public String getIp() {
      return this.ip;
   }

   public void setIp(String ip) {
      this.ip = ip;
   }

   public String getVersion() {
      return this.version;
   }

   public void setVersion(String version) {
      this.version = version;
   }

   public String getKnowledgeName() {
      return this.knowledgeName;
   }

   public void setKnowledgeId(Long knowledgeId) {
      this.knowledgeId = knowledgeId;
   }

   public Long getKnowledgeId() {
      return this.knowledgeId;
   }

   public void setKnowledgeName(String knowledgeName) {
      this.knowledgeName = knowledgeName;
   }

   public Long getTime() {
      return this.time;
   }

   public void setTime(Long time) {
      this.time = time;
   }

   public String getInParams() {
      return this.inParams;
   }

   public void setInParams(String inParams) {
      this.inParams = inParams;
   }

   public String getOutParams() {
      return this.outParams;
   }

   public void setOutParams(String outParams) {
      this.outParams = outParams;
   }

   public String getLogs() {
      return this.logs;
   }

   public void setLogs(String logs) {
      this.logs = logs;
   }

   public Long getProjectId() {
      return this.projectId;
   }

   public void setProjectId(Long projectId) {
      this.projectId = projectId;
   }

   public String getGroupId() {
      return this.groupId;
   }

   public void setGroupId(String groupId) {
      this.groupId = groupId;
   }

   public Date getStartTime() {
      return this.startTime;
   }

   public void setStartTime(Date startTime) {
      this.startTime = startTime;
   }

   public Date getEndTime() {
      return this.endTime;
   }

   public void setEndTime(Date endTime) {
      this.endTime = endTime;
   }

   public String getGroupName() {
      return this.groupName;
   }

   public void setGroupName(String groupName) {
      this.groupName = groupName;
   }

   public String getProjectName() {
      return this.projectName;
   }

   public void setProjectName(String projectName) {
      this.projectName = projectName;
   }

   public String toString() {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(this.getClass().getSimpleName());
      stringBuilder.append(" [");
      stringBuilder.append("Hash = ").append(this.hashCode());
      stringBuilder.append(", id=").append(this.getId());
      stringBuilder.append(", username=").append(this.getUsername());
      stringBuilder.append(", ip=").append(this.ip);
      stringBuilder.append(", userAgent=").append(this.userAgent);
      stringBuilder.append(", knowledge=").append(this.knowledgeName);
      stringBuilder.append(", time=").append(this.time);
      stringBuilder.append(", createDate=").append(this.getCreateDate());
      stringBuilder.append(", projectId=").append(this.projectId);
      stringBuilder.append(", serialVersionUID=").append(1L);
      stringBuilder.append("]");
      return stringBuilder.toString();
   }
}
