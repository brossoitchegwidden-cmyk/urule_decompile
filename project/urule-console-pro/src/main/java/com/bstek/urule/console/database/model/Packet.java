package com.bstek.urule.console.database.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Packet {
   private long id;
   private long projectId;
   private String code;
   private String name;
   private PacketType type;
   private String desc;
   private String inputData;
   private String outputData;
   private boolean deleteEnable;
   private boolean enable;
   private boolean auditEnable;
   private String auditInput;
   private String auditOutput;
   private boolean restEnable;
   private boolean restSecurityEnable;
   private String restSecurityUser;
   private String restSecurityPassword;
   private String restInput;
   private String restOutput;
   private String createUser;
   private String updateUser;
   private Date createDate;
   private Date updateDate;
   private long deployedCount;
   private PacketPackage packetPackage;
   private List files;

   public Packet() {
      this.type = PacketType.file;
      this.deleteEnable = true;
      this.files = new ArrayList();
   }

   public long getId() {
      return this.id;
   }

   public void setId(long id) {
      this.id = id;
   }

   public long getProjectId() {
      return this.projectId;
   }

   public void setProjectId(long projectId) {
      this.projectId = projectId;
   }

   public String getCode() {
      return this.code;
   }

   public void setCode(String code) {
      this.code = code;
   }

   public String getName() {
      return this.name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public PacketType getType() {
      return this.type;
   }

   public void setType(PacketType type) {
      this.type = type;
   }

   public String getDesc() {
      return this.desc;
   }

   public void setDesc(String desc) {
      this.desc = desc;
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

   public boolean isEnable() {
      return this.enable;
   }

   public void setEnable(boolean enable) {
      this.enable = enable;
   }

   public boolean isDeleteEnable() {
      return this.deleteEnable;
   }

   public void setDeleteEnable(boolean deleteEnable) {
      this.deleteEnable = deleteEnable;
   }

   public boolean isAuditEnable() {
      return this.auditEnable;
   }

   public void setAuditEnable(boolean auditEnable) {
      this.auditEnable = auditEnable;
   }

   public boolean isRestEnable() {
      return this.restEnable;
   }

   public void setRestEnable(boolean restEnable) {
      this.restEnable = restEnable;
   }

   public boolean isRestSecurityEnable() {
      return this.restSecurityEnable;
   }

   public void setRestSecurityEnable(boolean restSecurityEnable) {
      this.restSecurityEnable = restSecurityEnable;
   }

   public String getRestSecurityUser() {
      return this.restSecurityUser;
   }

   public void setRestSecurityUser(String restSecurityUser) {
      this.restSecurityUser = restSecurityUser;
   }

   public String getRestSecurityPassword() {
      return this.restSecurityPassword;
   }

   public void setRestSecurityPassword(String restSecurityPassword) {
      this.restSecurityPassword = restSecurityPassword;
   }

   public String getRestInput() {
      return this.restInput;
   }

   public void setRestInput(String restInput) {
      this.restInput = restInput;
   }

   public String getRestOutput() {
      return this.restOutput;
   }

   public void setRestOutput(String restOutput) {
      this.restOutput = restOutput;
   }

   public String getAuditInput() {
      return this.auditInput;
   }

   public void setAuditInput(String auditInput) {
      this.auditInput = auditInput;
   }

   public String getAuditOutput() {
      return this.auditOutput;
   }

   public void setAuditOutput(String auditOutput) {
      this.auditOutput = auditOutput;
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

   public long getDeployedCount() {
      return this.deployedCount;
   }

   public void setDeployedCount(long deployedCount) {
      this.deployedCount = deployedCount;
   }

   public PacketPackage getPacketPackage() {
      return this.packetPackage;
   }

   public void setPacketPackage(PacketPackage packetPackage) {
      this.packetPackage = packetPackage;
   }

   public List getFiles() {
      return this.files;
   }

   public void setFiles(List files) {
      this.files = files;
   }
}
