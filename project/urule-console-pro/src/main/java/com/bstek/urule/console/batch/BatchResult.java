package com.bstek.urule.console.batch;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BatchResult {
   private Long batchId;
   private String batchName;
   private Date startTime;
   private Date endTime;
   private String ip;
   private int readCount = 0;
   private int filterCount = 0;
   private BatchStatus status;
   private String msg;
   private String userAgent;
   @JsonIgnore
   private List exceptions = new ArrayList();
   private Map itemResults = new HashMap();

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

   public Date getStartTime() {
      return this.startTime;
   }

   public void setStartTime(Date startTime) {
      this.startTime = startTime;
   }

   public String getIp() {
      return this.ip;
   }

   public void setIp(String ip) {
      this.ip = ip;
   }

   public int getReadCount() {
      return this.readCount;
   }

   public void setReadCount(int readCount) {
      this.readCount = readCount;
   }

   public BatchStatus getStatus() {
      return this.status;
   }

   public void setStatus(BatchStatus status) {
      this.status = status;
   }

   public String getMsg() {
      return this.msg;
   }

   public void setMsg(String msg) {
      this.msg = msg;
   }

   public Date getEndTime() {
      return this.endTime;
   }

   public void setEndTime(Date endTime) {
      this.endTime = endTime;
   }

   public Map getItemResults() {
      return this.itemResults;
   }

   public void setItemResults(Map itemResults) {
      this.itemResults = itemResults;
   }

   public String getUserAgent() {
      return this.userAgent;
   }

   public void setUserAgent(String userAgent) {
      this.userAgent = userAgent;
   }

   public void setException(Exception exception) {
      this.msg = exception.getClass() + ":" + exception.getMessage();
   }

   public int getFilterCount() {
      return this.filterCount;
   }

   public void setFilterCount(int filterCount) {
      this.filterCount = filterCount;
   }

   public List getExceptions() {
      return this.exceptions;
   }

   public void setExceptions(List exceptions) {
      this.exceptions = exceptions;
   }

   public String toString() {
      String toStringResult = "id:" + this.batchId + ",batchName:" + this.batchName + ",status:" + this.status + ",recordCount:" + this.readCount;
      if (this.endTime != null) {
         toStringResult = toStringResult + ",time:" + (this.endTime.getTime() - this.startTime.getTime()) / 1000L;
      }

      return toStringResult;
   }
}
