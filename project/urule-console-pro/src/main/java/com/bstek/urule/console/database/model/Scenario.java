package com.bstek.urule.console.database.model;

import java.util.Date;

public class Scenario {
   private long id;
   private long packetId;
   private long projectId;
   private String name;
   private String desc;
   private byte[] excelFile;
   private String excelFileName;
   private String inputData;
   private String outputData;
   private String createUser;
   private String updateUser;
   private Date createDate;
   private Date updateDate;

   public long getId() {
      return this.id;
   }

   public void setId(long id) {
      this.id = id;
   }

   public long getPacketId() {
      return this.packetId;
   }

   public void setPacketId(long packetId) {
      this.packetId = packetId;
   }

   public long getProjectId() {
      return this.projectId;
   }

   public void setProjectId(long projectId) {
      this.projectId = projectId;
   }

   public String getName() {
      return this.name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public String getDesc() {
      return this.desc;
   }

   public void setDesc(String desc) {
      this.desc = desc;
   }

   public byte[] getExcelFile() {
      return this.excelFile;
   }

   public void setExcelFile(byte[] excelFile) {
      this.excelFile = excelFile;
   }

   public String getExcelFileName() {
      return this.excelFileName;
   }

   public void setExcelFileName(String excelFileName) {
      this.excelFileName = excelFileName;
   }

   public String getInputData() {
      return this.inputData;
   }

   public void setInputData(String inputData) {
      this.inputData = inputData;
   }

   public String getOutputData() {
      return this.outputData;
   }

   public void setOutputData(String outputData) {
      this.outputData = outputData;
   }

   public String getCreateUser() {
      return this.createUser;
   }

   public void setCreateUser(String createUser) {
      this.createUser = createUser;
   }

   public String getUpdateUser() {
      return this.updateUser;
   }

   public void setUpdateUser(String updateUser) {
      this.updateUser = updateUser;
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
}
