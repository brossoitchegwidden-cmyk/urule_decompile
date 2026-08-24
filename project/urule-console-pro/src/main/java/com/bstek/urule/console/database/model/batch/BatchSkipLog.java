package com.bstek.urule.console.database.model.batch;

import com.bstek.urule.console.database.model.URuleLog;

public class BatchSkipLog extends URuleLog {
   private static final long serialVersionUID = -9032084549084743906L;
   private Long batchId;
   private Long logId;
   private String type;
   private String msg;
   private String detail;
   private String data;
   private String groupId;
   private Long projectId;

   public String getType() {
      return this.type;
   }

   public void setType(String type) {
      this.type = type;
   }

   public String getMsg() {
      return this.msg;
   }

   public void setMsg(String msg) {
      this.msg = msg;
   }

   public String getDetail() {
      return this.detail;
   }

   public void setDetail(String detail) {
      this.detail = detail;
   }

   public String getData() {
      return this.data;
   }

   public void setData(String data) {
      this.data = data;
   }

   public String getGroupId() {
      return this.groupId;
   }

   public void setGroupId(String groupId) {
      this.groupId = groupId;
   }

   public Long getProjectId() {
      return this.projectId;
   }

   public void setProjectId(Long projectId) {
      this.projectId = projectId;
   }

   public Long getBatchId() {
      return this.batchId;
   }

   public void setBatchId(Long batchId) {
      this.batchId = batchId;
   }

   public Long getLogId() {
      return this.logId;
   }

   public void setLogId(Long logId) {
      this.logId = logId;
   }
}
