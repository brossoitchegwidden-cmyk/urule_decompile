package com.bstek.urule.console.database.model.batch;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.Date;

@JsonIgnoreProperties(
   ignoreUnknown = true
)
public class BatchDataResolverItemField {
   private Long id;
   private Long batchId;
   private Long projectId;
   private Long resolverId;
   private Long resolverItemId;
   private String srcProperty;
   private String dataType;
   private boolean key;
   private String destProperty;
   private String createUser;
   private String updateUser;
   private Date createDate;
   private Date updateDate;
   private Object value;

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

   public Long getResolverId() {
      return this.resolverId;
   }

   public void setResolverId(Long resolverId) {
      this.resolverId = resolverId;
   }

   public Long getProjectId() {
      return this.projectId;
   }

   public void setProjectId(Long projectId) {
      this.projectId = projectId;
   }

   public Long getResolverItemId() {
      return this.resolverItemId;
   }

   public void setResolverItemId(Long resolverItemId) {
      this.resolverItemId = resolverItemId;
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

   public boolean isKey() {
      return this.key;
   }

   public void setKey(boolean key) {
      this.key = key;
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

   public Object getValue() {
      return this.value;
   }

   public void setValue(Object value) {
      this.value = value;
   }
}
