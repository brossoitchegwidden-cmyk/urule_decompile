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
public class BatchDataProvider {
   private Long id;
   private String name;
   private Long batchId;
   private Long projectId;
   private String desc;
   private String type;
   private Long datasourceId;
   private String listener;
   private String inputData;
   private String packetVarName;
   private Integer pageSize = 100;
   private Boolean supportsPaging = false;
   private String pageSql;
   private String countSql;
   private String pageLimitType;
   private String orderField;
   private String orderFieldParamName;
   private String filterData;
   private String createUser;
   private String updateUser;
   private Date createDate;
   private Date updateDate;
   private List params;
   @JsonIgnore
   private Dialect dialect;
   private List filters = new ArrayList();
   @JsonIgnore
   private DataSource datasource;
   private List fields = new ArrayList();

   public DataSource getDatasource() {
      return this.datasource;
   }

   public List getParams() {
      return this.params;
   }

   public void setParams(List params) {
      this.params = params;
   }

   public void setDatasource(DataSource datasource) {
      this.datasource = datasource;
   }

   public List getFields() {
      return this.fields;
   }

   public void setFields(List fields) {
      this.fields = fields;
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

   public String getType() {
      return this.type;
   }

   public void setType(String type) {
      this.type = type;
   }

   public String getInputData() {
      return this.inputData;
   }

   public void setInputData(String inputData) {
      this.inputData = inputData;
   }

   public String getCountSql() {
      return this.countSql;
   }

   public void setCountSql(String countSql) {
      this.countSql = countSql;
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

   public String getPageSql() {
      return this.pageSql;
   }

   public void setPageSql(String pageSql) {
      this.pageSql = pageSql;
   }

   public Long getDatasourceId() {
      return this.datasourceId;
   }

   public void setDatasourceId(Long datasourceId) {
      this.datasourceId = datasourceId;
   }

   public Integer getPageSize() {
      return this.pageSize;
   }

   public void setPageSize(Integer pageSize) {
      this.pageSize = pageSize;
   }

   public Boolean isSupportsPaging() {
      return this.supportsPaging;
   }

   public void setSupportsPaging(Boolean supportsPaging) {
      this.supportsPaging = supportsPaging;
   }

   public String getPacketVarName() {
      return this.packetVarName;
   }

   public void setPacketVarName(String packetVarName) {
      this.packetVarName = packetVarName;
   }

   public Dialect getDialect() {
      return this.dialect;
   }

   public void setDialect(Dialect dialect) {
      this.dialect = dialect;
   }

   public String getFilterData() {
      return this.filterData;
   }

   public void setFilterData(String filterData) {
      this.filterData = filterData;
   }

   public List getFilters() {
      return this.filters;
   }

   public void setFilters(List filters) {
      this.filters = filters;
   }

   public String getOrderField() {
      return this.orderField;
   }

   public void setOrderField(String orderField) {
      this.orderField = orderField;
   }

   public String getPageLimitType() {
      return this.pageLimitType;
   }

   public void setPageLimitType(String pageLimitType) {
      this.pageLimitType = pageLimitType;
   }

   public String getOrderFieldParamName() {
      return this.orderFieldParamName;
   }

   public void setOrderFieldParamName(String orderFieldParamName) {
      this.orderFieldParamName = orderFieldParamName;
   }

   public String getListener() {
      return this.listener;
   }

   public void setListener(String listener) {
      this.listener = listener;
   }
}
