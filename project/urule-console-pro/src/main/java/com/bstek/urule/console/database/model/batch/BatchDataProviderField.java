package com.bstek.urule.console.database.model.batch;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.Date;

@JsonIgnoreProperties(
   ignoreUnknown = true
)
public class BatchDataProviderField {
   private Long id;
   private Long batchId;
   private Long projectId;
   private Long providerId;
   private String srcProperty;
   private String dataType;
   private String destProperty;
   private String classPath;
   private Long dataProviderId;
   private String createUser;
   private String updateUser;
   private Date createDate;
   private Date updateDate;
   private BatchDataProvider dataProvider;

   public Long getId() {
      return this.id;
   }

   public void setId(Long id) {
      this.id = id;
   }

   public Long getBatchId() {
      return this.batchId;
   }

   public void setBatchId(Long batchId) {
      this.batchId = batchId;
   }

   public Long getProjectId() {
      return this.projectId;
   }

   public void setProjectId(Long projectId) {
      this.projectId = projectId;
   }

   public Long getProviderId() {
      return this.providerId;
   }

   public void setProviderId(Long providerId) {
      this.providerId = providerId;
   }

   public String getSrcProperty() {
      return this.srcProperty;
   }

   public void setSrcProperty(String srcProperty) {
      this.srcProperty = srcProperty;
   }

   public String getDataType() {
      return this.dataType;
   }

   public void setDataType(String dataType) {
      this.dataType = dataType;
   }

   public String getDestProperty() {
      return this.destProperty;
   }

   public void setDestProperty(String destProperty) {
      this.destProperty = destProperty;
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

   public Long getDataProviderId() {
      return this.dataProviderId;
   }

   public void setDataProviderId(Long dataProviderId) {
      this.dataProviderId = dataProviderId;
   }

   public BatchDataProvider getDataProvider() {
      return this.dataProvider;
   }

   public void setDataProvider(BatchDataProvider dataProvider) {
      this.dataProvider = dataProvider;
   }

   public String getClassPath() {
      return this.classPath;
   }

   public void setClassPath(String classPath) {
      this.classPath = classPath;
   }
}
