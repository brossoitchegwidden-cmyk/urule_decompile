package com.bstek.urule.console.cache.packet;

import java.util.List;

public class PacketConfig {
   private long id;
   private long projectId;
   private boolean enable;
   private boolean restEnable;
   private boolean restSecurityEnable;
   private String code;
   private String restSecurityUser;
   private String restSecurityPassword;
   private List restInput;
   private List restOutput;
   private List auditInput;
   private List auditOutput;
   private boolean auditEnable;

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

   public boolean isEnable() {
      return this.enable;
   }

   public void setEnable(boolean enable) {
      this.enable = enable;
   }

   public boolean isRestEnable() {
      return this.restEnable;
   }

   public void setRestEnable(boolean restEnable) {
      this.restEnable = restEnable;
   }

   public String getCode() {
      return this.code;
   }

   public void setCode(String code) {
      this.code = code;
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

   public List getRestInput() {
      return this.restInput;
   }

   public void setRestInput(List restInput) {
      this.restInput = restInput;
   }

   public List getRestOutput() {
      return this.restOutput;
   }

   public void setRestOutput(List restOutput) {
      this.restOutput = restOutput;
   }

   public List getAuditInput() {
      return this.auditInput;
   }

   public void setAuditInput(List auditInput) {
      this.auditInput = auditInput;
   }

   public List getAuditOutput() {
      return this.auditOutput;
   }

   public void setAuditOutput(List auditOutput) {
      this.auditOutput = auditOutput;
   }

   public boolean isAuditEnable() {
      return this.auditEnable;
   }

   public void setAuditEnable(boolean auditEnable) {
      this.auditEnable = auditEnable;
   }
}
