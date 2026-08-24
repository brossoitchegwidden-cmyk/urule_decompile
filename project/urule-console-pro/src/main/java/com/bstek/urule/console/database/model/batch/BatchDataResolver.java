package com.bstek.urule.console.database.model.batch;

import com.bstek.urule.console.config.dialect.Dialect;
import com.bstek.urule.console.database.model.datasource.DataSource;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@JsonIgnoreProperties(
   ignoreUnknown = true
)
public class BatchDataResolver {
   private Long id;
   private String name;
   private Long batchId;
   private Long projectId;
   private Long datasourceId;
   private TranScope tranScope;
   private String filterData;
   private String desc;
   private String listener;
   private String createUser;
   private String updateUser;
   private Date createDate;
   private Date updateDate;
   @JsonIgnore
   private DataSource datasource;
   @JsonIgnore
   private Dialect dialect;
   private List resolverItems = new ArrayList();

   public DataSource getDatasource() {
      return this.datasource;
   }

   public void setDatasource(DataSource datasource) {
      this.datasource = datasource;
   }

   public List getItems() {
      return this.resolverItems;
   }

   public void setItems(List items) {
      this.resolverItems = items;
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

   public Long getDatasourceId() {
      return this.datasourceId;
   }

   public void setDatasourceId(Long datasourceId) {
      this.datasourceId = datasourceId;
   }

   public TranScope getTranScope() {
      return this.tranScope;
   }

   public void setTranScope(TranScope tranScope) {
      this.tranScope = tranScope;
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

   public Dialect getDialect() {
      return this.dialect;
   }

   public void setDialect(Dialect dialect) {
      this.dialect = dialect;
   }

   public String getListener() {
      return this.listener;
   }

   public void setListener(String listener) {
      this.listener = listener;
   }

   public String getFilterData() {
      return this.filterData;
   }

   public void setFilterData(String filterData) {
      this.filterData = filterData;
   }
}
