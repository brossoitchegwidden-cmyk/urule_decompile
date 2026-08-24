package com.bstek.urule.console.database.model.batch;

import com.bstek.urule.console.batch.BatchStatus;
import com.bstek.urule.console.database.model.URuleLog;
import java.util.Date;

public class BatchLog extends URuleLog {
   private static final long serialVersionUID = -7678531060299019128L;
   private String ip;
   private String userAgent;
   private Long batchId;
   private String batchName;
   private BatchStatus status;
   private int readCount = 0;
   private int filterCount = 0;
   private String itemData;
   private String inParams;
   private Long packetId;
   private String packetParams;
   private Date startTime;
   private Date endTime;
   private long time;
   private String msg;
   private String detail;
   private String groupId;
   private String groupName;
   private Long projectId;
   private String projectName;

   public String getDetail() {
      return this.detail;
   }

   public void setDetail(String detail) {
      this.detail = detail;
   }

   public String getIp() {
      return this.ip;
   }

   public void setIp(String ip) {
      this.ip = ip;
   }

   public Long getBatchId() {
      return this.batchId;
   }

   public void setBatchId(Long batchId) {
      this.batchId = batchId;
   }

   public String getBatchName() {
      return this.batchName;
   }

   public void setBatchName(String batchName) {
      this.batchName = batchName;
   }

   public String getUserAgent() {
      return this.userAgent;
   }

   public void setUserAgent(String userAgent) {
      this.userAgent = userAgent;
   }

   public BatchStatus getStatus() {
      return this.status;
   }

   public void setStatus(BatchStatus status) {
      this.status = status;
   }

   public int getReadCount() {
      return this.readCount;
   }

   public void setReadCount(int readCount) {
      this.readCount = readCount;
   }

   public int getFilterCount() {
      return this.filterCount;
   }

   public void setFilterCount(int filterCount) {
      this.filterCount = filterCount;
   }

   public String getItemData() {
      return this.itemData;
   }

   public void setItemData(String itemData) {
      this.itemData = itemData;
   }

   public String getInParams() {
      return this.inParams;
   }

   public void setInParams(String inputParams) {
      this.inParams = inputParams;
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

   public long getTime() {
      return this.time;
   }

   public void setTime(long time) {
      this.time = time;
   }

   public String getMsg() {
      return this.msg;
   }

   public void setMsg(String msg) {
      this.msg = msg;
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

   public Long getPacketId() {
      return this.packetId;
   }

   public void setPacketId(Long packetId) {
      this.packetId = packetId;
   }

   public String getPacketParams() {
      return this.packetParams;
   }

   public void setPacketParams(String packetParams) {
      this.packetParams = packetParams;
   }
}
