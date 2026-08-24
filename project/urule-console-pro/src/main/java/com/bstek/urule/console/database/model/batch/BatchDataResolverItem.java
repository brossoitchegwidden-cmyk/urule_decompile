package com.bstek.urule.console.database.model.batch;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@JsonIgnoreProperties(
   ignoreUnknown = true
)
public class BatchDataResolverItem {
   private Long id;
   private String name;
   private Long batchId;
   private Long projectId;
   private Long resolverId;
   private BatchUpdateMode updateMode;
   private String tableName;
   private String filterData;
   private String partitionName;
   private String partitionValue;
   private int commitLimit;
   private String desc;
   private String createUser;
   private String updateUser;
   private Date createDate;
   private Date updateDate;
   private List filters;
   private String updateSql;
   private List params;
   private PreparedStatement stmt;
   private List fields;

   public BatchDataResolverItem() {
      this.updateMode = BatchUpdateMode.update;
      this.commitLimit = 1000;
      this.filters = new ArrayList();
      this.fields = new ArrayList();
   }

   public List getFilters() {
      return this.filters;
   }

   public void setFilters(List filters) {
      this.filters = filters;
   }

   public List getFields() {
      return this.fields;
   }

   public void setFields(List fields) {
      this.fields = fields;
   }

   public Long getResolverId() {
      return this.resolverId;
   }

   public void setResolverId(Long resolverId) {
      this.resolverId = resolverId;
   }

   public Long getId() {
      return this.id;
   }

   public void setId(Long id) {
      this.id = id;
   }

   public String getName() {
      return this.name;
   }

   public void setName(String name) {
      this.name = name;
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

   public String getDesc() {
      return this.desc;
   }

   public void setDesc(String desc) {
      this.desc = desc;
   }

   public String getTableName() {
      return this.tableName;
   }

   public void setTableName(String tableName) {
      this.tableName = tableName;
   }

   public BatchUpdateMode getUpdateMode() {
      return this.updateMode;
   }

   public void setUpdateMode(BatchUpdateMode updateMode) {
      this.updateMode = updateMode;
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

   public String getFilterData() {
      return this.filterData;
   }

   public void setFilterData(String filterData) {
      this.filterData = filterData;
   }

   public String getUpdateSql() {
      return this.updateSql;
   }

   public void setUpdateSql(String updateSql) {
      this.updateSql = updateSql;
   }

   public List getParams() {
      return this.params;
   }

   public void setParams(List params) {
      this.params = params;
   }

   public PreparedStatement getStmt() {
      return this.stmt;
   }

   public void setStmt(PreparedStatement stmt) {
      this.stmt = stmt;
   }

   public String getPartitionName() {
      return this.partitionName;
   }

   public void setPartitionName(String partitionName) {
      this.partitionName = partitionName;
   }

   public String getPartitionValue() {
      return this.partitionValue;
   }

   public void setPartitionValue(String partitionValue) {
      this.partitionValue = partitionValue;
   }

   public int getCommitLimit() {
      return this.commitLimit;
   }

   public void setCommitLimit(int commitLimit) {
      this.commitLimit = commitLimit;
   }
}
